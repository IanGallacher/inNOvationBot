package Debug;
// Giving credit where it is due: most of the map and health bar drawing functions have been ripped from UAlbertaBot
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Color;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

import Globals.Globals;
import UnitController.UnitController;

public class DebugController {
	private static int _debugConsoleYPosition = 0;
	
	// reset the debug console Y position variable.
	public static void onFrame() {
		_debugConsoleYPosition = 0;
	}
	
	// The debug console is for quick and dirty real time value debugging.
	public static void debugConsolePrint(String label, int value)
	{
    	Globals.game.drawTextScreen
    		(
    			10, 10 + (15 * _debugConsoleYPosition), 
    			label + ": " + Integer.toString(value)
    		);
    	_debugConsoleYPosition++;
	}	
	
	// The debug console is for quick and dirty real time value debugging.
	public static void debugConsolePrint(String label, boolean value)
	{
    	Globals.game.drawTextScreen
    		(
    			10, 10 + (15 * _debugConsoleYPosition), 
    			label + ": " + Boolean.toString(value)
    		);
    	_debugConsoleYPosition++;
	}
	
	public static void drawWorkerPaths() {
		for(UnitController uc : UnitController.workers.values()) 
		{
			if(uc.getJob() != UnitController.JobType.GatherMinerals) continue;
			
			Globals.game.drawLineMap
				(
					uc.getUnit().getX(), uc.getUnit().getY(), 
					uc.gatheringMineralPatch.getX(), uc.gatheringMineralPatch.getY(), 
					Color.Green
				);
		}
	}

	public static void drawHealthBars()
	{
	    int verticalOffset = -10;
	    
	    
	    // draw neutral units and our units
	    for (Unit unit : Globals.game.getAllUnits())
	    {
	        if (unit.getPlayer() == Globals.game.enemy())
	        {
	            continue;
	        }
	
	        Position pos = unit.getPosition();
	
	        int left    = pos.getX() - unit.getType().dimensionLeft();
	        int right   = pos.getX() + unit.getType().dimensionRight();
	        int top     = pos.getY() - unit.getType().dimensionUp();
	        int bottom  = pos.getY() + unit.getType().dimensionDown();
	
	        //Globals.game.drawBoxMap(Position(left, top), Position(right, bottom), Colors::Grey, false);
	
	        if (!unit.getType().isResourceContainer() 
	        	&& unit.getType().maxHitPoints() > 0
	        	&& unit.getType().maxHitPoints() != unit.getHitPoints()
	        	)
	        {
	            double hpRatio = (double)unit.getHitPoints() / (double)unit.getType().maxHitPoints();
	        
	            Color hpColor = Color.Green;
	            if (hpRatio < 0.66) hpColor = Color.Orange;
	            if (hpRatio < 0.33) hpColor = Color.Red;
	
	            int ratioRight = left + (int)((right-left) * hpRatio);
	            int hpTop = top + verticalOffset;
	            int hpBottom = top + 4 + verticalOffset;
	
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Grey, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(ratioRight, hpBottom), hpColor, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Black, false);
	        }
	
	        if (!unit.getType().isResourceContainer() 
	        	&& unit.getType().maxShields() > 0
	        	&& unit.getType().maxShields() != unit.getShields()
	        	)
	        {
	            double shieldRatio = (double)unit.getShields() / (double)unit.getType().maxShields();
	        
	            int ratioRight = left + (int)((right-left) * shieldRatio);
	            int hpTop = top - 3 + verticalOffset;
	            int hpBottom = top + 1 + verticalOffset;
	            
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Grey, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(ratioRight, hpBottom), Color.Blue, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Black, false);
	        }
	    }
	}
	

	public static void drawResourcesRemaining()
	{
	    int verticalOffset = -10;
	    
	    // draw neutral units and our units
	    for (Unit unit : Globals.game.getAllUnits())
	    {
	        if (unit.getPlayer() == Globals.game.enemy())
	        {
	            continue;
	        }
	
	        Position pos = unit.getPosition();
	
	        int left    = pos.getX() - unit.getType().dimensionLeft();
	        int right   = pos.getX() + unit.getType().dimensionRight();
	        int top     = pos.getY() - unit.getType().dimensionUp();
	        int bottom  = pos.getY() + unit.getType().dimensionDown();
	        
	        if (unit.getType().isResourceContainer() && unit.getInitialResources() > 0)
	        {
	            
	            double mineralRatio = (double)unit.getResources() / (double)unit.getInitialResources();
	        
	            int ratioRight = left + (int)((right-left) * mineralRatio);
	            int hpTop = top + verticalOffset;
	            int hpBottom = top + 4 + verticalOffset;
	            
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Grey, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(ratioRight, hpBottom), Color.Cyan, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Black, false);
	
	            int ticWidth = 3;
	
	            for (int i = left; i < right-1; i+=ticWidth)
	            {
	                Globals.game.drawLineMap(new Position(i, hpTop), new Position(i, hpBottom), Color.Black);
	            }
	        }
	    }
	}
	
	
	public static void drawUnitInformation(int x, int y) 
	{
//		if (!Config::Debug::DrawEnemyUnitInfo)
//	    {
//	        return;
//	    }
	
//		String prefix = "\x04";
//	
//		Globals.game.drawTextScreen(x, y-10, "\x03 Self Loss:\x04 Minerals: \x1f%d \x04Gas: \x07%d", _playerData[_self].getMineralsLost(), _playerData[_self].getGasLost());
//	    Globals.game.drawTextScreen(x, y, "\x03 Enemy Loss:\x04 Minerals: \x1f%d \x04Gas: \x07%d", _playerData[_enemy].getMineralsLost(), _playerData[_enemy].getGasLost());
//		Globals.game.drawTextScreen(x, y+10, "\x04 Enemy: %s", Globals.game.enemy().getName().c_str());
//		Globals.game.drawTextScreen(x, y+20, "\x04 UNIT NAME");
//		Globals.game.drawTextScreen(x+140, y+20, "\x04#");
//		Globals.game.drawTextScreen(x+160, y+20, "\x04X");
	
//		int yspace = 0;
//	
//		// for each unit in the queue
//		for (UnitType t : UnitTypes.allUnitTypes()) 
//		{
//			int numUnits = _playerData[_enemy].getNumUnits(t);
//			int numDeadUnits = _playerData[_enemy].getNumDeadUnits(t);
//	
//			// if there exist units in the vector
//			if (numUnits > 0) 
//			{
//				if (t.isDetector())			{ prefix = "\x10"; }		
//				else if (t.canAttack())		{ prefix = "\x08"; }		
//				else if (t.isBuilding())	{ prefix = "\x03"; }
//				else						{ prefix = "\x04"; }
//	
//				Globals.game.drawTextScreen(x, y+40+((yspace)*10), " %s%s", prefix.c_str(), t.getName().c_str());
//				Globals.game.drawTextScreen(x+140, y+40+((yspace)*10), "%s%d", prefix.c_str(), numUnits);
//				Globals.game.drawTextScreen(x+160, y+40+((yspace++)*10), "%s%d", prefix.c_str(), numDeadUnits);
//			}
//		}
	}
	
	public static void drawMapInformation()
	{
//	    if (!Config::Debug::DrawBWTAInfo)
//	    {
//	        return;
//	    }
	
		//we will iterate through all the base locations, and draw their outlines.
		//for (Set<BaseLocation> const_iterator i = getBaseLocations().begin(); i != getBaseLocations().end(); i++)
		for (BaseLocation i : BWTA.getBaseLocations())
		{
			TilePosition p = i.getTilePosition();
			Position c = i.getPosition();
	
			//draw outline of center location
			Globals.game.drawBoxMap(p.getX() * 32, p.getY() * 32, p.getX() * 32 + 4 * 32, p.getY() * 32 + 3 * 32, Color.Blue);
	
			//if this is an island expansion, draw a yellow circle around the base location
			if (i.isIsland())
				Globals.game.drawCircleMap(c, 80, Color.Yellow);
		}
	
		//we will iterate through all the regions and draw the polygon outline of it in green.
		for (bwta.Region r : BWTA.getRegions())
		{
			List<Position> p = r.getPolygon().getPoints();
			for (int j = 0; j<(int)p.size(); j++)
			{
				Position point1 = p.get(j);
				Position point2 = p.get( (j+1) % p.size() );
				Globals.game.drawLineMap(point1, point2, Color.Green);
			}
		}
	
		//we will visualize the chokepoints with red lines
		for (bwta.Region r : BWTA.getRegions())
		{
			for (Chokepoint c : r.getChokepoints())
			{
				Position point1 = c.getSides().first;
				Position point2 = c.getSides().second;
				Globals.game.drawLineMap(point1, point2, Color.Red);
			}
		}
	}
}
