import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;


public class MacroController {
    
    private static int planned_production = 10; // start at 8 to account for the four scv's that spawn.
    private static int planned_minerals = 502; // start at 8 to account for the four scv's that spawn.
    private static int planned_supply_depots = 0; // don't spen all our money on depots if capped. 

    public static void DebugInfo() {
    	Globals.game.drawTextScreen(10, 10, Integer.toString(planned_production) + ' ' 
    			+ Integer.toString(planned_supply_depots * UnitType.Terran_Supply_Depot.supplyProvided() ));
    }
    
    // Always called when a unit is created by the MasterBot
    public static void onUnitCreate(Unit unit) {
        planned_production -= unit.getType().supplyRequired();
        
        if(unit.getType().isBuilding())
        	planned_minerals -= unit.getType().mineralPrice();
    }
    
    public static void onUnitComplete(Unit unit)
    {
    	if(unit.getType() == UnitType.Terran_Supply_Depot)
    	{
    		planned_supply_depots--;
    	}
    }
    
    // Every frame, see if our planned production will require another supply depot.
    public static void PreventSupplyBlock()
    {
        if ((Globals.self.supplyTotal() + (planned_supply_depots * UnitType.Terran_Supply_Depot.supplyProvided() ) - (Globals.self.supplyUsed() + planned_production) <= 0)) {
        	BuildBuilding(UnitType.Terran_Supply_Depot);
        }
    }
    
    public static void BuildBuilding(UnitType building)
    {
    	assert(building.isBuilding());
    	
    	// System.out.println(building.toString());
        if (Globals.self.minerals()-planned_minerals >= building.mineralPrice()) {
        	//iterate over units to find a worker
        	for (Unit myUnit : Globals.self.getUnits()) {
        		if (myUnit.getType() == UnitType.Terran_SCV && myUnit.isConstructing() == false) {
        			
        			//get a nice place to build a supply depot 
        			TilePosition buildTile = BuildingPlacementController.getBuildTile (
        			    													 myUnit, 
																			 building, 
																			 Globals.self.getStartLocation() );
        			//and, if found, send the worker to build it (and leave others alone - break;)
        			if (buildTile != null) {
        				

        		    	if(building == UnitType.Terran_Supply_Depot)
        		    	{
        		    		planned_supply_depots++;
        		    	}
        		    	
        				System.out.println("asdfasdf");
            			UnitController.get(myUnit.getID()).StopTask();
        				myUnit.build(building, buildTile); planned_minerals += building.mineralPrice();
        				break;
        			}
        		}
        	}
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
            if (myUnit.getType() == UnitType.Terran_Command_Center 
            		&& myUnit.getTrainingQueue().isEmpty() 
            		&& myUnit.isBeingConstructed() == false 
            		&& Globals.self.supplyTotal() > Globals.self.supplyUsed() 
            		&& Globals.self.minerals() - planned_minerals >= UnitType.Terran_SCV.mineralPrice()
            	) 
            {
            	TrainUnit(myUnit, UnitType.Terran_SCV);
            }
        }
    }
    
    public static void TrainArmy()
    {
        // iterate through my units
        for (Unit myUnit : Globals.self.getUnits()) {
            
            if (myUnit.getType() == UnitType.Terran_Barracks 
            		&& myUnit.getTrainingQueue().isEmpty() 
            		&& myUnit.isBeingConstructed() == false &&
            		Globals.self.supplyTotal() > Globals.self.supplyUsed() 
            		&& Globals.self.minerals() - planned_minerals >= UnitType.Terran_Marine.mineralPrice()) {
            	TrainUnit(myUnit, UnitType.Terran_Marine);
                
                bwta.Region r = BWTA.getRegion(Globals.self.getStartLocation().toPosition());
                myUnit.setRallyPoint(r.getChokepoints().get(0).getCenter());
            }
        }
    }
    
    private static void AssignJobsToIdleWorkers()
    {
    	FinishIncompleteStructures();
    }
    
    private static void FinishIncompleteStructures()
    {
    	
    }
}
