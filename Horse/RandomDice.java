package Horse;
import java.util.Random;
public class RandomDice extends GameHorse {
      private int scores=0;
      private void ThrowDice() {
    	  Random random=new Random();
    	  scores=(random.nextInt(6)+1);
      }
      private int getScores() {
    	  return scores;
      }
      
}
