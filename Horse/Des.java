package Horse;

public class Des extends GameHorse {
      static final int No_Rank=-1;
      static final int Number_Rank=6;
      static final int No_Horse =-1;
      private int MaxRank =Number_Rank-1;// vị trí cao nhất vó ngựa trên bảng
      private int rank[]= new int[Number_Rank];// vị trí cá ngựa trên màn hình
      
      Des(int color) {
    	  for(int i=0;i<Number_Rank;i++) {
    		  rank[i]=No_Horse;
    	  }
      } 
      public int getRank(int rank) {
    	  return this.rank[rank];
      }
      public boolean setdes(int rank,int newRank,HorseSea horse) {
    	  newRank--;
    	  if(newRank > MaxRank) {
    		  Error("Không thể thăng hạng cho quân ngựa này!");
    		  return false;
    	  }
    	  if(rank==No_Rank) {
    		  for(int i=0;i<=newRank;i++) {
    			  if(this.rank[i]!= No_Horse) {
    				  Error("Không thể thăng hạng quân ngựa này!");
    				  return false;
    			  }
    		   }
    		  
    		   this.rank[newRank]=horse.getID();
    		   horse.setRank(newRank);
    		  
    	  }
    	  else {// điều kiện thăng hạng
    		  if(newRank - rank==1 && this.rank[newRank]==No_Horse) {
    			 this.rank[newRank] =this.rank[rank];
    			 horse.setRank(newRank);
    			 this.rank[rank]=No_Horse;
    		  }
    		  else {
    			  Error("Không thể thăng hạng quân ngựa này");
    		  }
    	  }
    	  if(this.rank[MaxRank]!=No_Horse) {
    		  MaxRank--;
    	  }
    	  return true;
      }
      public boolean isWin() {
    	  return MaxRank==1;
      }
     
}

