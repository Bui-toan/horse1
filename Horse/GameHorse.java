package Horse;
import javax.swing.JOptionPane;
import java.util.concurrent.Semaphore;

public class GameHorse {
      static final int RED=4;
      static final int YELLOW=2;
      static final int BLUE=1;
      static final int GREEN=3;
      static final int DISTANCES=50;
      
      static boolean ThrowPhaseFlag=true; //tung xúc sắc
      static boolean HorseFLAG =false;   // đi cá ngựa
      static Semaphore throwFlagSema =new Semaphore(0);
      static Semaphore HorseFlagSema= new Semaphore(0);
      public void Error(String error) {
    	     JOptionPane.showMessageDialog(null, error);
      }
      public void sleep(int MiliSecond) {
    	  try {
  			Thread.sleep(MiliSecond);
  		} catch (InterruptedException ex) {
  			Thread.currentThread().interrupt();
  		}
      }
}
