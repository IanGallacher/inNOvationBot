/**
* This file is an extension to code based on and translated from the open source project: Sparcraft
* https://code.google.com/p/sparcraft/
* author of the source: David Churchill
**/
package uct.flatguctcd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clustering.UPGMA;
import uct.UctNode;
import uct.UnitState;
import uct.UnitStateTypes;
import uct.NodeType;
import sparcraft.EvaluationMethods;
import sparcraft.Game;
import sparcraft.GameState;
import sparcraft.Players;
import sparcraft.StateEvalScore;
import sparcraft.Unit;
import sparcraft.UnitAction;
import sparcraft.players.Player;
import sparcraft.players.Player_AttackClosest;
import sparcraft.players.Player_Defense;
import sparcraft.players.Player_Kite;
import sparcraft.players.Player_KiteDPS;
import sparcraft.players.Player_NoOverKillAttackValue;
import uct.UCT;
import uct.UctConfig;
import uct.UctStats;
import uct.guctcd.ClusteringConfig;

public class FlatGUCTCD extends UCT {

	private ClusteringConfig guctConfig;
	
	private List<List<Unit>> clusters;
	private List<List<Unit>> clustersB;
	
	public FlatGUCTCD(UctConfig uctConfig, ClusteringConfig guctConfig){
		super(uctConfig);
		this.guctConfig = guctConfig;
	}

	public List<UnitAction> search(GameState state, long timeBudget){
		
		if (config.getMaxPlayerIndex() == 0 && state.whoCanMove() == Players.Player_Two){
			return new ArrayList<UnitAction>(); 
		} else if (config.getMaxPlayerIndex() == 1 && state.whoCanMove() == Players.Player_One){
			return new ArrayList<UnitAction>(); 
		}
		
		long start = System.currentTimeMillis();
		long startNs = System.nanoTime();
		
		// Get clusters
		clusters = guctConfig.getClusterAlg().getClusters(state.getAllUnit()[config.getMaxPlayerIndex()], 6, guctConfig.getHpMulitplier());
		clustersB = new ArrayList<List<Unit>>();
		int enemy = 0;
		if (config.getMaxPlayerIndex() == 0)
			enemy = 1;
		List<Unit> enemyUnits = Arrays.asList(state.getAllUnit()[enemy]);
		clustersB.add(enemyUnits);
		
		//System.out.println("Nano time: " + (System.nanoTime() - startNs));
		
		UctNode root = new GuctNode(null, NodeType.ROOT, new ArrayList<UnitState>(), config.getMaxPlayerIndex(), "ROOT");
		root.setVisits(1);
		
		// Reset stats if new game
		if (state.getTime()==0)
			stats.reset();
		
		int t = 0;
		while(System.currentTimeMillis() <= start + timeBudget){
			
			traverse(root, state.clone());
			t++;
			
		}
		
		stats.getIterations().add(t);
		
		UctNode best = mostVisitedChildOf(root);
		//GuctNode best = mostWinningChildOf(root);
			
		if (config.isDebug())
			writeToFile(root.print(0), "tree.xml");
		
		if (best == null)
			return new ArrayList<UnitAction>();
		
		List<UnitAction> actions = statesToActions(((GuctNode)best).getAbstractMove(), state.clone());
		
		return actions;
		
	}

	private float traverse(UctNode node, GameState state) {
		
		float score = 0;
		if (node.getType() != NodeType.ROOT){
			if (node.getMove() == null)
				node.setMove(statesToActions(((GuctNode)node).getAbstractMove(), state));
			updateState(node, state, true);
			score = evaluate(state.clone());
		} else {
			updateState(node, state, false);
			if (state.isTerminal()){
				score = evaluate(state.clone());
			} else {
				int playerToMove = getPlayerToMove(node, state);
				if (node.getChildren().isEmpty())
					generateChildren(node, state, playerToMove);
				score = traverse(selectNode(node), state);
			}
		}
		
		if (config.isLTD2()){
			node.setTotalScore(node.getTotalScore() + score);
		} else {
			if (score > 0)
				node.setTotalScore(node.getTotalScore() + 1);
			else if (score == 0)
				node.setTotalScore(node.getTotalScore() + 0.5f);
		}
		
		node.setVisits(node.getVisits() + 1);
		
		return score;
	}

	private void generateChildren(UctNode node, GameState state, int playerToMove) {

		HashMap<Integer, List<UnitAction>> map;
		if (node.getPossibleMoves() == null){

			map = new HashMap<Integer, List<UnitAction>>();
			try {
				state.generateMoves(map, playerToMove);
			} catch (Exception e) {
				e.printStackTrace();
			}
			node.setPossibleMoves(map);
			
		}
				
		List<UnitState> moveAttack = new ArrayList<UnitState>();
		moveAttack.addAll(getAllMove(UnitStateTypes.ATTACK, clusters));
		GuctNode childAttack = new GuctNode((GuctNode)node, getChildNodeType(node, state), moveAttack, playerToMove, "NOK-AV");
		node.getChildren().add(childAttack);
		
		List<UnitState> moveKite = new ArrayList<UnitState>();
		moveKite.addAll(getAllMove(UnitStateTypes.KITE, clusters));
		GuctNode childKite = new GuctNode((GuctNode)node, getChildNodeType(node, state), moveKite, playerToMove, "NOK-AV");
		node.getChildren().add(childKite);
		
		int e = 2;
		while(e < config.getMaxChildren()){
			List<UnitState> moveRandom = new ArrayList<UnitState>();
			moveRandom = getRandomMove(playerToMove, clusters);
			if (uniqueMove(moveRandom, node)){
				GuctNode childRandom = new GuctNode((GuctNode)node, getChildNodeType(node, state), moveRandom, playerToMove, "NOK-AV");
				node.getChildren().add(childRandom);
			}
			e++;
		}
		/*
		if (uniqueMove(move, node)){
			GuctNode child = new GuctNode((GuctNode)node, getChildNodeType(node, state), move, playerToMove, label);
			node.getChildren().add(child);
		}
		*/
	}
	
	private List<UnitState> getAllMove(UnitStateTypes type, List<List<Unit>> clusters) {

		List<UnitState> states = new ArrayList<UnitState>();
		
		int i = 0;
		for(List<Unit> units : clusters){
			
			UnitState state = new UnitState(type, i, units.get(0).player());
			states.add(state);
			i++;
			
		}
		
		return states;
	}

	private boolean uniqueMove(List<UnitState> move, UctNode node) {

		if(node.getChildren().isEmpty())
			return true;
		
		for (UctNode child : node.getChildren()){
			int identical = 0;
			for(int i = 0; i < move.size(); i++){
				if (((GuctNode)child).getAbstractMove().get(i).equals(move.get(i)))
					identical++;
			}
			if (identical == move.size())
				return false;
		}
		
		return true;
		
	}

	private NodeType getChildNodeType(GuctNode parent, GameState prevState) {
		
		if(!prevState.bothCanMove()){
			
			return NodeType.SOLO;
			
		} else { 
			
			if (parent.getType() == NodeType.ROOT)
		
				return NodeType.FIRST;
			
			if (parent.getType() == NodeType.SOLO)
				
				return NodeType.FIRST;
			
			if (parent.getType() == NodeType.SECOND)
				
				return NodeType.FIRST;
			
			if (parent.getType() == NodeType.FIRST)
				
				return NodeType.SECOND;
		}
			
		return NodeType.DEFAULT;
	}

	private List<UnitState> getRandomMove(int playerToMove, List<List<Unit>> clusters) {
		
		List<UnitState> states = new ArrayList<UnitState>();
		
		int i = 0;
		for(List<Unit> units : clusters){
			
			// Random state
			UnitStateTypes type = UnitStateTypes.ATTACK;
			if (Math.random() >= 0.5f)
				type = UnitStateTypes.KITE;
			
			UnitState state = new UnitState(type, i, units.get(0).player());
			states.add(state);
			i++;
			
		}
		
		return states;
		
	}

	private List<UnitAction> statesToActions(List<UnitState> move, GameState state) {
		
		if (move == null || move.isEmpty() || move.get(0) == null)
			return new ArrayList<UnitAction>();
		
		Player attack = new Player_NoOverKillAttackValue(config.getMaxPlayerIndex());
		Player kite = new Player_Kite(config.getMaxPlayerIndex());
		
		HashMap<Integer, List<UnitAction>> map = new HashMap<Integer, List<UnitAction>>();
		
		try {
			state.generateMoves(map, config.getMaxPlayerIndex());
		} catch (Exception e) {e.printStackTrace();}
		
		List<Integer> attackingUnits = new ArrayList<Integer>();
		List<Integer> kitingUnits = new ArrayList<Integer>();
		
		// Divide units into two groups
		for(UnitState unitState : move){
			
			// Add units in cluster
			for(Unit u : clusters.get(unitState.unit)){
				
				if (u.isAlive() && (u.canAttackNow() || u.canMoveNow())){
				
					if (unitState.type == UnitStateTypes.ATTACK && u.isAlive())
						attackingUnits.add(u.getId());
					else if (unitState.type == UnitStateTypes.KITE && u.isAlive())
						kitingUnits.add(u.getId());
					
				}
				
			}
			
		}
		
		List<UnitAction> allActions = new ArrayList<UnitAction>();
		HashMap<Integer, List<UnitAction>> attackingMap = new HashMap<Integer, List<UnitAction>>();
		HashMap<Integer, List<UnitAction>> kitingMap = new HashMap<Integer, List<UnitAction>>();
		
		// Loop through the map
		for(Integer i : map.keySet()){
			int u = map.get(i).get(0)._unit;
			int unitId = state.getUnit(config.getMaxPlayerIndex(), u).getId();
			if (attackingUnits.contains(unitId))
				attackingMap.put(i, map.get(i)); 
			if (kitingUnits.contains(unitId))
				kitingMap.put(i, map.get(i));
		}
		
		// Add attack actions
		List<UnitAction> attackActions = new ArrayList<UnitAction>();
		attack.getMoves(state, attackingMap, attackActions);
		allActions.addAll(attackActions);
		
		// Add defend actions
		List<UnitAction> defendActions = new ArrayList<UnitAction>();
		kite.getMoves(state, kitingMap, defendActions);
		allActions.addAll(defendActions);
		
		return allActions;
	}

	public List<List<Unit>> getClusters() {
		return clusters;
	}
	
}