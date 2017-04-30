import Macro.MacroController;
import bwapi.*;

import bwta.BWTA;
import Debug.DebugController;
import Globals.Globals;
import Information.InformationManager;
import UnitController.UnitController;
import UnitController.UnitManager;

// BunBot only supports Protoss, but there is code that mentions the other races scattered throughout the code. 
// Hopefully that code saves someone time down the road when they refactor it to support different races. 
public class inNOVationBot extends DefaultBWListener {

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
        
        Globals.game.setLocalSpeed(5);
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");   
        
        
        StrategyController.calculateStrategy();
        InformationManager.OnStart();
    }
    
    // NOTE: NULL REFERENCE EXCEPTIONS CAUSE THIS TO FAIL
    @Override
    public void onFrame() {
//    	System.out.println("ExecuteStrategy");
    	StrategyController.executeStrategy();
    	
    	System.out.println("ControllAllUnits");
    	UnitManager.controllAllUnits();
    	
    	// Debug data to draw
    	DebugController.onFrame(); // Be sure to clear the debug console.
    	//DebugController.drawWorkerPaths();
		MacroController.debugVariables();
 //   	StrategyController.debugVariables();
		InformationManager.writeToDebugConsole();
		
    	DebugController.drawMapInformation();
    	
    	// BaseData is no longer a singleton representing the current players main base. 
    	// Instead it is replaced with an arrayList called _allBases.
    	//Globals.BaseData.drawMapInformation(); 
    	DebugController.drawHealthBars();
    	//InformationManager.drawUnitInformation(425,30);
    }
    
    // NOTE: DOES NOT INCLUDE REFINERIES.
    @Override
    public void onUnitCreate(Unit unit) {
    	InformationManager.updateUnitData(unit);
    	InformationManager.onUnitCreate(unit);
    	MacroController.onUnitCreate(unit);
    	if(unit.getPlayer() == Globals.self && unit.isCompleted())
    		UnitManager.put(unit.getID(), new UnitController(unit));
    }
    
    @Override
    public void onUnitComplete(Unit unit) {
    	InformationManager.updateUnitData(unit);
    	
		MacroController.onUnitComplete(unit);
    	if(unit.getPlayer() == Globals.self)
    		UnitManager.put(unit.getID(), new UnitController(unit));
    }   
    
    // Overlaps a bit with onUnitCreate, because we can discover our own units.
    @Override
    public void onUnitDiscover(Unit unit) {
    	InformationManager.updateUnitData(unit);
    }

    @Override
    public void onUnitDestroy(Unit unit) {
    	InformationManager.onUnitDestroy(unit);
	    UnitManager.get(unit.getID()).stopTask();
    }
    
    // NOTE: THIS INCLUDES ASSIMILATORS/EXTRACTORS/REFINERIES
    @Override
    public void onUnitMorph(Unit unit) {
    	// May not take into account lurkers.
    	InformationManager.updateUnitData(unit);
    	InformationManager.onUnitCreate(unit);
    	
    	// If an assimilator has been constructed, tell the macro controller to stop planning for that mineral expenditure.
    	MacroController.onUnitCreate(unit);
    	if(unit.getPlayer() == Globals.self && unit.isCompleted())
    		UnitManager.put(unit.getID(), new UnitController(unit));
    }
    public static void main(String[] args) {
        new inNOVationBot().run();
    }
}