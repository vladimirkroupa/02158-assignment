
import java.util.HashMap;
import java.util.Map;

public class OriginalBarrier extends Barrier {

	Semaphore[] carBarrierSema = new Semaphore[9];
	Semaphore[] carContinueSema = new Semaphore[9];

    Map<Integer,Integer> isWaiting = new HashMap<>();
    
    boolean barrierOn = false;
    boolean barrierToBeShutDown = false;
    boolean barrierToBeOff = false;

    	
	public OriginalBarrier() {
		for (int i = 0; i < carBarrierSema.length; i++) {
			carBarrierSema[i] = new Semaphore(0);
		}
		for (int i = 0; i < carContinueSema.length; i++) {
			carContinueSema[i] = new Semaphore(0);
		}
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
	
	@Override
	protected boolean isOn() {
		return barrierOn;
	}

	//TO BE CALLED BY BARRIER
	@Override
	public void turnOn() {
		this.barrierOn = true;
		this.barrierToBeOff = false;
		this.barrierToBeShutDown = false;
		
	} // Activate barrier
	
	@Override
	public void turnOff() {

		this.barrierOn = false;
		this.barrierToBeOff = true;
		this.barrierToBeShutDown = false;

		for (int i = 0; i < carContinueSema.length; i++) {
			if (isWaiting.get(i) == 1) {
				carContinueSema[i].V(); //signal those waiting in front of barrier			
				isWaiting.put(i, 0);
			}
			else {
				carBarrierSema[i].V(); //pretend signal for cars not reach barrier yet
			}
		} 
	} // Deactivate barrier

	@Override
	public void shutdown() {
		this.barrierToBeShutDown = true;
	} // Shutdown barrier

	@Override
	public void run() {
		while(true){
			try {

				for (int i = 0; i < carBarrierSema.length; i++) {				
					carBarrierSema[i].P(); // wait for all

				}
				if(barrierToBeOff == true) {
					this.barrierToBeOff = false;// barrier off then wait for all again
					continue;
				}
				for (int i = 0; i < carContinueSema.length; i++) {
					carContinueSema[i].V(); //signal all
					isWaiting.put(i, 0);					
				}

				System.out.println("Signal all to start again");

				
				if (barrierToBeShutDown) {
					this.barrierOn = false;
					this.barrierToBeShutDown = false;
				}
			} catch (InterruptedException e) {
				System.out.println("Thread terminated");
			}
		}
	}
	
	//TO BE CALLED BY CARS
	@Override
	public void arrive(int carNo) {
		try {
			System.out.println("Called barrier sync by " + carNo);
			carBarrierSema[carNo].V();
			carContinueSema[carNo].P();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
