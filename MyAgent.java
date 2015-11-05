import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        if(iCanWin(iAmRed) > -1) // if my agent has a winning position
        {
            moveOnColumn(iCanWin(iAmRed)); //do the winning move
        }
        else if(theyCanWin(iAmRed) > -1) //if the opponent has a winning position
        {
            moveOnColumn(theyCanWin(iAmRed)); // do the blocking move
        }
        else
        {
            moveOnColumn(randomMove()); //do the random move
        }
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @param Color of a player.
     * @return the column that would allow the agent to win.
     */
    public int iCanWin(boolean color)
    {
        char find; //Char to find(depends on the color of a player)
        if (color) //define what symbol to find
        {
            find = 'R'; //R for red player
        }
        else
        {
            find = 'Y'; //Y for yellow player
        }
        char[][] matrix = myGame.getBoardMatrix(); //get the matrix of the game board to simplify the search of the sequences
        
        for (int i = 0; i < myGame.getColumnCount(); i++) // look at columns
        {
            for (int j = 0; j < myGame.getRowCount(); j++) // look at the rows
            {
                    if (j + 3 < myGame.getRowCount()) //not to get out af the square at the bottom. 3 is because of the sequence length
                    {
                        if('B' == matrix[j][i] && find == matrix[j + 1][i] && find == matrix[j + 2][i] && find == matrix[j + 3][i]) // search for vertical sequence "B-R-R-R" for red and "B-Y-Y-Y" for yellow
                        {
                             return i; //return column index to fill the slot
                        }
                    }
                    if (i + 3 < myGame.getColumnCount()) //not to get out af the square at the right side. 3 is because of the sequence length
                    {
                        if('B' == matrix[j][i] && find == matrix[j][i + 1] && find == matrix[j][i + 2] && find == matrix[j][i + 3] && getLowestEmptyIndex(myGame.getColumn(i)) == j) // search for horizontal sequence "B-R-R-R" for red and "B-Y-Y-Y" for yellow
                        {
                            return i; //return column index to fill the slot                           
                        }
                        else if(find == matrix[j][i] && 'B' == matrix[j][i + 1] && find == matrix[j][i + 2] && find == matrix[j][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+1)) == j) // search for horizontal sequence "R-B-R-R" for red and "Y-B-Y-Y" for yellow
                        {
                            return i+1; //return column index to fill the slot                          
                        }
                        else if(find == matrix[j][i] && find == matrix[j][i + 1] && 'B' == matrix[j][i + 2] && find == matrix[j][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+2)) == j) // search for horizontal sequence "R-R-B-R" for red and "Y-Y-B-Y" for yellow
                        {
                            return i+2; //return column index to fill the slot                           
                        }
                        else if(find == matrix[j][i] && find == matrix[j][i + 1] && find == matrix[j][i + 2] && 'B' == matrix[j][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+3)) == j) // search for horizontal sequence "R-R-R-B" for red and "Y-Y-Y-B" for yellow
                        {
                            return i+3; //return column index to fill the slot                           
                        }
                    }
                    if (i + 3 < myGame.getColumnCount() && j + 3 < myGame.getRowCount())  //not to get out af the square at the bottom and right side. 3 is because of the sequence length
                    {
                        if('B' == matrix[j][i] && find == matrix[j + 1][i + 1] && find == matrix[j + 2][i + 2] && find == matrix[j + 3][i + 3] && getLowestEmptyIndex(myGame.getColumn(i)) == j) // search for diagonal (left to right) sequence "B-R-R-R" for red and "B-Y-Y-Y" for yellow
                        {
                            return i; //return column index to fill the slot
                        }
                        else if(find == matrix[j][i] && 'B' == matrix[j + 1][i + 1] && find == matrix[j + 2][i + 2] && find == matrix[j + 3][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+1)) == j+1) // search for diagonal (left to right) sequence "R-B-R-R" for red and "Y-B-Y-Y" for yellow
                        {
                            return i+1; //return column index to fill the slot                           
                        }
                        else if(find == matrix[j][i] && find == matrix[j + 1][i + 1] && 'B' == matrix[j + 2][i + 2] && find == matrix[j + 3][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+2)) == j+2) // search for diagonal (left to right) sequence "R-R-B-R" for red and "Y-Y-B-Y" for yellow
                        {
                            return i+2; //return column index to fill the slot                          
                        }
                        else if(find == matrix[j][i] && find == matrix[j + 1][i + 1] && find == matrix[j + 2][i + 2] && 'B' == matrix[j + 3][i + 3] && getLowestEmptyIndex(myGame.getColumn(i+3)) == j+3) // search for diagonal (left to right) sequence "R-R-R-B" for red and "Y-Y-Y-B" for yellow
                        {
                            return i+3; //return column index to fill the slot                           
                        }
                    }
                    if (i > 2 && j + 3 < myGame.getRowCount()) //not to get out af the square at the bottom and left side. 3 is because of the sequence length. i > 2, so i-th minimum value is 3.
                    {
                        if ('B' == matrix[j][i] && find == matrix[j + 1][i - 1] && find == matrix[j + 2][i - 2] && find == matrix[j + 3][i - 3] && getLowestEmptyIndex(myGame.getColumn(i)) == j) // search for diagonal (right to left) sequence "B-R-R-R" for red and "B-Y-Y-Y" for yellow
                        {
                            return i; //return column index to fill the slot                          
                        }
                        else if(find == matrix[j][i] && 'B' == matrix[j + 1][i - 1] && find == matrix[j + 2][i - 2] && find == matrix[j + 3][i - 3] && getLowestEmptyIndex(myGame.getColumn(i-1)) == j+1) // search for diagonal (right to left) sequence "R-B-R-R" for red and "Y-B-Y-Y" for yellow
                        {
                            return i-1; //return column index to fill the slot                           
                        }
                        else if(find == matrix[j][i] && find == matrix[j + 1][i - 1] && 'B' == matrix[j + 2][i - 2] && find == matrix[j + 3][i - 3] && getLowestEmptyIndex(myGame.getColumn(i-2)) == j+2) // search for diagonal (right to left) sequence "R-R-B-R" for red and "Y-Y-B-Y" for yellow
                        {
                            return i-2; //return column index to fill the slot                        
                        }
                        else if(find == matrix[j][i] && find == matrix[j + 1][i - 1] && find == matrix[j + 2][i - 2] && 'B' == matrix[j + 3][i - 3] && getLowestEmptyIndex(myGame.getColumn(i-3)) == j+3) // search for diagonal (right to left) sequence "R-R-R-B" for red and "Y-Y-Y-B" for yellow
                        {
                            return i-3; //return column index to fill the slot
                        }
                    }
                
            }
        }

        return -1; //return -1 if step is not found
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin(boolean color)
    {
        return iCanWin(!color); //search for a winning column of the opponent
    }

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "Eugene";
    }
}
