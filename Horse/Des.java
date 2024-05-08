package Horse;
public class Des extends GameHorse {
	/* Đích */
	static final int NO_RANK = -1;
	static final int NUMBER_RANK = 6;
	static final int NO_HORSE = -1;
	private int MaxRank = NUMBER_RANK - 1;// max rank
	private int rank[] = new int[NUMBER_RANK]; 

	Des(int color) {
		for(int i = 0; i < NUMBER_RANK; i++){
			rank[i] = NO_HORSE;
		}
	}

	public boolean isWin() {
		return MaxRank == 1;
	}

	public int getRank(int rank){
		return this.rank[rank];
	}

	public boolean setDestination(int rank, int newRank, HorseSea horse) {
		newRank--;
		// bậc rank cao nhất là 6
		if (newRank > MaxRank) {
			Error("Không thể thăng hạng quân này.");
			return false;
		}
		
		if(rank == NO_RANK){
			for (int i = 0; i <= newRank; i++) {
				if (this.rank[i] != NO_HORSE) {
					Error("Không thể thăng hạng quân này.");
					return false;
				}
			}

			this.rank[newRank] = horse.getId();
			horse.setRank(newRank);
		} else {// điều kiện để thăng hạng
			if(newRank - rank == 1 && this.rank[newRank] == NO_HORSE){
				this.rank[newRank] = this.rank[rank];
				horse.setRank(newRank);
				this.rank[rank] =NO_HORSE;
			} else {
				Error("Không thể thăng hạng quân này.");
			}
		}

		if (this.rank[MaxRank] != NO_HORSE) {
			MaxRank--;
		}
		return true;
	}
}
