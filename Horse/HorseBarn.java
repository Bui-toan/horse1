package Horse;
public class HorseBarn extends GameHorse{
	/* Chuồng */
	private int number = 0;
	static final int NO_HORSE = -1;
	private boolean id[] = new boolean[Player.Horse]; 

	HorseBarn(int color){
		number = Player.Horse;
		for(int i = 0; i < Player.Horse; i++){
			id[i] = true;
		}
	}

	public void add(int id){
		number++;
		this.id[id] = true;
	}

	public int getHorse(){
		if(isEmpty()){
			return NO_HORSE;
		}

		for(int i = 0; i < Player.Horse; i++){
			if(id[i] == true){
				return i;
			}
		}
		
		return NO_HORSE;
	}

	public void remove(int id){
		number--;	
		this.id[id] = false;
	}

	public boolean isEmpty(){
		return number == 0;
	}
}
