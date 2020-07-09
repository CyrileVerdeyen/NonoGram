import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class launcher {

    public static void main(String[] args) {
        
        Scanner kb = new Scanner(System.in);
        
        System.out.println("Type 1 to write your own puzzle; Type 2 to import from file");
        int input = inputUtils.inputCheck(kb);
        
        
        if(input == 1){
            System.out.println("How many rows do you want in the problem? "); //Getting the rows for the puzzle
            int rows = inputUtils.positiveInt(kb);

            System.out.println("How many columns do you want in the problem? "); //Getting the columns for the puzzle
            int columns = inputUtils.positiveInt(kb);

            int[][] board = new int [rows][columns]; //making board
            
            int[][] rowRestrictions = new int [rows][]; //allocating the array for row exceptions
            int[][] columnRestrictions = new int [columns][]; //allocating the array for column exceptions
            
            for (int i = 0; i < rows; i++){
                System.out.println("What are the restrictions for rows " + i + " ?");
                rowRestrictions[i] = inputUtils.parameters(kb); //getting the restrictions for each row
            }
            
            for (int i = 0; i < columns; i++){
                System.out.println("What are the restrictions for column " + i + " ?");
                columnRestrictions[i] = inputUtils.parameters(kb); //getting the restrictions for each column
            }
            
            board = solver.solver(board, rowRestrictions, columnRestrictions); //this runs and prints the solved puzzle!
            
        } else if (input == 2){
            
            System.out.println("What is the name of the file?");
            String fileName = inputUtils.fileName(kb);

            try (BufferedReader br = new BufferedReader(new FileReader("src/" + fileName))) {
         
                int rows = Integer.parseInt(br.readLine());
                int columns = Integer.parseInt(br.readLine());
                
                int[][] board = new int [rows][columns]; //making board
                
                int[][] rowRestrictions = new int [rows][]; //allocating the array for row exceptions
                int[][] columnRestrictions = new int [columns][]; //allocating the array for column exceptions
                
                for (int i = 0; i < rows; i++){
                    rowRestrictions[i] = inputUtils.parameters(br.readLine()); //getting the restrictions for each row
                }
                
                for (int i = 0; i < columns; i++){
                    columnRestrictions[i] = inputUtils.parameters(br.readLine()); //getting the restrictions for each column
                }
                
                board = solver.solver(board, rowRestrictions, columnRestrictions); //this runs and prints the solved puzzle!
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
             
    }
}