import java.util.Scanner;

public class Main {

    private static void AgentAgent (int numberOfGames)
    {
        Board board = new Board();
        Agent agent = new Agent(board, 1);
        Agent agent2 = new Agent(board, 2);
        int tie = 0;

        Boolean gameover = false;

        int i = 0;
        // uncomment line 20 to run until time runs out; uncomment line 21 to run a set number of games
		for (int i = 0; i < numberOfGames; i++) // game count limit
		{
            gameover = false;
	        while (!gameover)
	        {
	            gameover = agent.agentTurn(board);
	            if (!gameover)
	                gameover = agent2.agentTurn(board);
	        }

			agent.teachAgent(board);
			agent2.teachAgent(board);

            int winner = board.whoWonAgent(agent, agent2);
            // for testing
			if (winner == 1)
				System.out.print("");
			else if (winner == 2)
				System.out.print("");
			else
                tie++;

            board.refreshBoard();
		}

        board.killHashmap(agent, agent2);

        System.out.println("Number of Games: " + i);
        System.out.println(agent.winCount + " " + agent2.winCount + " " + tie);
    }

    private static void AgentHuman()
    {
		Board board = new Board();
        Agent agent = new Agent(board, 1);
        Player player = new Player(agent);
        Boolean gameover = false;

        while (!gameover)
        {
            gameover = agent.agentTurn(board);
            if (!gameover)
                gameover = player.playerTurn(board);
        }

        int winner = board.whoWonHuman(agent);
        if (winner == 1)
            System.out.println("The computer wins!");
        else if (winner == 2)
            System.out.println("You win!");
        else
            System.out.println("It's a tie!");

        agent.teachAgent(board);
    }

    private static void HumanAgent()
    {
        Board board = new Board();
        Agent agent = new Agent(board, 2);
        Player player = new Player(agent);
        Boolean gameover = false;

        while (!gameover)
        {
            gameover = player.playerTurn(board);
            if (!gameover)
               gameover = agent.agentTurn(board);
        }

        int winner = board.whoWonHuman(agent);
        if (winner == 1)
            System.out.println("You win!");
        else if (winner == 2)
            System.out.println("The computer wins!");
        else
            System.out.println("It's a tie!");

        agent.teachAgent(board);
    }

    public static void main (String[] args) {

        int gamechoice;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose which game you want to run.");
        System.out.println("1. Agent vs Agent");
        System.out.println("2. Agent vs Human");
        System.out.println("3. Human vs Agent");
        gamechoice = scanner.nextInt();

        if (gamechoice == 1)
        {
            int num;
            System.out.println("How many games do you want to run? ");
            num = scanner.nextInt();
            AgentAgent(num); // play agent vs agent
        }
        else if (gamechoice == 2)
            AgentHuman(); // play agent (p1) vs human (p2)
        else if (gamechoice == 3)
            HumanAgent(); // play human (p1) vs agent (p2)
        else
            System.out.println("Please try again and enter a valid number.");
    }

}
