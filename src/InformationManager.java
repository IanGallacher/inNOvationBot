
import java.util.HashMap;
import java.util.HashSet;
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


	static HashMap<Player, PlayerUnitData> _playerUnitData = new HashMap<Player, PlayerUnitData>(); // a set of unit data that contains a set of unit info for each player
	static HashMap<Player, TilePosition>       _mainBaseLocations = new HashMap<Player, TilePosition>(); // Baselocations are where it makes sense to throw a base
	static HashMap<Player, HashSet<bwta.Region> >  _occupiedRegions = new HashMap<Player, HashSet<bwta.Region>>(); // bwta.Region IS DIFFERENT from bwapi.Region
	
	static HashMap<Integer, Unit> EnemeyBuildings = new HashMap<Integer, Unit>();

	
	public static void OnStart()
	{
		_playerUnitData = new HashMap<Player, PlayerUnitData>(); // a set of unit data that contains a set of unit info for each player
		_playerUnitData.put(Globals.self, new PlayerUnitData());
		_playerUnitData.put(Globals.enemy, new PlayerUnitData());
		
		_mainBaseLocations = new HashMap<Player, TilePosition>(); // Baselocations are where it makes sense to throw a base
		

//		// set initial pointers to null
//		_mainBaseLocations[_self] = getStartLocation(Globals.game.self());
//		_mainBaseLocations[_enemy] = getStartLocation(Globals.game.enemy());

//		// push that region into our occupied vector
//		updateOccupiedRegions(getRegion(_mainBaseLocations[_self].getTilePosition()), Globals.game.self());
		
		
		// What areas on the map are OK to build on? IE Not occupied by buildings
		_occupiedRegions = new HashMap<Player, HashSet<bwta.Region>>(); // bwta.Region IS DIFFERENT from bwapi.Region 
//		
		_occupiedRegions.put(Globals.self, new HashSet<bwta.Region>());
		_occupiedRegions.put(Globals.enemy, new HashSet<bwta.Region>());
	}
	
//	public static void GatherInformation() 
//	{
//		updateUnitInfo();
//		updateBuildingInfo();
//	}
	
	static void writeToDebugConsole() {
		int i = _playerUnitData.get(Globals.self).getNumUnits(UnitType.Protoss_Nexus);
		DebugController.debugConsolePrint("Nexus", i);
	}

	
	
	// Not strictly necessary because updateUnit gets called all the time. 
	// However, this makes the code more reliable and readable. 
	static void onUnitComplete(Unit unit) 
	{ 
	    if (unit.getType().isNeutral())
	    {
	        return;
	    }
	
	    _playerUnitData.get(unit.getPlayer()).addUnit(unit);
	    
	    if(unit.getPlayer() == Globals.enemy && unit.getType().isBuilding()) {
	    	EnemeyBuildings.remove(unit.getID());
	    }
	}
	
	static void onUnitDestroy(Unit unit) 
	{ 
	    if (unit.getType().isNeutral())
	    {
	        return;
	    }
	
	    _playerUnitData.get(unit.getPlayer()).removeUnit(unit);
	    
	    if(unit.getPlayer() == Globals.enemy && unit.getType().isBuilding()) {
	    	EnemeyBuildings.remove(unit.getID());
	    }
	}
	
	static void updateUnitData(Unit unit)
	{
	    if (!(unit.getPlayer() == Globals.self || unit.getPlayer() == Globals.enemy))
	    {
	        return;
	    }

	    PlayerUnitData p = _playerUnitData.get(unit.getPlayer() );

	    p.updateUnit(unit);
	    
	    updateKnownBaseInfo(unit) ;
	    

	    if(unit.getPlayer() == Globals.enemy && unit.getType().isBuilding() && EnemeyBuildings.containsKey(unit.getID()) == false) {
	    	EnemeyBuildings.put(unit.getID(), unit);
	    }
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
	
	
//	static void recalculateKnwonInfo() 
//	{
//		for (Unit unit : Globals.game.enemy().getUnits())
//		{
//			updateUnitData(unit);
//		}
//	
//		for (Unit unit : Globals.game.self().getUnits())
//		{
//			updateUnitData(unit);
//		}
//	
//		// remove bad enemy units
////		_playerData[_enemy].removeBadUnits();
////		_playerData[_self].removeBadUnits();
//	}
	
	// Check to see if any new buildings have been discovered.
	static void updateKnownBaseInfo(Unit unit) 
	{
		// if the unit is a building
		if (unit.getType().isBuilding()) 
		{
			// update the enemy occupied regions
			updateOccupiedRegions(BWTA.getRegion(unit.getPosition().toTilePosition()), unit.getPlayer());
		}
		
		// if we haven't found the enemy main base location yet
		if (_mainBaseLocations.containsKey(Globals.enemy) == false) 
		{ 
			// how many start locations have we explored
			int exploredStartLocations = 0;
			boolean mainBaseFound = false;
	
			// an unexplored base location holder
			TilePosition unexplored = null;
	
			for (TilePosition startLocation : Globals.game.getStartLocations()) 
			{
				
				if (isEnemyBuildingInRegion(BWTA.getRegion(startLocation.toPosition()))) 
				{
					Globals.game.printf("Enemy base found by seeing it");
	
					mainBaseFound = true;
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
			if (!mainBaseFound && exploredStartLocations == ((int) Globals.game.getStartLocations().size() - 1)) 
			{
	            Globals.game.printf("Enemy base found by process of elimination");
				
				_mainBaseLocations.put(Globals.enemy, unexplored);
				updateOccupiedRegions(BWTA.getRegion(unexplored), Globals.enemy);
			}
	// otherwise we do know it, so push it back
		} 
		else 
		{
		//	updateOccupiedRegions(BWTA.getRegion(_mainBaseLocations.get(Globals.enemy)), Globals.enemy);
		}
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

		for (UnitInfo unitInfo : _playerUnitData.get(Globals.enemy).UnitData.values())
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
		for (UnitInfo ui : _playerUnitData.get(player).UnitData.values())
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
	    for (UnitInfo ui : _playerUnitData.get(Globals.enemy).UnitData.values())
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
