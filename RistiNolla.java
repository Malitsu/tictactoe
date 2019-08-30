import java.util.Scanner;
import java.lang.*;

/**
 * RistiNolla contains the main method of the program.
 *
 * The class RistiNolla is the starting point of the program.
 * It creates the objects needed for play and governs
 * who the players are and how many games are played between them.
 *
 * @author Tiina Malinen
 * @version 2018.1214
 */
class RistiNolla {

    /**
    * Main method of the program.
    *
    * @param args is not used.
    */
    public static void main(String[] args) {
        Player player1 = new Player('X', true);
        Player skynet = new Player('O', false);
        boolean newGame = true;

        do {
            Board grid = new Board();
            io.printBoard(grid);
            Game game = new Game(grid, player1, skynet);
            game.play();
            System.out.println("Ihminen " +player1.getScore() +" - Tietokone " +skynet.getScore());
            newGame = io.askContinue("Haluatko uuden kierroksen?");
        } while (newGame);
    }

}

/**
 * Game controls the flow of a single game session.
 *
 * The class Game contains turns, players and board.
 * It takes care of alternating the turns between players
 * and checks when the session should be ended.
 *
 * @author Tiina Malinen
 * @version 2018.1214
 */
class Game {

    /**
    * The game board.
    */    
    Board grid;

    /**
    * First player to make a move.
    */
    Player player1;

    /**
    * Second player to make a move
    */
    Player player2;

    /**
    * Should the game be continued or not
    */
    boolean keepPlaying = true;

   /**
    * Constructor of the class Game.
    *
    * The constructor takes in the elements needed to run the game
    * and saves the information.
    *
    * @param grid the Board where the game takes place
    * @param player1 first player to play the game
    * @param player2 second player to play the game
    */
    public Game(Board grid, Player player1, Player player2) {
        this.grid = grid;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
    * Runs the game by giving turns to players.
    *
    * The method alnternates between players and
    * prints the current board situation after every 
    * round. Quits when either one of the players wins.
    */    
    public void play() {
        while (getContinue()) {
            turn(player1);
            turn(player2);
            io.printBoard(grid);
        }
    }

    /**
    * Takes care of an individual turn of the given player.
    * If the player is human, x and y coordinates are 
    * asked after which the move is placed on the board.
    * If the player is not human, the mission is given 
    * to AI algorithm. The turn ends by checking if the
    * victory condition has been met.
    *
    * @param player The player who's turn is being played.
    */
    private void turn(Player player) {
        Move move;
        int x = -1;
        int y = -1;

        if (player.isHuman()) {
            do {
                do {
                    x = io.askCoordinate('X', grid);
                } while (x == -1);
                do {
                    y = io.askCoordinate('Y', grid);
                } while (y == -1);
                move = new Move(player, y, x);
            } while (!grid.isValid(move));
        }
        else {
            move = player.ai(grid);
        }

        grid.addMove(move);

        if (grid.checkWin(move)) {
            io.winMessage(player.isHuman());
            player.addScore();
            keepPlaying = false;
        }

    }

    /**
    * A getter to return the value that marks if the game
    * should still be continued or not.
    *
    * @return the value stored in the variable keepPlaying
    */
    public boolean getContinue() {
        return keepPlaying;
    }

}

/**
 * Move contains information of a single move on the board.
 *
 * The class Move contains the location values and the player
 * of a given move that has taken place on the board. It only
 * has some basic methods intended to govern the information.
 *
 * @author Tiina Malinen
 * @version 2018.1214
 */
class Move {

    /**
    * The column where the move is located.
    */
    int xValue;

    /**
    * The row where the move is located.
    */
    int yValue;

    /**
    * The player who has made the move.
    */
    Player player;

    /**
    * A getter to return the marker used by the player who
    * is responsible for this particular move.
    *
    * @return the mark of the player who has made the move
    */    
    public char getPlayer() {
        return player.getMarker();
    }

    /**
    * A getter to return the column where this move is placed.
    *
    * @return the X-coordinate of the move's location
    */    
    public int getX() {
        return xValue;
    }

    /**
    * A getter to return the row where this move is placed.
    *
    * @return the Y-coordinate of the move's location
    */        
    public int getY() {
        return yValue;
    }

   /**
    * Constructor of the class Move.
    *
    * The constructor takes in the particularities of this move
    * and stores the information.
    *
    * @param player the player who commits this move
    * @param yValue the Y-coordinate of the move's location
    * @param xValue the X-coordinate of the move's location
    */
    public Move(Player player, int yValue, int xValue) {
        this.player = player;
        this.xValue = xValue;
        this.yValue = yValue;
    }

}

/**
 * Player contains the information of a single player.
 *
 * The class Player contains everything that has to do with
 * the players such as their marker, score and whether they 
 * are human or not. In case of a computer player Player also
 * contains the methods to decide its move.
 *
 * @author Tiina Malinen
 * @version 2018.1214
 */
class Player {

    /**
    * How many games the player has won.
    */
    private int score = 0;

    /**
    * The mark the player uses on the board.
    */
    private char marker = '.';

    /**
    * Whether the player is the user or the computer.
    */
    private boolean isHuman = true;

   /**
    * Constructor of the class Player.
    *
    * The constructor takes in which character is used as
    * the player's marker and whether they are a human or
    * not and stores the information.
    *
    * @param marker the player who commits this move
    * @param isHuman the Y-coordinate of the move's location
    */
    public Player(char marker, boolean isHuman) {
        if (marker != ' ') {
            this.marker = marker;
        }
        if (isHuman == false) {
            this.isHuman = false;
        }
    }

    /**
    * A getter to return the marker of the player
    *
    * @return the character used to mark this player
    */   
    public char getMarker() {
        return marker;
    }

    /**
    * A method to raise the score value by one point
    */   
        public void addScore() {
        score++;
    }

    /**
    * A getter to return the score of the player
    *
    * @return how many games the player has won
    */   
    public int getScore() {
        return score;
    }

    /**
    * A getter to return whether the player is human or not
    *
    * @return the boolean value to tell if this player is human
    */   
    public boolean isHuman() {
        return isHuman;
    }

   /**
    * Decides the computer player's move
    *
    * The computer's move is decided by getting random
    * X and Y values that match the board constraints.
    * If the location is already in use, a new location
    * is determined.
    *
    * @param grid the Board where the Move should be made
    * @return the Move that the computer commits
    */
    public Move ai(Board grid) {
        Move divineStrike;
        int x;
        int y;
        do {
            x = (int)(Math.random() * grid.getWidth());
            y = (int)(Math.random() * grid.getHeight());
            divineStrike = new Move(this, y, x);
        } while (!grid.isEmpty(y, x));
        return divineStrike;
    }

}

/**
 * Board keeps track of the board situation and contains the methods to alter it.
 *
 * The class Board consists of the information of the current board
 * instance and the methods to either check the situation of the board
 * or alter it, such as creating the board and checking the win conditions.
 *
 * @author Tiina Malinen
 * @version 2018.1214
 */
class Board {

    /**
    * The mark used to express a free square.
    */
    final private char EMPTY = '.';

    /**
    * The amount of columns on the board.
    */
    private int width;

    /**
    * The amount of rows on the board.
    */
    private int height;

    /**
    * How many subsequent marks are needed to win the game.
    */
    private int winCondition;

    /**
    * An array used to store the game situation.
    */
    private char[][] board;

   /**
    * Constructor of the class Board.
    *
    * The constructor is not given any initiazon values.
    * Instead it goes on to send the needed information 
    * to different  submethods that aks the values from the user.
    */
    public Board() {
        setSize();
        setBoard();
        setWinCondition();
    }

   /**
    * A method for asking the board size from the user.
    *
    * The method asks the amount of columns and
    * rows of the board from the user.
    */
    private void setSize() {
        System.out.println("Anna ruudukon leveys (min. 3):");
        width = io.askInt(3, 500);
        System.out.println("Anna ruudukon korkeus (min. 3):");
        height = io.askInt(3, 500);
    }

    /**
    * A getter to return the height of the game board
    *
    * @return the amount of rows
    */   
        public int getHeight() {
        return height;
    }

    /**
    * A getter to return the width of the game board
    *
    * @return the amount of columns
    */       
    public int getWidth() {
        return width;
    }

    /**
    * A method to return what is marked in the given location
    *
    * @param y Y-coordinate of the wanted location
    * @param x X-coordinate of the wanted location
    * @return the amount of rows
    */   
    public char getMove(int y, int x) {
        return board[y][x];
    }

   /**
    * A method for asking the win condition.
    *
    * The method asks the amount of marks needed
    * side by side to win the game. The amount can
    * not be larger than the longest border of the
    * board.
    */
        private void setWinCondition() {
        System.out.println("Kuinka monta vierekkäistä pelimerkkiä vaaditaan voittoon? (min. 3)");
        int max = Math.max(getHeight(), getWidth());
        winCondition = io.askInt(3, max);
    }

   /**
    * A method to initialize the board.
    *
    * The method creates the game board array
    * based on the values stored in the class,
    * after which it proceeds to initialize
    * all the values with the empty value.
    */
    private void setBoard() {
        board = new char[getHeight()][getWidth()];
        for (int i=0; i<getHeight(); i++) {
            for (int j=0; j<getWidth(); j++) {
                board[i][j] = EMPTY;
            }
        } 
    }

    /**
    * A method to return if the given location already
    * contains a move or is still available
    *
    * @param y Y-coordinate of the wanted location
    * @param x X-coordinate of the wanted location
    * @return the boolean value of empty or not
    */   
        public boolean isEmpty(int y, int x) {
        if (isInside(y, x)) {
            if (board[y][x] == EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
    * A method to return if the given location exists
    * inside the board or would exceed its borders
    *
    * @param y Y-coordinate of the wanted location
    * @param x X-coordinate of the wanted location
    * @return the boolean value of the location status
    */ 
    public boolean isInside(int y, int x) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
    * A method to add a new move to the board.
    *
    * @param newMove the move to add
    */
        public void addMove(Move newMove) {
        int x = newMove.getX();
        int y = newMove.getY();
        board[y][x] = newMove.getPlayer();
    }

    /**
    * A method to return if the given move is possible
    * to make. Checks if the square exists and does
    * it already contain a move.
    *
    * @param move the possible move asked
    * @return the boolean value of whether the move can be played
    */ 
    public boolean isValid(Move move) {
        int x = move.getX();
        int y = move.getY();
        if (!isInside(y, x)) {
            return false;
        }
        else if (isEmpty(y, x)) {
            board[y][x] = move.getPlayer();
            return true;
        }
        else {
            System.out.println("Paikassa on jo " +board[y][x]);
            return false;
        }
    }

    /**
    * A method to return if adding the given move to
    * the board makes such a long series of marks to
    * form that its player wins the game. Four axis are
    * checked: horizontal, vertical, rising diagonal and
    * falling diagonal.
    *
    * @param move the last move added to the board
    * @return whether the winning condition is met
    */ 
    public boolean checkWin(Move move) {
        if (howMany(move, 1, 1)) {
            return true;
        }
        else if (howMany(move, 1, 0)) {
            return true;
        }
        else if (howMany(move, 0, 1)) {
            return true;
        }
        else if (howMany(move, 1, -1)) {
            return true;
        }
        else {
            return false;
        }        
    }

    /**
    * A help method to determine whether the given axis originating
    * from the given move forms a string of marks long enough to
    * win the game. The line is first continued to the given 
    * direction and then towards its opposite. The total amount
    * of the same mark in the given axis is then compared to
    * the amount needed for win.
    *
    * @param move the move where the check is initiated
    * @param xDirection X-coordinate of the direction to be checked
    * @param yDirection Y-coordinate of the direction to be checked
    * @return whether this axis meets the winning criteria or not
    */     
    private boolean howMany(Move move, int xDirection, int yDirection) {
        int x = move.getX();
        int y = move.getY();
        int count = 1;
        char search = move.getPlayer();
        boolean checkForward;
        boolean checkBackward;

        int i = 1;
        do {
            checkForward = false;

            int forwardX = x + (i*xDirection);
            int forwardY = y + (i*yDirection);

            if (isInside(forwardY, forwardX)) {
                if (getMove(forwardY,forwardX) == search) {
                    count++;
                    checkForward = true;
                }
            }

            if (count >= winCondition) {
                return true;
            }

            i++;
        } while (checkForward);

        i = 1;
        do {
            checkBackward = false;

            int backwardX = x - (i*xDirection);
            int backwardY = y - (i*yDirection);

            if (isInside(backwardY, backwardX)) {
                    if (getMove(backwardY, backwardX) == search) {
                        count++;
                        checkBackward = true;
                    }
            }

            if (count >= winCondition) {
                return true;
            }

            i++;
        } while(checkBackward);

        return false;
    }

}

/**
 * Io contains methods for interaction with the user.
 *
 * The class io is a collection of methods for asking user input
 * and printing output for the user, such as asking coordinate
 * values, asking the user yes/no -type questions and printing the
 * game situation.
 *
 * @author Tiina Malinen
 * @version 2018.1214
 */
class io {
    private static Scanner input = new Scanner(System.in);

    /**
    * A method to prompt the user for an int value
    * inside the given range. The method ends only
    * after a number that meets the criteria is given.
    *
    * @param min the smallest value possible
    * @param max the largest value possible
    * @return the int value given by the user
    */ 
    public static int askInt(int min, int max) {
        int number = 0;
        do {
            if (input.hasNextInt()) {
                number = input.nextInt();
            }
            if (number > max) {
                System.out.println("Luku voi olla enintään " +max);
            }
            input.nextLine();
        } while (number < min || number > max);
        return number;
    }

    /**
    * A method to print the situation on the
    * game board for the user to see. In addition
    * to the board values upper and left side are
    * given numbering to help the user to determine
    * the right value they wish to place their next
    * move to.
    *
    * @param board the board to be printed
    */     
    public static void printBoard(Board board) {
        System.out.print(' ');
        int t = 1;
        for (int j=0; j<board.getWidth(); j++) {
            System.out.print(t);
            t++;
            if (t==10) {
                t = 0;
            }
        }
        System.out.println();

        t = 1;
        for (int i=0; i<board.getHeight(); i++) {
            System.out.print(t);
            t++;
            if (t==10) {
                t = 0;
            }
            for (int j=0; j<board.getWidth(); j++) {
                System.out.print(board.getMove(i,j));
            }
            System.out.println();
        } 
    }

    /**
    * A method to prompt the user for a "yes or no" type
    * of question in various situations. Only the first
    * character of the input is read and others are
    * disregarded.
    *
    * @param question the string to be presented to the user
    * @return the boolean value of the user's answer
    */ 
        public static boolean askContinue(String question) {
        char yesNo = '?';
        System.out.println(question +" (k/e)");
        do {
            String line = input.nextLine();
            if (line.length()>0) {
                yesNo = line.charAt(0);
                yesNo = Character.toLowerCase(yesNo);
            }
        } while (yesNo != 'k' && yesNo != 'e');
        if (yesNo == 'e') {
            return false;
        }
        else {
            return true;
        }
    }

    /**
    * A method to declare the winner of the game. The
    * message depends on the winner's status inside
    * humanity or outside it.
    *
    * @param human whether the winner is human or not
    */ 
        public static void winMessage(boolean human) {
        if (human) {
            System.out.println("Voitit tietokoneen ylivertaisella älylläsi!");
        }
        else {
            System.out.println("Tietokone voitti jälleen säälittävät ihmiset!");
        }
    }

    /**
    * A method to prompt the user for an X or Y
    * coordinate for the location of the new move.
    * If the user gives an int value not inside the
    * borders, they are given an error. If something
    * else than an int is given, the user is prompted
    * on whether they wish to quit playing. If an
    * appropriate value isn't given, the method returns
    * value -1;
    *
    * @param toAsk what coordinate to prompt for
    * @param grid the Board where the coordinate should fit
    * @return the int value given by the user
    */ 
    public static int askCoordinate(char toAsk, Board grid) {
        int place = 0;
        System.out.println("Anna " +toAsk +"-koordinaatti siirrollesi:");
        if(input.hasNextInt()) {
            place = input.nextInt();
            input.nextLine();
            if (toAsk == 'X') {
                if (place<1 || place > grid.getWidth() +1) {
                    place = 0;
                    System.out.println("Koordinaatti ei ole laudan sisällä!");
                }
            }
            else {
                if (place<1 || place > grid.getHeight() +1) {
                    place = 0;
                    System.out.println("Koordinaatti ei ole laudan sisällä!");
                }
            }
        }
        else {
            input.nextLine();
            if (!askContinue("Haluatko jatkaa peliä?")) {
                System.exit(0);
            }
        }
        return place-1;
    }
    
}