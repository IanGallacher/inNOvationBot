package Macro;
import bwapi.Unit;

public class MineralPatch {
	private int numberOfWorkers = 0;
	private Unit _mineralPatch;
	public Unit getMineralPatch() { return _mineralPatch; }
	// public float DistanceToBase;
	
	public MineralPatch(Unit m) {
		this._mineralPatch = m;
	}
	
	public int currentWorkerCount() {
		return numberOfWorkers;
	}
	
	public void assignWorker(Unit worker) {
		numberOfWorkers++;
    	worker.gather(this.getMineralPatch(), false);
	}
	
	public void removeWorker() {
		numberOfWorkers--;
	}

	// Wrapper for the bwapi get distance function.
	public int getDistance(Unit objectToMeasureDistanceWith) {
		int d = this.getMineralPatch().getDistance(objectToMeasureDistanceWith);
		return d;
	}
	
	public int getX() {
		return _mineralPatch.getX();
	}
	
	public int getY() {
		return _mineralPatch.getY();
	}
}
