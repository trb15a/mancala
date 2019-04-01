import java.io.*;
import java.util.*;

public class Board {

    // p1 holes 0-5, s1 = 6, p2 holes 7-12, s2 = 13;
    public int[] board_holes = new int[14];
    public int store1 = 0;
    public int store2 = 0;
    public HashMap<String, String> hashmap;
    private int p1Win = 0;
    private int p2Win = 0;
    private String filename = "hashmap.txt";

    // constructor
    public Board () {
        // initialize game board
        refreshBoard();

        this.hashmap = new HashMap<String, String>();
        // read hashmap from file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            // get win count
            String win = reader.readLine();
            this.p1Win = Integer.parseInt(win);
            win = reader.readLine();
            this.p2Win = Integer.parseInt(win);

            // get rest of file
            String key = reader.readLine();
            String value = reader.readLine();
            while (key != null && value != null)
            {
                this.hashmap.put(key,value);

                key = reader.readLine();
                value = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBoard ()
    {
        // refresh board for new game
        for (int i = 0; i < 14; i++)
        {
            if (i == 6 || i == 13)
                this.board_holes[i] = 0;
            else
                this.board_holes[i] = 4;
        }
    }

    public void killHashmap (Agent agent, Agent agent2)
    {
        // write hashmap to file
        try {
            PrintWriter writer = new PrintWriter(filename);

            // write win count
            writer.println(agent.winCount);
            writer.println(agent2.winCount);

            // write hashmap
            for (String key : this.hashmap.keySet())
            {
                writer.println(key);
                writer.println(this.hashmap.get(key));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // called when a player makes a move
    public Boolean move (int holeNumber, int whoseTurn)
    {
        if (whoseTurn == 2)
            holeNumber += 7;

        int marbles = this.board_holes[holeNumber];
        this.board_holes[holeNumber] = 0;
        for (; marbles > 0; marbles--)
        {
            // increments holeNumber and wraps
            holeNumber++;
            if (holeNumber > 13) ////////////////////////////////////changed this from 12
                holeNumber = 0;

            // skip opponent's store
            if ((whoseTurn == 1 && holeNumber == 13) || (whoseTurn == 2 && holeNumber == 6))
            {
                // replace one already decremented
                marbles++;
                continue;
            }

            this.board_holes[holeNumber]++;
        }

        // update store variables;
        this.store1 = this.board_holes[6];
        this.store2 = this.board_holes[13];

        if ((holeNumber == 6 || holeNumber == 13) && !this.gameOver())
            return true;
        return false;
    }

    // Game is over when all 6 holes on one side of the board is empty
    public Boolean gameOver()
    {
        Boolean p1Empty = true;
        Boolean p2Empty = true;

        // check player 1's holes
        for (int i = 0; i < 6; i++)
        {
            // if there are any marbles at all
            if (this.board_holes[i] != 0)
                p1Empty = false;
        }
        // check player 2's holes
        for (int i = 7; i < 13; i++)
        {
            // if there are any marbles at all
            if (this.board_holes[i] != 0)
                p2Empty = false;
        }

        return (p1Empty || p2Empty);
    }

    // for agent vs agent
    public int whoWonAgent (Agent agent, Agent agent2)
    {
        // player 1 wins
        if (this.board_holes[6] > this.board_holes[13])
        {
            if (agent.playerNumber == 1) // agent 1 wins
            {
                agent.win = 1;
                agent2.win = 0;
                p1Win++;
                agent.winCount = p1Win;
            }
			else if (agent2.playerNumber == 1) // agent 2 wins
			{
                agent.win = 0;
				agent2.win = 1;
                p2Win++;
                agent2.winCount = p2Win;
            }
            return 1;
        }
        // player 2 wins
        else if (this.board_holes[6] < this.board_holes[13])
        {
            if (agent2.playerNumber == 2) // agent 2 wins
            {
                agent.win = 0;
				agent2.win = 1;
                p2Win++;
                agent2.winCount = p2Win;
            }
			else if (agent.playerNumber == 1) // agent 1 wins
            {
                agent.win = 1;
                agent2.win = 0;
                p1Win++;
                agent.winCount = p1Win;
            }
            return 2;
        }
        // tie
        else
        {
            agent.win = 2;
            return 3;
        }
    }

    // for at least 1 human player
    public int whoWonHuman (Agent agent)
    {
        // player 1 wins
        if (this.board_holes[6] > this.board_holes[13])
        {
            if (agent.playerNumber == 1) // agent wins as p1
            {
                agent.win = 1;
                p1Win++;
                agent.winCount = p1Win;
            }
			else if (agent.playerNumber == 2) // agent loses as p1
			{
                agent.win = 0;
            }
            return 1;
        }
        // player 2 wins
        else if (this.board_holes[6] < this.board_holes[13])
        {
            if (agent.playerNumber == 2) // agent wins as p2
            {
                agent.win = 1;
                p1Win++;
                agent.winCount = p1Win;
            }
			else if (agent.playerNumber == 1) // agent loses as p1
            {
                agent.win = 0;
            }
            return 2;
        }
        // tie
        else
        {
            agent.win = 2;
            return 3;
        }
    }
}
