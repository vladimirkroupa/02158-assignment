import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alley base class. 
 *
 */
public abstract class Alley {
	
	private Map<Pos, List<Integer>> entries = new HashMap<>();
	private Map<Pos, List<Integer>> exits = new HashMap<>();

	protected CarDisplayI cd;
	
	Alley(CarDisplayI cd) {
		this.cd = cd;
		
		addMapPosEntry(entries, new Pos(2, 1), 1, 2);
		addMapPosEntry(entries, new Pos(1, 2), 3, 4);
		addMapPosEntry(entries, new Pos(10, 0), 5, 6, 7, 8);

		addMapPosEntry(exits, new Pos(9, 1), 1, 2, 3, 4);
		addMapPosEntry(exits, new Pos(0, 1), 5, 6, 7, 8);
	}

	private void addMapPosEntry(Map<Pos, List<Integer>> map, Pos pos, Integer... carNos) {
		List<Integer> carNoList = new ArrayList<>();
		for (Integer carNo : carNos) {
			carNoList.add(carNo);
		}
		map.put(pos, carNoList);
	}

	protected boolean isGoingClockWise(int carNo) {
		return (carNo < 5);
	}

	protected boolean isGoingCounterClockWise(int carNo) {
		return (carNo >= 5);
	}

	public boolean isAboutToEnter(int carNo, Pos curCarPos) {
		List<Integer> entriesForCar = this.entries.get(curCarPos);
		return entriesForCar != null && entriesForCar.contains(carNo);
	}

	public boolean hasLeft(int carNo, Pos curCarPos) {
		List<Integer> exitsForCar = this.exits.get(curCarPos);
		return exitsForCar != null && exitsForCar.contains(carNo);
	}

	public abstract void enter(int no) throws InterruptedException;

	public abstract void leave(int no) throws InterruptedException;
	
}
