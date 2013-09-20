import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Jethro Muller
 * 
 */
@SuppressWarnings("serial")
public class MinesweeperGUI extends JFrame implements ActionListener,
		MouseListener {
	
	/**
	 * The width of the grid in terms of number of MyButtons.
	 */
	private int gridWidth;
	/**
	 * The height of the grid in terms of number of MyButtons.
	 */
	private int gridHeight;
	/**
	 * The number of creepers to be created and put into the MyButton grid.
	 */
	private int numCreepers;
	/**
	 * The number of signs the player has available to them.
	 */
	private int numSigns;
	/**
	 * The number of creepers left without signs marking their positions.
	 */
	private int creepersLeft;
	/**
	 * The total number of creepers.
	 */
	private int totalCreepers;
	
	/**
	 * The main background of the whole application.
	 */
	private JLabel background = new JLabel(new ImageIcon(getClass().getResource("/bg.jpg")));
	
	/**
	 * The main heading image.
	 */
	private Icon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
	/**
	 * The image that contains the text "Signs Left - ".
	 */
	private Icon signsLeft = new ImageIcon(getClass().getResource("/signsLeft.png"));

	/**
	 * The first digit of the signs count numbers.
	 */
	private JLabel signsCount1 = new JLabel();
	/**
	 * The second digit of the signs count numbers.
	 */
	private JLabel signsCount2 = new JLabel();
	
	/**
	 * The main JFrames dimensions.
	 */
	private Dimension windowDimensions = new Dimension(600, 600);
	/**
	 * The main MyButton 2D array.
	 */
	private MyButton[][] creeperGrid;
	/**
	 * Boolean to determine whether or not the user is clicking a MyButton for the first time.
	 */
	private boolean firstClick = true;
	/**
	 * The height and width of the buttonHolder JPanel.
	 */
	private int holderSize = 350;
	/**
	 * The height of the JPanel that holds the buttonHolder JPanel.
	 */
	private int containerSize = 400;
	
	/**
	 * The elapsed time in seconds.
	 */
	private int timeTracker = 0;
	/**
	 * The time array. time[0] is hours, time[1] is minutes and time[3] is seconds.
	 */
	private int[] time = {0,0,0};
	
	/**
	 * Boolean used to determine if the right mouse button is currently being pressed.
	 */
	private boolean rmbPressed=false;
	/**
	 * Boolean used to determine if the left mouse button is currently being pressed.
	 */
	private boolean lmbPressed=false;
	
	/**
	 * Tells the startTiming() method whether or not to update the time.
	 */
	private boolean keepTime=true;
	
	/**
	 * The JLabel containing an image with the word "Time - ".
	 */
	private JLabel timeLabel = new JLabel(new ImageIcon(getClass().getResource("/time.png")));
	/**
	 * The first digit of the minutes for the timer.
	 */
	private JLabel minutesFirst = new JLabel(new ImageIcon(getClass().getResource("/0.png")));
	/**
	 * The second digit of the minutes for the timer.
	 */
	private JLabel minutesSecond = new JLabel(new ImageIcon(getClass().getResource("/0.png")));
	/**
	 * The JLabel containing an image with the word "Min".
	 */
	private JLabel m = new JLabel(new ImageIcon(getClass().getResource("/m.png")));
	/**
	 * The first digit of the seconds for the timer.
	 */
	private JLabel secondsFirst = new JLabel(new ImageIcon(getClass().getResource("/0.png")));
	/**
	 * The second digit of the seconds for the timer.
	 */
	private JLabel secondsSecond = new JLabel(new ImageIcon(getClass().getResource("/0.png")));
	/**
	 * The JLabel containing an image with the word "Sec".
	 */
	private JLabel s = new JLabel(new ImageIcon(getClass().getResource("/s.png")));
	
	/**
	 * Parameterized constructor. Takes in three parameters.
	 * @param width int that defines the width.
	 * @param height int that defines the height.
	 * @param creepers int that defines the number of creepers to generate.
	 */
	public MinesweeperGUI(int width, int height, int creepers) {
		//sets up all the vital variables for initialization
		gridWidth = width;
		gridHeight = height;
		totalCreepers = creepers;
		numCreepers = totalCreepers;
		creepersLeft = numCreepers;
		numSigns = numCreepers;

		//sets the Frame size based on the difficulty settings
		setJFrameSize();
		
		//sets up the JFrame and general important settings
		createJFrame();
		
		JLabel logo = new JLabel(logoIcon);
		
		//Adds the heading to background
		background.add(logo);
		
		//Builds the MyButton grid
		buildGrid();
		
		//Initialises the signs left icons
		JLabel signLabel = new JLabel(signsLeft);
		setSignsIcons();
		
		//Adding all timer related components to the background
		startTiming();
		background.add(timeLabel);
		background.add(minutesFirst);
		background.add(minutesSecond);
		background.add(m);
		background.add(secondsFirst);
		background.add(secondsSecond);
		background.add(s);
		
		//Adding all sign count related components to the background
		background.add(signLabel);
		background.add(signsCount1);
		background.add(signsCount2);
	}

	/**
	 * Starts the timer process. It creates a scheduled task that runs every second. It updates the recorded time and the time displayed and then creates a new timed event.
	 */
	private void startTiming() {
		if (keepTime) {
			new Timer().schedule(new TimerTask() {
				@Override
				// this code will be executed after 1 second
				public void run() {
					if (keepTime) {
						int currentTimeSecs = timeTracker;
						timeTracker = currentTimeSecs+1;
						time = handleTime(Long.parseLong(String.valueOf(timeTracker)));
						updateTime(time);
						startTiming();
					}
				}
			}, 1000);
		}
	}
	
	/**
	 * Updates the time being displayed.
	 * @param time int [] of length 3 that has hours as its first element, minutes as its second and seconds as its third.
	 */
	private void updateTime(int [] time) {
		char [] min = String.valueOf(time[1]).toCharArray();
		char [] sec = String.valueOf(time[2]).toCharArray();
		
		if (min.length>1) {
			minutesFirst.setIcon(new ImageIcon(getClass().getResource("/"+min[0]+".png")));
			minutesSecond.setIcon(new ImageIcon(getClass().getResource("/"+min[1]+".png")));
		} else if (min.length==1){
			minutesFirst.setIcon(new ImageIcon(getClass().getResource("/0.png")));
			minutesSecond.setIcon(new ImageIcon(getClass().getResource("/"+min[0]+".png")));
		}
		
		if (sec.length>1) {
			secondsFirst.setIcon(new ImageIcon(getClass().getResource("/"+sec[0]+".png")));
			secondsSecond.setIcon(new ImageIcon(getClass().getResource("/"+sec[1]+".png")));
		} else if (sec.length==1){
			secondsFirst.setIcon(new ImageIcon(getClass().getResource("/0.png")));
			secondsSecond.setIcon(new ImageIcon(getClass().getResource("/"+sec[0]+".png")));
		}
	}
	
	/**
	 * Sets the size of the JFrame and various container Frames based on the
	 * number of mines.
	 */
	private void setJFrameSize() {
		//If any difficulty above easy is picked it changed the size of the JFrame.
		if (totalCreepers == 40 || totalCreepers == 80) {
			windowDimensions = new Dimension(700, 700);
			holderSize = 500;
			containerSize = 530;
		}
	}

	/**
	 * Initializes the main JFrame and changes all necessary settings.
	 */
	private void createJFrame() {
		setTitle("Minesweeper");
		setIconImage(new ImageIcon(getClass().getResource("/creeper.png")).getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(windowDimensions);
		pack();
		setLayout(new BorderLayout());
		background.setLayout(new FlowLayout(1,2,0));
		
		//Main menu panel that holds all the top menu items
		JPanel mainMenu = new JPanel();
		mainMenu.setLayout(new GridLayout(0,3));
		mainMenu.setPreferredSize(new Dimension(getWidth(), 30));
		
		//New Game button
		JButton newGame = new JButton();
		newGame.setIcon(new ImageIcon(getClass().getResource("/newgame.png")));
		newGame.setPreferredSize(new Dimension(1, mainMenu.getHeight()));
		newGame.setBackground(Color.DARK_GRAY);
		newGame.setForeground(Color.WHITE);
		newGame.setFocusable(false);
		newGame.setActionCommand("newGame");
		newGame.addActionListener(this);
		mainMenu.add(newGame);
	
		//Help Button
		JButton help = new JButton();
		help.setIcon(new ImageIcon(getClass().getResource("/help.png")));
		help.setPreferredSize(new Dimension(1, mainMenu.getHeight()));
		help.setBackground(Color.DARK_GRAY);
		help.setForeground(Color.WHITE);
		help.setFocusable(false);
		help.setActionCommand("help");
		help.addActionListener(this);
		mainMenu.add(help);
		
		//Quit button
		JButton quit = new JButton();
		quit.setIcon(new ImageIcon(getClass().getResource("/quit.png")));
		quit.setPreferredSize(new Dimension(1, mainMenu.getHeight()));
		quit.setBackground(Color.DARK_GRAY);
		quit.setForeground(Color.WHITE);
		quit.setFocusable(false);
		quit.setActionCommand("quit");
		quit.addActionListener(this);
		mainMenu.add(quit);
		
		add(background);
		background.add(mainMenu);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	/**
	 * Updates the counter that indicates the number of signs left.
	 */
	private void setSignsIcons() {
		String firstDigit = String.valueOf(numSigns).substring(0, 1);
		String secondDigit;
		if (String.valueOf(numSigns).length()>1) {
			secondDigit = String.valueOf(numSigns).substring(1);
		} else {
			secondDigit=null;
		}
		
		signsCount1.setIcon(new ImageIcon(getClass().getResource("/"+firstDigit + ".png")));
		
		if (secondDigit!=null) {
			signsCount2.setIcon(new ImageIcon(getClass().getResource("/"+secondDigit + ".png")));
		} else {
			signsCount2.setIcon(null);
		}
	}

	/**
	 * Builds the grid used for playing.
	 */
	private void buildGrid() {
		//The grid of all the MyButtons used for playing the game.
		creeperGrid = new MyButton[gridHeight][gridWidth];
		//Number of buttons
		int buttonCount = 0;
		
		//The container that holds the button container
		JPanel containerHolder = new JPanel();
		containerHolder.setOpaque(false);
		containerHolder.setPreferredSize(new Dimension(this.getWidth(),
				containerSize));
		background.add(containerHolder);
		
		//The container for the button grid
		JPanel buttonHolder = new JPanel();
		buttonHolder.setOpaque(false);
		buttonHolder.setLayout(new GridLayout(gridHeight, gridWidth));
		buttonHolder.setPreferredSize(new Dimension(holderSize, holderSize));
		containerHolder.add(buttonHolder);

		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				creeperGrid[y][x] = new MyButton();
				buttonHolder.add(creeperGrid[y][x]);
				creeperGrid[y][x].addActionListener(this);
				creeperGrid[y][x].addMouseListener(this);
				creeperGrid[y][x].setName(String.valueOf(buttonCount));
				creeperGrid[y][x].setActionCommand(x + "|" + y);
				buttonCount++;
			}
		}
	}

	/**
	 * Generates mines randomly, while avoiding the first clicked MyButtons
	 * position.
	 * 
	 * @param clicked
	 *            int of the position of the first click. The generation process avoids using this MyButton to hold a creeper.
	 */
	private void generateCreepers(int clicked) {
		// System.out.println("Clicked: "+clicked);
		Random randInt = new Random();
		int[] creeperField = new int[gridWidth * gridHeight];
		int currentRand = 0;

		while (numCreepers > 0) {
			currentRand = randInt.nextInt((gridWidth * gridHeight));

			if (creeperField[currentRand] != 1 && (currentRand + 1) != clicked) {
				creeperField[currentRand] = 1;
				numCreepers--;
				// System.out.println("Mines still to be Allocated: "+NUM_MINES+"\tPosition of Newest Mine: "+currentRand);
			}
		}

		assignMines(creeperField);
	}

	/**
	 * Takes the creeperField array of creeper positions and assigns the creepers to the
	 * 2D MyButton grid.
	 * 
	 * @param creeperField
	 *            int array that holds the positions of all the creepers.
	 */
	private void assignMines(int[] creeperField) {
		int yTracker = 0;
		int xTracker = 0;

		int minePositions[] = creeperField.clone();
		int leng = minePositions.length;

		for (int i = 0; i < leng; i++) {
			xTracker = (xTracker < gridWidth) ? (xTracker + 1) : xTracker;
			yTracker = ((xTracker == gridHeight) && (yTracker < (gridWidth - 1))) ? (yTracker + 1)
					: yTracker;
			xTracker = (xTracker == gridWidth) ? 0 : xTracker;

			if (minePositions[i] == 1) {
				// System.out.println(xTracker+" "+yTracker);
				creeperGrid[yTracker][xTracker].setHasCreeper();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		//If the user clicks the new game button
		if (ae.getActionCommand().equals("newGame")) {
			keepTime=false;
			reset(3);
		}
		//If the user clicks the quit button
		if (ae.getActionCommand().equals("quit")) {
			keepTime=false;
			handleQuit();
		}
		
		//If the user clicks the help button
		if (ae.getActionCommand().equals("help")) {
			keepTime=false;
			help();
		}
		
		/*
		 * If the user clicks on the game grid for the first time.
		 * Generates the creepers.
		 */
		
		if (ae.getSource() instanceof MyButton && firstClick) {
			// System.out.println(((Component) ae.getSource()).getName());
			generateCreepers(Integer.parseInt(((Component) ae.getSource())
					.getName()));
			firstClick = false;
		}
	}

	/**
	 * Generates the help box.
	 */
	private void help() {
		JOptionPane.showMessageDialog(this, "Welcome to Minesweeper!\n\nIn this game your goal is to put a sign over each block you think has a creeper spawn beneath it.\nYou do this by Right-Clicking.\n\nTo reveal what is underneath a block you Left-Click on the block.\n\nThe numbers that appear in certain blocks represent the number of blocks containing creeper spawns adjacent to that block.\n\nYou have a limited number of signs to place, you can however recover them by Right-Clicking on them.\n\nIf you have placed signs adjacent to a block and their number is equal to or greater than the number in the block,\nyou click on the numbered block with both mousekeys to automatically open all unchecked, unsigned blocks adjacent to the one you're clicking on.\n\nWhen you have covered all creeper spawns with signs, you win!\n\nMade by: Jethro Muller\nStudent Number: MLLJET001\n\nThis game uses images from Minecraft.");
		keepTime=true;
		startTiming();
	}

	/**
	 * Handles the quit request by the user. For when the user clicks the quit button.
	 */
	private void handleQuit() {
		int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (answer==0) {
			System.exit(0);
		} else {
			keepTime = true;
			startTiming();
		}
		
	}

	/**
	 * Resets the board. Regenerating all the mine positions, reseting the timer and flag count.
	 * 
	 * @param choice
	 *            int that specifies which reset action the user wishes to
	 *            perform. 0 is for when the user loses. 1 is for a win. 3 is for a manual reset.
	 */
	private void reset(int choice) {
		String totalTime = ""+time[0]+"h "+time[1]+"m "+time[2]+"s";
		String options[] = { "Yes", "No" };

		// Default
		String message = "Would you like to start a new Game?", heading = "New Game";
		
		//if the user clicks on a hidden creeper
		if (choice == 0) {
			message = "Game Over!\nWould you like to try again?";
			heading = "Game Over";
		//if the user flags all the creeper spawns
		} else if (choice == 1) {
			message = "Congratulations! You took: "+totalTime+" to finish.\nWould you like to try again?";
			heading = "Congratulations!";
		}
		
		//Shows the appropriate message depending on how the user triggered the reset method.
		int ans = JOptionPane.showOptionDialog(this, message, heading,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, "Yes");
		
		if (ans == 0) {
			//If the user clicks Yes after choosing to start a new game.
			if (choice==3) {
				selectDifficulty();
			//If the user clicks Yes after losing or winning
			} else {
				MinesweeperGUI na = new MinesweeperGUI(gridWidth, gridHeight,
						totalCreepers);
				na.setVisible(true);
				this.dispose();
			}
		} else {
			//if the user clicks no after choosing anything except for new game
			if (choice!=3) {
				System.exit(0);
			//if the user clicks no after choosing new game
			} else {
				keepTime=true;
				startTiming();
			}
		}
	}

	/**
	 * Shows the dialog that allows the user to select the difficulty.
	 */
	private void selectDifficulty() {
		String[] options = { "Easy: 9x9 Grid - 10 Spawns",
				"Medium: 16x16 Grid - 40 Spawns",
				"Hardcore: 16x16 Grid - 80 Spawns" };
		int choice;
		int gridWidth = 9, gridHeight=9, numCreepers=10;

		choice = JOptionPane.showOptionDialog(null, "Choose a difficulty:",
				"Choose wisely!", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		switch (choice) {
		case 0:
			numCreepers = 10;
			gridWidth = 9;
			gridHeight = 9;
			break;
		case 1:
			numCreepers = 40;
			gridWidth = 16;
			gridHeight = 16;
			break;
		case 2:
			numCreepers = 80;
			gridWidth = 16;
			gridHeight = 16;
			break;
		}
		
		MinesweeperGUI na = new MinesweeperGUI(gridWidth, gridHeight,numCreepers);
		na.setVisible(true);
		this.dispose();
		
	}

	/**
	 * Takes in the current time and returns an int array that has hours as its first element, minutes as its second and seconds as its third.
	 * @param timeToHandle long with the current time.
	 * @return int [] of length 3 that has hours as its first element, minutes as its second and seconds as its third. 
	 */
	private int [] handleTime(long timeToHandle) {
		int time = (int) timeToHandle;
		int hours=0, mins=0, secs=0;
		int [] toReturn=new int [3];
		
		//If the time is less than 1 hour but more than 1 minute
		if (time>=60 && time<3600) {
			mins = time/60;
			secs = time%60;
		//If the time is less than or equal to 1 hour
		} else if (time>=3600) {
			hours=time/3600;
			mins=(time%3600)/60;
			secs=((time%3600)/60)%60;
		//If the time is less than 1 minute
		} else {
			secs = time;
		}
		
		toReturn[0] =hours;
		//System.out.print(toReturn[0]);
		toReturn[1] =mins;
		//System.out.print(toReturn[1]);
		toReturn[2] =secs;
		//System.out.println(toReturn[2]);
		
		return toReturn;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		//Button that was clicked to fire the event
		MyButton clickedButton = ((MyButton) me.getComponent());
		//The action command of the button that fired the event
		String actionCommand = clickedButton.getActionCommand();
		
		boolean hasSign = clickedButton.hasSign();
		boolean wasChecked = clickedButton.wasChecked();
		
		
	    
		//If the user clicked on a button with the left mouse button.
		if (me.getButton() == MouseEvent.BUTTON1) {
			if (!hasSign) {
				if (clickedButton.hasCreeper()) {
					showBombs();
					keepTime = false;
					reset(0);
				} else {
					int middle = actionCommand.indexOf("|");
					// System.out.println(actionCommand);
					uncover(actionCommand.substring(0, middle),
							actionCommand.substring(middle + 1),0);
				}
			}
		//If the user clicked on a button with the right mouse button.
		} else if (me.getButton() == MouseEvent.BUTTON3) {
			//If there is already a sign on the button
			if (hasSign) {
				if (clickedButton.hasCreeper()) {
					creepersLeft++;
				}
				clickedButton.setHasSign(false);
				numSigns++;
			//If there is no sign and there are more than 0 signs left
			} else if (numSigns > 0 && !wasChecked) {
				clickedButton.setHasSign(true);
				numSigns--;
				//If the button the sign was placed on has a creeper
				if (clickedButton.hasCreeper()) {
					creepersLeft--;
					//If there are no more creepers to cover
					if (creepersLeft == 0) {
						keepTime = false;
						reset(1);
					}
				}
			}
			//Updated the number of signs
			setSignsIcons();
		}
	}

	/**
	 * Makes all the bombs visible after the user clicks on a bomb.
	 * Loops through all the buttons and sets the icons of buttons that have a creeper.
	 */
	private void showBombs() {
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				creeperGrid[y][x].explode();
			}
		}

	}

	/**
	 * Uncovers all tiles not adjacent to a tile with a bomb.
	 * 
	 * @param xPosition
	 *            int containing the x position of the button clicked.
	 * @param yPosition
	 *            int containing the x position of the button clicked.
	 * @param choice int which represents which function the uncover will perform. 0 is the default option. 1 is the option for when the user clicks with both mouse buttons.
	 */
	private void uncover(String xPosition, String yPosition, int choice) {
		//The x position of the button clicked in the grid.
		int xPos = Integer.parseInt(xPosition);
		//The y position of the button clicked in the grid.
		int yPos = Integer.parseInt(yPosition);
		//The number of bombs adjacent to the button being checked
		int creeperCounter = 0;
		//The number of bombs adjacent to the button being checked
		int signCounter = 0;
		//Arraylist containing coords of new positions to check
		ArrayList<String> positions = new ArrayList<String>();

		// System.out.println("Poisition: "+x+" "+y);
		
		//startY is set to y if y==0. Otherwise it's set to y-1
		int startY = (yPos == 0) ? yPos : (yPos - 1);
		//endY is set to y if y==gridHeight. Otherwise it's set to y+1
		int endY = (yPos == gridHeight - 1) ? yPos : (yPos + 1);
		//startX is set to x if x==0. Otherwise it's set to x-1
		int startX = (xPos == 0) ? xPos : (xPos - 1);
		//endX is set to x if x==gridHeight. Otherwise it's set to x+1
		int endX = (xPos == gridWidth - 1) ? xPos : (xPos + 1);

		// System.out.println(startY+" "+endY+" "+startX+" "+endX);

		// Checks in all directions possible as determined above.
		for (int yord = startY; yord <= endY; yord++) {
			for (int xord = startX; xord <= endX; xord++) {
				//if a grid button has a creeper it increases the bomb count
				if (creeperGrid[yord][xord].hasCreeper() && choice==0) {
					creeperCounter++;
				/*
				 * If there is no creeper and it hasn't already been checked and there is no sign on it.
				 * It is added to the checking queue.
				 */
				} else if (choice==1) {
					if (creeperGrid[yord][xord].hasSign()) {
						signCounter++;
					} else {
						positions.add(xord + "|" + yord);
					}
				} else if (!creeperGrid[yord][xord].wasChecked()
						&& !creeperGrid[yord][xord].hasSign()) {
					positions.add(xord + "|" + yord);
				}
			}
		}
		/*
		 * if the button being checked has 1 or more bombs adjacent to it then the checking process is halted and a number is assigned to the block.
		 */
		if (creeperCounter > 0 && choice==0) {
			// System.out.println(""+bombCounter);
			creeperGrid[yPos][xPos].setChecked();
			creeperGrid[yPos][xPos]
					.setIcon(new ImageIcon(getClass().getResource("/"+creeperCounter + ".png")));
			creeperGrid[yPos][xPos].setCreeperNumber(creeperCounter);
		/*
		 * if the button being checked has 0 bombs adjacent it expands the search to adjacent blocks identified earlier.
		 */
		} else {
			//loops over the collected positions to be checked
			for (int i = 0; i < positions.size(); i++) {
				//if it's a regular uncover operation
				if (choice==0) {
					int middle = positions.get(i).indexOf("|");
					xPos = Integer.parseInt(positions.get(i).substring(0, middle));
					yPos = Integer.parseInt(positions.get(i).substring(middle + 1));
					creeperGrid[yPos][xPos].setChecked();
					uncover(String.valueOf(xPos), String.valueOf(yPos),0);
				//if it's a rmb and lmb uncover
				} else if (signCounter>=creeperGrid[yPos][xPos].getCreeperNumber()) {
					int middle = positions.get(i).indexOf("|");
					int x = Integer.parseInt(positions.get(i).substring(0, middle));
					int y = Integer.parseInt(positions.get(i).substring(middle + 1));
					
					//if the button about to be uncovered has a creeper. The user loses.
					if (creeperGrid[y][x].hasCreeper()) {
						showBombs();
						keepTime=false;
						reset(0);
					//if the button about to be uncovered has no creeper. Run the normal uncover operation on it.
					} else {
						uncover(String.valueOf(x),String.valueOf(y),0);
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent mep) {
		//if the user pushes the left mouse button
		if (mep.getButton()==MouseEvent.BUTTON1) {
			lmbPressed = true;
		}
		//if the user pushes the right mouse button
		if (mep.getButton()==MouseEvent.BUTTON3) {
			rmbPressed = true;
		}
		//if the user pushes the right mouse button and the left mouse button
		if (lmbPressed && rmbPressed) {
			MyButton clickedButton = (MyButton) mep.getComponent(); 
			String actionCommand = clickedButton.getActionCommand();
			int middle = actionCommand.indexOf("|");
			
			//if the button has an icon and it's already been checked
			if (clickedButton.getIcon()!=null && clickedButton.wasChecked()) {
				uncover(actionCommand.substring(0, middle),
						actionCommand.substring(middle + 1),1);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent mer) {
		if (mer.getButton()==MouseEvent.BUTTON1) {
			lmbPressed = false;
		}
		if (mer.getButton()==MouseEvent.BUTTON3) {
			rmbPressed = false;
		}
	}
}