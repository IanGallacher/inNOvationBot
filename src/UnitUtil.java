// Again, giving credit where it is due. Almost the whole thing is copied & pasted from UAlbertaBot. 
// https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/UnitUtil.cpp

import java.awt.Rectangle;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwapi.WeaponType;

class UnitUtil
{
	boolean IsCombatUnit(Unit unit)
	{
	    assert unit != null;
	    if (unit == null)
	    {
	        return false;
	    }
	
	    // no workers or buildings allowed
	    if (unit != null && unit.getType().isWorker() || unit.getType().isBuilding())
	    {
	        return false;
	    }
	
	    // check for various types of combat units
	    if (unit.getType().canAttack() || 
	        unit.getType() == UnitType.Terran_Medic ||
	        unit.getType() == UnitType.Protoss_High_Templar ||
	        unit.getType() == UnitType.Protoss_Observer ||
	        unit.isFlying() && unit.getType().spaceProvided() > 0)
	    {
	        return true;
	    }
			
	    return false;
	}
	
	boolean IsValidUnit(Unit unit)
	{
	    if (unit == null)
	    {
	        return false;
	    }
	
	    if (unit.isCompleted() 
	        && unit.getHitPoints() > 0 
	        && unit.exists() 
	        && unit.getType() != UnitType.Unknown 
	        && unit.getPosition().getX() != Position.Unknown.getX() 
	        && unit.getPosition().getY() != Position.Unknown.getY()) 
	    {
	        return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	
	Rectangle GetRect(Unit unit)
	{
		Rectangle r = new Rectangle();
	
	    r.x = unit.getLeft();
	    r.y = unit.getTop();
	    r.height = unit.getBottom() - unit.getTop();
	    r.width = unit.getLeft() - unit.getRight();
	
	    return r;
	}
	
	double GetDistanceBetweenTwoRectangles(Rectangle rect1, Rectangle rect2)
	{
	    Rectangle mostLeft = rect1.x < rect2.x ? rect1 : rect2;
	    Rectangle mostRight = rect2.x < rect1.x ? rect1 : rect2;
	    Rectangle upper = rect1.y < rect2.y ? rect1 : rect2;
	    Rectangle lower = rect2.y < rect1.y ? rect1 : rect2;
	
	    int diffX = Math.max(0, mostLeft.x == mostRight.x ? 0 : mostRight.x - (mostLeft.x + mostLeft.width));
	    int diffY = Math.max(0, upper.y == lower.y ? 0 : lower.y - (upper.y + upper.height));
	    
	    return Math.sqrt( (float) (diffX*diffX + diffY*diffY));
	}
	
//	boolean CanAttack(Unit attacker, Unit target)
//	{
//	    return GetWeapon(attacker, target) != UnitType.None;
//	}
	
	boolean CanAttackAir(Unit unit)
	{
	    return unit.getType().airWeapon() != WeaponType.None;
	}
	
	boolean CanAttackGround(Unit unit)
	{
	    return unit.getType().groundWeapon() != WeaponType.None;
	}
	
	double CalculateLTD(Unit attacker, Unit target)
	{
	    WeaponType weapon = GetWeapon(attacker, target);
	
	    if (weapon == WeaponType.None)
	    {
	        return 0;
	    }
	
	    return (double) (weapon.damageAmount()) / weapon.damageCooldown();
	}
	
	WeaponType GetWeapon(Unit attacker, Unit target)
	{
	    return target.isFlying() ? attacker.getType().airWeapon() : attacker.getType().groundWeapon();
	}
	
	WeaponType GetWeapon(UnitType attacker, UnitType target)
	{
	    return target.isFlyer() ? attacker.airWeapon() : attacker.groundWeapon();
	}
	
	int GetAttackRange(Unit attacker, Unit target)
	{
	    WeaponType weapon = GetWeapon(attacker, target);
	
	    if (weapon == WeaponType.None)
	    {
	        return 0;
	    }
	
	    int range = weapon.maxRange();
	
	    if ((attacker.getType() == UnitType.Protoss_Dragoon) 
	        && (attacker.getPlayer() == Globals.self)
	        && Globals.self.getUpgradeLevel(UpgradeType.Singularity_Charge) > 0)
		{
			range = 6 * 32;
		}
	
	    return range;
	}
	
	int GetAttackRange(UnitType attacker, UnitType target)
	{
	    WeaponType weapon = GetWeapon(attacker, target);
	
	    if (weapon == WeaponType.None)
	    {
	        return 0;
	    }
	
	    return weapon.maxRange();
	}

	
	
	Unit GetClosestUnitTypeToTarget(UnitType type, Position target)
	{
		Unit closestUnit = null;
		double closestDist = 100000;
	
		for (Unit unit : Globals.self.getUnits())
		{
			if (unit.getType() == type)
			{
				double dist = unit.getDistance(target);
				if (closestUnit == null || dist < closestDist)
				{
					closestUnit = unit;
					closestDist = dist;
				}
			}
		}
	
		return closestUnit;
	}
}