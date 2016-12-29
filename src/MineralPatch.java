import bwapi.Unit;

public class MineralPatch {
	private int numberOfWorkers = 0;
	Unit mineralPatch;
	// public float DistanceToBase;

	
	public int CurrentWorkerCount() {
		return numberOfWorkers;
	}
	
	public MineralPatch(Unit m) {
		this.mineralPatch = m;
	}
	
	public void AssignWorker(Unit worker) {
		numberOfWorkers++;
    	worker.gather(this.mineralPatch, false);
	}
	
	public void RemoveWorker() {
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
