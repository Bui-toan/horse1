package Horse;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameGraphic extends GameHorse{

	static final int W_FRAME=1000;
	static final int H_FRAME=800;
	
	static final int x0_position = 335	;
	static final int y0_position = 20;

	/* Các tọa độ cơ sở của chuồng, đích đến */
	static final Coordinates baseStableCoor[] = {null};
	static final Coordinates baseDestinationCoor[] = {null, new Coordinates (x0_position + DISTANCES , y0_position + DISTANCES),
	                                                 new Coordinates (x0_position - 5 * DISTANCES, y0_position + 7 * DISTANCES),
	                                                 new Coordinates (x0_position + DISTANCES, y0_position + 13 * DISTANCES),
	                                                 new Coordinates (x0_position + 7 * DISTANCES, y0_position + 7 * DISTANCES)
	                                                };
	private JPanel mapPanel;
	private JPanel controlPanel,control;
	private JFrame mainFrame;
	private JButton xuatQuanButton, dropButton;

	private Icon iconDie[], iconHorse[];
	private Image imMap;
	private JLabel labelDie, turnLabel;
	
	GameMap map;
	String time1;

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

	GameGraphic() {
		mainFrame = new JFrame();
		mainFrame.setSize(W_FRAME, H_FRAME);
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
		control.add(labelDie);
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
				for (int i = 1; i < 10; i++) {
					dice.ThrowDice();
					labelDie.setIcon(iconDie[dice.getScores()]);
					sleep(100);
				}

				mainFrame.setVisible(true);
				throwFlagSema.release();
			}
		}

		JButton throwButton = new JButton("Đổ");
		throwButton.setBackground(Color.gray);
		Font font= new Font("Arial",Font.BOLD,30);
		throwButton.setFont(font);

		throwButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ThrowPhaseFlag) {
					ThrowPhaseFlag= false;
					new AnimationDie();
				}
			}
		});

		control.add(throwButton);
		mainFrame.setVisible(true);
	}
	String timeString = "";
	public void drawTime() {
		class DigitalWatch implements Runnable {
			Thread thread = null;
			Date startTime = null;
			
			JButton button;
			

			DigitalWatch() {
				thread = new Thread(this);
				button = new JButton();
				control.add(button);
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

		control.add(dropButton);
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
		control.add(xuatQuanButton);
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
		control.add(turnLabel);
	}


	public void drawname() {
		JPanel jp2=new JPanel();
        JTextArea name= new JTextArea();
        name.setText("  Chúc các bạn \n     chơi game \n         vui vẻ!");
        name.setEditable(false);
    	name.setLineWrap(true);
    	name.setWrapStyleWord(true);
    	Font font=new Font("Arrial",Font.BOLD,24);
    	name.setFont(font);
    	name.setForeground(Color.RED);
    	jp2.add(name);
    	jp2.setPreferredSize(new Dimension(215,100));
    	jp2.setLayout(new GridLayout());
    	controlPanel.add(jp2,BorderLayout.SOUTH);
    	mainFrame.setVisible(true);
    	
 
    }
	GameSession ses;
    public void docfile() throws IOException {
		StringBuilder st=new StringBuilder();
		try {
			BufferedReader br=new BufferedReader(new FileReader("E:\\baitapjava\\btl\\history.txt"));
			String line;
			while((line=br.readLine())!=null) {
				st.append(line).append("\n");
			}
			br.close();
		}catch(IOException e) {
			throw new RuntimeException(e);
				}
		JOptionPane.showMessageDialog(null, st,"Lịch sử trò chơi",JOptionPane.INFORMATION_MESSAGE);
	}
    public void drawhistory(){
    	JPanel control2=new JPanel();
    	JButton history=new JButton("Lịch sử trò chơi");
    	JButton newgame=new JButton("Game mới");
    	JButton rule=new JButton("Luật chơi");
    	JButton exitgame=new JButton("Thoát game");
    	
    	control2.setLayout(new GridBagLayout());
    	GridBagConstraints c =new GridBagConstraints();
    	c.insets=new Insets(15, 15, 15, 15);
    	c.gridx=0;
    	c.gridy=0;
    	control2.add(history,c);
    	c.gridy=1;
    	control2.add(rule,c);
    	c.gridy=2;
    	control2.add(newgame,c);
    	c.gridy=3;
    	control2.add(exitgame,c);
    	
    	
    	
    	Font font=new Font("Arrial",Font.BOLD,16);
    	history.setFont(font);
    	rule.setFont(font);
    	exitgame.setFont(font);
    	newgame.setFont(font);
    	
    	history.setBackground(Color.LIGHT_GRAY);
    	rule.setBackground(Color.LIGHT_GRAY);
    	newgame.setBackground(Color.LIGHT_GRAY);
    	exitgame.setBackground(Color.LIGHT_GRAY);
    	
    	history.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
				try {
					docfile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
    		
    		
		});
    	rule.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
				showrule();
			}
    		
    		
		});
    	newgame.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
				newGame();
			}
    		
    		
		});
    	exitgame.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
				exitGame();
			}
	
		});
    	
    	controlPanel.add(control2);
    	mainFrame.setVisible(true);
    	
    }
    
    public void showrule() {	
            JPanel instructionPanel = new JPanel() ;
            instructionPanel.setLayout(new BorderLayout());           
            JTextArea instructionText = new JTextArea(30,30);
               instructionText.setEditable(false);
               instructionText.setLineWrap(true);
               instructionText.setWrapStyleWord(true);
               instructionText.setText(" + Cờ cá ngựa là một trò chơi có nguồn gốc từ Ấn Độ, được biết đến dưới tên Pachisi. Sau đó, trò chơi này được mang đến Mỹ và được biến thể thành Parcheesi. Ở Việt Nam, trò chơi này được gọi là Cờ cá ngựa.\r\n"
               		+ "\r\n"
               		+ "+ Trò chơi gồm 4 màu đại diện cho 4 người chơi: xanh biển, đỏ, xanh lá, và vàng. Mỗi người chơi có 4 quân cờ và một xúc sắc có 6 mặt từ 1 đến 6.\r\n"
               		+ "\r\n"
               		+ "+ Điều kiện thắng là người chơi phải đưa tất cả 4 quân cờ của mình về chuồng theo thứ tự 6, 5, 4, 3 để trở thành người chiến thắng.\r\n"
               		+ "\r\n"
               		+ "+ Luật chơi bao gồm việc tung xúc sắc đến khi ra mặt “6” mới được xuất quân và di chuyển quân. Bạn có thể chọn giữa việc xuất quân ra chuồng hoặc đi một con khác nếu có. Nếu không xuất quân được, bạn có thể bỏ lượt.\r\n"
               		+ "\r\n"
               		+ "+ Khi di chuyển, nếu điểm đến có quân cờ của đối thủ, bạn có thể đá quân đó về chuồng của đối thủ. Nếu điểm đến có quân của bạn, bạn không thể đi tiếp.\r\n"
               		+ "\r\n"
               		+ "+ Mẹo chơi gồm cố gắng xuất quân nhiều nhất có thể và di chuyển khéo léo đến đích. Đồng thời, phá quân đối thủ bằng cách đá quân của họ về chuồng của họ để đảm bảo chiến thắng.");

        JScrollPane scrollPane = new JScrollPane(instructionText);
        instructionPanel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, instructionPanel, "Hướng dẫn luật chơi cờ cá ngựa", JOptionPane.INFORMATION_MESSAGE);
    }
    public void newGame() {
    	 int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn chơi game mới  không?", "Trò chơi mới", JOptionPane.YES_NO_OPTION);
         if (option == JOptionPane.YES_OPTION) {
        	 	if(mainFrame!=null) {
        	 		mainFrame.dispose(); 		
        	 	}
        	 	ses=new GameSession();
     
         }
    }
    public void exitGame() {
        int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn thoát khỏi trò chơi không?", "Thoát trò chơi", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0); 
        }
    }
	public void drawControl(RandomDice dice) {
		controlPanel=new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setPreferredSize(new Dimension(200,H_FRAME));
		control=new JPanel();
		control.setLayout(new FlowLayout());
		control.setPreferredSize(new Dimension(200,120));
		drawTurnLabel();
		drawDie();
		drawThrowButton(dice);
		drawDropButton();
		drawTime();
		
		
		controlPanel.add(control,BorderLayout.NORTH);
		drawhistory();
		drawname();
		
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}

	@SuppressWarnings("serial")
	public void drawMap(GameMap map) {
		mapPanel = new JPanel()
		{
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(imMap, 0, 0, W_FRAME - 200, H_FRAME - 15, this); //ve nen
			}
		};

		mapPanel.setPreferredSize(new Dimension(W_FRAME - 200, H_FRAME - 15));
	   
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
