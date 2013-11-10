//Prototype implementation of Car Control
//Mandatory assignment
//Course 02158 Concurrent Programming, DTU, Fall 2013

//Hans Henrik Løvengreen     Oct 6, 2013


import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

class Gate {

    Semaphore g = new Semaphore(0);
    Semaphore e = new Semaphore(1);
    boolean isopen = false;

    public void pass() throws InterruptedException {
        g.P(); 
        g.V();
    }

    public void open() {
        try { e.P(); } catch (InterruptedException e) {}
        if (!isopen) { g.V();  isopen = true; }
        e.V();
    }

    public void close() {
        try { e.P(); } catch (InterruptedException e) {}
        if (isopen) { 
            try { g.P(); } catch (InterruptedException e) {}
            isopen = false;
        }
        e.V();
    }

}

class Car extends Thread {

    int basespeed = 100;             // Rather: degree of slowness
    int variation =  50;             // Percentage of base speed

    CarDisplayI cd;                  // GUI part

    int no;                          // Car number
    Pos startpos;                    // Startpositon (provided by GUI)
    Pos barpos;                      // Barrierpositon (provided by GUI)
    Color col;                       // Car  color
    Gate mygate;                     // Gate at startposition
    Alley redAlley;
    Alley blueAlley;

    int speed;                       // Current car speed
    Pos curpos;                      // Current position 
    Pos newpos;                      // New position to go to
    
    Map<Pos, Semaphore> posSemaMap;

    Barrier bar;
    
    public Car(int no, CarDisplayI cd, Gate g, Alley redAlley, Alley blueAlley, Map<Pos, Semaphore> posSemaMap, Barrier bar) {

    	this.no = no;
        this.cd = cd;
        mygate = g;
        this.redAlley = redAlley;
        this.blueAlley = blueAlley;
        this.posSemaMap = posSemaMap;
        this.bar = bar;
        
        startpos = cd.getStartPos(no);
        barpos = cd.getBarrierPos(no);  // For later use

        col = chooseColor();

        // do not change the special settings for car no. 0
        if (no==0) {
            basespeed = 0;  
            variation = 0; 
            setPriority(Thread.MAX_PRIORITY); 
        }
    }

    public synchronized void setSpeed(int speed) { 
        if (no != 0 && speed >= 0) {
            basespeed = speed;
        }
        else
            cd.println("Illegal speed settings");
    }

    public synchronized void setVariation(int var) { 
        if (no != 0 && 0 <= var && var <= 100) {
            variation = var;
        }
        else
            cd.println("Illegal variation settings");
    }

    synchronized int chooseSpeed() { 
        double factor = (1.0D+(Math.random()-0.5D)*2*variation/100);
        return (int)Math.round(factor*basespeed);
    }

    private int speed() {
        // Slow down if requested
        final int slowfactor = 3;  
        return speed * (cd.isSlow(curpos)? slowfactor : 1);
    }

    Color chooseColor() { 
        return Color.blue; // You can get any color, as longs as it's blue 
    }

    Pos nextPos(Pos pos) {
        // Get my track from display
        return cd.nextPos(no,pos);
    }

    boolean atGate(Pos pos) {
        return pos.equals(startpos);
    }

   public void run() {
        try {

            speed = chooseSpeed();
            curpos = startpos;
            cd.mark(curpos,col,no);

            while (true) { 
                sleep(speed());

                if (atGate(curpos)) { 
                    mygate.pass(); 
                    speed = chooseSpeed();
                }
                	
                newpos = nextPos(curpos);
                
                //Get the alley semaphore
                if (redAlley.isAboutToEnter(no, curpos)) {
                    cd.println("Car " + no + " is about to enter the red alley.");
                    redAlley.enter(no);
                }
                if (blueAlley.isAboutToEnter(no, curpos)) {
                	cd.println("Car " + no + " is about to enter the blue alley.");
                	blueAlley.enter(no);
                }
                
                
                if (bar.isInfrontOfBarrier(no, curpos)) {
                	//bar.isWaiting.put(no, 1); //keep record of which car is waiting
                	bar.sync(no);
                }
                
                if (curpos.equals(new Pos(5,3))) {
                	//for debugging car 0 when barrier is On, means that it has passed the barrier
                	//System.out.println("Car 0 in 5,3");
                }
                
                //Get the position semaphore
                Semaphore newPosSema = posSemaMap.get(newpos);
                newPosSema.P();
                
                //  Move to new position 
                cd.clear(curpos);
                cd.mark(curpos,newpos,col,no);
                sleep(speed());
                cd.clear(curpos,newpos);
                cd.mark(newpos,col,no);
                
                //remove the position semaphore
                Semaphore curPosSema = posSemaMap.get(curpos);
                curPosSema.V();
                
                
                if (blueAlley.hasLeft(no, curpos)) {
                    cd.println("Car " + no + " has left the blue alley.");
                    blueAlley.leave(no);
                }
                //remove the alley semaphore
                if (redAlley.hasLeft(no, curpos)) {
                    cd.println("Car " + no + " has left the red alley.");
                    redAlley.leave(no);
                }                

                curpos = newpos;
            }

        } catch (Exception e) {
            cd.println("Exception in Car no. " + no);
            System.err.println("Exception in Car no. " + no + ":" + e);
            e.printStackTrace();
        }
    }

}

public class CarControl implements CarControlI{

    CarDisplayI cd;           // Reference to GUI
    Car[]  car;               // Cars
    Gate[] gate;              // Gates
    Alley redAlley;
    Alley blueAlley;
    Barrier bar;

    Map<Pos, Semaphore> posSemaMap = new HashMap<Pos, Semaphore>();

    public CarControl(CarDisplayI cd) {
        this.cd = cd;
        car  = new  Car[9];
        gate = new Gate[9];
        redAlley = new FairMonitorAlley(cd);
        redAlley.initRedPositions();
        blueAlley = new MonitorAlley(cd);
        blueAlley.initBluePositions();
        bar = new Barrier();

        for (int row = 0;row<11;row++){
        	for (int col=0;col<12;col++){
        		posSemaMap.put(new Pos(row,col), new Semaphore(1));
        	}
        }
        
        for (int no = 0; no < 9; no++) {
            gate[no] = new Gate();
            car[no] = new Car(no,cd,gate[no], redAlley, blueAlley, posSemaMap, bar);
            car[no].start();
        }
        
        bar.start();

    }
    

    public boolean hasBridge() {
        return false;				// Change for bridge version
    }
    
    public void startCar(int no) {
        gate[no].open();
    }

    public void stopCar(int no) {
        gate[no].close();
    }

    public void barrierOn() { 
        cd.println("Barrier is ON");
        bar.on();
    }

    public void barrierOff() { 
        cd.println("Barrier is OFF");
        bar.off();
    }

    public void barrierShutDown() { 
        cd.println("Barrier shut down");
        bar.shutdown();
        // This sleep is for illustrating how blocking affects the GUI
        // Remove when shutdown is implemented.
        try { 
        	bar.shutdown();
        } catch (Exception e) {
        	e.printStackTrace();
        }

        // Recommendation: 
        //   If not implemented call barrier.off() instead to make graphics consistent
    }

    public void setLimit(int k) { 
        cd.println("Setting of bridge limit not implemented in this version");
    }

    public void removeCar(int no) { 
        cd.println("Remove Car not implemented in this version");
    }

    public void restoreCar(int no) { 
        cd.println("Restore Car not implemented in this version");
    }

    /* Speed settings for testing purposes */

    public void setSpeed(int no, int speed) { 
        car[no].setSpeed(speed);
    }

    public void setVariation(int no, int var) { 
        car[no].setVariation(var);
    }

}






