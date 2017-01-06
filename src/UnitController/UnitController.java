package UnitController;
import java.util.ArrayList;
import java.util.HashMap;

import bwapi.*;
import bwta.BWTA;

import Debug.DebugController;
import Globals.Globals;
import Information.InformationManager;
import Macro.GasGeyser;
import Macro.MineralPatch;

public class UnitController {
	// The static UnitController controlls ALL instances of UnitControllers. The
	// unitController instance controlls an individual unit.
	
	// DONT FORGET TO KEEP BOTH WORKER'S AND _unitController UP TO DATE. 
	// This redundancy is fast, but it is easy to break. That is why it is so important to eventually make workers private.
	private static HashMap<Integer, UnitController> _unitController = new HashMap<Integer, UnitController>();

	// TODO: Make the following private.
	public static HashMap<Integer, UnitController> workers = new HashMap<Integer, UnitController>();
	// TODO: Make the following private.
	public static HashMap<Integer, UnitController> gasWorkers = new HashMap<Integer, UnitController>();

	Unit _thisUnit;
	public Unit getUnit() { return _thisUnit; }
	
	
	
	public enum JobType { Idle, GatherMinerals, GatherGas, Combat, Build };
	
	private UnitType _buildingToBuild;
	private TilePosition _buildingToBuildLocation;

	public MineralPatch gatheringMineralPatch = null;
	public GasGeyser gatheringGasGeyser = null;
	
	public int combatSquad = -1;
	
	private JobType _job = JobType.Idle;
	public JobType getJob() { return _job; }
	

	public static void put(int unitID, UnitController uc) {
		_unitController.put(unitID, uc);
	}

	public static UnitController get(int unitID) {
		return _unitController.get(unitID);
	}

	
	// Execute tasks based on the current job of all units
	public static void controllAllUnits() {
		for (UnitController unit : _unitController.values()) {

			// if it's an idle worker that is not being trained, send it to the closest mineral patch
			if (unit.getType().isWorker() && unit.getJob() == JobType.Idle && unit._thisUnit.isCompleted()) {
				unit.assignToHarvest();
			}

			if (unit.getType() == UnitType.Protoss_Zealot && unit.combatSquad < 0) {
				unit.combatSquad = 1;
				unit.assignToCombat();
			}
			
			if(unit.getType().isWorker() && unit.getJob() == JobType.Build) {
				unit.buildBuilding();
			}
		}

		CombatController.commandCombatUnits();
	}

	// Public functions for UnitController instances.
	public UnitController(Unit unit) {
		_thisUnit = unit;
	}

	public void assignToCombat() {
		this._job = JobType.Combat;
		CombatController.assignToSquad(this);
	}
	


	public void assignToHarvest() {
		assert _thisUnit.getType() == UnitType.Protoss_Probe;
		if(gasWorkers.size() < 3 * InformationManager.getUnitCount(UnitType.Protoss_Assimilator))
		{
			this._job = JobType.GatherGas;
			gatheringMineralPatch = Globals.BaseData.findOptimalAssimilator(_thisUnit);
			gasWorkers.put(this._thisUnit.getID(), this);
		} else {
			this.assignToMinerals();
			this.gatherMinerals();
		}
	}

	public void assignToMinerals() {
		this._job = JobType.GatherMinerals;
		assert _thisUnit.getType() == UnitType.Protoss_Probe;
		gatheringMineralPatch = Globals.BaseData.findOptimalMineralPatch(_thisUnit);
		workers.put(this._thisUnit.getID(), this);
	}

	public void assignToGas() {
		this._job = JobType.GatherGas;
		gatheringMineralPatch = Globals.BaseData.findOptimalAssimilator(_thisUnit);
		gasWorkers.put(this._thisUnit.getID(), this);
	}
	
	private void buildBuilding() {
		DebugController.debugConsolePrint("THING", this._thisUnit.isIdle());
		if(this._thisUnit.isIdle()) 
		{
			this._thisUnit.build(_buildingToBuild,_buildingToBuildLocation);
			this.assignToMinerals();
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
		if (this.gatheringMineralPatch == null) {
			MineralPatch m = Globals.BaseData.findOptimalMineralPatch(_thisUnit);
			this.gatheringMineralPatch = m;
			m.assignWorker(_thisUnit);
		} else {
			_thisUnit.gather(UnitController.get(_thisUnit.getID()).gatheringMineralPatch.getMineralPatch());
		}
	}

	public void gatherGas() {
		// The mineralPatch assign unit function keeps track of how many units
		// are mining. If the unit is already mining from patch, bypass that
		// function.
		
		// TODO: if unit.gettarget is not the same as the targetMineralPatch, do something about it.
		if (this.gatheringGasGeyser == null) {
			MineralPatch m = Globals.BaseData.findOptimalAssimilator(_thisUnit);
			this.gatheringMineralPatch = m;
			m.assignWorker(_thisUnit);
		} else {
			_thisUnit.gather(UnitController.get(_thisUnit.getID()).gatheringGasGeyser.getAssimilator());
		}
	}

	public void stopTask() {
		if(this.gatheringMineralPatch != null) 
		{
			this.gatheringMineralPatch.removeWorker();
		}
//		this.workers.remove(this.thisUnit.getID());
		this.gatheringMineralPatch = null;
		this._job = JobType.Idle;
	}

	public UnitType getType() {
		return _thisUnit.getType();
	}

	public boolean isIdle() {
		return _thisUnit.isIdle();
	}
	
	public boolean isTraining() {
		return _thisUnit.isTraining();
	}
	


	
	

	protected static class CombatSquad {
		private ArrayList<Unit> _squad;

		public CombatSquad() {
			_squad = new ArrayList<Unit>();
		}

		public boolean isFull() {
			if(_squad.size() >= 15) return true;
			else return false;
		}

		public void assignToSquad(Unit unit) {
			_squad.add(unit);
		}

		public void removeFromSquad(Unit unit) {
			_squad.remove(unit);
		}
		public int troopsInSquad() {
			return _squad.size();
		}
		
		public Position calcCenter() {
			int xAccum = 0;
			int yAccum = 0;
			for (Unit unit : _squad)
			{
				xAccum += unit.getPosition().getX();
				yAccum += unit.getPosition().getY();
			}
			return new Position(xAccum / _squad.size(), yAccum / _squad.size());
		}
		

		boolean hasAttacked = false;
		public void attackEnemyBase(int baseNumber) {
			for (Unit unit : _squad) {
				if(hasAttacked == false)
				{
					unit.attack(InformationManager.getMainBaseLocation(Globals.enemy).toPosition());

					// bwapi.UnitCommand c = new bwapi.UnitCommand(unit,
					// bwapi.UnitCommandType.Attack_Move, , 1, 4,4);
					// unit.issueCommand(UnitCommanType.)
				} else if (unit.isIdle()) {
					Unit attackTarget = (Unit) InformationManager.getEnemeyBuildings().values().toArray()[0];
					unit.attack(attackTarget.getPosition());
					System.out.println("FINISH HIM");
				}
			} 
			
			if(hasAttacked == false) hasAttacked = true;
		}
	}

	// controlls squads of units. Once we get 15 zealots, move out with them.
	protected static class CombatController {

		protected static HashMap<Integer, CombatSquad> _squadManager = new HashMap<Integer, CombatSquad>();
		
		private static int currentSquadToAssignTroopsTo = 1;

		public static void assignToSquad(UnitController unit) {

			// If the squad we want to assign troops to does not exist, create it. 
			if (_squadManager.containsKey(currentSquadToAssignTroopsTo) != true) {
				CombatSquad c = new CombatSquad();
				_squadManager.put(currentSquadToAssignTroopsTo, c);
			}
			
			// If the squad is full, start assigning troops to the next squad. This must be called 
			if(_squadManager.get(currentSquadToAssignTroopsTo).isFull()) { currentSquadToAssignTroopsTo++; }
			
			
			_squadManager.get(currentSquadToAssignTroopsTo).assignToSquad(unit._thisUnit);
		}

		public static void removeFromSquad(UnitController unit) {
			if (_squadManager.containsKey(1) != true) {
				CombatSquad c = new CombatSquad();
				_squadManager.put(1, c);
			}
//			
//			for (CombatSquad squad : _squadManager.values()) {
//				if (squad.containsKey(1) == true) {
//					CombatSquad c = new CombatSquad();
//					_squadManager.put(1, c);
//				}
//			}

			_squadManager.get(1).removeFromSquad(unit._thisUnit);
		}
		
		public static void commandCombatUnits() {
			for (CombatSquad squad : _squadManager.values()) {
				if (squad.troopsInSquad() >= 15) {
					squad.attackEnemyBase(0);
				}
			}
		}
	}
}
