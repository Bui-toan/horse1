package Horse;

public class Player extends GameHorse {
      int color;
      static final int Horse=4;
      HorseBarn horsebarn =new HorseBarn(color);
      des des =new des(color);
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
      
      
}
