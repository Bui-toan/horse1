package Horse;
import java.awt.event.*;
import javax.swing.*;
public class HorseSea extends GameHorse {
      private int color;
      private int ID,rank=Des.No_Rank;
      private int location; //vị trí cá ngựa trong mảng
      private JLabel label =new JLabel();
      static final int Finish_Location =56;
      
      HorseSea(int color,int ID){
    	  this.color=color;
    	  this.ID=ID;
    	  
          switch(color) {//vị trí xuất quân
          case RED:
    	      location=0;
    	      break;
          case YELLOW:
    	      location=42;
    	      break;
          case BLUE:
	          location=14;
	          break;
          case GREEN:
    	      location=28;
    	      break;
          default:
    	      System.err.println("Không có người chơi màu "+color+" này trong hệ thống");
          }
       }
      public int getRank() {
    	  return rank;
      }
      public void setRank(int rank) {
    	  this.rank=rank;
      }
      public int getLocation() {
    	  return location;
      }
      public int getID() {
    	  return ID;
      }
      public int getColor() {
    	  return color;
      }
      public void toFinish() {
    	  location= Finish_Location;
      }
      public boolean ChangeRank(Des des,int scores) {
    	  if(des.setdes(rank, scores,this)) {
    		  return true;
    	  }
    	  return false;
      }
      public JLabel getLabel() {
    	  return label;
      }
      
      public void setIcon(final Icon icon[]) {
    	  label.setIcon(icon[color]);
      }
      public void RemoveMouseListener() {
  		if (label != null) {
  			MouseListener list[] = label.getMouseListeners();
  			for (int i = 0; i < list.length; i++) {
  				label.removeMouseListener(list[i]);
  			}
  		}
  	}
    
}
