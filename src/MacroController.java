import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;


public class MacroController {
    private static int planned_production = 10; // start at 8 to account for the four scv's that spawn.
    public static int GetPlannedProduction() { return planned_production; } // for debugging
    
    private static int planned_minerals = 502;
    public static int GetPlannedMinerals() { return planned_minerals; } // for debugging
    
    private static int planned_supply_depots = 0; // don't spend all our money on depots if capped. 
    public static int GetPlannedSupplyDepots() { return planned_supply_depots; } // for debugging

    public static void DebugVariables() {
    	DebugController.DebugConsolePrint("planned_production", planned_production);
    	DebugController.DebugConsolePrint("planned_minerals", planned_minerals);
    	DebugController.DebugConsolePrint("planned_supply_depots", planned_supply_depots);
    }
    
    // Always called when a unit is created by the MasterBot
    public static void onUnitCreate(Unit unit) {
        planned_production -= unit.getType().supplyRequired();
        
        if(unit.getType().isBuilding() && unit.getType() != UnitType.Special_Protoss_Temple)
        	planned_minerals -= unit.getType().mineralPrice();
    }
    
    public static void onUnitComplete(Unit unit)
    {
    	if(unit.getType() == UnitType.Protoss_Pylon)
    	{
    		planned_supply_depots--;
    	}
    }
    
    // Every frame, see if our planned production will require another supply depot.
    public static void PreventSupplyBlock()
    {
    	// if there is a pylon in production, don't keep spending minerals on more pylons. 
        if ((Globals.self.supplyTotal() + (planned_supply_depots * UnitType.Protoss_Pylon.supplyProvided() ) - (Globals.self.supplyUsed() + planned_production) <= 0)) {
        	BuildBuilding(UnitType.Protoss_Pylon);
        }
    }
    
    public static boolean BuildBuilding(UnitType building)
    {
    	assert(building.isBuilding());
    	
    	
    	if(building != UnitType.Protoss_Pylon && Globals.self.supplyTotal() < 20) {
    		return false;
    	}
    	
    	// System.out.println(building.toString());
        if (Globals.self.minerals()-planned_minerals >= building.mineralPrice()) {
        	System.out.println("Building Building");
        	//iterate over units to find a worker
        	for (Unit myUnit : Globals.self.getUnits()) {
				if (myUnit.getType() == UnitType.Protoss_Probe && myUnit.isConstructing() == false) {
        			
        			//get a nice place to build a supply depot 
        			TilePosition buildTile = BuildingPlacementController.getBuildTile (
        			    													 myUnit, 
																			 building, 
																			 Globals.self.getStartLocation() );
        			//and, if found, send the worker to build it (and leave others alone - break;)
        			if (buildTile != null) {

        	        	System.out.println("Building not null");

        		    	if(building == UnitType.Protoss_Pylon)
        		    	{
        		    		planned_supply_depots++;
        		    	}
        		    	
            			UnitController.get(myUnit.getID()).StopTask();
        				
            			if(myUnit.build(building, buildTile) )
            			{
            				planned_minerals += building.mineralPrice();
            			}
        			}
        			else {
        				System.out.println("building is null");
        			}
        		}
        	}
        	return true;
        } else {
            return false;
        }

    }    
    
    private static void TrainUnit(Unit building, UnitType unitToTrain) {
        if(building.train(unitToTrain))
        { 
        	planned_production += unitToTrain.supplyRequired();
        	System.out.println("supply required: " + unitToTrain.supplyRequired() );
        }
    }
    
    // Trains a worker at all command centers.
    public static void TrainWorkers () {
        
        // iterate through my units
        for (Unit myUnit : Globals.self.getUnits()) {
        	
            // if there's enough minerals, train an SCV
            if (myUnit.getType() == UnitType.Protoss_Nexus 
            		&& myUnit.getTrainingQueue().isEmpty() 
            		&& myUnit.isBeingConstructed() == false 
            		&& Globals.self.supplyTotal() > Globals.self.supplyUsed() 
            		&& Globals.self.minerals() - planned_minerals >= UnitType.Protoss_Probe.mineralPrice()
            	) 
            {
            	TrainUnit(myUnit, UnitType.Protoss_Probe);
            }
        }
    }
    
    public static void TrainArmy()
    {
        // iterate through my units
        for (Unit myUnit : Globals.self.getUnits()) {
            
            if (myUnit.getType() == UnitType.Protoss_Gateway 
            		&& myUnit.getTrainingQueue().isEmpty() 
            		&& myUnit.isBeingConstructed() == false &&
            		Globals.self.supplyTotal() > Globals.self.supplyUsed() 
            		&& Globals.self.minerals() - planned_minerals >= UnitType.Protoss_Zealot.mineralPrice()) {
            	TrainUnit(myUnit, UnitType.Protoss_Zealot);
                
                bwta.Region r = BWTA.getRegion(Globals.self.getStartLocation().toPosition());
                myUnit.setRallyPoint(r.getChokepoints().get(0).getCenter());
            }
        }
    }
    
    public static boolean HarvestGas(int numberOfWorkersToHarvest) {
    	if( BuildBuilding(UnitType.Protoss_Assimilator) )
    		return true;
    	return false;
    }
    
    private static void AssignJobsToIdleWorkers()
    {
    	FinishIncompleteStructures();
    }
    
    private static void FinishIncompleteStructures()
    {
    	
    }
}
