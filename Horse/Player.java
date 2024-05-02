package Horse;

import Horse.GameMap;

public class Player extends GameHorse {
      int color;
      static final int Horse=4;
      HorseBarn horsebarn =new HorseBarn(color);
      Des des =new Des(color);
      HorseSea horse[]= new HorseSea[Horse];
      Player(int color){
    	  this.color=color;
      }
      public void RemoveHorse(int id) {
    	  horse[id]=null;
      }
      public void addHorse(int id) {
    	  horse[id]=new HorseSea(color,id);
      }
      public void addMouseListener(GameMap map, int steps){
  		for(int i = 0; i < Horse; i++){
  			if(horse[i] != null){
  				horse[i].addMouseListener(map, steps);
  			}	
  		}
  	}

  	public void removeMouseListener(){
  		for(int i = 0; i < Horse; i++){
  			if(horse[i] != null){
  				horse[i].RemoveMouseListener();
  			}	
  		}
  	}

  	public  boolean isWin(){
  		return des.isWin();
  	}
  }
