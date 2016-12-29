import bwapi.UnitType;

public class StrategyController {
	
	public enum UnitProduction { FocusOnWorkers, FocusOnArmy };

	public enum TechGoal { CommmandCenter, Factory };
	
	
	private int WorkerGoalBeforeExpand = 20;
	private int ArmySupplyGoal = 0;
	private static TechGoal techGoal = TechGoal.Factory;
	private static UnitProduction unitProductionFocus = UnitProduction.FocusOnWorkers;
	
	

	public static void DetectStrategy() {
		
	}
	
	public static void CalculateStrategy() {
		techGoal = TechGoal.Factory;
		unitProductionFocus = UnitProduction.FocusOnWorkers;
	}
	
	public static void ExecuteStrategy() {
		MacroController.DebugInfo();
		
		if(techGoal == TechGoal.Factory) {
			MacroController.PreventSupplyBlock();
	    	MacroController.BuildBuilding(UnitType.Terran_Barracks);
	    	
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
		if(techGoal == TechGoal.CommmandCenter) {
	    	MacroController.BuildBuilding(UnitType.Terran_Command_Center);
		}
    	
    	
    	
		// multiply expected supply by 2 because zerglings take half supply.
    	if(Globals.game.getStartLocations().size() > 2 && Globals.self.supplyUsed() > (8 * 2)) {
        	ScoutingController.onFrame();
    	}
	}
}
