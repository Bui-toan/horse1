package Horse;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;
public class GameHorse {
      static final int RED=1;
      static final int YELLOW=2;
      static final int BLUE=3;
      static final int GREEN=4;
      static final int DISTANCES=50;
      
      static boolean ThrowPhaseFlag=true; //tung xúc sắc
      static boolean HorseFLAG=false;   // đi cá ngựa
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
