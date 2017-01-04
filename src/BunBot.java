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
        
        Globals.game.setLocalSpeed(4);
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");   
        
        
        StrategyController.calculateStrategy();
        InformationManager.OnStart();
        Globals.BaseData.onStart();
    }
    
    // NOTE: NULL REFERENCE EXCEPTIONS CAUSE THIS TO FAIL
    @Override
    public void onFrame() {
//    	System.out.println("ExecuteStrategy");
    	StrategyController.executeStrategy();
    	
//    	System.out.println("ControllAllUnits");
    	UnitController.controllAllUnits();
    	
    	// Debug data to draw
    	DebugController.onFrame(); // Be sure to clear the debug console.
    	DebugController.drawWorkerPaths();
		MacroController.debugVariables();
		InformationManager.writeToDebugConsole();
		
    	DebugController.drawMapInformation();
    	Globals.BaseData.drawMapInformation();
    	DebugController.drawHealthBars();
    	//InformationManager.drawUnitInformation(425,30);
    }
    
    @Override
    public void onUnitCreate(Unit unit) {

    	InformationManager.updateUnitData(unit);
    	MacroController.onUnitCreate(unit);
    	if(unit.getPlayer() == Globals.self && unit.isCompleted())
    		UnitController.put(unit.getID(), new UnitController(unit));
    }
    
    @Override
    public void onUnitComplete(Unit unit) {
    	InformationManager.updateUnitData(unit);
    	
		MacroController.onUnitComplete(unit);
    	if(unit.getPlayer() == Globals.self)
    		UnitController.put(unit.getID(), new UnitController(unit));
    }

    @Override
    public void onUnitDestroy(Unit unit) {
    	InformationManager.onUnitDestroy(unit);
	    UnitController.get(unit.getID()).stopTask();
    }

    public static void main(String[] args) {
        new BunBot().run();
    }
}