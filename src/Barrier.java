import java.util.HashMap;

public class Barrier extends Thread {

	Semaphore[] barrierSemaArr = new Semaphore[9];
	Semaphore cont = new Semaphore(0);

    HashMap<Pos,Integer> entries = new HashMap<>();
    
    boolean barrierOn = false;
    	
	public Barrier() {
		for (int i = 0; i < barrierSemaArr.length; i++) {
			barrierSemaArr[i] = new Semaphore(0);
		}
		
		entries.put(new Pos(5,3),new Integer(0));
		entries.put(new Pos(6,4),new Integer(1));
		entries.put(new Pos(6,5),new Integer(2));
		entries.put(new Pos(6,6),new Integer(3));
		entries.put(new Pos(6,7),new Integer(4));
		entries.put(new Pos(5,8),new Integer(5));
		entries.put(new Pos(5,9),new Integer(6));
		entries.put(new Pos(5,10),new Integer(7));
		entries.put(new Pos(5,11),new Integer(8));

	}
	
	

	//TO BE CALLED BY BARRIER
	
	public void on() {
		this.start();
		this.barrierOn = true;
	} // Activate barrier

	public void off() {
		
		this.barrierOn = false;
		barrierSemaArr = new Semaphore[9];
		for (int i = 0; i < barrierSemaArr.length; i++) {
			barrierSemaArr[i] = new Semaphore(0);
		}		
		cont = new Semaphore(0);
		this.interrupt();
	} // Deactivate barrier

	
	public void run() {
		while(true){
			try {
				for (int i = 0; i < barrierSemaArr.length; i++) {				
					System.out.println("In BARRIER RUN barrierSemaArr "+ i + ":" +barrierSemaArr[i] );					
					barrierSemaArr[i].P(); // wait for all
				}
				for (int i = 0; i < barrierSemaArr.length; i++) {
					System.out.println("In BARRIER RUN  cont :" + cont );
					cont.V(); //signal all
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	//TO BE CALLED BY CARS
	public void sync(int carNo){
		try {
			System.out.println("Called barrier sync by " + carNo);
			barrierSemaArr[carNo].V();
			System.out.println("barrierSemaArr "+ carNo + ":" +barrierSemaArr[carNo] );
			cont.P();
			System.out.println("cont :" + cont );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean isInfrontOfBarrier(int carNo, Pos curCarPos){
		return barrierOn && entries != null && entries.get(curCarPos) !=null 
				&& entries.get(curCarPos) == carNo;
	}

}
