package Information;

import bwapi.*;

//Giving credit where credit is due. Much of this code was inspired by UAlbertaBot.
//https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/UnitInfo.cpp

class UnitInfo
	{
	    // we need to store all of this data because if the unit is not visible, we
	    // can't reference it from the unit pointer

	    int      unitID;
	    int      lastHealth;
	    int      lastShields;
	    Player   player;
	    Unit     unit;
	    Position lastPosition;
	    UnitType type;
	    boolean  completed;

	    public UnitInfo()
	    {
	        unitID = 0;
	        lastHealth = 0;
	        player = null;
	        unit = null;
	        lastPosition = Position.None;
	        type = UnitType.None;
	        completed = false;
	    }

	    boolean ComapareID (Unit unit) 
	    {
	        return this.unitID == unit.getID();
	    }

	    boolean CompareID (UnitInfo rhs) 
	    {
	        return this.unitID == rhs.unitID;
	    }

//	    boolean operator < (const UnitInfo & rhs) const
//	    {
//	        return this.unitID < rhs.unitID;
//	    }
	};