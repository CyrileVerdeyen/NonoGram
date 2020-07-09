import java.util.stream.IntStream;

/* 
 * This is the class that solves the given board.
 * The array which represents the board can have 5 different values
 * 0 is unchecked
 * 1 is placed as block
 * 2 is placed as blank
 * 3 is placed known block
 * 4 is known blank  
 * 
 */

public class solver {
    
public static int partSolve(int row[],int position, int fill, int offset){ //this takes a row and a restriction, plus an offset, and returns the partially solved array
        
        for (int i = 0; i < offset; i++){ //2 is cells that have been placed as blank
            row[position + i] = 2;
        }
        
        position += offset;
        
        for (int i = 0; i < fill; i++){
            row[position + i] = 1;
        }
        
        position += fill;

        return position;

    }
    
    static boolean solveRecursive(int[][] board, int[] currentRestriction, int[][] rowRestrictions, int[][] columnRestrictions, int position){ //This is the recursive solver for the puzzle
        if (currentRestriction[0] >= board.length) { //all rows solved
            return true;
        }

        int maxOffSet = (board.length - (IntStream.of(rowRestrictions[currentRestriction[0]]).sum())+ rowRestrictions[currentRestriction[0]].length);
        
        for(int offset = 0; offset <= maxOffSet; offset++){ //checking to see if using differing offsets will allow each section to be placed
            
          //This checks to see if a group that has not already been placed before can be placed in said position
            if(canPlace(board, currentRestriction, position, rowRestrictions, columnRestrictions, offset)){
                int prevPostition = position;
                position = partSolve(board[currentRestriction[0]], position, rowRestrictions[currentRestriction[0]][currentRestriction[1]], offset); //places group and records new position
                
                if((currentRestriction[1] + 1) == rowRestrictions[currentRestriction[0]].length){
                    int[] nextRestrictions = {currentRestriction[0] + 1, 0};
                    
                    if (solveRecursive(board, nextRestrictions, rowRestrictions, columnRestrictions, 0)){
                        return true;
                    }
                } else {
                    
                    int[] nextRestrictions = {currentRestriction[0], currentRestriction[1] + 1};
                    
                    if (solveRecursive(board, nextRestrictions, rowRestrictions, columnRestrictions, position)){
                        return true;
                    }
                }
                
                for(int n = 1; n < ((rowRestrictions[currentRestriction[0]][currentRestriction[1]] + 1)+ offset); n++){ //else wipe what was placed
                    if(board[currentRestriction[0]][position - n] != 3 || board[currentRestriction[0]][position - n] != 4 ){ //But don't wipe blocks that are certain
                        board[currentRestriction[0]][position - n] = 0; 
                    }
                }
                
                position = prevPostition;

            }
        }

        return false; //and return false
    
    }
    
    //this is where the actual restriction testing will happen
    static boolean canPlace(int[][] board, int[] currentRestriction, int position, int[][] rowRestrictions, int[][] columnRestrictions, int offset){ 
        
        if(((position) != (0) && offset == 0)){ //checks to see that if offset is 0, it is not the first position
            return false;
        }
        
        if(position + rowRestrictions[currentRestriction[0]][currentRestriction[1]] + offset > board[0].length){ //checks to make sure that the blocks being added don't exceed the length of the board
            return false;
        }
        
        int[][] newBoard = new int[board.length][board[0].length]; //this is the board if we were to place the blocks
        
        for(int i = 0; i < board.length; i++){
            newBoard[i] = board[i].clone();
        }
        
        partSolve(newBoard[currentRestriction[0]], position, rowRestrictions[currentRestriction[0]][currentRestriction[1]], offset);
        
        for(int i = position; i < (position + offset + rowRestrictions[currentRestriction[0]][currentRestriction[1]]); i++){
            int maxBlocksColumn = (IntStream.of(columnRestrictions[i]).sum()); //sum of the restrictions
            int blocksPlaced = 0;
            
            for(int j = 0; j < newBoard.length; j++){ //takes blocks that have been placed
                if(newBoard[j][i] == 1 || newBoard[j][i] == 3){
                    blocksPlaced ++;
                }
            }
            
            if((maxBlocksColumn - blocksPlaced) < 0){ //more blocks than allowed have been placed
                return false;
            }
            
            int rowPosition = 0;
            
            for(int j = 0; j < columnRestrictions[i].length; j++){ //Checking all the restrictions of the columns to make sure the shape it asks for is not violated
                
                int grouplength = columnRestrictions[i][j]; //length of blocks that should be placed
                int contBlocks = 0;
                
                while(rowPosition < newBoard.length && (newBoard[rowPosition][i] == 2 || newBoard[rowPosition][i] == 4)){ //going down column to find start of group
                    rowPosition ++;
                }
                
                if(rowPosition < newBoard.length && (newBoard[rowPosition][i] == 1 || newBoard[rowPosition][i] == 3)){ //we found group
                    while(rowPosition < newBoard.length && (newBoard[rowPosition][i] == 1 || newBoard[rowPosition][i] == 3)){ //go through
                        if(!(rowPosition >= newBoard.length)){
                            grouplength --;
                            rowPosition++;
                            contBlocks++;
                        } else { //The final blocks have been placed and the current restriction has not been completed yet
                            return false;
                        }

                        if(grouplength < 0){ //there are too many blocks in a column 
                            return false;
                        }
                    }
                }
                
                if((rowPosition < newBoard.length) &&(newBoard[rowPosition][i] == 2 || newBoard[rowPosition][i] == 4) && (contBlocks != columnRestrictions[i][j])){
                    return false;
                } 
                
            }
        }
        
        
        
        return true;
    }
    
    public static int[][] solver(int board[][], int rowRestrictions[][], int columnRestrictions[][]){ //Calls the recursive function, if it can be completed it prints it, else it says it can not be printed.
        
        int[] currentRestriction = {0 ,0 };
        if( solveRecursive(board, currentRestriction, rowRestrictions, columnRestrictions, 0) ){
            clPrint.print(board, rowRestrictions, columnRestrictions, true);
        } else {
            System.out.println("Game can not be solved");
        }
        
        return board;
    }

}
