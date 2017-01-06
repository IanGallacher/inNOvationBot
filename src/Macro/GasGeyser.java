package Macro;
import bwapi.Unit;

public class GasGeyser {
	private int _numberOfWorkers = 0;
	private Unit _gasGeyser;
	public Unit getAssimilator() { return _gasGeyser; }
	// public float DistanceToBase;
	
	public GasGeyser(Unit m) {
		this._gasGeyser = m;
	}
	
	public int currentWorkerCount() {
		return _numberOfWorkers;
	}
	
	public void assignWorker(Unit worker) {
		_numberOfWorkers++;
    	worker.gather(this._gasGeyser, false);
	}
	
	public void removeWorker() {
		_numberOfWorkers--;
	}

	// Wrapper for the bwapi get distance function.
	public int getDistance(Unit objectToMeasureDistanceWith) {
		int d = this._gasGeyser.getDistance(objectToMeasureDistanceWith);
		return d;
	}
	
	public int getX() {
		return _gasGeyser.getX();
	}
	
	public int getY() {
		return _gasGeyser.getY();
	}
}
