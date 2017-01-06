import Macro.MacroController;
import bwapi.UnitType;

import Globals.Globals;
import Globals.MapTools;
import Information.InformationManager;
import UnitController.ScoutingController;
import UnitController.UnitController;

public class StrategyController {
	
	public enum UnitProduction { FocusOnWorkers, FocusOnArmy };

	public enum TechGoal { Nexus, DragoonTech };
	
	
	private static int _workerGoalBeforeExpand = 15;
	private static int _armySquadSizeGoal = 15;
	private static TechGoal techGoal = TechGoal.DragoonTech;
	private static UnitProduction unitProductionFocus = UnitProduction.FocusOnWorkers;
	
	

	public static void detectStrategy() {
		
	}
	
	public static void calculateStrategy() {
		techGoal = TechGoal.DragoonTech;
		unitProductionFocus = UnitProduction.FocusOnWorkers;
	}
	
	
	
	
	// be sure to only call once per frame.
	public static void executeStrategy() {
		if(techGoal == TechGoal.DragoonTech) { // heavy macro strategy
			MacroController.preventSupplyBlock();
			
//			if(hasGas == false)
//			{
//				MacroController.HarvestGas(3);
//				hasGas = true;
//			}
			
			
			// Mineral spending goals.
			
			if(UnitController.workers.size() > _workerGoalBeforeExpand *  InformationManager.getUnitCount(UnitType.Protoss_Nexus)) {
				techGoal = TechGoal.Nexus;
				System.out.printf("EXPANDING");
			} else if(InformationManager.getUnitCount(UnitType.Protoss_Gateway) < 1 || InformationManager.getUnitCount(UnitType.Protoss_Cybernetics_Core) >= 1) {
				MacroController.buildBuilding(UnitType.Protoss_Gateway);
			} else if(InformationManager.getUnitCount(UnitType.Protoss_Assimilator) < 1) { 
				MacroController.buildBuilding(UnitType.Protoss_Assimilator);
			} else if (InformationManager.getUnitCount(UnitType.Protoss_Assimilator) < 1) {
				MacroController.buildBuilding(UnitType.Protoss_Cybernetics_Core);
			}
	    	
	    	if(unitProductionFocus == UnitProduction.FocusOnWorkers)
	    	{
	    		// If the current strategy is focusing on training workers, do that first. 
	        	MacroController.trainWorkers();
	        	MacroController.trainArmy();
	    	} else 
	    	{
	    		// If the current strategy is focusing on training army, do that first. 
	        	MacroController.trainArmy();
	        	MacroController.trainWorkers();
	    	} 
		} 
		
		// Save up to expand. 
		if(techGoal == TechGoal.Nexus) {
			if(
			    	MacroController.buildAtLocation(UnitType.Protoss_Nexus, MapTools.getNextExpansion())
			    ) {
				techGoal = TechGoal.DragoonTech;
				Globals.game.printf("EXPANDING");
				// Have to make a move and build function in order for this to work properly
			}
		}
    	
    	
    	
		// multiply expected supply by 2 because zerglings take half supply.
    	if(Globals.game.getStartLocations().size() > 2 && Globals.self.supplyUsed() > (8 * 2)) {
        	ScoutingController.onFrame();
    	}
	}
}
