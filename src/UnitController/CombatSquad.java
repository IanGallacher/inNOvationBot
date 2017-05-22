package UnitController;
import java.util.ArrayList;

import bwapi.*;
import Globals.Globals;
import Information.InformationManager;
import Information.UnitInfo;

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
	

	public void attackEnemyBase(int baseNumber) {
		for (UnitController unit : _squad) {
			if (unit.isIdle()) {
				UnitInfo attackTarget = (UnitInfo) InformationManager.getEnemeyBuildings().values().toArray()[0];
				unit.attack(attackTarget.getUnitPosition());
			}
		} 
	}
}
