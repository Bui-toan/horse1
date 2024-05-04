package Horse;

import javax.swing.JOptionPane;

public class GameMap extends GameHorse {
	private Player player[];
	private final int map[] = new int[NUMBER_NODE];

	static final int NUMBER_NODE = 56;
	private final int NO_HORSE = 0;
	/* Vị trí đích đến của các quân cờ */
	private final int DES[] = { -1, 55, 13, 27, 41};
	private final int START[] = { -1, 0, 14, 28, 42};
	private int numberPlayer;

	GameMap() {
		numberPlayer = numberplayer();
		player = new Player[numberPlayer + 1];

		for (int color = 1; color <= numberPlayer; color++) {
			player[color] = new Player(color);
		}
	}
    public int numberplayer() {
    	int n;
    	while(true) {
    		 n=Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập số người chơi (1/2/3/4) : ", JOptionPane.INFORMATION_MESSAGE)); 
        	if (n>=1 && n<=4) {
    		     break;
    		} else {
    			Error("Bạn nhập sai số player. Xin mời nhập lại.");
    		}
    	}
    	return n;
    }
	public int getNumberPlayer() {
		return numberPlayer;
	}

	public int getMap(int index) {
		return map[index];
	}

	public Player[] getPlayer() {
		return player;
	}

	public boolean xuatQuan(int color) {
		int idHorse = player[color].horsebarn.getHorse();
		
		// kiểm tra xem còn quân nào ở chuồng để xuất hay không
		if (idHorse == HorseBarn.No_Horse) {
			return false;
		}
        // nếu có quân ở chỗ xuất quân thì đá 
		if (setMap(color, idHorse, START[color], 0)) {
			player[color].horsebarn.remove(idHorse);
			player[color].addHorse(idHorse);
			return true;
		}

		return false;
	}

	public void addPlayerListener(int color, int steps) {
		player[color].addMouseListener(this, steps);
	}

	public void removePlayerListener(int color) {
		player[color].removeMouseListener();
	}

	public boolean setMap(int color, int idHorse, int start, int steps) {
		/* Quân cờ đang ở đích đến cuối cùng */
		if (start == DES[color]) {
			if (player[color].des.setdes(Des.No_Rank, steps, player[color].horse[idHorse])) {
				player[color].horse[idHorse].toFinish();
				map[start] = NO_HORSE;
				return true;
			} else {
				return false;
			}
		}

		if (start < DES[color] && start + steps > DES[color]) {
			Error("Quân này không thể di chuyển, quá đích đến.");
			return false;
		}

		/* Kiểm tra trên đường di chuyển có quân cờ nào cản không */
		for (int step = 1; step < steps; step++) {
			if (map[(start + step) % NUMBER_NODE] != NO_HORSE) {
				Error("Quân này không thể di chuyển, có quân cản mặt.");
				return false;
			}
		}
		/* Tính toán vị trí đến */
		int pos = (start + steps) % NUMBER_NODE;

		if (map[pos] == NO_HORSE) {
			/* Trường hợp có thể di chuyển */
			map[start] = NO_HORSE;
			map[pos] = color * 10 + idHorse;
		} else if (map[pos] / 10 == color) {
			/* Trường hợp vị trí đến là quân cùng màu */
			Error("Quân này không thể di chuyển, đích đến là quân cùng màu.");
			return false;
		} else if (map[pos]  / 10 < 5) {
			/* Trường hợp ăn quân */
			int victimPlayer = map[pos]  / 10;
			int victimHorse = map[pos] % 10;

			player[victimPlayer].RemoveHorse(victimHorse);
			player[victimPlayer].horsebarn.add(victimHorse);

			map[start] = NO_HORSE;
			map[pos] = color * 10 + idHorse;
		}

		return true;
	}
	
	public boolean isWin() {
		for (int i = 1; i <= numberPlayer; i++) {
			if(player[i].isWin()){
				Error("Player " + i + " is winner.");
				return true;
			}
		}

		return false;
	}
}

