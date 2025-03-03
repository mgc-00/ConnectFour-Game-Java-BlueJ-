import java.util.Observable; // Import for Observable
import java.util.Observer;   // Import for Observer
import java.util.Stack; // Import for Stack data structure
import java.util.Random; // Import for Random number generation
import javax.swing.*; // Swing GUI components
import javax.swing.border.*; // Swing border classes
import javax.swing.JOptionPane;
import javax.swing.border.AbstractBorder; // AbstractBorder class for custom borders
import java.awt.*; // AWT (Abstract Window Toolkit) for GUI components
import java.awt.event.ActionEvent; // ActionEvent for GUI event handling
import java.awt.event.ActionListener; // ActionListener for GUI event handling
import java.io.ObjectOutputStream; // ObjectOutputStream for object serialization
import java.io.FileOutputStream; // FileOutputStream for file output
import java.io.IOException; // IOException for handling I/O exceptions
import java.io.ObjectInputStream; // ObjectInputStream for object deserialization
import java.io.FileInputStream; // FileInputStream for file input

/**
 * ConnectFourGUI
 * Graphical user interface class for ConnectFour game
 * @author MGC https://github.com/mgc-00/ 07/02/2025
 */

public class ConnectFourGUI  extends JFrame  implements ActionListener, Observer {
    private ConnectFour game;            // Reference to the ConnectFour game logic
    private JButton[][] buttons;         // Array to hold the GUI buttons
    private JLabel statusLabel;          // Label to display the game status
    private boolean gameOver;            // Flag to indicate whether the game is over

    // Constructor for the ConnectFourGUI class
    public ConnectFourGUI() {
        game = new ConnectFour();        // Initialize the ConnectFour game
        buttons = new JButton[7][6];     // Create an array to hold the GUI buttons
        statusLabel = new JLabel("Current Player: Player"); // Create a label for displaying game status
        gameOver = false;                // Initialize the game over flag

        // Create a panel for the game buttons with a 6x7 grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(6, 7));
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                buttons[col][row] = new JButton();                  // Create a new button
                buttons[col][row].setActionCommand(Integer.toString(col)); // Set the action command
                buttons[col][row].addActionListener(this);          // Add an action listener
                buttons[col][row].setPreferredSize(new Dimension(80, 80)); // Set button size
                buttons[col][row].setBackground(Color.BLUE);        // Set button background color to blue
                buttonPanel.add(buttons[col][row]);                 // Add button to the panel

                // Sets a thicker border for the connect four frame
                buttons[col][row].setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));

                buttonPanel.add(buttons[col][row]); // Add button to the panel

            }
        }

        // Create a custom border using AbstractBorder 
        Border customBorder = new AbstractBorder() {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { //FLAGGED
                    // Define the size of small squares
                    int squareSize = 5;

                    // Define colors for the squares
                    Color[] squareColors = {Color.RED, Color.YELLOW};

                    // Loop through rows and columns to draw the checkered pattern
                    for (int row = 0; row < height; row += squareSize) {
                        for (int col = 0; col < width; col += squareSize) { 
                            // Determine the color index based on the square's position
                            int colorIndex = (row / squareSize + col / squareSize) % 2;
                            g.setColor(squareColors[colorIndex]);
                            g.fillRect(x + col, y + row, squareSize, squareSize);
                        }
                    }
                }

                @Override
                public Insets getBorderInsets(Component c) {
                    // Return border insets (10 pixels on all sides)
                    return new Insets(10, 10, 10, 10);
                }

                @Override
                public Insets getBorderInsets(Component c, Insets insets) {
                    // Adjust border insets (10 pixels on all sides) and return them
                    insets.left = insets.top = insets.right = insets.bottom = 10;
                    return insets;
                }
            };
        // Set the custom border to the button panel
        buttonPanel.setBorder(customBorder);

        // Creates the menu section with buttons 
        JPanel menuPanel = new JPanel(new GridLayout(5, 1));
        String[] menuButtons = {"[S] Save Game", "[L] Load Game", "[U] Undo Move", "[C] Clear Game", "[Q] Quit Game"};
        for (String menuButton : menuButtons) {
            JButton menuBtn = new JButton(menuButton);
            menuBtn.setBackground(Color.WHITE);
            menuBtn.setActionCommand(menuButton);
            menuBtn.addActionListener(this);
            menuPanel.add(menuBtn);

        }

        // Creates main panel plus menu & buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, BorderLayout.EAST); // Adjust layout to place menu on the right
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        // Assembles the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(mainPanel);
        this.setTitle("Welcome to Mike's game of Connect Four, Good luck!!\n");
        this.pack();
        this.setVisible(true);
    }

    // ActionListener implementation for handling button clicks and menu actions
    /**
     * actionPerformed
     * Event Listener - performs action when a button is clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("[S] Save Game")) {
            saveGame(); // Call the saveGame method when the "Save Game" button is clicked
        } else if (cmd.equals("[L] Load Game")) {
            loadGame();
        } else if (cmd.equals("[U] Undo Move")) {
            undoMove();
        } else if (cmd.equals("[C] Clear Game")) {
            clearGame();
        } else if (cmd.equals("[Q] Quit Game")) {
            System.exit(0);
        } else if (!gameOver) {
            int col = Integer.parseInt(cmd);
            game.addMove(col, true);
            updateGUI();
            String winner = game.checkWin();
            if (winner != null) {
                gameOver = true;
                statusLabel.setText("WE HAVE A WINNER!!! " + winner);

                // Custom YES/NO option dialog
                Object[] options = {"Sure thing!", "Bye bye!"};

                int choice = JOptionPane.showOptionDialog(
                        this,  // Parent component, use 'this' if ConnectFourGUI is the parent
                        "Another game?",  // Message to begin a fresh game or Quit playing
                        "GAME OVER!",  // Title of dialogue box for Continue or Quit playing 
                        JOptionPane.YES_NO_OPTION,  // Option type
                        JOptionPane.QUESTION_MESSAGE,  // Message type
                        null,  // Icon, null for default
                        options,  // Custom button texts
                        options[0]  // Default button
                    );

                if (choice == JOptionPane.YES_OPTION) {
                    // Start a new game, calls the clearGame method
                    clearGame();
                } else if (choice == JOptionPane.NO_OPTION) {
                    // Quit and exit the game
                    System.exit(0);
                }
            } else {
                game.generateComputerMove();
                updateGUI();
                winner = game.checkWin();
                if (winner != null) {
                    gameOver = true;
                    statusLabel.setText("WE HAVE A WINNER!!! " + winner);

                    // For a custom YES/NO option dialog
                    Object[] options = {"Sure thing!", "Bye bye!"};

                    int choice = JOptionPane.showOptionDialog(
                            this,  // Parent component, use 'this' if ConnectFourGUI is the parent
                            "Another game?",  // Message to begin a fresh game or Quit playing
                            "GAME OVER!",  // Title of dialogue box for Continue or Quit playing 
                            JOptionPane.YES_NO_OPTION,  // Option type
                            JOptionPane.QUESTION_MESSAGE,  // Message type
                            null,  // Icon, null for default
                            options,  // Custom button texts
                            options[0]  // Default button
                        );

                    if (choice == JOptionPane.YES_OPTION) {
                        // Start a new game (you may call your clearGame method here)
                        clearGame();
                    } else if (choice == JOptionPane.NO_OPTION) {
                        // Quit and exit the game
                        System.exit(0);
                    }
                }
            }
        }
    }

    // Observer pattern update method
    @Override
    public void update(Observable o, Object arg) {
        // Update the GUI here based on changes in the ConnectFour game state
        updateGUI();
    }

    /**
     * clearGame
     * This method clears the game board and any record of moves, to reset the game
     */
    private void clearGame() {
        game.createGame(); // Reset the game board // Call the clearGame method to reset the game
        gameOver = false; // Reset the game over status
        updateGUI(); // Update the GUI to clear the buttons
        statusLabel.setText("Current Player: Player"); // Reset the status label
    }

    // Method to save the current game state
    /**
     * saveGame
     * This method saves the current game state
     */
    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser dialog to select the save location

        int result = fileChooser.showSaveDialog(null); // Display the save dialog and capture the user's choice

        if (result == JFileChooser.APPROVE_OPTION) { // Check if the user selected the "Save" option in the dialog
            String fileName = fileChooser.getSelectedFile().getAbsolutePath(); // Get the absolute path of the selected file

            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName)); // Create an output stream to write the game data to a file
                outputStream.writeObject(game); // Serialize and write the game object to the file
                outputStream.close(); // Close the output stream
                JOptionPane.showMessageDialog(null, "Game saved successfully!"); // Display a success message
            } catch (IOException e) { //FLAGGED
                JOptionPane.showMessageDialog(null, "Error saving the game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Display an error message if an exception occurs
            }
        }
    }

    // Method to load a saved game
    /**
     * loadGame
     * This method successfully loads a previous saved game state
     */
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser dialog to select the saved game file

        int result = fileChooser.showOpenDialog(null); // Show the open file dialog and store the user's choice

        if (result == JFileChooser.APPROVE_OPTION) { // Check if the user selected a file
            String fileName = fileChooser.getSelectedFile().getAbsolutePath(); // Get the absolute file path of the selected file
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName)); // Create an ObjectInputStream to read the saved game file
                game = (ConnectFour) inputStream.readObject(); // Deserialize the saved game and assign it to the 'game' variable
                inputStream.close(); // Close the ObjectInputStream
                gameOver = false; // Reset the game over status
                updateGUI(); // Update the GUI to display the loaded game state
                JOptionPane.showMessageDialog(null, "Game loaded successfully!"); // Show a success message dialog
            } catch (IOException | ClassNotFoundException e) { //FLAGGED
                JOptionPane.showMessageDialog(null, "Error loading the game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Show an error message dialog if loading fails
            }
        }
    }

    // Method to undo the last move of player and computer
    /**
     * undoMove
     * This method undoes the previous move made in the game, along with the corresponding computer move
     */
    private void undoMove() {
        if (!gameOver) { // Check if the game is not over
            game.undoMove(); // Call the undoPlayerMove method of the game object to undo the last player move
            updateGUI(); // Update the graphical user interface (GUI) to reflect the changes
            gameOver = false; // Reset the game over status to indicate the game is not over
        }
    }

    // Method to update the GUI based on the game state
    /**
     * updateGUI
     * This method updates GUI based on the game status
     */
    private void updateGUI() {
        Slot[][] moves = game.getMoves(); // Retrieve the game's move data
        for (int col = 0; col < 7; col++) { // Iterate through columns
            for (int row = 0; row < 6; row++) { // Iterate through rows
                String state = moves[col][row].getState(); // Get the state of the slot
                if (state.equals(ConnectFour.PLAYERMOVE)) { // If it's a player's move
                    buttons[col][row].setBackground(Color.RED); // Set the button background to RED for the player
                } else if (state.equals(ConnectFour.COMPUTERMOVE)) { // If it's a computer's move
                    buttons[col][row].setBackground(Color.YELLOW); // Set the button background to YELLOW for th
                } else { // If the slot is empty
                    buttons[col][row].setBackground(Color.BLUE); // Set the button background to BLUE
                }
            }
        }
    }

    // Main method to start the ConnectFourGUI application
    /**
     * Main thread which executes the GUI
     */
    public static void main(String[] args) {
        // Schedule the GUI creation on the Event Dispatch Thread (EDT) using SwingUtilities.invokeLater
        SwingUtilities.invokeLater(() -> 
                {
                    // Create an instance of the ConnectFourGUI class
                    ConnectFourGUI gui = new ConnectFourGUI();
                    // Set the size of the game window
                    gui.setSize(1000, 640);
            });
    }

}//End of class ConnectFourGUI
