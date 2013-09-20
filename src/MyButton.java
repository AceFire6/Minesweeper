import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

/**
 * @author Jethro Muller
 * 
 */
@SuppressWarnings("serial")
public class MyButton extends JButton implements MouseListener {
	/**
	 * The dark brown colour used for the mouse over effect.
	 */
	private Color Dark_Brown = new Color(139, 69, 19).darker();
	/**
	 * Int that stores the number of creepers adjacent to this MyButton.
	 */
	private int creeperNumber = 0;
	/**
	 * Boolean that stores whether or not the current MyButton has a creeper.
	 */
	private boolean hasCreeper = false;
	/**
	 * Boolean that stores whether or not the current MyButton has a sign.
	 */
	private boolean hasSign = false;
	/**
	 * Boolean that stores whether or not the current MyButton has been checked already.
	 */
	private boolean checked = false;

	/**
	 * The creeper icon. Displayed when the user clicks on a MyButton that has a creeper.
	 */
	private Icon creeperIcon = new ImageIcon(getClass().getResource("/creeper.png"));
	/**
	 * The sign icon. Displayed when the user right-clicks on a MyButton that hasn't already been checked.
	 */
	private Icon signIcon = new ImageIcon(getClass().getResource("/sign.png"));
	/**
	 * The signOnMine icon. Displayed when the user loses on a MyButton that has a sign and a creeper.
	 */
	private Icon signOnMineIcon = new ImageIcon(getClass().getResource("/signOnCreeper.png"));
	/**
	 * The grass icon. It's the icon on all MyButtons in the grid that aren't checked.
	 */
	private Icon grass = new ImageIcon(getClass().getResource("/grass.png"));

	/**
	 * Default constructor.
	 */
	public MyButton() {
		//Sets up all the default settings
		super();
		setIcon(grass);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setSelected(false);
		setFocusable(false);
		this.addMouseListener(this);
	}

	/**
	 * Upon click a bomb. This method checks to see if a MyButton has a bomb. If
	 * it does, it changes the background colour and displays the bomb image.
	 */
	public void explode() {
		if (hasCreeper() && hasSign()) {
			setBackground(Color.ORANGE);
			setIcon(signOnMineIcon);
		} else if (hasCreeper()) {
			setBackground(Color.RED.darker());
			setIcon(creeperIcon);
		}
	}
	
	/**
	 * Returns hasCreeper.
	 * @return boolean hasCreeper
	 */
	public boolean hasCreeper() {
		return hasCreeper;
	}

	/**
	 * Returns hasSign.
	 * @return boolean hasSign
	 */
	public boolean hasSign() {
		return hasSign;
	}

	/**
	 * Sets hasCreeper to true.
	 */
	public void setHasCreeper() {
		hasCreeper = true;
	}

	/**
	 * Toggles the icon between the sign icon and the default grass icon.
	 * @param does boolean designating whether or not the button has a sign.
	 */
	public void setHasSign(boolean does) {
		hasSign = does;
		if (hasSign) {
			setIcon(signIcon);
		} else {
			setIcon(grass);
		}

	}

	/**
	 * Returns checked.
	 * @return boolean checked
	 */
	public boolean wasChecked() {
		return checked;
	}
	
	/**
	 * Sets the creeper number.
	 * @param num int that is the number of adjacent creepers.
	 */
	public void setCreeperNumber(int num) {
		creeperNumber = num;
	}
	
	/**
	 * Sets the creeper number.
	 * @return creeperNumber int that is the number of adjacent creepers.
	 */
	public int getCreeperNumber() {
		return creeperNumber;
	}

	/**
	 * Sets checked to true. Sets icon to null and the background colour to DARK_GRAY.
	 */
	public void setChecked() {
		checked = true;
		setIcon(null);
		setBackground(Color.DARK_GRAY);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//If the MyButton the mouse entered hasn't been checked
		if (!this.wasChecked()) {
			this.setBackground(Dark_Brown);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//If the MyButton the mouse exits hasn't been checked
		if (!this.wasChecked()) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					// this code will be executed after 0.1 seconds
					setBackground(new Color(130, 69, 19));
				}
			}, 100);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					// this code will be executed after 0.2 seconds
					setBackground(Color.DARK_GRAY);
				}
			}, 200);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
