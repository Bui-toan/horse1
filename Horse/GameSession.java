package Horse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class GameSession extends GameHorse {
	private final int ONE_BONUS = -1;
	private final int NO_BONUS = 0;

	private int turn, turnBonus = 0;
	private GameMap map;
	private boolean endGameFlag;
	private GameGraphic graphic;
	private RandomDice dice;

	GameSession() {
		
		map = new GameMap();
		setTurn();
		dice = new RandomDice();
		graphic = new GameGraphic();
		graphic.drawMap(map);
		graphic.drawControl(dice);

		endGameFlag = false;
	}

	public void setTurn() {
		while (true) {
			 turn = Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập player đi trước (1/ 2/ 3/ 4): ", JOptionPane.INFORMATION_MESSAGE));		 
			if (turn >= 1 && turn <= 4 && turn <= map.getNumberPlayer()) {
				break;
			}
			else {
				Error("Bạn nhập sai thông tin. Xin mời nhập lại.");
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
			 int steps= dice.getScores();
			 steps = Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập steps: ", JOptionPane.INFORMATION_MESSAGE)); 
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
				try {
					saveFile(graphic.timeString,map.playerwin);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void saveFile(String time,int color) throws IOException {
		String content="\nLịch sử trò chơi\n";
		String file="E:\\baitapjava\\btl\\history.txt";
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			fw=new FileWriter(file,true);
			bw=new BufferedWriter(fw);
			bw.write(content);
			bw.write("Thời gian: "+time);
			bw.write("\nNgười chơi "+ color+" chiến thắng.\n");
			bw.write("-------------------------------------");
			
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
		finally {
			if(bw != null) 
			bw.close();
			if(fw != null)
			fw.close();
			}
	}

	public static void main(String args[]) {
		GameSession session = new GameSession();
		session.playGame();
	}

}
