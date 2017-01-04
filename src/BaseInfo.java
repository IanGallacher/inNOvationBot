import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bwapi.Color;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;


// Accessed from global by giving a position.
public class BaseInfo {
	public ArrayList<MineralPatch> MineralPatches = new ArrayList<MineralPatch>();

	private int _numberOfWorkers = 0;
	
// Access the map data by doing BaseInfo.BaseData
    
    public BaseInfo(/*bwta.BaseLocation b*/) {
    	
    	// Eventually sort by distance to assigned base.
//    	for( Unit mineral : b.getMinerals() )
//    	{
//    		MineralPatches.add( new MineralPatch(mineral) );
//    	}
    }
    
    public void onStart()
    {
        //for (Unit neutralUnit : Globals.game.neutral().getUnits()) {
    	for(Unit neutralUnit : BWTA.getNearestBaseLocation(Globals.self.getStartLocation()).getMinerals()) {
            if (neutralUnit.getType().isMineralField()) {
        		MineralPatches.add( new MineralPatch(neutralUnit) );
            }
        }
    }
    
    public MineralPatch findOptimalMineralPatch(Unit worker)
    {                
    	// TODO: Include if a mineral patch is being gathered from in this algorithm.
    	
    	MineralPatch closestMineral = null;

    	int i = 1;
        for( MineralPatch mineral : this.MineralPatches ) {
        	
        	// Yes, these can be condensed into a big if statement. However, I believe the following is more readable.
        	
        	// if there is no mineral marked the closest, mark it as the closest. 
        	if(closestMineral == null) 
	            closestMineral = mineral;
        	
        	// Most important criteria is to assign the SCV to the patch with the least amount of workers.
        	if(mineral.currentWorkerCount() < closestMineral.currentWorkerCount()) 
        	{
	            closestMineral = mineral;
        	}
        	
        	// If there is the same amount of workers, put it on a closer one
        	if(mineral.currentWorkerCount() < closestMineral.currentWorkerCount()
        	&& mineral.getDistance(worker) < closestMineral.getDistance(worker))
        		closestMineral = mineral;
        }
        
        return closestMineral;
    }
    
    public boolean isFullySaturated() {
    	return false;
    }

	public void drawMapInformation()
	{
//	    if (!Config::Debug::DrawBWTAInfo)
//	    {
//	        return;
//	    }
	
		//we will iterate through all the base locations, and draw their outlines.
		//for (Set<BaseLocation> const_iterator i = getBaseLocations().begin(); i != getBaseLocations().end(); i++)
		BaseLocation i = BWTA.getNearestBaseLocation(Globals.self.getStartLocation());

		// Draw the number of workers at a mineral patch.
		for (MineralPatch m : this.MineralPatches)
		{
			Globals.game.drawTextMap(m.getX(), m.getY(), Integer.toString(m.currentWorkerCount()));
		}
	}
}
