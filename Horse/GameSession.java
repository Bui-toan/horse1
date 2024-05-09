package Horse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import SQL.Information;

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

	public void playGame() throws SQLException {
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
				try {
					saveFile(graphic.timeString,map.playerwin);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Information.History(map.playerwin,graphic.timeString);
			}
		}
	}
	 public void saveFile(String time, int color) throws IOException {
	        String content = "\nLịch sử trò chơi\n";
	        String filename = "history.txt";
	        clearFile(filename); // Gọi hàm clearFile để xóa hết dữ liệu trong file
	        FileWriter fw = null;
	        BufferedWriter bw = null;
	        try {
	            File file = new File(filename);
	            if (!file.exists()) {
	                file.createNewFile(); // Tạo file mới nếu không tồn tại
	            }
	            clearFile(filename);
	            fw = new FileWriter(file, true);
	            bw = new BufferedWriter(fw);
	            bw.write(content);
	            bw.write("Thời gian: " + time);
	            bw.write("\nNgười chơi " + color + " chiến thắng.\n");
	            bw.write("-------------------------------------");
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        } finally {
	            try {
	                if (bw != null)
	                    bw.close();
	                if (fw != null)
	                    fw.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	    // Phương thức clearFile để xóa hết dữ liệu trong file
	    public void clearFile(String fileName) {
	        try (PrintWriter writer = new PrintWriter(fileName)) {
	            // Ghi dữ liệu trống vào file
	            writer.print("");
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException(e);
	        }
	    }

	public static void main(String args[]) throws SQLException {
		GameSession session = new GameSession();
		session.playGame();
	}

}
