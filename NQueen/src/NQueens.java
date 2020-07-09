

//Some of this code was inspired by code found at https://www.geeksforgeeks.org/n-queen-problem-backtracking-3/

import java.util.Scanner;

//This program prompts the user how many queens they want to be placed, and with that makes a game board of that size to place queens on.
//The program returns a 2d array where 1 represents a queen, and 0 represents a blank slot.
public class NQueens {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.println("How many queens do you want in the problem? ");
        
        String userInput = kb.nextLine();
        
        while (!userInput.matches("^[1-9][0-9]*$")){
            System.out.println("Please type a postive integer");
            userInput = kb.nextLine();
        } 
        
        int n = Integer.valueOf(userInput);
        solveNQueens(n);
        kb.close();

    }
    
    static void solveNQueens(int n){
        
        //Create game board
        int[][] game = new int[n][n];
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                game[row][col] = 0;
            }
        }
        
        if(solveNQRecursive(game, n, 0) == false){
            System.out.println("Game can not be solved");
        } else {
            print(game, n);
        }
    }
    
    static boolean solveNQRecursive(int game[][], int n, int col){
        
        
        //All queens placed
        if (col >= n) {
            return true;
        }
        
        for( int i = 0; i < n; i++){
            if (canPlace(game, i, col, n)){
                
                //we place the queen
                game[i][col] = 1;
                
                //move to next col recursively
                if (solveNQRecursive(game, n, col + 1) == true){
                    return true;
                }
                
                //if this does not lead to a solution via recursion, we get rid of it to allow for backtracking
                game[i][col] = 0;
            }
        }
        
        return false;
    }
    
    //This goes over the game board to check to see if the queen we currently want to place can be placed, or if there is a queen that could attack it.
    //It only needs to check left since there is nothing to the right.
    static boolean canPlace(int game[][], int row, int col, int n) 
    { 
        int i, j; 

        //This checks to see if there is anything left in the same row
        for (i = 0; i < col; i++) 
            if (game[row][i] == 1) 
                return false; 
  
        //This checks to see if there is anything diagonally left upwards
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--) 
            if (game[i][j] == 1) 
                return false; 
  
        //This checks to see if there is anything diagonally left downwards
        for (i = row, j = col; j >= 0 && i < n; i++, j--) 
            if (game[i][j] == 1) 
                return false; 
  
        //The queen is safe to place
        return true; 
    } 
    
    //Prints out the game board
    static void print(int game[][], int n){
        for (int i = 0; i < n; i++) 
        { 
            for (int j = 0; j < n; j++) 
                System.out.print(" " + game[i][j] 
                                 + " "); 
            System.out.println(); 
        } 
    }

}
