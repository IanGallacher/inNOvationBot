package Macro;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import BuildingPlacementController;
import Debug.DebugController;
import Globals.Globals;
import Information.InformationManager;
import UnitController.UnitController;
import UnitController.UnitManager;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;


public class MacroController {
	// We used to compare only against things that are planned on being made
	// We now compare against total supply. This line of code may come back again in a future version.
    private static int _planned_production = 8; // start at 8 to account for the four probes that spawn.
    
    // The game starts by creating a Nexus. OnUnitCreate will get called, and our engine will think that we are planning to spend 400 minerals to build a Nexus.
    // In order to start the game with 0 planned minerals, account for the 400 minerals that the nexus will require. 
    private static int _planned_minerals = UnitType.Protoss_Nexus.mineralPrice(); 
    
    private static int _planned_supply_depots = 0; // don't spend all our money on depots if capped. 
    
    
    public static int _number_of_gateways = 0;
    private static int _number_of_nexus = 1;

    public static void debugVariables() {
//    	DebugController.debugConsolePrint("planned_production", _planned_production);
    	DebugController.debugConsolePrint("planned_minerals", _planned_minerals);
//    	DebugController.debugConsolePrint("planned_supply_depots", _planned_supply_depots);
//    	DebugController.debugConsolePrint("Production Capacity", productionCapacity());
    	
//    	DebugController.debugConsolePrint("_number_of_gateways", _number_of_gateways);
//    	DebugController.debugConsolePrint("_number_of_nexus", _number_of_nexus);
    	//DebugController.debugConsolePrint("UnitController.workers.size()", UnitManager.mineralWorkers.size());
    	
    //	DebugController.debugConsolePrint("GREATER", Globals.self.supplyUsed() + productionCapacity() );
    //	DebugController.debugConsolePrint("LESSER", Globals.self.supplyTotal() + ( _planned_supply_depots * UnitType.Protoss_Pylon.supplyProvided() ));
    }
    
    // Always called when a unit is created by the MasterBot
    public static void onUnitCreate(Unit unit) {
        _planned_production -= unit.getType().supplyRequired();
        
        // If we don't check for the unit belonging to the main player, sometimes neutral structures at the start of the game will mess up the initial mineral count.
        if(unit.getType().isBuilding() && unit.getPlayer() == Globals.self)
        	_planned_minerals -= unit.getType().mineralPrice();
    }
    
    public static void onUnitComplete(Unit unit) {
    	if(unit.getType() == UnitType.Protoss_Pylon)
    	{
    		_planned_supply_depots--;
    	}
    	if(unit.getType() == UnitType.Protoss_Gateway)
    	{
    		_number_of_gateways++;
    	}
    }
    
    // Every frame, see if more pylons are required. 
    public static void preventSupplyBlock() {
    	// if there is a pylon in production, don't keep spending minerals on more pylons. 
    	
    	
    	// Full breakdown of how this works. The following few lines are all part of one big if statement
    	
    	// If the current supply that we have plus the total amount of things that could be made 
        if ( 
        	    ( Globals.self.supplyUsed() + productionCapacity() )  // We used to compare only against things that are planned on being made // _planned_production)
        // Is greater than 
        		>=
        // the player supply capacity, including pylons in production. 
        // The pylons in production is key, otherwise you will build hundreds of pylons while supply blocked.
        	   ( Globals.self.supplyTotal() + ( _planned_supply_depots * UnitType.Protoss_Pylon.supplyProvided() ) ) 
           ) 
        {
        	buildBuilding(UnitType.Protoss_Pylon);
        }
    }
    
    
    public static boolean buildBuilding(UnitType building) {
    	assert(building.isBuilding());

    	
    	// Don't try to build anything that requires power before there is a pylon.
    	if(Globals.self.supplyTotal() < 15*2 // all supply is double in BWAPI due to zerglings taking half supply.
    	&& building != UnitType.Protoss_Nexus 
    	&& building != UnitType.Protoss_Pylon 
    	&& building != UnitType.Protoss_Assimilator)
    			return false;
    	
		//get a nice place to build a supply depot 
		TilePosition buildTileLocation = BuildingPlacementController.getBuildTile 
											(
												 building, 
												 Globals.self.getStartLocation() 
											);
    	
		return buildAtLocation(building, buildTileLocation);
    }    
    
    
    public static boolean buildAtLocation(UnitType building, TilePosition buildLocation) {
    	assert building.isBuilding();
    	assert buildLocation != null;

    	
    	// Don't try to build anything that requires power before there is a pylon.
    	if(Globals.self.supplyTotal() < 15*2 // all supply is double in BWAPI due to zerglings taking half supply.
    	&& building != UnitType.Protoss_Nexus 
    	&& building != UnitType.Protoss_Pylon 
    	&& building != UnitType.Protoss_Assimilator)
    			return false;
    	
    	// System.out.println(building.toString());
        if (Globals.self.minerals()-_planned_minerals >= building.mineralPrice()) {
        	// Look for workers that can build our building. 
        	// TODO: Find the closest worker that can build the building. 
        	for (UnitController workerController : UnitManager.mineralWorkers.values()) 
        	{
        		Unit worker = workerController.getUnit();
        		// If the worker is busy, find a different one
				if (worker.isConstructing() == true) continue;
				// Although this check should have already been called by now, 
				// make sure that THIS PARTICULAR WORKER can build at this location.
				// We should have already checked to make sure the build location is vacant. 
				// If the worker can not build at the specified location, find a different worker. 
				if (Globals.game.canBuildHere(buildLocation, building, worker) != true) continue;
				
				
		    	if(building == UnitType.Protoss_Pylon)
		    	{
		    		_planned_supply_depots++;
		    	}

				_planned_minerals += building.mineralPrice();
		    	workerController.buildBuilding(building, buildLocation);
		    	return true;
//		    	
//    			if(worker.build(building, buildLocation) )
//    			{
//    				_planned_minerals += building.mineralPrice();
//    			}
//    			
//    			worker.move(buildLocation.toPosition());
//		    	
//    			workerController.stopTask();
//    			
//    			// Send the worker to build the building (
//    			// No need to keep looking for a worker - we found one and sent it to go build something;
//            	return true;
        	}
        	
        	// Not a single one of our workers are able to build the building. Very unlikely. 
        	return false;
        } else {
        	// We don't have enough minerals to afford the building.
            return false;
        }
    }
    
    public static boolean canTrainUnit(Unit building, UnitType unitToTrain) {
    	return 
    		(
    		building.canTrain(unitToTrain)
    		&& building.getTrainingQueue().isEmpty() // Don't waste money by queueing up units.
    		&& building.isBeingConstructed() == false 
    		&& Globals.self.supplyTotal() > Globals.self.supplyUsed()  // Is there enough supply?
    		&& Globals.self.minerals() - _planned_minerals >= unitToTrain.mineralPrice()
    	    && Globals.self.gas() >= unitToTrain.gasPrice()
    		);
    }
    
    private static void trainUnit(Unit building, UnitType unitToTrain) {
        if(building.train(unitToTrain)) 
        { 
        	_planned_production += unitToTrain.supplyRequired();
        }
    }
    
    // Trains a worker at all command centers.
    public static void trainWorkers() {
        // iterate through my units
        for (Unit potentialBuilding : Globals.self.getUnits()) {
        	
            // if there's enough minerals, train an SCV
            if (canTrainUnit(potentialBuilding, UnitType.Protoss_Probe )
    		&& InformationManager.getUnitCount(UnitType.Protoss_Probe) <= 22 * InformationManager.getUnitCount(UnitType.Protoss_Nexus)
               ) 
            {
            	trainUnit(potentialBuilding, UnitType.Protoss_Probe);
            }
        }
    }
    
    public static void trainArmy() {
        // iterate through my units
        for (Unit myUnit : Globals.self.getUnits()) {

            
            
            if (myUnit.getType() == UnitType.Protoss_Gateway 
    		&& myUnit.getTrainingQueue().isEmpty() 
    		&& myUnit.isBeingConstructed() == false
    		&& Globals.self.supplyTotal() > Globals.self.supplyUsed() 
    		&& Globals.self.minerals() - _planned_minerals >= UnitType.Protoss_Dragoon.mineralPrice()
    		&& Globals.self.gas() >= UnitType.Protoss_Dragoon.gasPrice()
    		&& InformationManager.getUnitCount(UnitType.Protoss_Cybernetics_Core) >= 1) 
            {
            	trainUnit(myUnit, UnitType.Protoss_Dragoon);
                
                bwta.Region r = BWTA.getRegion(Globals.self.getStartLocation().toPosition());
               // myUnit.setRallyPoint(r.getChokepoints().get(0).getCenter());
            }
            
            
//            if (myUnit.getType() == UnitType.Protoss_Gateway 
//            		&& myUnit.getTrainingQueue().isEmpty() 
//            		&& myUnit.isBeingConstructed() == false &&
//            		Globals.self.supplyTotal() > Globals.self.supplyUsed() 
//            		&& Globals.self.minerals() - _planned_minerals >= UnitType.Protoss_Zealot.mineralPrice()) {
//            	trainUnit(myUnit, UnitType.Protoss_Zealot);
//                
//                bwta.Region r = BWTA.getRegion(Globals.self.getStartLocation().toPosition());
//                myUnit.setRallyPoint(r.getChokepoints().get(0).getCenter());
//            }
        }
    }
    
    public static boolean harvestGas(int numberOfWorkersToHarvest) {
    	if( buildBuilding(UnitType.Protoss_Assimilator) )
    		return true;
    	return false;
    }
    
    // total amount of supply that can possibly be in production.
    public static int productionCapacity() { 
    	// Probes take take up twice as much supply as usual because two can finish before a pylon is done.
    	return (4 * _number_of_nexus) + (4 * _number_of_gateways);
    }
}
