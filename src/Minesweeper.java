import javax.swing.JOptionPane;

/**
 * @author Jethro Muller
 * 
 */

public class Minesweeper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Welcome message and difficulty settings
		 */
		JOptionPane
				.showMessageDialog(
						null,
						"Welcome\nYour duty is to mark the blocks containing creeper spawns!\nYou'll be marking them with signposts.\nIf you accidentally enter a creeper spawn, you will die.\nGood luck.", "Welcome to Minesweeper", JOptionPane.PLAIN_MESSAGE);
		String[] options = { "Easy: 9x9 Grid - 10 Spawns",
				"Medium: 16x16 Grid - 40 Spawns",
				"Hardcore: 16x16 Grid - 80 Spawns" };
		int choice;
		int gridWidth = 9, gridHeight = 9, numCreepers = 10;

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
		
		//Creates a new gui object
		MinesweeperGUI gameGUI = new MinesweeperGUI(gridWidth, gridHeight,
				numCreepers);
		gameGUI.setVisible(true);
	}

}
