import java.util.Scanner;

public class inputUtils {
    
    public static int positiveInt(Scanner kb){ //checks to see if integers are positive of row and column lengths

        String input = kb.nextLine();
        
        while (!input.matches("^[0-9][0-9]*$")){
            System.out.println("Please type a postive integer");
            input = kb.nextLine();
        }
        
        int positive;
        positive = Integer.valueOf(input);
        return positive;
    }
    
    public static int inputCheck(Scanner kb){ //checks to see if integers are positive of row and column lengths

        String input = kb.nextLine();
        
        while (!input.matches("^(1)|(2)$")){
            System.out.println("Please type 1 or 2");
            input = kb.nextLine();
        }
        
        int inputChoice;
        inputChoice = Integer.valueOf(input);
        return inputChoice;
    }
    
    public static String fileName(Scanner kb){ //checks to see string entered for the parameters, and then splits it into an int array
        
        String input = kb.nextLine();
        
        while (!input.matches("^.*(.txt)$")){
            System.out.println("Please type a file name example: xxx.txt ");
            input = kb.nextLine();
        }

        return input;
    }
    
    public static int[] parameters(Scanner kb){ //checks to see string entered for the parameters, and then splits it into an int array
        
        String input = kb.nextLine();
        
        while (!input.matches("^[0-9][0-9]*(\\s[1-9][0-9]*)*$")){
            System.out.println("Please type a postive integer/s seperacted by spaces");
            input = kb.nextLine();
        }
        
        String[] strArray = input.split(" "); //splitting the string into separate strings
        int[] numbers = new int[strArray.length]; 

        for(int i = 0; i < strArray.length; i++) { //parses the strings into ints
            numbers[i] = Integer.parseInt(strArray[i]);
        }

        return numbers;
    }
    
public static int[] parameters(String string){ //checks to see string entered for the parameters, and then splits it into an int array
        
        String input = string;
        
        String[] strArray = input.split(" "); //splitting the string into separate strings
        int[] numbers = new int[strArray.length]; 

        for(int i = 0; i < strArray.length; i++) { //parses the strings into ints
            numbers[i] = Integer.parseInt(strArray[i]);
        }

        return numbers;
    }
    
    
}
