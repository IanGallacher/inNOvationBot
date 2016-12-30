import bwapi.UnitType;

public class StrategyController {
	
	public enum UnitProduction { FocusOnWorkers, FocusOnArmy };

	public enum TechGoal { Nexus, DragoonTech };
	
	
	private int WorkerGoalBeforeExpand = 20;
	private int ArmySupplyGoal = 0;
	private static TechGoal techGoal = TechGoal.DragoonTech;
	private static UnitProduction unitProductionFocus = UnitProduction.FocusOnWorkers;
	
	
	
	static boolean hasGas;
	static boolean hasGateway;
	static boolean hasCyberneticsCore;
	
	

	public static void DetectStrategy() {
		
	}
	
	public static void CalculateStrategy() {
		techGoal = TechGoal.DragoonTech;
		unitProductionFocus = UnitProduction.FocusOnWorkers;
		
		hasGas = false;
		hasGateway = false;
		hasCyberneticsCore = false;
	}
	
	public static void ExecuteStrategy() {
		if(techGoal == TechGoal.DragoonTech) {
			MacroController.PreventSupplyBlock();
			
			if(hasGas == false)
			{
				MacroController.HarvestGas(3);
				hasGas = true;
			}

			if(hasGateway == false) {
				if(MacroController.BuildBuilding(UnitType.Protoss_Gateway))
				{
					System.out.println("MADE GATEWAY YEAH YEAH YEAH");

					hasGateway = true;
				}
			} else if (hasCyberneticsCore == false && MacroController.BuildBuilding(UnitType.Protoss_Cybernetics_Core) ) {
				System.out.println("has cybernetics coreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
				hasCyberneticsCore = true;
			} else {
				//MacroController.BuildBuilding(UnitType.Protoss_Gateway);
			}
	    	MacroController.BuildBuilding(UnitType.Protoss_Gateway);
	    	
	    	if(unitProductionFocus == UnitProduction.FocusOnWorkers)
	    	{
	    		// If the current strategy is focusing on training workers, do that first. 
	        	MacroController.TrainWorkers();
	        	MacroController.TrainArmy();
	    	} else 
	    	{
	    		// If the current strategy is focusing on training army, do that first. 
	        	MacroController.TrainArmy();
	        	MacroController.TrainWorkers();
	    	} 
		} 
		
		// Save up to expand. 
		if(techGoal == TechGoal.Nexus) {
	    	MacroController.BuildBuilding(UnitType.Protoss_Nexus);
		}
    	
    	
    	
		// multiply expected supply by 2 because zerglings take half supply.
    	if(Globals.game.getStartLocations().size() > 2 && Globals.self.supplyUsed() > (8 * 2)) {
        	ScoutingController.onFrame();
    	}
	}
}
