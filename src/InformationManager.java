
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import bwapi.*;

import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import bwta.Polygon;



//Giving credit where credit is due. Much of this code was inspired by UAlbertaBot.
//https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/InformationManager.cpp
public class InformationManager {


	static HashMap<Player, PlayerData> _playerData = new HashMap<Player, PlayerData>(); // a set of unit data that contains a set of unit info for each player
	static HashMap<Player, TilePosition>       _mainBaseLocations = new HashMap<Player, TilePosition>(); // Baselocations are where it makes sense to throw a base
	static HashMap<Player, Set<bwta.Region> >  _occupiedRegions = new HashMap<Player, Set<bwta.Region>>(); // bwta.Region IS DIFFERENT from bwapi.Region

	
	public static void OnStart()
	{
		_playerData = new HashMap<Player, PlayerData>(); // a set of unit data that contains a set of unit info for each player
		_playerData.put(Globals.self, new PlayerData());
		_playerData.put(Globals.enemy, new PlayerData());
		
		_mainBaseLocations = new HashMap<Player, TilePosition>(); // Baselocations are where it makes sense to throw a base
		
		
		_occupiedRegions = new HashMap<Player, Set<bwta.Region>>(); // bwta.Region IS DIFFERENT from bwapi.Region
//		
//		_occupiedRegions.put(Globals.self, new Set<bwta.Region>());
//		_occupiedRegions.put(Globals.enemy, Set<bwta.Region>);
	}
	
	public static void GatherInformation() 
	{
		//updateUnitInfo();
		updateBaseLocationInfo();
		
    	//InformationManager.drawExtendedInterface();
    	//InformationManager.drawUnitInformation(425,30);
    	InformationManager.drawMapInformation();
    	Globals.BaseData.drawMapInformation();
	}
	
	void updateUnitInfo() 
	{
		for (Unit unit : Globals.game.enemy().getUnits())
		{
			updateUnit(unit);
		}
	
		for (Unit unit : Globals.game.self().getUnits())
		{
			updateUnit(unit);
		}
	
		// remove bad enemy units
//		_playerData[_enemy].removeBadUnits();
//		_playerData[_self].removeBadUnits();
	}
//	
//	void initializeRegionInformation() 
//	{
//		// set initial pointers to null
//		_mainBaseLocations[_self] = getStartLocation(Globals.game.self());
//		_mainBaseLocations[_enemy] = getStartLocation(Globals.game.enemy());
//	
//		// push that region into our occupied vector
//		updateOccupiedRegions(getRegion(_mainBaseLocations[_self].getTilePosition()), Globals.game.self());
//	}
//	
//	
	static void updateBaseLocationInfo() 
	{
		// _occupiedRegions.get(Globals.self).clear();
		// _occupiedRegions.get(Globals.enemy).clear();

		// if we haven't found the enemy main base location yet
		if (_mainBaseLocations.containsKey(Globals.enemy) == false) 
		{ 
			// how many start locations have we explored
			int exploredStartLocations = 0;
			boolean baseFound = false;
	
			// an unexplored base location holder
			TilePosition unexplored = null;
	
			for (TilePosition startLocation : Globals.game.getStartLocations()) 
			{
				
				if (isEnemyBuildingInRegion(BWTA.getRegion(startLocation.toPosition()))) 
				{
					Globals.game.printf("Enemy base found by seeing it");
	
					baseFound = true;
					_mainBaseLocations.put(Globals.enemy, startLocation);
					updateOccupiedRegions(BWTA.getRegion(startLocation.toPosition()), Globals.enemy);
				}
				
	
				// if it's explored, increment
				if (Globals.game.isExplored(startLocation)) 
				{
					exploredStartLocations++;
	
				// otherwise set the unexplored base
				} 
				else 
				{
					unexplored = startLocation;
				}
			}

			// if we've explored every start location except one, it's the enemy
			if (!baseFound && exploredStartLocations == ((int) Globals.game.getStartLocations().size() - 1)) 
			{
	            Globals.game.printf("Enemy base found by process of elimination");
				
				_mainBaseLocations.put(Globals.enemy, unexplored);
				updateOccupiedRegions(BWTA.getRegion(unexplored), Globals.enemy);
			}
	// otherwise we do know it, so push it back
		} 
		else 
		{
			//updateOccupiedRegions(BWTA.getRegion(_mainBaseLocations.get(Globals.enemy)), Globals.enemy);
		}
		
		// for each enemy unit we know about
//		for (UnitInfo ui : _playerData.get(Globals.enemy).getUnits().values())
//		{
//			UnitType type = ui.type;
//	
//			// if the unit is a building
//			if (type.isBuilding()) 
//			{
//				// update the enemy occupied regions
//				updateOccupiedRegions(getRegion(TilePosition(ui.lastPosition)), Globals.game.enemy());
//			}
//		}
	
		// for each of our units
//		for (UnitInfo ui : _playerData.get(Globals.self).getUnits().values())
//		{
//			UnitType type = ui.type;
//	
//			// if the unit is a building
//			if (type.isBuilding()) 
//			{
//				// update the enemy occupied regions
//				updateOccupiedRegions(BWTA.getRegion(ui.lastPosition), Globals.game.self());
//			}
//		}
	}
	
	static void updateOccupiedRegions(bwta.Region region, Player player) 
	{
		// if the region is valid (flying buildings may be in nullptr regions)
		if (region != null)
		{
			// add it to the list of occupied regions
			_occupiedRegions.get(player).add(region);
		}
	}
	
	static boolean isEnemyBuildingInRegion(bwta.Region region) 
	{
		// invalid regions aren't considered the same, but they will both be null
		if (region == null)
		{
			return false;
		}

		for (UnitInfo unitInfo : _playerData.get(Globals.enemy).UnitData.values())
		{
			if (unitInfo.type.isBuilding()) 
			{
				if (BWTA.getRegion(unitInfo.lastPosition) == region) 
				{
					return true;
				}
			}
		}
	
		return false;
	}
	
	static Set<bwta.Region> getOccupiedRegions(Player player)
	{
		return _occupiedRegions.get(player);
	}
	
	static TilePosition getMainBaseLocation(Player player) 
	{
		return _mainBaseLocations.get(player);
	}
	
	public static void drawExtendedInterface()
	{
//	    if (!Config::Debug::DrawUnitHealthBars)
//	    {
//	        return;
//	    }
	
	    int verticalOffset = -10;
	
	    // draw enemy units
		for (UnitInfo unitInfo : _playerData.get(Globals.enemy).UnitData.values())
		{
			UnitType type = unitInfo.type;
	        int hitPoints = unitInfo.lastHealth;
	        int shields = unitInfo.lastShields;
	
	        Position pos = unitInfo.lastPosition;
	
	        int left    = pos.getX() - type.dimensionLeft();
	        int right   = pos.getX() + type.dimensionRight();
	        int top     = pos.getY() - type.dimensionUp();
	        int bottom  = pos.getY() + type.dimensionDown();
	
	        if (!Globals.game.isVisible(unitInfo.lastPosition.toTilePosition()))
	        {
	            Globals.game.drawBoxMap(new Position(left, top), new Position(right, bottom), Color.Grey, false);
	            Globals.game.drawTextMap(new Position(left + 3, top + 4), unitInfo.type.toString());
	        }
	        
	        if (!type.isResourceContainer() && type.maxHitPoints() > 0)
	        {
	            double hpRatio = (double)hitPoints / (double)type.maxHitPoints();
	        
	            Color hpColor = Color.Green;
	            if (hpRatio < 0.66) hpColor = Color.Orange;
	            if (hpRatio < 0.33) hpColor = Color.Red;
	
	            int ratioRight = left + (int)((right-left) * hpRatio);
	            int hpTop = top + verticalOffset;
	            int hpBottom = top + 4 + verticalOffset;
	
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Grey, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(ratioRight, hpBottom), hpColor, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Black, false);
	
	            int ticWidth = 3;
	
	            for (int i = left; i < right-1; i+=ticWidth)
	            {
	                Globals.game.drawLineMap(new Position(i, hpTop), new Position(i, hpBottom), Color.Black);
	            }
	        }
	
	        if (!type.isResourceContainer() && type.maxShields() > 0)
	        {
	            double shieldRatio = (double)shields / (double)type.maxShields();
	        
	            int ratioRight = left + (int)((right-left) * shieldRatio);
	            int hpTop = top - 3 + verticalOffset;
	            int hpBottom = top + 1 + verticalOffset;
	
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Grey, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(ratioRight, hpBottom), Color.Blue, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Black, false);
	                                                       
	            int ticWidth = 3;
	
	            for (int i = left; i < right-1; i += ticWidth)
	            {
	                Globals.game.drawLineMap(new Position(i, hpTop), new Position(i, hpBottom), Color.Black);
	            }
	        }
	
	    }
	
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
	
	        if (!unit.getType().isResourceContainer() && unit.getType().maxHitPoints() > 0)
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
	
	            int ticWidth = 3;
	
	            for (int i = left; i < right-1; i += ticWidth)
	            {
	                Globals.game.drawLineMap(new Position(i, hpTop), new Position(i, hpBottom), Color.Black);
	            }
	        }
	
	        if (!unit.getType().isResourceContainer() && unit.getType().maxShields() > 0)
	        {
	            double shieldRatio = (double)unit.getShields() / (double)unit.getType().maxShields();
	        
	            int ratioRight = left + (int)((right-left) * shieldRatio);
	            int hpTop = top - 3 + verticalOffset;
	            int hpBottom = top + 1 + verticalOffset;
	            
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Grey, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(ratioRight, hpBottom), Color.Blue, true);
	            Globals.game.drawBoxMap(new Position(left, hpTop), new Position(right, hpBottom), Color.Black, false);
	
	            int ticWidth = 3;
	
	            for (int i = left; i < right-1; i += ticWidth)
	            {
	                Globals.game.drawLineMap(new Position(i, hpTop), new Position(i, hpBottom), Color.Black);
	            }
	        }
	
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
	
			//draw a circle at each mineral patch
			for (Unit j : i.getStaticMinerals())
			{
				Position q = j.getInitialPosition();
				Globals.game.drawCircleMap(q.getX(), q.getY(), 30, Color.Cyan);
			}
	
			//draw the outlines of vespene geysers
			for (Unit j : i.getGeysers())
			{
				TilePosition q = j.getInitialTilePosition();
				Globals.game.drawBoxMap(q.getX() * 32, q.getY() * 32, q.getX() * 32 + 4 * 32, q.getY() * 32 + 2 * 32, Color.Orange);
			}
	
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

	public static void onUnitCreate(Unit unit)
	{
		updateUnit(unit);
	}
	
	static void updateUnit(Unit unit)
	{
	    if (!(unit.getPlayer() == Globals.self || unit.getPlayer() == Globals.enemy))
	    {
	        return;
	    }
	
	    _playerData.get(unit.getPlayer()).updateUnit(unit);
	}
	
	// Is the unit a controllable unit that belongs to one of the players?
	static boolean isValidUnit(Unit unit) 
	{
		// we only care about our units and enemy units
		if (unit.getPlayer() != Globals.game.self() && unit.getPlayer() != Globals.game.enemy()) 
		{
			return false;
		}
	
		// if it's a weird unit, don't bother
		if (unit.getType() == UnitType.None || unit.getType() == UnitType.Unknown ||
			unit.getType() == UnitType.Zerg_Larva || unit.getType() == UnitType.Zerg_Egg) 
		{
			return false;
		}
	
		// if the position isn't valid throw it out
		if (!unit.getPosition().isValid()) 
		{
			return false;	
		}
	
		// s'all good baby baby
		return true;
	}
	
	static void onUnitDestroy(Unit unit) 
	{ 
	    if (unit.getType().isNeutral())
	    {
	        return;
	    }
	
//	    _playerData[unit.getPlayer()].removeUnit(unit);
	}
	
	static boolean isCombatUnit(UnitType type)
	{
		if (type == UnitType.Zerg_Lurker/* || type == UnitTypes.Protoss_Dark_Templar*/)
		{
			return false;
		}
	
		// check for various types of combat units
		if (type.canAttack() || 
			type == UnitType.Terran_Medic || 
			type == UnitType.Protoss_Observer ||
	        type == UnitType.Terran_Bunker)
		{
			return true;
		}
			
		return false;
	}
	
	static void getNearbyForce(Vector<UnitInfo> unitInfo, Position p, Player player, int radius) 
	{
		boolean hasBunker = false;
		// for each unit we know about for that player
		for (UnitInfo ui : _playerData.get(player).UnitData.values())
		{
			// if it's a combat unit we care about
			// and it's finished! 
			if (isCombatUnit(ui.type) && ui.completed)
			{
				// determine its attack range
				int range = 0;
				if (ui.type.groundWeapon() != WeaponType.None)
				{
					range = ui.type.groundWeapon().maxRange() + 40;
				}
	
				// if it can attack into the radius we care about
				if (ui.lastPosition.getDistance(p) <= (radius + range))
				{
					// add it to the vector
					unitInfo.add(ui);
				}
			}
			else if (ui.type.isDetector() && ui.lastPosition.getDistance(p) <= (radius + 250))
	        {
				// add it to the vector
				unitInfo.add(ui);
	        }
		}
	}
	
//	int getNumUnits(UnitType t, Player player)
//	{
//		return _playerData.get(player).getNumUnits(t);
//	}
	
	static boolean enemyHasCloakedUnits()
	{
	    for (UnitInfo ui : _playerData.get(Globals.enemy).UnitData.values())
		{
	        if (ui.type.isCloakable())
	        {
	            return true;
	        }
	
	        // assume they're going dts
	        if (ui.type == UnitType.Protoss_Citadel_of_Adun)
	        {
	            return true;
	        }
	
	        if (ui.type == UnitType.Protoss_Observatory)
	        {
	            return true;
	        }
	    }
	
		return false;
	}
}
