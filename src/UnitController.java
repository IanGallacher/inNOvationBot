import java.util.ArrayList;
import java.util.HashMap;

import bwapi.*;
import bwta.BWTA;

public class UnitController {
	
	protected static class CombatSquad {
		private ArrayList<Unit> _squad;
		
		public CombatSquad() {
			_squad = new ArrayList<Unit>();
		}
		
		public void AssignToSquad(Unit unit) {
			_squad.add(unit);
		}
		
		public int TroopsInSquad() {
			return _squad.size();
		}
		
		public void AttackEnemy() {
			for ( Unit unit : _squad ) {
				unit.move(InformationManager.getMainBaseLocation(Globals.enemy).toPosition());
				
//				bwapi.UnitCommand c = new bwapi.UnitCommand(unit, bwapi.UnitCommandType.Attack_Move, , 1, 4,4);
//				unit.issueCommand(UnitCommanType.)
			}
		}
		
	}
	
	protected static class CombatController {
		
		protected static HashMap<Integer, CombatSquad> _squadManager = new HashMap<Integer, CombatSquad>();

		
		
		public static void AssignToSquad(UnitController unit) {
			if ( _squadManager.containsKey(1) != true ) {
				CombatSquad c = new CombatSquad();
				_squadManager.put(1, c);
			}
			
			_squadManager.get(1).AssignToSquad(unit.thisUnit);
		}
		
		public static void CommandCombatUnits() {
			for ( CombatSquad squad : _squadManager.values() )
			{
				if(squad.TroopsInSquad() > 10) {
					// System.out.println("ready to attack");
					squad.AttackEnemy();
				}
				
			}
		}
	}
	
	
	
	// The static UnitController controlls ALL instances of UnitControllers. The unitController instance controlls an individual unit. 
    private static HashMap<Integer, UnitController> _unitController = new HashMap<Integer, UnitController>();
    
	Unit thisUnit;
	
	public MineralPatch gatheringMineralPatch;
	public int combatSquad = -1;
    
	
	
	
    public static void put(int unitID, UnitController uc) {
    	_unitController.put(unitID, uc);
    }
    
    public static UnitController get(int unitID) {
    	return _unitController.get(unitID);
    }

    
	public static void ControllAllUnits() {
		for (UnitController unit : _unitController.values()) {
			
	        // if it's a worker and it's idle, send it to the closest mineral patch
	        if (unit.getType().isWorker() && unit.isIdle()) {
	        	unit.AssignToMinerals();
	        	unit.GatherMinerals();
	        }

			if (unit.getType() == UnitType.Terran_Marine && unit.combatSquad < 0) {
				System.out.println("ASSIGNED TO COMBAT");
				unit.combatSquad = 1;
	        	unit.AssignToCombat();
	        }
			
			CombatController.CommandCombatUnits();
		}
	}
	
	
	
	// Public functions for UnitController instances.
	public UnitController(Unit unit)
	{
		thisUnit = unit;
	}

	
	public void AssignToCombat() {
		CombatController.AssignToSquad(this);
	}
	
	public void AssignToMinerals() {
    	assert thisUnit.getType() == UnitType.Terran_SCV;
    	MineralPatch m = Globals.BaseData.FindOptimalMineralPatch(thisUnit);
    }
	
	public void GatherMinerals() {
		// The mineralPatch assign unit function keeps track of how many units are mining. If the unit is already mining from patch, bypass that function.
    	if(this.gatheringMineralPatch == null)
    	{
        	MineralPatch m = Globals.BaseData.FindOptimalMineralPatch(thisUnit);
    		this.gatheringMineralPatch = m;
    		m.AssignWorker(thisUnit);
    	}
    	else 
    	{
        	thisUnit.gather(UnitController.get(thisUnit.getID()).gatheringMineralPatch.mineralPatch);
    	}
	}
	
	public void StopTask() {
		this.gatheringMineralPatch.RemoveWorker();
		this.gatheringMineralPatch = null;
	}
	
	public UnitType getType() {
		return thisUnit.getType();
	}
	
	public boolean isIdle() {
		return thisUnit.isIdle();
	}
}
