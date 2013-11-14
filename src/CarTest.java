//Prototype implementation of Car Test class
//Mandatory assignment
//Course 02158 Concurrent Programming, DTU, Fall 2013

//Hans Henrik LÃ¸vengreen     Oct 6, 2013

public class CarTest extends Thread {

    CarTestingI cars;
    int testno;

    public CarTest(CarTestingI ct, int no) {
        cars = ct;
        testno = no;
    }

    public void run() {
        try {
            switch (testno) { 
            case 0:
                // Demonstration of startAll/stopAll.
                // Should let the cars go one round (unless very fast)
                cars.startAll();
                sleep(3000);
                cars.stopAll();
                break;
            case 1:
                // Demonstration of barrierOn/barrierOff.
                // Should let the cars go pass barrier one time
            	// Then show barrierOff one time
            	// At last,barrier on at least again and then barrierOff¡BstopAll
            	
                cars.startAll();
                cars.startCar(0);
                sleep(1000);
                cars.barrierOn();
                sleep(30000);
                cars.barrierOff();
                sleep(500);
                cars.barrierOn();
                sleep(15000);                
                cars.barrierOff();
                cars.stopAll();
                break;
            case 2:
                // Demonstration of barrierOn/barrierShutdown/.
                // Should let the cars go pass barrier at least one time
            	// Then show barrierShutdown
            	// At last,barrier on at least again and then barrierOff¡BstopAll
            	
                cars.startAll();
                cars.startCar(0);
                sleep(1000);
                cars.barrierOn();
                sleep(30000);
                cars.barrierShutDown();
                sleep(15000);
                cars.barrierOn();
                sleep(15000);                
                cars.barrierOff();
                cars.stopAll();
                break;
            
            case 3:
                // Demonstration of mixed barrierOn/barrierOff/barrierShutdown/.
                // Should let the cars go pass barrier at least one time
            	// Then show barrierShutdown
            	// At last,barrier on at least again and then barrierOff
            	// At barrierOn again to ensure no problem after shutdown/off
            	// barrierOff
            	// stopAll
            	
                cars.startAll();
                cars.startCar(0);
                sleep(1000);
                cars.barrierOn();
                sleep(30000);
                cars.barrierShutDown();
                sleep(15000);
                cars.barrierOn();
                sleep(20000);                
                cars.barrierOff();
                sleep(3000);
                cars.barrierOn();
                sleep(15000);
                cars.barrierOff();
                cars.stopAll();
                break;
            case 4:
                // Demonstration of setLimit 
            	// default limit is 4
            	// startAll
            	// higher the limit to 8
                // set the limit back to 4
            	// stopAll
            	
                cars.startAll();
                sleep(10000);
                cars.setLimit(8);
                sleep(30000);
                cars.setLimit(4);
                sleep(20000);
                cars.stopAll();
                break;
                
            case 5:
                // Demonstration of setLimit 
            	// default limit is 4
            	// startAll
            	// lower the limit to 1,definitely cause deadlock
                // restart the program manually
            	
                cars.startAll();
                sleep(30000);
                cars.setLimit(1);
                break;
                
            case 6:
                // Demonstration of setSlow 
            	// startAll
            	// setSlow for a while
                // stopAll
            	
                cars.startAll();
                sleep(10000);
                cars.setSlow(true);
                sleep(15000);
                cars.setSlow(false);
                sleep(10000);
                cars.stopAll();
                break;            

            case 7:
                // Demonstration of speed setting.
                // Change speed to double of default values
                cars.println("Doubling speeds");
                for (int i = 1; i < 9; i++) {
                    cars.setSpeed(i,50);
                };
                cars.startAll();
                sleep(5000);
                cars.stopAll();
                break;

            default:
                cars.println("Test " + testno + " not available");
            }

            cars.println("Test ended");

        } catch (Exception e) {
            System.err.println("Exception in test: "+e);
        }
    }

}



