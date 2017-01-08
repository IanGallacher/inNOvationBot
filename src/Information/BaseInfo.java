package Information;

import java.util.ArrayList;
import java.util.HashMap;

import Macro.GasGeyser;
import Macro.MineralPatch;
import UnitController.UnitController;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

import Globals.Globals;
import Globals.Utility;


// Accessed from global by giving a position.
public class BaseInfo {    
	
	public ArrayList<MineralPatch> MineralPatches = new ArrayList<MineralPatch>();
	public ArrayList<GasGeyser> gasGeysers = new ArrayList<GasGeyser>();
	
	public TilePosition Location = null; // gets assigned in constructor.

	
	

	// TODO: Make the following private.
	public HashMap<Integer, UnitController> mineralWorkers = new HashMap<Integer, UnitController>();
	public HashMap<Integer, UnitController> gasWorkers = new HashMap<Integer, UnitController>();
	
	// Number of workers is total workers, including those on gas. 
	private int _numberOfWorkers = 0;
	public int getWorkersOnMineralsCount() { return _numberOfWorkers - getWorkersOnGasCount(); };
	public int getWorkersOnGasCount() { return gasWorkers.size(); };
	
	
    public BaseInfo(TilePosition baseLocation)
    {
    	this.Location = baseLocation;

    	// Eventually sort by distance to assigned base.
//    	for( bwta.BaseLocation b : BWTA.getBaseLocations() )
//    	{
//    		b.getMinerals().sort(c);;    		
//    	}
    	
    	for(Unit mineralPatch : BWTA.getNearestBaseLocation(baseLocation).getMinerals()) {
            if (mineralPatch.getType().isMineralField()) {
        		MineralPatches.add( new MineralPatch(mineralPatch) );
            }
        }

    	for(Unit gasGyser : BWTA.getNearestBaseLocation(baseLocation).getGeysers()) {
            if (gasGyser.getType() == UnitType.Resource_Vespene_Geyser) {
            	gasGeysers.add( new GasGeyser(gasGyser) );
            }
        }
    }
    
    public boolean closerThan(TilePosition t, BaseInfo compareLocation) {
    	int thisX = this.Location.getX();
    	int thisY = this.Location.getY();
    	double ourDistance = Utility.distance(thisX, thisY, t.getX(), t.getY());
    	
    	int theirX = compareLocation.Location.getX();
    	int theirY = compareLocation.Location.getY();
    	double theirDistance = Utility.distance(theirX, theirY, t.getX(), t.getY());
    	
    	if(ourDistance < theirDistance) return true;
    	else return false;
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
    
    public GasGeyser findOptimalAssimilator(Unit worker)
    {                
    	// TODO: Include if a mineral patch is being gathered from in this algorithm.
    	
    	GasGeyser closestGeyser = null;

        for( GasGeyser geyser : this.gasGeysers ) {
        	
        	// Yes, these can be condensed into a big if statement. However, I believe the following is more readable.
        	
        	// if there is no mineral marked the closest, mark it as the closest. 
        	if(closestGeyser == null) 
	            closestGeyser = geyser;
        	
        	// Most important criteria is to assign the SCV to the patch with the least amount of workers.
        	if(geyser.currentWorkerCount() < closestGeyser.currentWorkerCount()) 
        	{
	            closestGeyser = geyser;
        	}
        	
        	// If there is the same amount of workers, put it on a closer one
        	if(geyser.currentWorkerCount() < closestGeyser.currentWorkerCount()
        	&& geyser.getDistance(worker) < closestGeyser.getDistance(worker))
        		closestGeyser = geyser;
        }
        
        return closestGeyser;
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
