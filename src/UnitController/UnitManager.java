package UnitController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import bwapi.*;
import bwta.BWTA;

import Debug.DebugController;
import Globals.Globals;
import Information.InformationManager;
import Macro.GasGeyser;
import Macro.MineralPatch;

public class UnitManager {
	// The static UnitController controls ALL instances of UnitControllers. The
	// unitController instance controls an individual unit.
	
	// DONT FORGET TO KEEP BOTH WORKER'S AND _unitController UP TO DATE. 
	// This redundancy is fast, but it is easy to break. That is why it is so important to eventually make workers private.
	private static HashMap<Integer, UnitController> _unitControllers = new HashMap<Integer, UnitController>();
	// TODO: Make the following private.
	public static HashMap<Integer, UnitController> mineralWorkers = new HashMap<Integer, UnitController>( );
	
	
	public static Collection<UnitController> GetAllUnitControllers()
	{
		return _unitControllers.values();
	}
	
	

	// Called inside the onUnitCreate override.
	public static void put(int unitID, UnitController uc) {
		_unitControllers.put(unitID, uc);
	}

	public static UnitController get(int unitID) {
		return _unitControllers.get(unitID);
	}
	
	public static void NOTIFYDESTRUCTION(int unitID)
	{
		_unitControllers.remove(unitID);
	}

	
	// Execute tasks based on the current job of all units
	public static void controllAllUnits() {
		for (UnitController unit : _unitControllers.values()) {

			// if it's an idle worker that is not being trained, send it to the closest mineral patch
			if (unit.getType().isWorker() && unit.getJob() == JobType.Idle && unit.isCompleted()) {
				unit.assignToHarvest();
			}
			
			
			// If our units forgot what to do for some reason, go remind them.
			if(unit.isIdle()) {
				if(unit.getJob() == JobType.GatherGas) {
					unit.gatherGas();
				} else if(unit.getJob() == JobType.GatherMinerals) {
					unit.gatherMinerals();
				}
			}
			
			
			

			if (unit.getType() == UnitType.Protoss_Dragoon && unit.combatSquad < 0) {
				unit.combatSquad = 1;
				unit.assignToCombat();
			}
			
			if(unit.getType().isWorker() && unit.getJob() == JobType.Build) {
				unit.buildAssignedBuilding();
			}
		}

		CombatController.commandCombatUnits();
	}
}
