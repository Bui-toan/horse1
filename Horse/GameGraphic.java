package Horse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameGraphic extends GameHorse{

	static final int W_FRAME=1000;
	static final int H_FRAME=800;
	
	static final int x0_position = 290;
	static final int y0_position = 10;

	/* Các tọa độ cơ sở của chuồng, đích đến */
	static final Coordinates baseStableCoor[] = {null};
	static final Coordinates baseDestinationCoor[] = {null, new Coordinates (x0_position+DISTANCES , y0_position + DISTANCES),
	                                                 new Coordinates (x0_position - 5 * DISTANCES, y0_position + 7 * DISTANCES),
	                                                 new Coordinates (x0_position + DISTANCES, y0_position + 13 * DISTANCES),
	                                                 new Coordinates (x0_position + 7 * DISTANCES, y0_position + 7 * DISTANCES)
	                                                };
	private JPanel mapPanel;
	private JPanel controlPanel;
	private JFrame mainFrame;
	private JButton xuatQuanButton, dropButton;

	private Icon iconDie[], iconHorse[];
	private Image imMap, imControl;
	private JLabel labelDie, turnLabel;

	void prepareDie() {
		final int numberSide = 7;
		iconDie = new ImageIcon[numberSide];
		for (int i = 0; i < numberSide; i++) {
			iconDie[i] = new ImageIcon(getClass().getResource("D" + i + ".JPG"));
		}
	}

	void prepareHorse() {
		iconHorse = new ImageIcon[Player.Horse + 1];
		for (int i = 1; i <= Player.Horse; i++) {
			iconHorse[i] = new ImageIcon(getClass().getResource("H" + i + ".GIF"));
		}
	}

	void prepareMap() {
		imMap = new ImageIcon(getClass().getResource("co_ca_ngua.png")).getImage();
	}

	void prepareControl() {
		imControl = new ImageIcon(getClass().getResource("h3ttnt.jpg")).getImage();
	}

	GameGraphic() {
		mainFrame = new JFrame();
		mainFrame.setSize(W_FRAME, H_FRAME);
//		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		mainFrame.setUndecorated(true);
		mainFrame.setTitle("Cờ Cá Ngựa");
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setLayout(new BorderLayout());

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		prepareMap();
		prepareControl();
		prepareHorse();
		prepareDie();
	}

	private final int point[] = {0, 6, 12, 14, 20, 26, 28, 34, 40, 42, 48, 54, 56};// Vị trí các điểm mốc trên bàn cờ
	private final int sign[] = { 1, 1, -1, 1, 1, 1, 1, -1, 1, -1, -1, -1, -1};// Dấu trừ thể hiện đi ngược chiều trục tọa độ

	/* Ánh xạ từ độ trong map ra tọa độ trên màn hình */
	public Coordinates getCoordinate(HorseSea horse) {
		Coordinates coor = new Coordinates(x0_position, y0_position);
		int location = horse.getLocation();

		/* Tính toán tọa độ khi quân cờ đang ở vị trí đích đến cuối cùng */
		if ( location== HorseSea.Finish_Location) {
			int color = horse.getColor();
			coor.x = baseDestinationCoor[color].x;
			coor.y = baseDestinationCoor[color].y;

			if (color == BLUE) {
				coor.y += DISTANCES * horse.getRank();
			} else if (color == YELLOW) {
				coor.x += DISTANCES * horse.getRank();
			} else if (color == GREEN) {
				coor.y -= DISTANCES * horse.getRank();
			} else if (color == RED) {
				coor.x -= DISTANCES * horse.getRank();
			}

			return coor;
		}

		/* Tính toán tọa độ của quân cờ dựa vào tọa độ cơ sở x0, y0 */
		for (int i = 1; i < point.length; i++) {
			boolean oddFlag = (i % 2 != 0);

			if (location < point[i]) {
				if (oddFlag) {
					coor.y += sign[i] * DISTANCES * (location - point[i - 1]);
				} else {
					coor.x += sign[i] * DISTANCES * (location - point[i - 1]);
				}
				return coor;
			}

			if (oddFlag) {
				coor.y += sign[i] * DISTANCES * (point[i] - point[i - 1]);
			} else {
				coor.x += sign[i] * DISTANCES * (point[i] - point[i - 1]);
			}
		}
		return coor;
	}

	public void drawHorse(HorseSea horse) {
		Coordinates coor = getCoordinate(horse);// Vị trí cá ngựa trên màn hình.

		horse.setIcon(iconHorse);
		horse.getLabel().setBounds(coor.x, coor.y, 30, 30);
		mapPanel.add(horse.getLabel());
		mainFrame.setVisible(true);
	}

	public void drawDie() {
		labelDie = new JLabel(iconDie[0]);
		controlPanel.add(labelDie);
		mainFrame.setVisible(true);
	}

	public void drawThrowButton(RandomDice dice) {
		class AnimationDie implements Runnable {
			Thread thread = null;

			AnimationDie() {
				thread = new Thread(this);
				thread.start();
			}

			public void run() {
				/* Tạo hiệu ứng tung xúc xắc */
				for (int i = 1; i < 15; i++) {
					dice.ThrowDice();
					labelDie.setIcon(iconDie[dice.getScores()]);
					sleep(100);
				}

				mainFrame.setVisible(true);
				throwFlagSema.release();
			}
		}

		JButton throwButton = new JButton("Đổ");

		throwButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ThrowPhaseFlag) {
					ThrowPhaseFlag= false;
					new AnimationDie();
				}
			}
		});

		controlPanel.add(throwButton);
		mainFrame.setVisible(true);
	}

	public void drawTime() {
		class DigitalWatch implements Runnable {
			Thread thread = null;
			Date startTime = null;
			String timeString = "";
			JButton button;

			DigitalWatch() {
				thread = new Thread(this);
				button = new JButton();
				controlPanel.add(button);
				thread.start();
				mainFrame.setVisible(true);
			}

			public void reset() {
				Calendar cal = Calendar.getInstance();
				startTime = cal.getTime();
			}

			public void run() {
				reset();

				while (true) {
					Calendar cal = Calendar.getInstance();
					Date currentTime = cal.getTime();
					long time = (currentTime.getTime() - startTime.getTime()) / 1000;
					long second = time % 60, hour = (time / 3600), minute = (time / 60) % 60;
					String s = Long.toString(second), h = Long.toString(hour), m = Long.toString(minute);

					if(second < 10){
						s = "0" + s;
					}
					if(hour < 10){
						h = "0" + h;
					}
					if(minute < 10){
						m = "0" + m;
					}

					timeString = h + ":" + m + ":" + s;

					printTime();					
					sleep(1000);
				}
			}

			public void printTime() {
				button.setText(timeString);
			}
		}

		new DigitalWatch();
	}

	public void drawStable(HorseBarn stable) {

	}

	public void drawDropButton() {
		dropButton = new JButton("Bỏ lượt");

		dropButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (HorseFLAG) {
					HorseFLAG = false;
					HorseFlagSema.release();
				}
			}
		});

		controlPanel.add(dropButton);
		mainFrame.setVisible(true);
	}

	public void drawTurnLabel(int color) {
		turnLabel.setIcon(iconHorse[color]);
		mainFrame.setVisible(true);
	}

	public void drawXuatQuanButton(GameMap map, int color) {
		xuatQuanButton = new JButton("Xuất quân");
		xuatQuanButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (HorseFLAG) {
					if (map.xuatQuan(color)) {
						HorseFLAG = false;
						HorseFlagSema.release();
					}
				}
			}
		});
		controlPanel.add(xuatQuanButton);
		mainFrame.setVisible(true);
	}

	public void removeXuatQuanButton() {
		if (xuatQuanButton != null) {
			controlPanel.remove(xuatQuanButton);
			xuatQuanButton = null;
		}
	}

	public void drawTurnLabel() {
		turnLabel = new JLabel("");
		turnLabel.setOpaque(true);
		controlPanel.add(turnLabel);
	}

	@SuppressWarnings("serial")
	public void drawControl(RandomDice die) {
		controlPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(imControl, 0, 100, W_FRAME - (W_FRAME - 215), H_FRAME - 15 - 100 + 35 - 8, this); //ve nen
			}
		};
		mainFrame.add(controlPanel);

		drawTurnLabel();
		drawDie();
		drawThrowButton(die);
		drawDropButton();
		drawTime();

		mainFrame.setVisible(true);
	}

	public void drawMap(GameMap map) {
		mapPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(imMap, 0, 0, W_FRAME - 215, H_FRAME - 15, this); //ve nen
			}
		};
		mapPanel.setPreferredSize(new Dimension(W_FRAME - 215, H_FRAME - 15));
		mapPanel.setLayout(null);

		int num = map.getNumberPlayer();
		for (int i = 1; i <= num; i++) {
			for (int j = 0; j < Player.Horse; j++) {
				if (map.getPlayer()[i].horse[j] != null) {
					drawHorse(map.getPlayer()[i].horse[j]);
				}
			}
		}

		mainFrame.add(mapPanel, BorderLayout.WEST);
		mainFrame.setVisible(true);
	}
}
