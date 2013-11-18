import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

class Bridge {

	private int numBridgelimit = 4;
	private int numBridge = 0;
	Map<Pos, List<Integer>> entries = new HashMap<>();
	Map<Pos, List<Integer>> exits = new HashMap<>();
	
	HashSet<Integer> carsOnBridge = new HashSet<Integer>();

	public Bridge() {
		addMapPosEntry(entries, new Pos(9, 0), 1, 2, 3, 4);
		addMapPosEntry(entries, new Pos(10, 4), 5, 6, 7, 8);

		addMapPosEntry(exits, new Pos(9, 4), 1, 2, 3, 4);
		addMapPosEntry(exits, new Pos(10, 0), 5, 6, 7, 8);
	}

	public synchronized void enter(int no) throws InterruptedException {
		if (numBridge >= numBridgelimit) {
			this.wait();
			numBridge++;
		} else {
			numBridge++;
		}
		carsOnBridge.add(no);
	}

	public synchronized void leave(int no){
		numBridge--;
		carsOnBridge.remove(no);
		this.notify();
	}

	boolean isInforntofBridge(int carNo, Pos pos) {
		List<Integer> entriesForCar = this.entries.get(pos);
		return entriesForCar != null && entriesForCar.contains(carNo);
	}

	boolean hasLeft(int carNo, Pos pos) {
		List<Integer> exitsForCar = this.exits.get(pos);
		return exitsForCar != null && exitsForCar.contains(carNo);
	}

	public void setLimit(int k) {
		numBridgelimit = k;
	}

	protected boolean isGoingCounterClockWise(int carNo) {
		return (carNo < 5);
	}

	protected boolean isGoingClockWise(int carNo) {
		return (carNo >= 5);
	}

	private void addMapPosEntry(Map<Pos, List<Integer>> map, Pos pos, Integer... carNos) {
		List<Integer> carNoList = new ArrayList<>();
		for (Integer carNo : carNos) {
			carNoList.add(carNo);
		}
		map.put(pos, carNoList);
	}
	
	public void removeCar(int no){
		if(carsOnBridge.contains(no)){
			leave(no);
		}
	}
}
