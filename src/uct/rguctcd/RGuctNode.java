/**
* This file is an extension to code based on and translated from the open source project: Sparcraft
* https://code.google.com/p/sparcraft/
* author of the source: David Churchill
**/
package uct.rguctcd;

import java.util.List;

import sparcraft.Unit;
import uct.UnitState;
import uct.NodeType;
import uct.UctNode;

public class RGuctNode extends UctNode {

	private List<UnitState> abstractMove;
	private List<List<Unit>> clusters;
	
	public RGuctNode(RGuctNode parent, NodeType type, List<UnitState> abstractMove, int movingPlayerIndex, String label) {
		super(parent, type, null, movingPlayerIndex, label);
		this.abstractMove = abstractMove;
	}
	
	@Override
	public String moveString(){
		String moves = "";
		for(UnitState a : abstractMove){
			moves += a.type + ";";
		}
		return moves;
	}
	
	public List<UnitState> getAbstractMove() {
		return abstractMove;
	}

	public void setAbstractMove(List<UnitState> abstractMove) {
		this.abstractMove = abstractMove;
	}

	public List<List<Unit>> getClusters() {
		return clusters;
	}

	public void setClusters(List<List<Unit>> clusters) {
		this.clusters = clusters;
	}
	
}