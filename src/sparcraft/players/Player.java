/**
* This file is based on and translated from the open source project: Sparcraft
* https://code.google.com/p/sparcraft/
* author of the source: David Churchill
**/
package sparcraft.players;



import sparcraft.GameState;
import sparcraft.UnitAction;

import java.util.HashMap;
import java.util.List;

public abstract class Player {
 //Sparcraft players
	
	public int _id=0;
	
	public Player(){
		
	}
	
	public Player(int id){
		_id=id;
	}
	public void getMoves(GameState state, HashMap<Integer,List<UnitAction>> moves, List<UnitAction> moveVec){
		
	}
	public int ID(){return _id;}
	public void setID(int id){_id=id;}
}
