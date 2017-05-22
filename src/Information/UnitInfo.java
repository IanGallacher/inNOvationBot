package Information;

import bwapi.*;

//Giving credit where credit is due. Much of this code was inspired by UAlbertaBot.
//https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/UnitInfo.cpp

// This code is needed to keep track of enemy units in the fog of war.
public class UnitInfo
	{
	    // we need to store all of this data because if the unit is not visible, we
	    // can't reference it from the unit pointer

	    int      unitID;
	    int      lastHealth;
	    int      lastShields;
	    Unit     unit;
	    Position lastPosition;
	    UnitType type;
	    boolean  completed;

	    public UnitInfo()
	    {
	        this.unitID = 0;
	        this.lastHealth = 0;
	        this.unit = null;
	        this.lastPosition = Position.None;
	        this.type = UnitType.None;
	        this.completed = false;
	    }
	    
	    public UnitInfo(Unit unit)
	    {
	    	this.unitID = unit.getID();
	    	this.lastHealth = unit.getHitPoints();
	        this.unit = unit;
	        this.lastPosition = unit.getPosition();
	        this.type = unit.getType();
	        this.completed = unit.isCompleted();
	    }

	    boolean ComapareID (Unit unit) 
	    {
	        return this.unitID == unit.getID();
	    }

	    boolean CompareID (UnitInfo rhs) 
	    {
	        return this.unitID == rhs.unitID;
	    }
	    
	    public Unit getUnit()
	    {
	    	return this.unit;
	    }
	    
	    public Position getUnitPosition()
	    {
	    	return lastPosition;
	    }
//	    boolean operator < (const UnitInfo & rhs) const
//	    {
//	        return this.unitID < rhs.unitID;
//	    }
	};