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

public class CombatSquad {
	private ArrayList<UnitController> _squad;

	public CombatSquad() {
		_squad = new ArrayList<UnitController>();
	}

	public boolean isFull() {
		if(_squad.size() >= 15) return true;
		else return false;
	}

	public void assignToSquad(UnitController unit) {
		_squad.add(unit);
	}

	public void removeFromSquad(UnitController unit) {
		_squad.remove(unit);
	}
	public int troopsInSquad() {
		return _squad.size();
	}
	
	public Position calcCenter() {
		int xAccum = 0;
		int yAccum = 0;
		for (UnitController unit : _squad)
		{
			xAccum += unit.getPosition().getX();
			yAccum += unit.getPosition().getY();
		}
		return new Position(xAccum / _squad.size(), yAccum / _squad.size());
	}
	

	boolean hasAttacked = false;
	public void attackEnemyBase(int baseNumber) {
		for (UnitController unit : _squad) {
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
