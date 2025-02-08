import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.io.EOFException;
import java.io.Serializable;
import java.io.*;

/**
 * ConnectFourUI
 * Text-based user interface class for ConnectFour game
 * @author MGC https://github.com/mgc-00/mgc-git-repo 07/02/2025
 */
 
public class ConnectFourUI {
    private Scanner consoleReader; //Reads inputs from the console
    private ConnectFour game; //The ConnectFour game
    private Slot moves[][];// 2D array of moves
    private String menuChoice = "x"; // user's selection from the menu, x by default

    /**
     * Constructor for the ConnectFourUI class.
     * This method creates the game, initialises the scanner to read input, displays the initial game board and menu.
     */
    public ConnectFourUI() {
        game = new ConnectFour();
        consoleReader = new Scanner(System.in);
        displayGame();
        menu();
    }

    /**
     * Public static void main - the first method that runs when the project is run. 
     * This method initialises a new instance of the ConnectFour UI.
     * @param args 
     */
    public static void main(String args[]) {
        ConnectFourUI ui = new ConnectFourUI();
    }

    /**
     * Menu
     * This method displays the menu that provides users with the options needed to play the game
     */
    public void menu() {
        while (!menuChoice.equalsIgnoreCase("Q")) {
            System.out.println("Please select an option: \n"
                + "       [M] make move\n"
                + "       [S] save game\n"
                + "       [L] load saved game\n"
                + "       [U] undo move\n"
                + "       [C] clear game\n"
                + "       [Q] quit game\n");
            menuChoice = getChoice();
        }
    }

    /**
     * getChoice
     * This method processes the selection the user has made in the menu
     * @return the choice of the user
     */
    public String getChoice() {
        String choice = consoleReader.next();
        if (choice.equalsIgnoreCase("M")) {
            makeMove();
            checkWin();
        } else if (choice.equalsIgnoreCase("S")) {
            saveGame();
        } else if (choice.equalsIgnoreCase("U")) {
            undoMove();
        } else if (choice.equalsIgnoreCase("L")) {
            loadGame();
        } else if (choice.equalsIgnoreCase("C")) {
            clearGame();
        } else if (choice.equalsIgnoreCase("Q")) {
            System.exit(0);
        }
        return choice;
    }

    /**
     * saveGame
     * Saves game to a file
     */
    public void saveGame() {
        try {
            System.out.println("Enter the file name to save the game:");
            String fileName = consoleReader.next();

            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

                // Serialize and save the game object
                objectOutputStream.writeObject(game);

                System.out.println("Game saved successfully to " + fileName);
            } catch (IOException e) {
                System.err.println("Error while saving the game: " + e.getMessage());
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid file name format.");
            consoleReader.nextLine(); // Consume the invalid input
        }
    }

    /**
     * Undoes the last moves of player and computer
     */
    public void undoMove() {
        System.out.println("Move has been undone");
        game.undoMove();
        displayGame(); // Update the display after undoing the move
    }

    /**
     * loadGame
     * Loads a saved game file
     */

    public void loadGame() {
        try {
            System.out.println("Enter the file name to load a saved game:");
            String fileName = consoleReader.next();

            File file = new File(fileName);

            if (!file.exists()) {
                System.err.println("File not found: " + fileName);
                return; // Exit the method if the file doesn't exist
            }

            try (FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                // Deserialize and load the game object
                ConnectFour loadedGame = (ConnectFour) objectInputStream.readObject();

                game = loadedGame; // Replace the current game with the loaded game

                System.out.println("Game loaded successfully from " + fileName);
                displayGame(); // Update the display to show the loaded game state
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error while loading the game: " + e.getMessage());
                e.printStackTrace(); // Print the stack trace for debugging
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid file name format.");
            consoleReader.nextLine(); // Consume the invalid input
        }
    }

    /**
     * clearGame
     * Clears the game
     */
    public void clearGame() {
        game = new ConnectFour();
        displayGame();
    }

    /**
     * displayGame
     * Sets game board display 
     */    
    
    public void displayGame() {
        Slot[][] moves = game.getMoves(); // Retrieve the game's moves
        System.out.println("*****************************\n"
            + "  Welcome to Mike's game of \n"
            + "   Connect Four, Good luck!\n"
            + "*****************************");

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                String state = moves[c][r].getState();
                System.out.print("| " + (state.equals(ConnectFour.EMPTYSLOT) ? " " : state) + " ");
            }
            System.out.print("|\n");
            System.out.println(" - - - - - - - - - - - - - -");
        }

        System.out.println("  0   1   2   3   4   5   6");
    }

    /**
     * makeMove
     * Displays player's move with the computer's move
     */
    public void makeMove() {
        System.out.println("Please enter the column you wish to select");
        int playerMove = consoleReader.nextInt();
        game.addMove(playerMove, true);
        game.generateComputerMove();

        displayGame(); // Update the display after making a move

        checkWin();
    }

    /**
     * checkWin
     * Displays game winner
     */
    public void checkWin() {
        if (game.checkWin()!= null) {
            System.out.println("And the winner is... " + game.checkWin());
            replay();
        };
    }

    /**
     * replay
     * This method gives the user the option to play again, or quit, once the game is won
     */
    public void replay() {
        System.out.println("Would you like to play again? (Y/N)");
        String choice = consoleReader.next();
        if (choice.equalsIgnoreCase("Y")) {
            game = new ConnectFour();
            displayGame();
            menu();  
        } else if (choice.equalsIgnoreCase("N")) {
            System.exit(0);

        }
    }
}//End of class ConnectFourUI
