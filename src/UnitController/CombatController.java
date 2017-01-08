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

public class CombatController {
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
		
		
		_squadManager.get(currentSquadToAssignTroopsTo).assignToSquad(unit);
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

		_squadManager.get(1).removeFromSquad(unit);
	}
	
	public static void commandCombatUnits() {
		for (CombatSquad squad : _squadManager.values()) {
			if (squad.troopsInSquad() >= 15) {
				squad.attackEnemyBase(0);
			}
		}
	}
}
