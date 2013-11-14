import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alley base class. Knows the position of the cars.
 *
 */
public abstract class Alley {
	
	private Map<Pos, List<Integer>> entries = new HashMap<>();
	private Map<Pos, List<Integer>> exits = new HashMap<>();

	protected CarDisplayI cd;
	
	/**
	 * Predefined positions for "Red" alley.
	 */
	public void initRedPositions() {
		addMapPosEntry(entries, new Pos(2, 1), 1, 2);
		addMapPosEntry(entries, new Pos(1, 2), 3, 4);
		addMapPosEntry(entries, new Pos(10, 0), 5, 6, 7, 8);
		
		addMapPosEntry(exits, new Pos(9, 1), 1, 2, 3, 4);
		addMapPosEntry(exits, new Pos(1, 0), 5, 6, 7, 8);
	}
	
	/**
	 * Predefined positions for "Blue" alley.
	 */
	public void initBluePositions() {
		addMapPosEntry(entries, new Pos(1, 1), 3, 4);
		addMapPosEntry(entries, new Pos(1, 0), 5, 6, 7, 8);
		
		addMapPosEntry(exits, new Pos(1, 0), 3, 4);
		addMapPosEntry(exits, new Pos(1, 1), 5, 6, 7, 8);
	}
	
	Alley(CarDisplayI cd) {
		this.cd = cd;
	}

	/**
	 * Convenience method to fill a "multimap".
	 */
	private void addMapPosEntry(Map<Pos, List<Integer>> map, Pos pos, Integer... carNos) {
		List<Integer> carNoList = new ArrayList<>();
		for (Integer carNo : carNos) {
			carNoList.add(carNo);
		}
		map.put(pos, carNoList);
	}

	/**
	 * @param carNo car number
	 * @return true if car carNo is going in clockwise direction, false otherwise.
	 */
	protected boolean isGoingClockWise(int carNo) {
		return (carNo >= 5);
	}

	/**
	 * @param carNo car number
	 * @return true if car carNo is going in counterclockwise direction, false otherwise.
	 */
	protected boolean isGoingCounterClockWise(int carNo) {
		return (carNo < 5);
	}

	/**
	 * @param carNo number of the car
	 * @param curCarPos current position of the car
	 * @return true if car carNo is located on the entrance position of the alley. 
	 */
	public boolean isAboutToEnter(int carNo, Pos curCarPos) {
		List<Integer> entriesForCar = this.entries.get(curCarPos);
		return entriesForCar != null && entriesForCar.contains(carNo);
	}

	/**
	 * @param carNo number of the car
	 * @param curCarPos current position of the car
	 * @return true if car carNo is located on the exit position of the alley. 
	 */
	public boolean hasLeft(int carNo, Pos curCarPos) {
		List<Integer> exitsForCar = this.exits.get(curCarPos);
		return exitsForCar != null && exitsForCar.contains(carNo);
	}

	/**
	 * TODO javadoc
	 * @param no number of the car
	 */
	public abstract void enter(int no) throws InterruptedException;

	public abstract void leave(int no) throws InterruptedException;
	
	public abstract void removeCar(int no) throws InterruptedException;
	
	
}
