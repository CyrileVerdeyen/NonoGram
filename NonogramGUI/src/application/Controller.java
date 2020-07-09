package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {
    
    int[][] board;
    int[][] rowRestrictions;
    int[][] columnRestrictions;
    boolean loaded = false;
    List<int[][]> boards = null;
    int count = 0;
    boolean solvePressed = false;
    boolean completed = false;

    @FXML
    private AnchorPane Main;
    
    @FXML
    private Button load;
    
    @FXML
    private Button loadStep;

    @FXML
    private GridPane gameGrid;
    
    @FXML
    private GridPane gameGridStep;
    
    @FXML
    private Button solve;
    
    @FXML
    private Button solveStep;
    
    @FXML
    private Button previousStep;
    
    @FXML
    private ComboBox<String> SelectPuzzle;
    
    @FXML
    private ComboBox<String> SelectPuzzleStep;
    
    
    void loadExamplesList(){

        File examples = new File("./src/examples/");
        File[] exampleList = examples.listFiles();
            
        for (File ex : exampleList) {
            
            String fileName = new String(); //setting name
            fileName = (ex.getName().substring(0, ex.getName().length() - 4));
            
            SelectPuzzle.getItems().add(fileName);
            SelectPuzzleStep.getItems().add(fileName);
        }    
    }
    
    @FXML
    public void initialize(){
        loadExamplesList();
    }
    
    @FXML
    void loadButton(ActionEvent event) {
        if(SelectPuzzle.getValue() != null)
            load(gameGrid, SelectPuzzle.getValue());
    
    }
    
    @FXML
    void loadButtonStep(ActionEvent event) {
        if(SelectPuzzleStep.getValue() != null){
            load(gameGridStep, SelectPuzzleStep.getValue());
            solvePressed = false;
            solveStep.setText("Solve");
            completed = false;
            previousStep.setVisible(false);
        }
    }
    

    @FXML
    void solve(ActionEvent event) {
        solve(gameGrid, false);
    }
    
    @FXML
    void solveStep(ActionEvent event) {
        if(!solvePressed){
                solve(gameGridStep, true);
            if(loaded){
                solveStep.setText("Next step");
                previousStep.setVisible(true);
                solvePressed = true;
            }
        }
        else {
            nextStep(true);
        }
    }
    
    @FXML
    void previousStep(ActionEvent event) {
            nextStep(false);
    }
    
    void load(GridPane pane, String file){
        
        String puzzleLocation = "./src/examples/" + file + ".txt";
        System.out.println(puzzleLocation);
        File puzzle = new File (puzzleLocation);
        
        try (BufferedReader br = new BufferedReader(new FileReader(puzzle))) {
            
            int rows = Integer.parseInt(br.readLine());
            int columns = Integer.parseInt(br.readLine());
            
            board = new int [rows][columns]; //making board
            
            rowRestrictions = new int [rows][]; //allocating the array for row exceptions
            columnRestrictions = new int [columns][]; //allocating the array for column exceptions
            
            for (int i = 0; i < rows; i++){
                rowRestrictions[i] = inputUtils.parameters(br.readLine()); //getting the restrictions for each row
            }
            
            for (int i = 0; i < columns; i++){
                columnRestrictions[i] = inputUtils.parameters(br.readLine()); //getting the restrictions for each column
            }
            
            Node node = pane.getChildren().get(0); //clearing grid
            pane.getChildren().clear();
            pane.getChildren().add(0,node);
            
            Text t = new Text("    " + file);
            
            pane.add(t, 0, 0); //print name of puzzle
            
            GridPane rowRestrictionsGrid = new GridPane();
            GridPane columnRestrictionsGrid = new GridPane();
            
            rowRestrictionsGrid.setGridLinesVisible(true);
            columnRestrictionsGrid.setGridLinesVisible(true);
            
            int maxRowRestrictions = 0;
            int maxColumnRestrictions = 0;
            
            for(int i = 0; i < board.length; i++){
                if (rowRestrictions[i].length > maxRowRestrictions){
                    maxRowRestrictions = rowRestrictions[i].length;
                }
            }
            
            for(int i = 0; i < board[0].length; i++){
                if (columnRestrictions[i].length > maxColumnRestrictions){
                    maxColumnRestrictions = columnRestrictions[i].length;
                }
            }
            
            for(int i = 0; i < board.length; i++){ //This for loop creates the row restrictions and adds them to the row restriction gird
                int leadingSpaces = maxRowRestrictions - rowRestrictions[i].length;
                
                int j = 0;
                
                for(int k = 0; k < leadingSpaces; k++){
                    Text blanck = new Text(" ");
                    rowRestrictionsGrid.add(blanck, j, i);
                    j++;
                }
                
                for(int k = 0; k < rowRestrictions[i].length; k++){
                    Text restriction = new Text(String.valueOf(rowRestrictions[i][k]));
                    rowRestrictionsGrid.add(restriction, j, i);
                    j++;
                }
                    
            }
            
            for(int i = 0; i < board[0].length; i++){ //This for loop creates the column restrictions and adds them to the column restriction gird
                int leadingSpaces = maxColumnRestrictions - columnRestrictions[i].length;
                
                int j = 0;
                
                for(int k = 0; k < leadingSpaces; k++){
                    Text blanck = new Text(" ");
                    columnRestrictionsGrid.add(blanck, i, j);
                    j++;
                }
                
                for(int k = 0; k < columnRestrictions[i].length; k++){
                    Text restriction = new Text(String.valueOf(columnRestrictions[i][k]));
                    columnRestrictionsGrid.add(restriction, i, j);
                    j++;
                }
                    
            }
            
            
            for (int i = 0; i < maxRowRestrictions; i++) { //Setting the width of the column dynamically of Row Restrictions
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / maxRowRestrictions);
                colConst.setHalignment(HPos.CENTER);
                rowRestrictionsGrid.getColumnConstraints().add(colConst);
            }
            
            for (int i = 0; i < board.length; i++) { //Setting the height of the column dynamically of Row Restrictions
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / board.length);
                rowRestrictionsGrid.getRowConstraints().add(rowConst);
            }
            
            for (int i = 0; i < board[0].length; i++) { //Setting the width of the column dynamically of column Restrictions
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / board[0].length);
                colConst.setHalignment(HPos.CENTER);
                columnRestrictionsGrid.getColumnConstraints().add(colConst);
            }
            
            for (int i = 0; i < maxColumnRestrictions; i++) { //Setting the height of the column dynamically of column Restrictions
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / maxColumnRestrictions);
                columnRestrictionsGrid.getRowConstraints().add(rowConst);
            }
            
            pane.add(rowRestrictionsGrid, 0, 1); //row restrictions
            pane.add(columnRestrictionsGrid, 1, 0); //column restrictions
            
            loaded = true;
            
        } 
        
        catch (IOException e) {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("This is not a file in the examples folder");
   
            alert.showAndWait();
            
            e.printStackTrace();
        }
    
    }
    
    void solve (GridPane pane, Boolean step) {
        if(loaded){
            
            GridPane game = new GridPane();
            final int numCols = board.length;
            final int numRows = board[0].length;
            
            for (int i = 0; i < numCols; i++) { //Setting the width of the column dynamically
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / numRows);
                game.getColumnConstraints().add(colConst);
            }
            
            for (int i = 0; i < numRows; i++) { //Setting the height of the column dynamically
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / numCols);
                game.getRowConstraints().add(rowConst);
            }
            
            game.setGridLinesVisible(true); //makes it look more like game that is known
            
            if(!step){
                
                board = solver.solver(board, rowRestrictions, columnRestrictions); //this runs the solver
                
                for (int j = 0; j < numCols; j++) { //going through board and placing answers on grid
                    for (int i = 0; i < numRows; i++) {
                        if(board[j][i] == (1) || board[j][i] == (3)) {
                            
                            Rectangle r = new Rectangle(((pane.getColumnConstraints().get(1).getPrefWidth()/ numRows)-1),
                                    ((pane.getRowConstraints().get(1).getPrefHeight()/ numCols)-1)); //These fill the squares of the grid no matter the size of the grid
                            r.setFill(Color.BLACK);
                            
                            game.add(r, i, j);
                        }
                    }
                }
                
                pane.add(game, 1, 1); //print game
                
            } else {
                
                count = 0;
                boards = solver.solverStep(board, rowRestrictions, columnRestrictions);
                
                pane.add(game, 1, 1); //print game
                
            }
            
            
        } else {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("There is no puzzle loaded!");

            alert.showAndWait();
        }
    }
    
    void nextStep(Boolean step){
        if(step){
            if(!completed){
                
                stepSolve(step);
                
            } else {
            
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Done");
                alert.setHeaderText("Done");
                alert.setContentText("The puzzle is comepleted!");

                alert.showAndWait();
            }
        } else {
            stepSolve(step);
        }
    }
    
    //Gotten from: https://www.programcreek.com/java-api-examples/?class=javafx.scene.layout.GridPane&method=getChildren - Then modified
    private GridPane removeCellFromGridPane(int row, int col, GridPane gridPane ) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : gridPane.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);
            
            Integer colIndex = GridPane.getColumnIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;
            int c = colIndex == null ? 0 : colIndex;

            if (r == row) {
                // collect matching rows for deletion
                if (c == col){
                    deleteNodes.add(child);
                }
            }
        }
        gridPane.getChildren().removeAll(deleteNodes);
        return gridPane;
    }
    
    private void stepSolve(Boolean next){
        GridPane pane = gameGridStep;
        
        GridPane game = new GridPane();
        final int numCols = board.length;
        final int numRows = board[0].length;
        
        for (int i = 0; i < numCols; i++) { //Setting the width of the column dynamically
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numRows);
            game.getColumnConstraints().add(colConst);
        }
        
        for (int i = 0; i < numRows; i++) { //Setting the height of the column dynamically
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numCols);
            game.getRowConstraints().add(rowConst);
        }
        
        game.setGridLinesVisible(true); //makes it look more like game that is known
        
        board = boards.get(count);
        
        removeCellFromGridPane(1, 1, pane);
        
        for (int j = 0; j < numCols; j++) { //going through board and placing answers on grid
            for (int i = 0; i < numRows; i++) {
                if(board[j][i] == (1) || board[j][i] == (3)) {
                    
                    Rectangle r = new Rectangle(((pane.getColumnConstraints().get(1).getPrefWidth()/ numRows)-1),
                            ((pane.getRowConstraints().get(1).getPrefHeight()/ numCols)-1)); //These fill the squares of the grid no matter the size of the grid
                    r.setFill(Color.BLACK);
                    
                    game.add(r, i, j);
                }
            }
        }
        
        pane.add(game, 1, 1); //print game
        
        if(next){
            if((count+1) == boards.size()){

                completed = true;
            } else {
                count ++;
            }
        } else {
            if(count != 0){
                count --;
                completed = false;
            }
        }

    }

}
