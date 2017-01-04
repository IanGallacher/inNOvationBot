import java.util.HashMap;
import java.util.Vector;

import bwapi.Unit;
import bwapi.UnitType;


//Giving credit where credit is due. Much of this code was inspired by UAlbertaBot.
//https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/UnitData.cpp

// Only meant for the InformationManager to keep track of what units are alive and dead for each player. 
// There is plenty of redundancy for quick access. Make sure that if you edit the code, all the redundancies are properly updated. 
// TODO: Write tests to make sure redundancies get properly updated. 
class PlayerUnitData
	{
	    // List of all units that currently exist for a player. 
		public HashMap<Integer, UnitInfo> UnitData;
		
		
		/////////////////////////////////////////
		//      Data that we are tracking      //
		//   (sometimes redundant for speed)   //
		/////////////////////////////////////////
	    private int _mineralsLost;
	    public int getMineralsLost() { return _mineralsLost; }
	    
	    private int _gasLost;
	    public int getGasLost() { return _gasLost; }
	    
	    // UnitTypeCount is defined inside PlayerUnitData (bottom of this file)
	    UnitTypeCount _numUnits = new UnitTypeCount();
	    public int getNumUnits(UnitType ut) { return _numUnits.getCount(ut); }
	    
	    // UnitTypeCount is defined inside PlayerUnitData (bottom of this file)
	    UnitTypeCount _numDeadUnits = new UnitTypeCount();
	    public int getNumDeadUnits(UnitType ut) { return _numDeadUnits.getCount(ut); }

	    
	    
	    

		//////////////////////////////////////
	    //            Constructor           //
		//////////////////////////////////////
		public PlayerUnitData()
		{
			this.UnitData = new HashMap<Integer, UnitInfo>();
		}
		
		public void updateUnit(Unit unit) {	
	    	assert unit != null; 

		    // The unit has never been seen before. 
		    if (this.UnitData.containsKey(unit.getID()) == false)
		    {
			    // Make sure there is not going to be a null reference exception down the line.
		        this.UnitData.put(unit.getID(), new UnitInfo());
		        
			    // Increment the number of known units of UnitType. 
			    //     Example:there were four drones. 
			    //     We discovered the enemy had another. 
			    //     Now we know there are five drones.
		    	this._numUnits.increment(unit.getType());
		    }
		    
			UnitInfo ui     = this.UnitData.get(unit.getID());
		    ui.unit         = unit;
		    ui.player       = unit.getPlayer();
			ui.lastPosition = unit.getPosition();
			ui.lastHealth   = unit.getHitPoints();
		    ui.lastShields  = unit.getShields();
			ui.unitID       = unit.getID();
			ui.type         = unit.getType();
		    ui.completed    = unit.isCompleted();
		    
	        this.UnitData.put(unit.getID(), new UnitInfo());
	    }

	    public void addUnit(Unit unit) {	
	    	updateUnit(unit); // NumOfUnits will automatically be changed.
	    }
	    
	    public void removeUnit(Unit unit) {
	    	this._gasLost += unit.getType().gasPrice();
	    	this._mineralsLost += unit.getType().mineralPrice();
	    	
	    	_numDeadUnits.increment(unit.getType());
	    	_numUnits.decrement(unit.getType());
	    	
	    	UnitData.remove(unit.getID());
	    }

		
//	    boolean badUnitInfo(const UnitInfo & ui);

//	    public void removeBadUnits();
//	
			
	    
	    
	    
	    
	    


	    
	    // A class that lets you know how many known units of a certain type a player has. 
	    // Example, player has 2 Nexus, enemy is known to have 5 drones (but may have more)
	    class UnitTypeCount {
		    HashMap<UnitType, Integer> _numUnits = new HashMap<UnitType, Integer>();
			
			private void increment(UnitType ut) {
		    	// Prevent a null reference exception by making sure the UnitType exists in the HashMap.
			    if (this._numUnits.containsKey(ut) == false)
			        this._numUnits.put(ut, 0);
			    
			    // The Integer wrapper class is immutable. Get the value, and set the value to a new Integer.
		    	int currentVal = _numUnits.get(ut);
		    	_numUnits.put(ut, currentVal + 1);
				
			}
			
			private void decrement(UnitType ut) {
		    	// Prevent a null reference exception by making sure the UnitType exists in the HashMap.
			    if (this._numUnits.containsKey(ut) == false)
			        this._numUnits.put(ut, 0);
			    
			    // The Integer wrapper class is immutable. Get the value, and set the value to a new Integer.
		    	int currentVal = _numUnits.get(ut);
		    	_numUnits.put(ut, currentVal - 1);
				
			}
			
			public int getCount(UnitType ut) {
				return _numUnits.get(ut);
			}
	    }
	}