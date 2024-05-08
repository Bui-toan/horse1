package Horse;
import java.awt.event.*;
import javax.swing.*;
public class HorseSea extends GameHorse {
	/* Cá ngựa */
	private int color;
	private int id, rank = Des.NO_RANK;
	private int location;
	private JLabel label = new JLabel();
	static final int FINISH_POSITION = 56;

	HorseSea(int color, int id) {
		this.color = color;
		this.id = id;

		switch (color) {
		case BLUE:
			location = 0;
			break;
		case RED:
			location = 42;
			break;
		case YELLOW:
			location = 14;
			break;
		case GREEN:
			location = 28;
			break;
		default:
			System.err.println("Không có người chơi màu " + color);
		}
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getLocation() {
		return location;
	}

	public int getId() {
		return id;
	}

	public int getColor() {
		return color;
	}

	public void toFinish() {
		location = FINISH_POSITION;
	}

	public boolean move(GameMap map, int steps) {
		if (map.setMap(color, id, location, steps)) {
			if (location != FINISH_POSITION) {
				location = (location + steps) % GameMap.NUMBER_NODE;// để đảm bảo vị trí ngựa nằm trong bàn cờ
			}

			return true;
		}

		return false;
	}

	public boolean changeRank(Des destination, int steps) {
		if (destination.setDestination(rank, steps, this)) {
			return true;
		}

		return false;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setIcon(final Icon icon[]) {
		label.setIcon(icon[color]);
		
	}

	public void addMouseListener(GameMap map, int steps) {
		if (label != null) {
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (HorseFLAG) {
						if (location == FINISH_POSITION) {
							if(changeRank(map.getPlayer()[color].des, steps)){
								HorseFLAG = false;
								HorseFlagSema.release();
							}
						} else if (move(map, steps)) {
							HorseFLAG = false;
							HorseFlagSema.release();
						}
					}
				}
			});
		}
	}

	public void removeMouseListener() {
		if (label != null) {
			MouseListener list[] = label.getMouseListeners();
			for (int i = 0; i < list.length; i++) {
				label.removeMouseListener(list[i]);
			}
		}
	}
}
