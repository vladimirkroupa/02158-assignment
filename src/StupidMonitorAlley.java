/**
 * {@link Alley} implementation using monitors. Permits only one car at a time.
 *  
 */
public class StupidMonitorAlley extends Alley {

	private boolean occupied;
	
	public StupidMonitorAlley(CarDisplayI cd) {
		super(cd);
		occupied = false;
	}
	
	@Override
	public synchronized void enter(int no) throws InterruptedException {
		if (occupied) {
			this.wait();
		}
		occupied = true;
	}

	@Override
	public synchronized void leave(int no) {
		occupied = false;
		this.notifyAll();
	}
	
}

