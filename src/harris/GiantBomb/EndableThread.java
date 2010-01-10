package harris.GiantBomb;

public class EndableThread extends Thread {
	protected boolean isDone = false;
	
	public void end() {
		isDone = true;
	}

}
