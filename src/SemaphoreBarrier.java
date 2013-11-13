
public class SemaphoreBarrier extends Barrier {

	private BarrierState state;
	
	private boolean[] waiting = new boolean[CARS_NO];
	
	private Semaphore[] proceed = new Semaphore[CARS_NO];
	private Semaphore arrived;
	
	public SemaphoreBarrier() {

		// init semaphores
		for (int i = 0; i < proceed.length; i++) {
			proceed[i] = new Semaphore(0);
		}
		
		arrived = new Semaphore(0);
		
		initWaiting();
		
		state = BarrierState.OFF;
	}	
	
	private void initWaiting() {
		for (int i = 0; i < waiting.length; i++) {
			waiting[i] = false;
		}
	}
			
	@Override
	public void arrive(int carNo) {				
		waiting[carNo] = true;
		
		arrived.V();
		try {
			proceed[carNo].P();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {		
		try {
			while (true) {
				coordinate();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}			
	}
	
	public void coordinate() throws InterruptedException {
		for (int i = 0; i < CARS_NO; i++) {
			arrived.P();
		}
		for (int i = 0; i < CARS_NO; i++) {
			if (waiting[i]) {
				waiting[i] = false;
				proceed[i].V();
			}
		}
		
		if (state == BarrierState.SHUTDOWN) {
			state = BarrierState.OFF;
		}
	}

	@Override
	protected boolean isOn() {
		return (state != BarrierState.OFF);
	}

	@Override
	public void turnOn() {
		state = BarrierState.ON;
	}
	
	@Override
	public void shutdown() {
		if (state != BarrierState.OFF) { 
			state = BarrierState.SHUTDOWN;
		}
	}
	
	public void turnOff() {		
		state = BarrierState.OFF;
		for (int i = 0; i < CARS_NO; i++) {
			if (! waiting[i]) {
				arrived.V();
			}
		}
	}

	static enum BarrierState {
		
		ON, OFF, SHUTDOWN
		
	}
	
}