import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bwapi.Color;
import bwapi.UnitType;

public class DebugController {
	private static int _debugConsoleYPosition = 1;
	
	// reset the debug console Y position variable.
	public static void OnFrame() {
		_debugConsoleYPosition = 1;
	}
	
	// The debug console is for quick and dirty real time value debugging.
	public static void DebugConsolePrint(String label, int value)
	{
    	Globals.game.drawTextScreen
    		(
    			10, 10 + (20 * _debugConsoleYPosition), 
    			label + ": " + Integer.toString(value)
    		);
    	_debugConsoleYPosition++;
	}
	
	public static void DrawWorkerPaths() {
		for(UnitController uc : UnitController.workers.values()) 
		{
			Globals.game.drawLineMap
				(
					uc.thisUnit.getX(), uc.thisUnit.getY(), 
					uc.gatheringMineralPatch.getX(), uc.gatheringMineralPatch.getY(), 
					Color.Green
				);
		}
	}

}
