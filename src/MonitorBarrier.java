/**
 * Barrier implementation using single monitor. 
 *
 */
public class MonitorBarrier extends Barrier {

	private BarrierState state;
	
	private boolean[] waiting = new boolean[CARS_NO];
	
	public MonitorBarrier() {

		initWaiting();
		
		state = BarrierState.OFF;
	}

	private void initWaiting() {
		for (int i = 0; i < waiting.length; i++) {
			waiting[i] = false;
		}
	}
	
	private int numberWaiting() {
		int count = 0;
		for (boolean b : waiting) {
			if (b) {
				count++;
			}
		}
		return count;
	}		
			
	@Override
	public synchronized void arrive(int carNo) {								
		if (numberWaiting() < CARS_NO - 1) {			
			try {
				waiting[carNo] = true;
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		} else {
			this.notifyAll();
			
			if (state == BarrierState.SHUTDOWN) {
				state = BarrierState.OFF;
			}
		}
		
		waiting[carNo] = false;
	}
	
	@Override
	public void run() {		
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
	
	@Override
	public synchronized void turnOff() {		
		state = BarrierState.OFF;
		this.notifyAll();
	}

	static enum BarrierState {
		
		ON, OFF, SHUTDOWN
		
	}
	
}