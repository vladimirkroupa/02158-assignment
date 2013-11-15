import java.util.HashSet;
import java.util.Set;

/**
 * {@link Alley} implementation using monitors that multiple cars going in the same direction.
 * Does not guarantee fairness!
 *  
 */
public class MonitorAlley extends Alley {
	
	private int numAlleyDown = 0;
	private int numAlleyUp = 0;
	private Set<Integer> carsInAlley = new HashSet<Integer>();	
	
	public MonitorAlley(CarDisplayI cd) {
		super(cd);
	}

	public synchronized void enter(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			while (numAlleyDown > 0) {
				this.wait();
			}
			carsInAlley.add(no);
			numAlleyUp++;
		} else if (isGoingCounterClockWise(no)) {
			while (numAlleyUp > 0) {
				this.wait();
			}
			carsInAlley.add(no);
			numAlleyDown++;
		}

	}

	public synchronized void leave(int no){
		if (isGoingClockWise(no)) {
			carsInAlley.remove(no);
			numAlleyUp--;
			if (numAlleyUp == 0) {
				this.notifyAll();
			}
		} else if (isGoingCounterClockWise(no)) {
			carsInAlley.remove(no);
			numAlleyDown--;
			if (numAlleyDown == 0) {
				this.notifyAll();
			}
		}
	}
	
	public synchronized void removeCar(int no){
		if(carsInAlley.contains(no)){
			leave(no);
		}
	}
	
}
