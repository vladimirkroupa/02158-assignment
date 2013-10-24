import java.util.ArrayList;
import java.util.List;

public class Alley {

    Semaphore sem = new Semaphore(1);

    Pos alleyTop1 = new Pos(0, 0);
    Pos alleyTop2 = new Pos(1, 1);

    Pos alleyBottom1 = new Pos(9, 0);
    Pos alleyBottom2 = new Pos(8, 1);

    List<Pos> topPos;
    List<Pos> bottomPos;

    Alley() {
        topPos = new ArrayList<Pos>();
        topPos.add(alleyTop1);
        topPos.add(alleyTop2);
        bottomPos = new ArrayList<Pos>();
        bottomPos.add(alleyBottom1);
        bottomPos.add(alleyBottom2);
    }

    private boolean isGoingClockWise(int carNo) {
        return (carNo < 5);
    }

    private boolean isGoingCounterClockWise(int carNo) {
        return (carNo >= 5);
    }

    public boolean isAboutToEnter(int carNo, Pos curCarPos) {
        boolean fromBottom = bottomPos.contains(curCarPos) && isGoingClockWise(carNo);
        boolean fromTop = topPos.contains(curCarPos) && isGoingCounterClockWise(carNo);
        return fromBottom || fromTop;
    }

    public boolean hasLeft(int carNo, Pos curCarPos) {
        boolean leavingTop = topPos.contains(curCarPos) && isGoingClockWise(carNo);
        boolean leavingBottom =  bottomPos.contains(curCarPos) && isGoingCounterClockWise(carNo);
        return leavingBottom || leavingTop;
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