
/**
 * {@link Alley} implementation using monitors that multiple cars going in the same direction.
 * Does not guarantee fairness!
 *  
 */
public class FairMonitorAlley extends Alley {

	private int numAlleyDown = 0;
	private int numAlleyUp = 0;

	static final int PASS_DIFF_THRESHOLD = 4;
	
	private Integer passDirectionDiff = 0;
	private Integer waitingCW = null;
	private Integer waitingCCW = null;
	
	public FairMonitorAlley(CarDisplayI cd) {
		super(cd);
	}

	public synchronized void enter(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			enterCW(no);			
		} else if (isGoingCounterClockWise(no)) {
			enterCCW(no);
		}
	}
	
	public synchronized void leave(int no) throws InterruptedException {
		if (isGoingClockWise(no)) {
			leaveCW(no);
		} else if (isGoingCounterClockWise(no)) {
			leaveCCW(no);
		}		
	}
	
	private boolean tooMuchPassesCW(int carNo) {
		if (waitingCCW == null) {
			return false;
		}
		return (passDirectionDiff >= PASS_DIFF_THRESHOLD);  
	}
	
	private boolean tooMuchPassesCCW(int carNo) {
		if (waitingCW == null) {
			return false;
		}
		return (passDirectionDiff <= -PASS_DIFF_THRESHOLD);
	}	
	
	private synchronized void enterCW(int no) throws InterruptedException {
		while (numAlleyDown > 0 || tooMuchPassesCW(no)) {
			waitingCW = no;
			this.wait();
		}
		numAlleyUp++;
		passDirectionDiff++;
	}
	
	private synchronized void enterCCW(int no) throws InterruptedException {		
		while (numAlleyUp > 0 || tooMuchPassesCCW(no)) {
			waitingCCW = no;
			this.wait();
		}
		numAlleyDown++;
		passDirectionDiff--;
	}
	
	private synchronized void leaveCW(int no) throws InterruptedException {
		waitingCW = null;
		numAlleyUp--;
		if (numAlleyUp == 0) {
			this.notifyAll();			
		}
	}
	
	private synchronized void leaveCCW(int no) throws InterruptedException {
		waitingCCW = null;
		numAlleyDown--;
		if (numAlleyDown == 0) {
			this.notifyAll();			
		}
	}
	
}
