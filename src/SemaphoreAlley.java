/**
 * Semaphore implementation of {@link Alley} that permits several cars going in the same direction at a time. 
 *  
 */
public class SemaphoreAlley extends Alley {

	private int numAlleyDown;
	private int numAlleyUp;
	
	private Semaphore sem = new Semaphore(1);

	private Semaphore numAlleyDownSema = new Semaphore(1);
	private Semaphore numAlleyUpSema = new Semaphore(1);

	public SemaphoreAlley(CarDisplayI cd) {
		super(cd);
		numAlleyDown = 0;
		numAlleyUp = 0;
	}

	@Override
	public void enter(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			numAlleyUpSema.P();
			numAlleyUp++;
			if (numAlleyUp == 1) {
				sem.P();
			}
			numAlleyUpSema.V();

		}
		if (isGoingCounterClockWise(no)) {
			numAlleyDownSema.P();
			numAlleyDown++;
			if (numAlleyDown == 1) {
				sem.P();
			}
			numAlleyDownSema.V();
		}
	}

	@Override
	public void leave(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			numAlleyUpSema.P();
			numAlleyUp--;
			if (numAlleyUp == 0) {
				sem.V();
			}
			numAlleyUpSema.V();
		}
		if (isGoingCounterClockWise(no)) {
			numAlleyDownSema.P();
			numAlleyDown--;
			if (numAlleyDown == 0) {
				sem.V();
			}
			numAlleyDownSema.V();
		}
	}
	
}