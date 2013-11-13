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

	public void checkBarrierPosition(int carNo, Pos curCarPos) {
		if (isOn() && isInfrontOfBarrier(carNo, curCarPos)) {
			arrive(carNo);
		}
	}
	
	protected boolean isInfrontOfBarrier(int carNo, Pos curCarPos) {
		return barrierPositions[carNo].equals(curCarPos);
	}

	protected abstract void arrive(int carNo);
	
	protected abstract boolean isOn();
	
	public abstract void turnOn();
	
	public abstract void shutdown();
	
	public abstract void turnOff();
	
}
