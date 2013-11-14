/**
 * Abstract barrier that knows the positions of the cars in front of itself. 
 * Intended to be used as a coordinating thread.
 *
 */
public abstract class Barrier extends Thread {

	static final int CARS_NO = 9;
	
	protected Pos[] barrierPositions = new Pos[CARS_NO];
	
	public Barrier() {
		initPositions();
	}	
	
	private void initPositions() {
		barrierPositions[0] = new Pos(6, 3);
		barrierPositions[1] = new Pos(6, 4);
		barrierPositions[2] = new Pos(6, 5);
		barrierPositions[3] = new Pos(6, 6);
		barrierPositions[4] = new Pos(6, 7);
		barrierPositions[5] = new Pos(5, 8);
		barrierPositions[6] = new Pos(5, 9);
		barrierPositions[7] = new Pos(5, 10);
		barrierPositions[8] = new Pos(5, 11);		
	}

	/**
	 * Signals the barrier that a car is trying to enter it. 
	 * This is the only method that should be called from {@link CarControl}. 
	 * 
	 * @param carNo car number
	 * @param curCarPos current position of the car
	 */
	public void checkBarrierPosition(int carNo, Pos curCarPos) throws InterruptedException {
		if (isOn() && isInfrontOfBarrier(carNo, curCarPos)) {
			arrive(carNo);
		}
	}
	
	/**
	 * Checks if the car is located in front of the barrier.
	 * 
	 * @param carNo car number
	 * @param curCarPos current position of the car
	 * @return true if carNo is in front of the barrier
	 */
	protected boolean isInfrontOfBarrier(int carNo, Pos curCarPos) {
		return barrierPositions[carNo].equals(curCarPos);
	}

	/**
	 * Called when a car arrives at the barrier.
	 * 
	 * @param carNo number of the car.
	 */
	protected abstract void arrive(int carNo) throws InterruptedException;
	
	/**
	 * @return true if barrier should register incoming cars, false otherwise.
	 */
	protected abstract boolean isOn() throws InterruptedException;

	/**
	 * Turns barrier on.
	 */
	public abstract void turnOn() throws InterruptedException;
	
	/**
	 * Turns barrier off after this round is complete.
	 */
	public abstract void shutdown() throws InterruptedException;
	
	/**
	 * Turns barrier off immediately.
	 */
	public abstract void turnOff() throws InterruptedException;
	
}
