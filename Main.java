public class Main {

    private static void AgentAgent ()
    {
        // choose number of games or amount of time; change control on line 20/21
		int numberOfGames = 100;
        int numMins = 5;
        final long numberOfMinutes = 1000l*1000*1000*60*numMins;
        long startTime = System.nanoTime();

        Board board = new Board();
        Agent agent = new Agent(board, 1);
        Agent agent2 = new Agent(board, 2);
        int tie = 0;

        Boolean gameover = false;

        int i = 0;
        // uncomment line 20 to run until time runs out; uncomment line 21 to run a set number of games
		// for (; (System.nanoTime()-startTime) < numberOfMinutes; i++) // time limit
		for (; i < numberOfGames; i++) // game count limit
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

        // uncomment line for desired gameplay
        // AgentAgent(); // play agent vs agent
        // AgentHuman(); // play agent (p1) vs human (p2)
        HumanAgent(); // play human (p1) vs agent (p2)
    }

}
