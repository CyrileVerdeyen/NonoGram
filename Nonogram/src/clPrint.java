
public class clPrint {
    
    static void print(int board[][], int rowRestrictions[][], int columnRestrictions[][], boolean complete){ //prints board, still need to find best way to print restrictions
        if(complete){
            System.out.println(" \n The solved puzzle is: \n");
        }
        
        for (int i = 0; i < board.length; i++) {   
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] == 1){
                    System.out.print(" # ");    
                } else if (board[i][j] == 2){
                    System.out.print("   ");
                } else {
                    System.out.print("   "); 
                }
                
            }
            System.out.print("  ");
            for (int j = 0; j < rowRestrictions[i].length; j++){
                if(rowRestrictions[i][j] == 0 && j != 0){
                    System.out.print("  ");
                } else {
                    System.out.print(" " + rowRestrictions[i][j]);
                }
            }
            
        System.out.println();
    } 
        
        System.out.println();
        
        for(int j = 0; j < Math.ceil(board.length/2); j++){
            for(int i = 0; i < columnRestrictions.length; i++){
                if(columnRestrictions[i].length > j){
                    if(columnRestrictions[i][j] == 0 && j != 0){
                        System.out.print("    ");
                    } else {
                        System.out.print(" " + columnRestrictions[i][j] + " ");
                    }
                } else {
                    System.out.print("   ");
                }
                
            }
            System.out.println();
        }
        
    }
    
    static void print(int board[][], int rowRestrictions[][], int columnRestrictions[][]){
        print(board, rowRestrictions, columnRestrictions, false);
    }
}
