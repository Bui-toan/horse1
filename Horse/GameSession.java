package Horse;

import javax.swing.JOptionPane;

public class GameSession extends GameHorse {
	private final int ONE_BONUS = -1;
	private final int NO_BONUS = 0;

	private int turn, turnBonus = 0;
	private GameMap map;
	private boolean endGameFlag;
	private GameGraphic graphic;
	private RandomDice die;

	GameSession() {
		setTurn();
		map = new GameMap();
		die = new RandomDice();
		graphic = new GameGraphic();
		graphic.drawMap(map);
		graphic.drawControl(die);

		endGameFlag = false;
	}

	public void setTurn() {
		while (true) {
			String strTurn = JOptionPane.showInputDialog(null, "Nhập player đi trước (1/ 2/ 3/ 4): ", JOptionPane.INFORMATION_MESSAGE);

			if (strTurn.matches("[1234]")) {
				turn = Integer.parseInt(strTurn);
				break;
			} else {
				Error("Bạn nhập sai player. Xin mời nhập lại.");
			}
		}
	}

	public void playGame() {
		while (!endGameFlag) {
			int color = turn;
			turnBonus = NO_BONUS;
			graphic.drawTurnLabel(color);

			try {
				throwFlagSema.acquire();
			} catch (InterruptedException exc) {
				System.out.println(exc);
			}
			HorseFLAG = true;
			int steps = die.getScores();
			 //steps = Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập steps: ", JOptionPane.INFORMATION_MESSAGE)); 
			if(steps == 6){
				turnBonus = ONE_BONUS;
				graphic.drawXuatQuanButton(map, color);
			}

			map.addPlayerListener(color, steps);
			try {
				HorseFlagSema.acquire();
			} catch (InterruptedException exc) {
				System.out.println(exc);
			}
			
			map.removePlayerListener(color);
			graphic.removeXuatQuanButton();
			HorseFLAG = false;
			graphic.drawMap(map);
			
			ThrowPhaseFlag = true;
			turn = (turn + turnBonus) % map.getNumberPlayer() + 1;
			
			if(map.isWin()){
				endGameFlag = true;
			}
		}
	}

	public static void main(String args[]) {
		GameSession session = new GameSession();
		session.playGame();
	}

}
