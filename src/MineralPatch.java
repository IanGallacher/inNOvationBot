import bwapi.Unit;

public class MineralPatch {
	private int numberOfWorkers = 0;
	Unit mineralPatch;
	// public float DistanceToBase;
	
	public MineralPatch(Unit m) {
		this.mineralPatch = m;
	}
	
	public int currentWorkerCount() {
		return numberOfWorkers;
	}
	
	public void assignWorker(Unit worker) {
		numberOfWorkers++;
    	worker.gather(this.mineralPatch, false);
	}
	
	public void removeWorker() {
		numberOfWorkers--;
	}

	// Wrapper for the bwapi get distance function.
	public int getDistance(Unit objectToMeasureDistanceWith) {
		int d = this.mineralPatch.getDistance(objectToMeasureDistanceWith);
		return d;
	}
	
	public int getX() {
		return mineralPatch.getX();
	}
	
	public int getY() {
		return mineralPatch.getY();
	}
}
