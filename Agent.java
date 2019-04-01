import java.util.*;

public class Agent {

    // private HashMap<String, String> hashmap;
    public String boardstate;
    private Stack<String> stack;
    public int playerNumber;
    private int start = 0;
    private int end = 5;
    private int store = 6;
    public int win = 0;
    public int winCount = 0;
    private double adjust = -0.70;

    // constructor
    public Agent (Board board, int playerNum)
    {
        this.stack = new Stack<String>();
        this.playerNumber = playerNum;

        if (this.playerNumber == 2)
        {
            this.start += 7;
            this.end += 7;
            this.store += 7;
        }

        // save board state to variable and stack
        boardState(board);
    }

    // save board state to variable
    private void boardState (Board board)
    {
        this.boardstate = "";

        // take each index, add number to string
        if (this.playerNumber == 1)
        {
            for (int hole : board.board_holes)
                this.boardstate += hole;
        }
        else if (this.playerNumber == 2)
        {
            for (int i = 7; i != 6; i++)
            {
                if (i > 13)
                    i = 0;
                this.boardstate += board.board_holes[i];
            }
            this.boardstate += board.board_holes[6];
        }
    }

    public Boolean agentTurn (Board board)
    {
        Boolean anotherTurn = true;

        while (anotherTurn)
        {
            // save board state to variable and stack
            boardState(board);

            String value = board.hashmap.get(this.boardstate);
            // if there's no data to be found on this boardstate
            if (value == null)
            {
                value = "";
                int p;

                int count = 0;
                // see how many holes are empty
                for (int i = this.start; i <= this.end; i++)
                {
                    if (board.board_holes[i] != 0)
                        count++;
                }
                p = 100/count;

                // if 100 didn't divide evenly and there's a remainder
                int u = 100 - (p*count);

                // giving probs the starting probabilities for each hole
                for (int i = this.start; i <= this.end; i++)
                {
                    if (board.board_holes[i] != 0)
                    {
                        if (u-- > 0)
                            value += (p + 1);
                        else
                            value += p;
                    }
                    else // 0 chance of playing from empty hole
                        value += 0;

                    if (i != this.end)
                        value += ",";
                }
                board.hashmap.put(this.boardstate, value);
            }

            // split probabilities into array for easy access
            String[] probabilities = value.split(",", 6);
            int[] probs = new int [6];
            // make it be an integer array
            for (int i = 0; i < 6; i++)
                probs[i] = Integer.parseInt(probabilities[i]);

            // Obtain a number between [1 - 100]
            Random rand = new Random();
            int n = rand.nextInt(100) + 1;

            // find which index in probs[] has a matching value to n, that's the move we have chosen
            int low = 0;
            int high = 0;
            int index = -1;
            while (index < 5)
            {
                index++;
                int next = probs[index];

                high += next;

                if (next == 0)
                    continue;
                else if (n > low && n <= high)
                    break;

                low = high;
            }

            // does actual move-making
            anotherTurn = board.move(index, this.playerNumber);

            // Saves move to stack
            String ind = Integer.toString(index);
            stack.push(ind);
            stack.push(this.boardstate);
        }
        return board.gameOver();
    }

    public void teachAgent (Board board)
    {
        // if won, adjust by positive amount
        if (this.win == 1)
            this.adjust = 0.70;

        // if it was a tie, skip it all
        if (this.win == 2)
        {
            killAgent();
            return;
        }

        while(!stack.empty())
        {
            // get boardstates and subsequent moves from stack
            String state = stack.pop();
            int move = Integer.parseInt(stack.pop());

            String value = board.hashmap.get(state);

            // split probabilities into array for easy access
            String[] probabilities = value.split(",", 6);
            int[] probs = new int [6];
            // make it be an integer array
            for (int i = 0; i < 6; i++)
                probs[i] = Integer.parseInt(probabilities[i]);


            // find what change is for dividing between other holes
            int remainder = (int)(probs[move] * adjust);
            // flip sign because if add to target hole, subtract other holes
            remainder *= -1;

            // see how many zeros there aren't, -1 to account for targeted hole
            int count = -1;
            for (int i : probs)
            {
                if (i != 0)
                    count++;
            }

            // if there's a remainder on remainder
			// adding together because that's the amount that needs to be added to each probability
            int rr = 0;
            if (count != 0 && remainder != 0)
                rr = (remainder % count);// + (remainder / count);

			// remainder needs to be divided between all the holes
			int divs;
			if (count != 0)
				divs = remainder/count;
			else
				divs = remainder;

            // share around necessary decreases/increases
			int allHolesZero = 0;
            for (int i = 0; i < 6; i++)
            {
				// if a probability is 100 and all other holes are empty, don't want to adjust probability of that hole being chosen. This variable saves that info for use after for loop
				if (state.charAt(i) != '0')
					allHolesZero++;

                // if it's the targeted hole or there are 0 marbles, skip
                if (i == move || probs[i] == 0 || probs[i] == 100)
                    continue;

                // actually adjusting holes
                if (remainder != 0)
                {
                    probs[i] += divs;
                    remainder -= divs;

                    // account for a remainder that would be a float value if I were using floats
                    // if remainder positive, ergo, decreasing loser increasing others
                    if (rr > 0)
                    {
                        probs[i]++;
                        rr--;
                    }
                    // if remainder negative, ergo, increasing winner decreasing others
                    else if (rr < 0)
                    {
                        probs[i]--;
                        rr++;
                    }
                }

                // if now it's less than 0
                if (probs[i] < 0)
				{
					// add different back on to remainder
					int diff = probs[i] * -1;
					remainder += diff;

					// if less than 0, fix
                    probs[i] = 0;
				}
            }

			// do actual adjusting
			if (allHolesZero > 1)
            	probs[move] += (int)(probs[move] * this.adjust);

            // if it goes over 100, fix cos 100% is max; add difference
            int over100 = probs[move] - 100;
            if (over100 > 0)
            {
                probs[move] = 100;
                rr -= over100;
            }

            // adjust adjust
            this.adjust *= 0.8;

            // return probs to hashmap
            String p = "";
            for (int i = 0; i < 6; i++)
            {
                p += probs[i];

                if (i != 5)
                    p += ",";
            }
            board.hashmap.put(state, p);
        }

        killAgent();
    }


    private void killAgent ()
    {
        // reset variables
        if (this.playerNumber == 1)
        {
            this.start = 0;
            this.end = 5;
            this.store = 6;
        }
        this.win = 0;
        this.adjust = -0.70;
    }

}
