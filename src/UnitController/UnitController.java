package UnitController;

import bwapi.*;
import Debug.DebugController;
import Information.BaseInfo;
import Information.InformationManager;
import Macro.GasGeyser;
import Macro.MineralPatch;

public class UnitController {
	private Unit _thisUnit;
	public Unit getUnit() { return _thisUnit; }
	
	
	private UnitType _buildingToBuild;
	private TilePosition _buildingToBuildLocation;
	
	private MineralPatch _gatheringMineralPatch = null;
	private GasGeyser _gatheringGasGeyser = null;
	
	public int combatSquad = -1;
	
	private JobType _job = JobType.Idle;
	public JobType getJob() { return _job; }
	
	
	// Public functions for UnitController instances.
	public UnitController(Unit unit) {
		_thisUnit = unit;
	}

	public void assignToCombat() {
		this._job = JobType.Combat;
		CombatController.assignToSquad(this);
	}
	


	public void assignToHarvest() {
		// Ask the nearest base what resource do I need to harvest?
		assert _thisUnit.getType() == UnitType.Protoss_Probe;
		
		
		BaseInfo bi = InformationManager.getClosestUnsaturatedBase( _thisUnit.getPosition() );
		
		
		if(bi.getWorkersOnGasCount() < 3 * InformationManager.getUnitCount(UnitType.Protoss_Assimilator))
		{
			this._job = JobType.GatherGas;
			this.gatherGas();
		} else {
			this.assignToMinerals();
		}
	}

	public void assignToMinerals() {
		this._job = JobType.GatherMinerals;
		this.gatherMinerals();
	}
	
	// This will build the building the unit has been assigned to build. 
	public void buildAssignedBuilding() {
		DebugController.debugConsolePrint("THING", this._thisUnit.isIdle());
		if(this._thisUnit.isIdle()) 
		{
			if(Globals.Globals.self.minerals() <= _buildingToBuild.mineralPrice())
				return;
			if(this._thisUnit.build(_buildingToBuild, _buildingToBuildLocation))
				return;
				//this.assignToMinerals();
		}
	}
	
	public void buildBuilding(UnitType building, TilePosition p) {
		stopTask();
		_thisUnit.move(p.toPosition());
		this._buildingToBuild = building;
		this._buildingToBuildLocation = p;
		this._job = JobType.Build;
	}

	public void gatherMinerals() {
		// The mineralPatch assign unit function keeps track of how many units
		// are mining. If the unit is already mining from patch, bypass that
		// function.
		
		// TODO: if unit.gettarget is not the same as the targetMineralPatch, do something about it.
		if (this._gatheringMineralPatch == null) {
			BaseInfo bi = InformationManager.getClosestUnsaturatedBase( _thisUnit.getPosition() );
			MineralPatch m = bi.findOptimalMineralPatch(_thisUnit);
			this._gatheringMineralPatch = m;
			UnitManager.mineralWorkers.put(_thisUnit.getID(), this);
			bi.mineralWorkers.put(_thisUnit.getID(), this);
			m.assignWorker(_thisUnit);
		} else {
			_thisUnit.gather(this._gatheringMineralPatch.getMineralPatch());
		}
	}

	public void gatherGas() {
		// The mineralPatch assign unit function keeps track of how many units
		// are mining. If the unit is already mining from patch, bypass that
		// function.
		
		// TODO: if unit.gettarget is not the same as the targetMineralPatch, do something about it.
		if (this._gatheringGasGeyser == null) {
			BaseInfo bi = InformationManager.getClosestUnsaturatedBase( _thisUnit.getPosition() );
			GasGeyser g = bi.findOptimalAssimilator(_thisUnit);
			this._gatheringGasGeyser = g;
			bi.gasWorkers.put(_thisUnit.getID(), this);
			g.assignWorker(_thisUnit);
		} else {
			_thisUnit.gather(this._gatheringGasGeyser.getAssimilator());
		}
	}

	public void stopTask() {
		if(this._gatheringMineralPatch != null) 
		{
			this._gatheringMineralPatch.removeWorker();
		}
//		this.workers.remove(this.thisUnit.getID());
		this._gatheringMineralPatch = null;
		this._job = JobType.Idle;
	}
	
	
	
	
	//////////////////////////////
	/// WRAPPER FOR bwapi.UNIT ///
	//////////////////////////////


	public boolean attack(Position pos) {
		return _thisUnit.attack(pos);
	}
	
	public UnitType getType() {
		return _thisUnit.getType();
	}
	
	public Position getPosition()
	{
		return _thisUnit.getPosition();
	}

	public boolean isCompleted() {
		return _thisUnit.isCompleted();
	}
	
	public boolean isIdle() {
		return _thisUnit.isIdle();
	}
	
	public boolean isTraining() {
		return _thisUnit.isTraining();
	}
}
