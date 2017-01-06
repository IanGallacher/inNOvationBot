package Globals;
import java.util.HashMap;
import java.util.Vector;

import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;

// Credit where it is due: most of this file is a port of UAlbertaBot's MapTools.cpp
// https://github.com/davechurchill/ualbertabot/blob/922966f5f1442029f811d9c6a34d9ba94fc871df/UAlbertaBot/Source/MapTools.cpp
public class MapTools {
	// constructor for MapTools
	
    private static int _rows = Globals.game.mapHeight();
    private static int _cols = Globals.game.mapWidth();
//    private static HashMap<Position, DistanceMap> _allMaps;    
    private static Vector<Boolean> _map;        // the map stored at TilePosition resolution, values are 0/1 for walkable or not walkable
    private static Vector<Boolean> _units;      // map that stores whether a unit is on this position
    private static Vector<Integer>  _fringe;     // the fringe vector which is used as a sort of 'open list'
    
//	MapTools()
//	{
//	    _map    = std::vector<bool>(_rows*_cols,false);
//	    _units  = std::vector<bool>(_rows*_cols,false);
//	    _fringe = std::vector<int>(_rows*_cols,0);
//	
//	    setBWAPIMapData();
//	}
	
	// return the index of the 1D array from (row,col)
	public static int getIndex(int row,int col)
	{
	    return row * _cols + col;
	}
	
//	boolean unexplored(DistanceMap dmap, int index)
//	{
//	    return (index != -1) && dmap[index] == -1 && _map[index];
//	}
	
	// resets the distance and fringe vectors, call before each search
//	void reset()
//	{
//	    std::fill(_fringe.begin(),_fringe.end(),0);
//	}
//	
//	// reads in the map data from bwapi and stores it in our map format
//	void setBWAPIMapData()
//	{
//	    // for each row and column
//	    for (int r = 0; r < _rows; ++r)
//	    {
//	        for (int c = 0; c < _cols; ++c)
//	        {
//	            bool clear = true;
//	
//	            // check each walk tile within this TilePosition
//	            for (int i=0; i<4; ++i)
//	            {
//	                for (int j=0; j<4; ++j)
//	                {
//	                    if (!Globals.isWalkable(c*4 + i,r*4 + j))
//	                    {
//	                        clear = false;
//	                        break;
//	                    }
//	
//	                    if (clear)
//	                    {
//	                        break;
//	                    }
//	                }
//	            }
//	
//	            // set the map as binary clear or not
//	            _map[getIndex(r,c)] = clear;
//	        }
//	    }
//	}
	
//	void resetFringe()
//	{
//	    std::fill(_fringe.begin(),_fringe.end(),0);
//	}
	
	public static int getGroundDistance(Position origin,Position destination)
	{
//	    // if we have too many maps, reset our stored maps in case we run out of memory
//	    if (_allMaps.size() > 20)
//	    {
//	        _allMaps.clear();
//	
//	        Globals.printf("Cleared stored distance map cache");
//	    }
//	
//	    // if we haven't yet computed the distance map to the destination
//	    if (_allMaps.find(destination) == _allMaps.end())
//	    {
//	        // if we have computed the opposite direction, we can use that too
//	        if (_allMaps.find(origin) != _allMaps.end())
//	        {
//	            return _allMaps[origin][destination];
//	        }
//	
//	        // add the map and compute it
//	        _allMaps.insert(std::pair<Position,DistanceMap>(destination,DistanceMap()));
//	        computeDistance(_allMaps[destination],destination);
//	    }
//	
//	    // get the distance from the map
//	    return _allMaps[destination][origin];
		int deltaX = origin.getX() - destination.getX();
		int deltaY = origin.getY() - destination.getY();
		return (int) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}
	
	// computes walk distance from Position P to all other points on the map
//	void computeDistance(DistanceMap dmap, Position p)
//	{
//	    search(dmap, p.getY() / 32, p.getX() / 32);
//	}
	
//	// does the dynamic programming search
//	void search(DistanceMap dmap, int sR, int sC)
//	{
//	    // reset the internal variables
//	    resetFringe();
//	
//	    // set the starting position for this search
//	    dmap.setStartPosition(sR,sC);
//	
//	    // set the distance of the start cell to zero
//	    dmap[getIndex(sR,sC)] = 0;
//	
//	    // set the fringe variables accordingly
//	    int fringeSize(1);
//	    int fringeIndex(0);
//	    _fringe[0] = getIndex(sR,sC);
//	    dmap.addSorted(getTilePosition(_fringe[0]));
//	
//	    // temporary variables used in search loop
//	    int currentIndex,nextIndex;
//	    int newDist;
//	
//	    // the size of the map
//	    int size = _rows*_cols;
//	
//	    // while we still have things left to expand
//	    while (fringeIndex < fringeSize)
//	    {
//	        // grab the current index to expand from the fringe
//	        currentIndex = _fringe[fringeIndex++];
//	        newDist = dmap[currentIndex] + 1;
//	
//	        // search up
//	        nextIndex = (currentIndex > _cols) ? (currentIndex - _cols) : -1;
//	        if (unexplored(dmap,nextIndex))
//	        {
//	            // set the distance based on distance to current cell
//	            dmap.setDistance(nextIndex,newDist);
//	            dmap.setMoveTo(nextIndex,'D');
//	            dmap.addSorted(getTilePosition(nextIndex));
//	
//	            // put it in the fringe
//	            _fringe[fringeSize++] = nextIndex;
//	        }
//	
//	        // search down
//	        nextIndex = (currentIndex + _cols < size) ? (currentIndex + _cols) : -1;
//	        if (unexplored(dmap,nextIndex))
//	        {
//	            // set the distance based on distance to current cell
//	            dmap.setDistance(nextIndex,newDist);
//	            dmap.setMoveTo(nextIndex,'U');
//	            dmap.addSorted(getTilePosition(nextIndex));
//	
//	            // put it in the fringe
//	            _fringe[fringeSize++] = nextIndex;
//	        }
//	
//	        // search left
//	        nextIndex = (currentIndex % _cols > 0) ? (currentIndex - 1) : -1;
//	        if (unexplored(dmap,nextIndex))
//	        {
//	            // set the distance based on distance to current cell
//	            dmap.setDistance(nextIndex,newDist);
//	            dmap.setMoveTo(nextIndex,'R');
//	            dmap.addSorted(getTilePosition(nextIndex));
//	
//	            // put it in the fringe
//	            _fringe[fringeSize++] = nextIndex;
//	        }
//	
//	        // search right
//	        nextIndex = (currentIndex % _cols < _cols - 1) ? (currentIndex + 1) : -1;
//	        if (unexplored(dmap,nextIndex))
//	        {
//	            // set the distance based on distance to current cell
//	            dmap.setDistance(nextIndex,newDist);
//	            dmap.setMoveTo(nextIndex,'L');
//	            dmap.addSorted(getTilePosition(nextIndex));
//	
//	            // put it in the fringe
//	            _fringe[fringeSize++] = nextIndex;
//	        }
//	    }
//	}
	
//	Vector<TilePosition> getClosestTilesTo(Position pos)
//	{
//	    // make sure the distance map is calculated with pos as a destination
//	    int a = getGroundDistance(Globals.self.getStartLocation().toPosition(),pos);
//	
//	    return _allMaps[pos].getSortedTiles();
//	}
	
//	TilePosition getTilePosition(int index)
//	{
//	    return new TilePosition(index % _cols,index / _cols);
//	}
//	
	public static TilePosition getNextExpansion()
	{
	    return getNextExpansion(Globals.self);
	}
	
	public static void drawHomeDistanceMap()
	{
	    Position homePosition = Globals.self.getStartLocation().toPosition();
	    for (int x = 0; x < Globals.game.mapWidth(); ++x)
	    {
	        for (int y = 0; y < Globals.game.mapHeight(); ++y)
	        {
	            Position pos = new Position(x*32, y*32);
	
	            int dist = getGroundDistance(pos, homePosition);
	
	            Position p = new Position(pos.getX()+16, pos.getY()+16);
	            Globals.game.drawTextMap(p, Integer.toString(dist));
	        }
	    }
	}
	
	public static TilePosition getNextExpansion(Player player)
	{
	    bwta.BaseLocation closestBase = null;
	    double minDistance = 100000;
	
	    TilePosition homeTile = player.getStartLocation();
	
	    // for each base location
	    for (bwta.BaseLocation base : BWTA.getBaseLocations())
	    {
	        // if the base has gas
	        if (!base.isMineralOnly() && !(base == BWTA.getStartLocation(player)))
	        {
	            // get the tile position of the base
	            TilePosition tile = base.getTilePosition();
	            boolean buildingInTheWay = false;
	
	            for (int x = 0; x < Globals.self.getRace().getCenter().tileWidth(); ++x)
	            {
	                for (int y = 0; y < Globals.self.getRace().getCenter().tileHeight(); ++y)
	                {
	                    TilePosition tp = new TilePosition(tile.getX() + x, tile.getY() + y);
	
	                    for (Unit unit : Globals.game.getUnitsOnTile(tp))
	                    {
	                        if (unit.getType().isBuilding() && !unit.isFlying())
	                        {
	                            buildingInTheWay = true;
	                            break;
	                        }
	                    }
	                }
	            }
	            
	            if (buildingInTheWay)
	            {
	                continue;
	            }
	
	            // the base's distance from our main nexus
	            Position myBasePosition = player.getStartLocation().toPosition();
	            Position thisTile = tile.toPosition();
	            double distanceFromHome = getGroundDistance(thisTile,myBasePosition);
	
	            // if it is not connected, continue
	            if (!BWTA.isConnected(homeTile,tile) || distanceFromHome < 0)
	            {
	                continue;
	            }
	
	            if (closestBase == null || distanceFromHome < minDistance)
	            {
	                closestBase = base;
	                minDistance = distanceFromHome;
	            }
	        }
	
	    }
	
	    if (closestBase != null)
	    {
	        return closestBase.getTilePosition();
	    }
	    else
	    {
	        return TilePosition.None;
	    }
	}
	
//	void parseMap()
//	{
//	    std::ofstream mapFile;
//	    std::string file = "c:\\scmaps\\" + Globals.mapName() + ".txt";
//	    mapFile.open(file.c_str());
//	
//	    mapFile << Globals.mapWidth()*4 << "\n";
//	    mapFile << Globals.mapHeight()*4 << "\n";
//	
//	    for (int j=0; j<Globals.mapHeight()*4; j++) 
//	    {
//	        for (int i=0; i<Globals.mapWidth()*4; i++) 
//	        {
//	            if (Globals.isWalkable(i,j)) 
//	            {
//	                mapFile << "0";
//	            }
//	            else 
//	            {
//	                mapFile << "1";
//	            }
//	        }
//	
//	        mapFile << "\n";
//	    }
//	
//	    Globals.printf(file.c_str());
//	
//	    mapFile.close();
//	}
}