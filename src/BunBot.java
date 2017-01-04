import bwapi.*;

import bwta.BWTA;


// BunBot only supports Protoss, but there is code that mentions the other races scattered throughout the code. 
// Hopefully that code saves someone time down the road when they refactor it to support different races. 
public class BunBot extends DefaultBWListener {

    private Mirror mirror = new Mirror();
    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onStart() {
        Globals.game = mirror.getGame();
        Globals.self = Globals.game.self();
        Globals.enemy = Globals.game.enemy();
        
        Globals.game.setLocalSpeed(0);
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");   
        
        
        StrategyController.CalculateStrategy();
        InformationManager.OnStart();
        Globals.BaseData.onStart();
    }
    
    // NOTE: NULL REFERENCE EXCEPTIONS CAUSE THIS TO FAIL
    @Override
    public void onFrame() {
//    	System.out.println("GatherInformation");
    	InformationManager.GatherInformation();
    	
//    	System.out.println("ExecuteStrategy");
    	StrategyController.ExecuteStrategy();
    	
//    	System.out.println("ControllAllUnits");
    	UnitController.ControllAllUnits();
    	
    	// Debug data to draw
    	DebugController.OnFrame(); // Be sure to clear the debug console.
    	DebugController.DrawWorkerPaths();
		MacroController.DebugVariables();
		InformationManager.writeToDebugConsole();
    }
    
    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("Created " + unit.getType());
        
    	MacroController.onUnitCreate(unit);
    	InformationManager.onUnitCreate(unit);
    	UnitController.put(unit.getID(), new UnitController(unit));
    }
    
    @Override
    public void onUnitComplete(Unit unit) {
		MacroController.onUnitComplete(unit);
    }

    @Override
    public void onUnitDestroy(Unit unit) {
    	InformationManager.onUnitDestroy(unit);
    }

    public static void main(String[] args) {
        new BunBot().run();
    }
}