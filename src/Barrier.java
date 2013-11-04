import java.util.HashMap;

public class Barrier extends Thread {

	Semaphore[] carBarrierSema = new Semaphore[9];
	Semaphore[] carContinueSema = new Semaphore[9];

    HashMap<Pos,Integer> entries = new HashMap<>();
    HashMap<Integer,Integer> isWaiting = new HashMap<>();
    
    boolean barrierOn = false;
    boolean barrierToBeShutDown = false;
    boolean barrierToBeOff = false;
    	
	public Barrier() {
		for (int i = 0; i < carBarrierSema.length; i++) {
			carBarrierSema[i] = new Semaphore(0);
		}
		for (int i = 0; i < carContinueSema.length; i++) {
			carContinueSema[i] = new Semaphore(0);
		}
		
		entries.put(new Pos(6,3),new Integer(0));
		entries.put(new Pos(6,4),new Integer(1));
		entries.put(new Pos(6,5),new Integer(2));
		entries.put(new Pos(6,6),new Integer(3));
		entries.put(new Pos(6,7),new Integer(4));
		entries.put(new Pos(5,8),new Integer(5));
		entries.put(new Pos(5,9),new Integer(6));
		entries.put(new Pos(5,10),new Integer(7));
		entries.put(new Pos(5,11),new Integer(8));
		isWaiting.put(new Integer(0),new Integer(0));
		isWaiting.put(new Integer(1),new Integer(0));
		isWaiting.put(new Integer(2),new Integer(0));
		isWaiting.put(new Integer(3),new Integer(0));
		isWaiting.put(new Integer(4),new Integer(0));
		isWaiting.put(new Integer(5),new Integer(0));
		isWaiting.put(new Integer(6),new Integer(0));
		isWaiting.put(new Integer(7),new Integer(0));
		isWaiting.put(new Integer(8),new Integer(0));

	}
	
	

	//TO BE CALLED BY BARRIER
	
	public void on() {
		this.barrierOn = true;
		this.barrierToBeOff = false;
		this.barrierToBeShutDown = false;
		
	} // Activate barrier
	
	public void off() {
		this.barrierOn = false;
		this.barrierToBeOff = true;
		this.barrierToBeShutDown = false;

		for (int i = 0; i < carContinueSema.length; i++) {
			if(isWaiting.get(i)==1) {
				carContinueSema[i].V(); //signal those waiting in front of barrier			
				isWaiting.put(i, 0);
			}
			else
				carBarrierSema[i].V(); //pretend signal for cars not reach barrier yet
		} 
	} // Deactivate barrier

	public void shutdown() {
		this.barrierToBeShutDown = true;
	} // Shutdown barrier

	
	public void run() {
		while(true){
			try {

				for (int i = 0; i < carBarrierSema.length; i++) {				
					carBarrierSema[i].P(); // wait for all

				}
				if(barrierToBeOff == true ){
					this.barrierToBeOff = false;// barrier off then wait for all again
					continue;
				}
				for (int i = 0; i < carContinueSema.length; i++) {
					carContinueSema[i].V(); //signal all
					isWaiting.put(i, 0);					
				}

				System.out.println("Signal all to start again");

				
				if(barrierToBeShutDown){
					this.barrierOn = false;
					this.barrierToBeShutDown = false;
				}
			} catch (InterruptedException e) {
				System.out.println("Thread terminated");
			}
		}
	}
	
	
	//TO BE CALLED BY CARS
	public void sync(int carNo){
		try {
			System.out.println("Called barrier sync by " + carNo);
			carBarrierSema[carNo].V();
			carContinueSema[carNo].P();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean isInfrontOfBarrier(int carNo, Pos curCarPos){
		return barrierOn && entries != null && entries.get(curCarPos) !=null 
				&& entries.get(curCarPos) == carNo;
	}

}
