import java.util.Scanner;
import java.io.*;


public class Player {

    private int start = 0;
    private int end = 5;
    private int store = 6;
    public int playerNumber = 1;
    private static Scanner scanner = new Scanner(System.in);

    // constructor
    public Player (Agent agent)
    {
        if (agent.playerNumber == 1)
        {
            this.start += 7;
            this.end += 7;
            this.store += 7;
            this.playerNumber = 2;
        }
    }

    public Boolean playerTurn (Board board)
    {
        Boolean anotherTurn = true;

        // makeAMove returns true if move ends in store
        while (anotherTurn && !board.gameOver())
        {
            this.printBoard(board);
            int holeNumber = 0;

            // choose a move, verify valid move
            Boolean validMove = false;
            while (!validMove)
            {
                System.out.print("Enter the number of the hole you want to move: ");
                holeNumber = scanner.nextInt();

                if (holeNumber > 6)
                {
                    System.out.println("Invalid choice. Please enter a number 1-6.");
                    continue;
                }

                // align holeNumber with correct index
                holeNumber--;
                // to align hole with correct index for this method
                int hole = holeNumber;
                if (this.playerNumber == 2)
                    hole += 7;

                // move marbles around board
                int marbles = board.board_holes[hole];

                // if player chose an empty hole or a store, they can try again
                if (hole == 6 || hole == 13)
                    System.out.println("Invalid choice. Please enter a number 1-6.");
                else if (marbles == 0)
                    System.out.println("There are no marbles in that hole. Choose another.");
                else
                    validMove = true;
            }

            // doing the actual move-making
            anotherTurn = board.move(holeNumber, this.playerNumber);
        }

        return board.gameOver();
    }

    // print the board for a human player to see
    public void printBoard (Board board) {

        // clears screen
		try {
	        if (System.getProperty("os.name").contains("Windows"))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else
	            Runtime.getRuntime().exec("clear");
    	} catch (IOException | InterruptedException ex) {}

        if (this.playerNumber == 2)
            System.out.println("Player 2 (you)");
        else if (this.playerNumber ==1)
            System.out.println("Player 2 (computer)");

        // print top of box
        System.out.printf("%3s", "");
        for (int i = 1; i <= 6; i++)
            System.out.printf("%4s", i);
        System.out.println();
        System.out.print("+");
        for (int i = 0; i < 31; i++)
            System.out.print("-");
        System.out.println("+");

        // player 1 marbles
        System.out.printf("|%3s", "");
        for (int i = 7; i < 13; i++)
            System.out.print("| " + board.board_holes[i] + " ");
        System.out.println("|   |");

        // print stores
        System.out.print("| " + board.board_holes[6] + " |");
        // for (int i = 0; i < 4; i++)
            System.out.printf("%23s", "");
        System.out.println("| " + board.board_holes[13] + " |");

        // player 2 marbles
        System.out.printf("|%3s", "");
        for (int i = 5; i >= 0; i--)
            System.out.print("| " + board.board_holes[i] + " ");
        System.out.println("|   |");

        // print bottom of box
        System.out.print("+");
        for (int i = 0; i < 31; i++)
            System.out.print("-");
        System.out.println("+");
        System.out.printf("%3s", "");
        for (int i = 6; i >= 1; i--)
            System.out.printf("%4s", i);

        System.out.println();

        if (this.playerNumber == 1)
            System.out.println("Player 1 (you)");
        else if (this.playerNumber == 2)
            System.out.println("Player 1 (computer)");

        System.out.println();
    }

}
