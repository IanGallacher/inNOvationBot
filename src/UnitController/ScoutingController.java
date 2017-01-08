package UnitController;

import java.util.Vector;

import bwapi.*;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BaseLocation;

import Globals.Globals;
import Information.InformationManager;


// Giving credit where credit is due. Much of this code was inspired by UAlbertaBot.
// https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/ScoutManager.cpp
public class ScoutingController {
	//private static boolean _isScoutUnderAttack = false;
	private static int  _currentRegionVertexIndex = -1;
	// private static int  _previousScoutHP = 0;
	private static Unit _workerScout;
	static Vector<Position> _enemyRegionVertices;
	static boolean _scoutUnderAttack;
	
	static boolean _shouldScoutHarassEnemy = false;
	
	static BaseLocation enemyBaseLocation;
	
	static String _scoutStatus;
	
	
	
	
	public static void onFrame()
	{
		if(_workerScout == null) {
	        //iterate through my units
	        for (Unit myUnit : Globals.self.getUnits()) {
	        	
	            if (myUnit.getType().isWorker()) {
// gets called a bunch of times.
	    	        System.out.println("Set Scout");
	            	setWorkerScout(myUnit);
	            }
	        }
		}
//        
//	    // calculate enemy region vertices if we haven't yet
//	    if (_enemyRegionVertices.isEmpty())
//	    {
//	        calculateEnemyRegionVertices();
//	    }
	
		moveScouts();
	    //drawScoutInformation(200, 320);
	}
	
	public static void setWorkerScout(Unit unit)
	{
	    // if we have a previous worker scout, release it back to the worker manager
//	    if (_workerScout)
//	    {
//	        WorkerManager::Instance().finishedWithWorker(_workerScout);
//	    }

        System.out.println("set scout");
        
	    _workerScout = unit;
	    UnitManager.get(unit.getID()).stopTask();
//	    WorkerManager::Instance().setScoutWorker(_workerScout);
	}
	
	static void drawScoutInformation(int x, int y)
	{
		Globals.game.drawTextScreen(x, y, "ScoutInfo: " + _scoutStatus);
	    for (int i = 0; i < _enemyRegionVertices.size(); ++i)
	    {
	    	Globals.game.drawCircleMap(_enemyRegionVertices.get(i), 4, Color.Green, false);
	    	Globals.game.drawTextMap(_enemyRegionVertices.get(i), Integer.toString(i));
	    }
	}
	
	static void moveScouts()
	{
		if (_workerScout == null || !_workerScout.exists() || !(_workerScout.getHitPoints() > 0))
		{
			return;
		}
	
	     int scoutHP = _workerScout.getHitPoints() + _workerScout.getShields();
	    
	
		// get the enemy base location, if we have one
		TilePosition enemyBaseLocation = InformationManager.getMainBaseLocation(Globals.game.enemy());
	    
//	    int scoutDistanceThreshold = 30;
//	
//	    if (_workerScout.isCarryingGas())
//	    {
//	        Globals.game.drawCircleMap(_workerScout.getPosition(), 10, Color.Purple, true);
//	    }
//	    
//		// if we know where the enemy region is and where our scout is
//		if (_workerScout != null && enemyBaseLocation != null)
//		{
//	        int scoutDistanceToEnemy = MapTools.Instance().getGroundDistance(_workerScout.getPosition(), enemyBaseLocation.getPosition());
//	        boolean scoutInRangeOfenemy = scoutDistanceToEnemy <= scoutDistanceThreshold;
//	        
//	        // we only care if the scout is under attack within the enemy region
//	        // this ignores if their scout worker attacks it on the way to their base
////	        if (scoutHP < _previousScoutHP)
////	        {
////		        _scoutUnderAttack = true;
////	        }
////	
////	        if (!_workerScout.isUnderAttack() && !enemyWorkerInRadius())
////	        {
////		        _scoutUnderAttack = false;
////	        }
//	
//			// if the scout is in the enemy region
//			if (scoutInRangeOfenemy)
//			{
//		// get the closest enemy worker
//				Unit closestWorker = closestEnemyWorker();
//				
//				// if the worker scout is not under attack
//				if (!_scoutUnderAttack)
//				{
//					// if there is a worker nearby, harass it
//					if ( /* _shouldScoutHarassEnemy && closestWorker && */ (_workerScout.getDistance(closestWorker) < 800))
//					{
//				        _scoutStatus = "Harass enemy worker";
//				        _currentRegionVertexIndex = -1;
//						//Micro::SmartAttackUnit(_workerScout, closestWorker);
//					}
//					// otherwise keep moving to the enemy region
//					else
//					{
//				        _scoutStatus = "Following perimeter";
//				        followPerimeter();  
//				    }
//					
//				}
//				// if the worker scout is under attack
//				else
//				{
//				    _scoutStatus = "Under attack inside, fleeing";
//				    followPerimeter();
//				}
//			}
//			// if the scout is not in the enemy region
//			else if (_scoutUnderAttack)
//			{
//				_scoutStatus = "Under attack inside, fleeing";
//				
//				followPerimeter();
//			}
//			else
//			{
//				_scoutStatus = "Enemy region known, going there";
//				
//				// move to the enemy region
//				followPerimeter();
//			}
//			
//		}
		
		// for each start location in the level
		if (enemyBaseLocation == null)
		{
			_scoutStatus = "Enemy base unknown, exploring";
			
			for (TilePosition startLocation : Globals.game.getStartLocations())
			{
				// if we haven't explored it yet
				if (!Globals.game.isExplored(startLocation) )
				{
					// assign a zergling to go scout it
					//Micro::SmartMove(_workerScout, BWAPI::Position(startLocation.getTilePosition()));		
					_workerScout.move(startLocation.toPosition());
					return;
				}
			}
		}
		
		// _previousScoutHP = scoutHP;
	}
		
	static void followPerimeter()
	{
		Position fleeTo = getFleePosition();
		
		Globals.game.drawCircleMap(fleeTo, 5, Color.Red, true);
	
		//Micro::SmartMove(_workerScout, fleeTo);
		
		_workerScout.move(fleeTo);
	}
	

	
	static Unit closestEnemyWorker()
	{
		Unit enemyWorker = null;
		double maxDist = 0.0;
		
		
		Unit geyser = getEnemyGeyser();
		
		for (Unit unit : Globals.game.enemy().getUnits())
		{
			if (unit.getType().isWorker() && unit.isConstructing())
			{
				return unit;
			}
		}
		
			// for each enemy worker
		for (Unit unit : Globals.game.enemy().getUnits())
		{
			if (unit.getType().isWorker())
			{
				double dist = unit.getDistance(geyser);
				
				if (dist < 800 && dist > maxDist)
				{
					maxDist = dist;
					enemyWorker = unit;
				}
			}
		}
		
		return enemyWorker;
	}
	
	static Unit getEnemyGeyser()
	{
//		Unit geyser = nullptr;
//		BaseLocation enemyBaseLocation = InformationManager::Instance().getMainBaseLocation(BWAPI::Globals.game.enemy());
//		
//		for (auto & unit : enemyBaseLocation.getGeysers())
//		{
//			geyser = unit;
//		}
//		
//		return geyser;
		return null;
	}
	
	static boolean enemyWorkerInRadius()
	{
		for ( Unit unit : Globals.game.enemy().getUnits())
		{
			if (unit.getType().isWorker() && (unit.getDistance(_workerScout) < 300))
			{
				return true;
			}
		}
		
		return false;
	}
	
	static boolean immediateThreat()
	{
		// Unitset enemyAttackingWorkers;
		for (Unit unit : Globals.game.enemy().getUnits())
		{
			if (unit.getType().isWorker() && unit.isAttacking())
			{
				//enemyAttackingWorkers.insert(unit);
			}
		}
		
		if (_workerScout.isUnderAttack())
		{
			return true;
		}
		
		for (Unit unit : Globals.game.enemy().getUnits())
		{
			double dist = unit.getDistance(_workerScout);
			double range = unit.getType().groundWeapon().maxRange();
			
			if (unit.getType().canAttack() && !unit.getType().isWorker() && (dist <= range + 32))
			{
				return true;
			}
		}
		
		return false;
	}
	
	static int getClosestVertexIndex(Unit unit)
	{
		int closestIndex = -1;
		double closestDistance = 10000000;
		
		for (int i = 0; i < _enemyRegionVertices.size(); ++i)
		{
			double dist = unit.getDistance(_enemyRegionVertices.get(i));
			if (dist < closestDistance)
			{
				closestDistance = dist;
				closestIndex = i;
			}
		}
	
		return closestIndex;
	}
	
	static Position getFleePosition()
	{
	//	UAB_ASSERT_WARNING(!_enemyRegionVertices.empty(), "We should have an enemy region vertices if we are fleeing");
		
		// BaseLocation enemyBaseLocation = InformationManager::Instance().getMainBaseLocation(BWAPI::Globals.game.enemy());
		
		// if this is the first flee, we will not have a previous perimeter index
		if (_currentRegionVertexIndex == -1)
		{
			// so return the closest position in the polygon
			int closestPolygonIndex = getClosestVertexIndex(_workerScout);
			
			//UAB_ASSERT_WARNING(closestPolygonIndex != -1, "Couldn't find a closest vertex");
			
			if (closestPolygonIndex == -1)
			{
				return Globals.game.self().getStartLocation().toPosition();
			}
			else
			{
				// set the current index so we know how to iterate if we are still fleeing later
				_currentRegionVertexIndex = closestPolygonIndex;
				return _enemyRegionVertices.get(closestPolygonIndex);
			}
		}
		// if we are still fleeing from the previous frame, get the next location if we are close enough
		else
		{
			double distanceFromCurrentVertex = _enemyRegionVertices.get(_currentRegionVertexIndex).getDistance(_workerScout.getPosition());
			
			// keep going to the next vertex in the perimeter until we get to one we're far enough from to issue another move command
			while (distanceFromCurrentVertex < 128)
			{
				_currentRegionVertexIndex = (_currentRegionVertexIndex + 1) % _enemyRegionVertices.size();
				
				distanceFromCurrentVertex = _enemyRegionVertices.get(_currentRegionVertexIndex).getDistance(_workerScout.getPosition());
			}
			
			return _enemyRegionVertices.get(_currentRegionVertexIndex);
		}
		
	}
	
	static void calculateEnemyRegionVertices()
	{
//		BaseLocation enemyBaseLocation = InformationManager::Instance().getMainBaseLocation(Globals.game.enemy());
//		//UAB_ASSERT_WARNING(enemyBaseLocation, "We should have an enemy base location if we are fleeing");
//		
//		if (!enemyBaseLocation)
//		{
//			return;
//		}
//	
//		Region enemyRegion = enemyBaseLocation.getRegion();
//		//UAB_ASSERT_WARNING(enemyRegion, "We should have an enemy region if we are fleeing");
//		
//		if (!enemyRegion)
//		{
//			return;
//		}
//	
//		Position basePosition = Position(Globals.game.self().getStartLocation());
//		vector<TilePosition> & closestTobase = MapTools::Instance().getClosestTilesTo(basePosition);
//		
//		std::set<BWAPI::Position> unsortedVertices;
//		
//		// check each tile position
//		for (size_t i(0); i < closestTobase.size(); ++i)
//		{
//			const BWAPI::TilePosition & tp = closestTobase[i];
//			
//			if (BWTA::getRegion(tp) != enemyRegion)
//			{
//				continue;
//			}
//	
//			// a tile is 'surrounded' if
//			// 1) in all 4 directions there's a tile position in the current region
//			// 2) in all 4 directions there's a buildable tile
//			bool surrounded = true;
//			if (BWTA::getRegion(BWAPI::TilePosition(tp.x+1, tp.y)) != enemyRegion || !BWAPI::Globals.game.isBuildable(BWAPI::TilePosition(tp.x+1, tp.y))
//			|| BWTA::getRegion(BWAPI::TilePosition(tp.x, tp.y+1)) != enemyRegion || !BWAPI::Globals.game.isBuildable(BWAPI::TilePosition(tp.x, tp.y+1))
//			|| BWTA::getRegion(BWAPI::TilePosition(tp.x-1, tp.y)) != enemyRegion || !BWAPI::Globals.game.isBuildable(BWAPI::TilePosition(tp.x-1, tp.y))
//			|| BWTA::getRegion(BWAPI::TilePosition(tp.x, tp.y-1)) != enemyRegion || !BWAPI::Globals.game.isBuildable(BWAPI::TilePosition(tp.x, tp.y -1))) 
//			{ 
//			surrounded = false; 
//			}
//			
//			// push the tiles that aren't surrounded
//			if (!surrounded && BWAPI::Globals.game.isBuildable(tp))
//			{
//				if (Config::Debug::DrawScoutInfo)
//				{
//				    int x1 = tp.x * 32 + 2;
//				    int y1 = tp.y * 32 + 2;
//				    int x2 = (tp.x+1) * 32 - 2;
//				    int y2 = (tp.y+1) * 32 - 2;
//				
//				    BWAPI::Globals.game.drawTextMap(x1+3, y1+2, "%d", MapTools::Instance().getGroundDistance(BWAPI::Position(tp), basePosition));
//				    BWAPI::Globals.game.drawBoxMap(x1, y1, x2, y2, BWAPI::Colors::Green, false);
//				}
//				
//				unsortedVertices.insert(BWAPI::Position(tp) + BWAPI::Position(16, 16));
//			}
//		}
//	
//		
//		vector<BWAPI::Position> sortedVertices;
//		Position current = unsortedVertices.begin();
//		
//		_enemyRegionVertices.push_back(current);
//		unsortedVertices.erase(current);
//	
//		// while we still have unsorted vertices left, find the closest one remaining to current
//		while (!unsortedVertices.empty())
//		{
//			double bestDist = 1000000;
//			BWAPI::Position bestPos;
//			
//			for (const BWAPI::Position & pos : unsortedVertices)
//			{
//				double dist = pos.getDistance(current);
//				
//				if (dist < bestDist)
//				{
//				    bestDist = dist;
//				    bestPos = pos;
//				}
//			}
//		
//			current = bestPos;
//			sortedVertices.push_back(bestPos);
//			unsortedVertices.erase(bestPos);
//		}
//	
//		// let's close loops on a threshold, eliminating death grooves
//		int distanceThreshold = 100;
//		
//		while (true)
//		{
//			// find the largest index difference whose distance is less than the threshold
//			int maxFarthest = 0;
//			int maxFarthestStart = 0;
//			int maxFarthestEnd = 0;
//			
//			// for each starting vertex
//			for (int i(0); i < (int)sortedVertices.size(); ++i)
//			{
//				int farthest = 0;
//				int farthestIndex = 0;
//				
//				// only test half way around because we'll find the other one on the way back
//				for (size_t j(1); j < sortedVertices.size()/2; ++j)
//				{
//				    int jindex = (i + j) % sortedVertices.size();
//				
//				    if (sortedVertices[i].getDistance(sortedVertices[jindex]) < distanceThreshold)
//				    {
//				        farthest = j;
//				        farthestIndex = jindex;
//				    }
//				}
//				
//				if (farthest > maxFarthest)
//				{
//				    maxFarthest = farthest;
//				    maxFarthestStart = i;
//				    maxFarthestEnd = farthestIndex;
//				}
//			}
//		
//			// stop when we have no long chains within the threshold
//			if (maxFarthest < 4)
//			{
//				break;
//			}
//			
//			double dist = sortedVertices[maxFarthestStart].getDistance(sortedVertices[maxFarthestEnd]);
//			
//			std::vector<BWAPI::Position> temp;
//			
//			for (size_t s(maxFarthestEnd); s != maxFarthestStart; s = (s+1) % sortedVertices.size())
//			{
//			temp.push_back(sortedVertices[s]);
//			}
//			
//			sortedVertices = temp;
//		}
//		
//		_enemyRegionVertices = sortedVertices;
	}

}