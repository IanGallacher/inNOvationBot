import bwapi.UnitType;

public class StrategyController {
	
	public enum UnitProduction { FocusOnWorkers, FocusOnArmy };

	public enum TechGoal { Nexus, DragoonTech };
	
	
	private static int _workerGoalBeforeExpand = 15;
	private static int _armySquadSizeGoal = 15;
	private static TechGoal techGoal = TechGoal.DragoonTech;
	private static UnitProduction unitProductionFocus = UnitProduction.FocusOnWorkers;
	
	
	
	static boolean hasGas;
	static boolean hasCyberneticsCore;
	static boolean hasExpansion;
	
	

	public static void detectStrategy() {
		
	}
	
	public static void calculateStrategy() {
		techGoal = TechGoal.DragoonTech;
		unitProductionFocus = UnitProduction.FocusOnWorkers;
		
		hasGas = false;
		hasCyberneticsCore = false;
		hasExpansion = false;
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
			
			if(UnitController.workers.size() > _workerGoalBeforeExpand && hasExpansion == false) {
				techGoal = TechGoal.Nexus;
				System.out.printf("EXPANDING");
			} else if(MacroController._number_of_gateways < 1 || hasCyberneticsCore == true) {
				if(MacroController.buildBuilding(UnitType.Protoss_Gateway))
				{
					System.out.println("MADE GATEWAY YEAH YEAH YEAH");
				}
			} else if(hasGas == false && MacroController.buildBuilding(UnitType.Protoss_Assimilator)) {
				hasGas = true;
			} else if (hasCyberneticsCore == false && MacroController.buildBuilding(UnitType.Protoss_Cybernetics_Core) ) {
				hasCyberneticsCore = true;
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
				hasExpansion = true;
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
