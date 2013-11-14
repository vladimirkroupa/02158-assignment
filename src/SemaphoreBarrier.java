
public class SemaphoreBarrier extends Barrier {

	private BarrierState state;
	private Semaphore stateSem = new Semaphore(1);
	
	private boolean[] waiting = new boolean[CARS_NO];
	private Semaphore waitingSem = new Semaphore(1);
	
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
	public void arrive(int carNo) throws InterruptedException {				
		waitingSem.P();
		waiting[carNo] = true;
		waitingSem.V();
		
		arrived.V();
		proceed[carNo].P();
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
		waitingSem.P();
		for (int i = 0; i < CARS_NO; i++) {
			if (waiting[i]) {
				waiting[i] = false;
				proceed[i].V();
			}
		}
		waitingSem.V();
		
		stateSem.P();
		if (state == BarrierState.SHUTDOWN) {
			state = BarrierState.OFF;
		}
		stateSem.V();
	}

	@Override
	protected boolean isOn() throws InterruptedException {
		stateSem.P();
		boolean result = (state != BarrierState.OFF);
		stateSem.V();
		return result;
	}

	@Override
	public void turnOn() throws InterruptedException {
		stateSem.P();
		state = BarrierState.ON;
		stateSem.V();
	}
	
	@Override
	public void shutdown() throws InterruptedException {
		stateSem.P();
		if (state != BarrierState.OFF) { 
			state = BarrierState.SHUTDOWN;
		}
		stateSem.V();
	}
	
	public void turnOff() throws InterruptedException {		
		stateSem.P();
		state = BarrierState.OFF;
		stateSem.V();
		
		waitingSem.P();
		for (int i = 0; i < CARS_NO; i++) {
			if (! waiting[i]) {
				arrived.V();
			}
		}
		waitingSem.V();
	}

	static enum BarrierState {
		
		ON, OFF, SHUTDOWN
		
	}
	
}