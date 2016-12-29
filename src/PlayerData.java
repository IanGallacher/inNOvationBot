import java.util.HashMap;
import java.util.Vector;

import bwapi.Unit;


//Giving credit where credit is due. Much of this code was inspired by UAlbertaBot.
//https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/UnitData.cpp
class PlayerData
	{    
		public HashMap<Unit, UnitInfo> UnitData;
//
		public PlayerData()
		{
			this.UnitData = new HashMap<Unit, UnitInfo>();
		}
		
//	    boolean badUnitInfo(const UnitInfo & ui);
//	
//	    Vector<int> numDeadUnits;
//	    Vector<int> numUnits;
//	
	    int mineralsLost;
	    int gasLost;
//	
//	
//	
//	    public UnitData();
//	;
	    void updateUnit(Unit unit) {	
	    	if (unit == null) return; 

		    boolean firstSeen = false;
		    UnitInfo it = this.UnitData.get(unit);
		    
		    
		    // Make sure there is no null reference before the UnitInfo is loaded. 
		    if (this.UnitData.containsKey(unit) == false)
		    {
		        firstSeen = true;
		        this.UnitData.put(unit, new UnitInfo());
		    }
		    
			UnitInfo ui     = this.UnitData.get(unit);
		    ui.unit         = unit;
		    ui.player       = unit.getPlayer();
			ui.lastPosition = unit.getPosition();
			ui.lastHealth   = unit.getHitPoints();
		    ui.lastShields  = unit.getShields();
			ui.unitID       = unit.getID();
			ui.type         = unit.getType();
		    ui.completed    = unit.isCompleted();
		    
	        this.UnitData.put(unit, new UnitInfo());
	
//		    if (firstSeen)
//		    {
//		        numUnits[unit.getType().getID()]++;
//		    }
	    }
//	    public void removeUnit(Unit unit);
//	    public void removeBadUnits();
//	
//	    public int getGasLost();
//	    public int getMineralsLost();
//	    public int getNumUnits(UnitType t);
//	    public int getNumDeadUnits(UnitType t);
			
	}