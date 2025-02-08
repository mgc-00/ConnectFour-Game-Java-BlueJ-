VERSION: 2.3 
DATE: 2.3 07/02/2025
AUTHOR: MGC https://github.com/mgc-00/mgc-git-repo


Connect Four Game

A Java-based Connect Four game with a Graphical User Interface (GUI), designed to run primarily in BlueJ as well as standard Java environments. The game supports player vs. computer mode, move undoing, game saving/loading, and an intuitive visual interface.
Features

    Interactive GUI with a 6x7 grid layout
    Player vs. Computer mode with AI opponent
    Move undo feature
    Game state saving and loading
    Custom styling with bordered grid design
    Optional Text based game included
    Observer pattern to update the UI dynamically
 

Installation & Setup

    Ensure Java is installed (JDK 8 or higher recommended). 

    ******Java Development Kit is essential!!!!******

    https://sourceforge.net/projects/portableapps/files/JDK/jdk-8u411-windows-x64.exe/download

    *BlueJ users: Open the project folder in BlueJ and compile the classes.
    Run through "ConnectFourGUI" (or ConnectFourUI for the Text based game)

    **Standard Java users, add the file contents to a location on your machine. 
    Run CMD and set the directory.

    eg: C:\Windows\System32>cd c:/

    c:\>cd ConnectFour Game (Java-BlueJ)

    Compile and run using-

    javac *.java

    Once successfully compiled, run the games either as (Text based or GUI based):

    java ConnectFourUI

    java ConnectFourGUI


    The game launches in CMD to play as text based or in a GUI window where you can start playing immediately.

    ***To run as an executable JAR program, make sure everything is compiled and run the JAR file: ConnectFourGUI

    If there are any issues with this, DELETE the JAR file and enter in your CMD to create a new JAR:

    jar cfm ConnectFourGUI.jar ConnectFourGUI.txt *.class



Troubleshooting

    GUI not displaying? Ensure Swing components are being run on the Event Dispatch Thread (EDT).
    Game not responding? Restart and check console errors for details. Also, exit and restart CMD is needed.
    BlueJ users: Make sure all classes are compiled before execution.

    Issues with Javac in CMD not working? Follow the steps here to edit your System Environment Variables (found halfway down the page):
    
    https://stackoverflow.com/questions/7709041/javac-is-not-recognized-as-an-internal-or-external-command-operable-program-or

Contributions

Pull requests and issue reports are welcome! Feel free to fork and enhance the project.

Have fun!! Thank you ^^ 
