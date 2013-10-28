import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alley {

    Semaphore sem = new Semaphore(1);

    Map<Pos, List<Integer>> entries = new HashMap<>();
    Map<Pos, List<Integer>> exits = new HashMap<>();
    
   
    
    Alley() {
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

    private boolean isGoingClockWise(int carNo) {
        return (carNo < 5);
    }

    private boolean isGoingCounterClockWise(int carNo) {
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

    public void enter(int no) {
        try {
            sem.P();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void leave(int no) {
        sem.V();
    }
}