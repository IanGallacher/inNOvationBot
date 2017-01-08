package Macro;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

import Globals.Globals;

public class BuildingPlacementController {
	
	
	// The aroundTile argument will normally be the tile of the closest base, but does not have to be.
	// For example, proxy buildings won't be anywhere close to the base. 
    public static TilePosition getBuildTile(UnitType buildingType, TilePosition aroundTile) {
    	TilePosition ret = null;
    	int maxDist = 3;
    	int stopDist = 40;
    			
    	
    	// Refinery, Assimilator, Extractor
    	if (buildingType.isRefinery()) {
    		for (Unit n : Globals.game.neutral().getUnits()) {
    			if ((n.getType() == UnitType.Resource_Vespene_Geyser) && 
    					( Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist ) &&
    					( Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist )
    					) return n.getTilePosition();
    		}
    	}
    	
    	while ((maxDist < stopDist) && (ret == null)) {
    		for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
    			for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
    				if (Globals.game.canBuildHere(new TilePosition(i,j), buildingType)) {
    					// units that are blocking the tile
    					boolean unitsInWay = false;
    					for (Unit u : Globals.game.getAllUnits()) {
    						// if (u.getID() == builder.getID()) continue;
    						
    						// Place buildings far enough away from other buildings.
    						if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
    					}
    					if (!unitsInWay) {
    						return new TilePosition(i, j);
    					}
    				}
    			}
    		}
    		maxDist += 2;
    	}
    	
    	if (ret == null) Globals.game.printf("Unable to find suitable build position for "+buildingType.toString());
    	return ret;
    }
}
