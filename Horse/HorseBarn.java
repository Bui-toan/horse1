package Horse;

public class HorseBarn extends GameHorse {
      private int NumberHorse=0; //so ngựa trong chuồng
      static final int No_Horse=-1;
      private boolean id[]=new boolean [Player.Horse]; //vị trí cá ngựa trong chuồng
      
      HorseBarn(int color) {
    	  NumberHorse=Player.Horse;
    	  for(int i=0;i<Player.Horse;i++) {
    		  id[i]=true;
    	  }
      }
      public void add(int id) { //thêm ngựa vào chuồng
    	  NumberHorse++;
    	  this.id[id]=true;
      }
      public int getHorse() { //lấy thông tin nguựa trong chuồng
    	  if(isEmpty()) {
    		  return No_Horse;
    	  }
    	  
    		  for(int i=0;i<Player.Horse;i++) {
    			  if(id[i]=true) {
    				  return i;
    			  }
    		  }
    	  
    	  return No_Horse;
      }
      public void remove(int id) {// xóa ngựa khỏi chuồng
    	  NumberHorse--;
    	  this.id[id]=false;
      }
      public boolean isEmpty() {
    	  return NumberHorse==0;
      }
}
