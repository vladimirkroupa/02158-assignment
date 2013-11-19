import java.util.HashSet;

/**
 * Semaphore implementation of {@link Alley} that permits several cars going in
 * the same direction at a time.
 * 
 */
public class SemaphoreAlley extends Alley {

	private int numAlleyDown;
	private int numAlleyUp;

	private Semaphore sem = new Semaphore(1);

	private Semaphore numAlleyDownSema = new Semaphore(1);
	private Semaphore numAlleyUpSema = new Semaphore(1);

	private HashSet<Integer> carsInAlley = new HashSet<Integer>();

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
		carsInAlley.add(no);
	}

	@Override
	public void leave(int no) {
		if (isGoingClockWise(no)) {
			try {
				numAlleyUpSema.P();
				numAlleyUp--;
				if (numAlleyUp == 0) {
					sem.V();
				}
				numAlleyUpSema.V();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		if (isGoingCounterClockWise(no)) {
			try {
				numAlleyDownSema.P();
				numAlleyDown--;
				if (numAlleyDown == 0) {
					sem.V();
				}
				numAlleyDownSema.V();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		carsInAlley.remove(no);
	}

}