/**
 * {@link Alley} implementation using monitors that multiple cars going in the same direction.
 * Does not guarantee fairness!
 *  
 */
public class MonitorAlley extends Alley {
	
	private int numAlleyDown = 0;
	private int numAlleyUp = 0;
	
	public MonitorAlley(CarDisplayI cd) {
		super(cd);
	}

	public synchronized void enter(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			if (numAlleyDown > 0) {
				this.wait();
			}
			numAlleyUp++;
		} else if (isGoingCounterClockWise(no)) {
			if (numAlleyUp > 0) {
				this.wait();
			}
			numAlleyDown++;
		}

	}

	public synchronized void leave(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			numAlleyUp--;
			if (numAlleyUp == 0) {
				this.notifyAll();
			}
		} else if (isGoingCounterClockWise(no)) {
			numAlleyDown--;
			if (numAlleyDown == 0) {
				this.notifyAll();
			}
		}
	}
	
}
