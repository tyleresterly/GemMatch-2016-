/*************************************/
/* Tyler Esterly                     */
/* CS251                             */
/* This class describes a GemManager */
/* object, which essentially keeps   */
/* track of the various bookkeeping  */
/* required for a class "Bejewled"   */
/* style game                        */
/*************************************/

import java.awt.Color;
import java.util.*;




public class GemManager {

    Random randGem = new Random();

    //Boolean that tells checkMatch() not to clear and cascade gems
    //at the beginning of the game, and instead jsut replaces them
    //with a non-match.
    boolean startGame;
    int rows, columns, numGems, score, gemRemoval, clearedIndex;

    // 2D array that represents the gameBoard
    Gem[][] gameBoard;
    /*arrays that keep track of the horizontal and vertical
    * coordinates that are being removes (mostly used to fit
    * Brooke's formatting for the GemManagerTest)*/
    int[] tempHorizontalCoords = new int[100];
    int[] tempVerticalCoords = new int[100];
    /*Arrays that keep track of the coordinates of gems
    * that need to be cleared and the ones that
    * need to be cascaded downward*/
    int[] clearedCoords = new int[500];

    // List containing all possible gems
    LinkedList<Gem> gemSet = new LinkedList<>();
    LinkedList<Gem> playSet = new LinkedList<>();

    /* List that holds Lists that contain the x and y coordinates
    * of all the areas that might contain a match*/
    LinkedList <LinkedList<Integer> > matchCoords = new LinkedList<>();


    public GemManager(int columns, int rows){
        score = 0;
        this.rows = rows;
        this.columns = columns;
        gameBoard = new Gem[rows][columns];
        /*TODO
         make more gems and have them added to the playlist as the game goes on*/
        Gem redGem = new Gem('@', Color.red);
        Gem orangeGem = new Gem('.', Color.orange);
        Gem pinkGem = new Gem('!', Color.PINK);
        Gem cyanGem = new Gem('*', Color.cyan);
        Gem whiteGem = new Gem(',', Color.white);
        Gem greenGem = new Gem('?', Color.green);
        Gem magentaGem = new Gem('O', Color.MAGENTA);
        numGems = 4;
        fillBoard();
    }
    /*Class that contains the character representation and color of a gem
    * TODO
    * if I have time to add special gems with special powers this class might
    * act as a parent class for them*/
    public class Gem {
        char gemChar;
        Color gemColor;

        public Gem(char character, Color color){
            gemChar = character;
            gemColor = color;
            playSet.add(this);
        }
    }
    /*Swaps two gems, adds their coordinate to the list of coordinates to
    * check for matches, and then actually checks for a match*/
    void swapGems(int rowOne, int columnOne, int rowTwo, int columnTwo){
        int[] gemCoords = new int[]{rowOne, columnOne, rowTwo,columnTwo};

        // put the two coordinates in a list and add them to the list of coordinates
        // to check for matches
        LinkedList<Integer> matchCoordsOne = new LinkedList<>();
        LinkedList<Integer> matchCoordsTwo = new LinkedList<>();
        matchCoordsOne.add(rowOne);
        matchCoordsOne.add(columnOne);
        matchCoordsTwo.add(rowTwo);
        matchCoordsTwo.add(columnTwo);
        //actually swap the two gems now
        if (isLegal(gemCoords)) {
            Gem tempGem = gameBoard[rowOne][columnOne];
            gameBoard[rowOne][columnOne] = gameBoard[rowTwo][columnTwo];
            gameBoard[rowTwo][columnTwo] = tempGem;
            matchCoords.add(matchCoordsOne);
            matchCoords.add(matchCoordsTwo);
        }
    }

/*isLegal was meant to be used to make sure a swap was legal, but for the purposes
* of testing the manager it was never used so I commented it out.*/


    boolean isLegal(int[] gemCoords){
        //Makes sure the two rows are within one square of eachother
        if ((gemCoords[0] - gemCoords[2]) == 1 || (gemCoords[0] - gemCoords[2]) == -1 ||
                ( gemCoords[0] - gemCoords[2]) == 0){
            if ((gemCoords[1] - gemCoords[3]) == 1 || (gemCoords[1] - gemCoords[3]) == -1 ||
                    (gemCoords[1] - gemCoords[3]) == 0){
                return true;
            }
        }
        //Makes sure the two collumns are within one square of eachother

        return false;
    }


    /*Fills board with gems and makes sure it doesn't automatically generate a match*/

    void fillBoard() {
        //checkMatch() will know not to clear and cascade gems if it
        //detects a match at the start of the game
        startGame = true;
        int gemNum;
        int i = 0;
        int j = 0;
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                LinkedList<Integer> checkCoords = new LinkedList<>();
                checkCoords.add(0,i);
                checkCoords.add(1,j);
                matchCoords.add(checkCoords);
                gemNum = randGem.nextInt(numGems);
                gameBoard[i][j] = playSet.get(gemNum);
                while (checkMatch()) {
                    gemNum = randGem.nextInt(numGems);
                    gameBoard[i][j] = playSet.get(gemNum);
                }
            }
        }
        startGame = false;
    }


    public String toString(){
        String boardString = "";
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if (gameBoard[i][j] == null){
                    boardString += " ";
                }
                else
                boardString += (gameBoard[i][j].gemChar);
            }
            boardString += "\n";
        }
        return boardString;
    }

    /*Method loops through the master list of coordinates
    * of possible matches and checks to see if a match exists.*/
    /*TODO
    * possibly make it illegal to make a swap that does not result in a match*/
    boolean checkMatch() {
        boolean matchFlag = false;
        int x;
        int y;
        int tempHorizontalCoordsIndex = 0;
        int tempVerticalCoordsIndex = 0;
        clearedIndex = 0;
        gemRemoval = 0;
        LinkedList<LinkedList<Integer>> tempMatchCoords = (LinkedList) matchCoords.clone();
        Arrays.fill(tempHorizontalCoords, -1);
        Arrays.fill(tempVerticalCoords, -1);
        Arrays.fill(clearedCoords, -1);
        for (LinkedList<Integer> coordList: tempMatchCoords ){
            int horizontalCount = 0;
            int verticalCount = 0;

            x = coordList.get(1);
            y = coordList.get(0);

            // Check to the right of the swapped gem
            for (int i = x; i < columns; i++){
                if (gameBoard[y][i] != null &&
                        (gameBoard[y][i].gemColor.equals(gameBoard[y][x].gemColor)) ){
                    horizontalCount++;
                    tempHorizontalCoords[tempHorizontalCoordsIndex] = i;
                    tempHorizontalCoords[tempHorizontalCoordsIndex + 1] = y;
                    tempHorizontalCoordsIndex += 2;
                }
                else break;

            }


            // Check to the left
            for (int i = (x - 1); i >= 0; i--){
                if (gameBoard[y][i] != null &&
                        (gameBoard[y][i].gemColor.equals(gameBoard[y][x].gemColor))){
                    horizontalCount++;
                    tempHorizontalCoords[tempHorizontalCoordsIndex] = i;
                    tempHorizontalCoords[tempHorizontalCoordsIndex + 1] = y;
                    tempHorizontalCoordsIndex += 2;
                }
                else{
                    break;
                }
            }


            // Check downwards vertically
            for (int i = (y); i < rows; i++){
                if (gameBoard[i][x] != null &&
                        (gameBoard[i][x].gemColor.equals(gameBoard[y][x].gemColor)) ){
                    verticalCount++;
                    tempVerticalCoords[tempVerticalCoordsIndex] = x;
                    tempVerticalCoords[tempVerticalCoordsIndex + 1] = i;
                    tempVerticalCoordsIndex += 2;
                }
                else break;
            }


            //check upwards;
            for (int i = (y - 1); i >= 0; i--){
                if (gameBoard[i][x] != null &&
                        gameBoard[i][x].gemColor.equals(gameBoard[y][x].gemColor)){
                    verticalCount++;
                    tempVerticalCoords[tempVerticalCoordsIndex] = x;
                    tempVerticalCoords[tempVerticalCoordsIndex + 1] = i;
                    tempVerticalCoordsIndex += 2;
                }
                else break;

                }
            // Check if there was a horizontal match
            if (horizontalCount >= 3){
                // if we are building the starting board, we don't need to clear matches
                if (!startGame) {
                    for (int i = 0; tempHorizontalCoords[i] != -1; i++){
                        clearedCoords[clearedIndex] = tempHorizontalCoords[i];
                        clearedIndex++;
                    }
                }
                    gemRemoval += horizontalCount;
                matchFlag = true;
            }

            // Check if there was a vertical match
            else if (verticalCount >= 3){
                // if we are building the starting board, we don't need to clear matches
                if (!startGame) {
                    for (int i = 0; tempVerticalCoords[i] != -1; i++){
                        clearedCoords[clearedIndex] = tempVerticalCoords[i];
                        clearedIndex++;
                    }
                }
                gemRemoval += verticalCount;
                matchFlag = true;
            }

            // There was no match, remove these coordinates from the master list.
            else
                matchCoords.remove(coordList);


            //Reset bookkeeping variables for the next coordinates
            tempHorizontalCoordsIndex = 0;
            tempVerticalCoordsIndex = 0;
            Arrays.fill(tempHorizontalCoords, -1);
            Arrays.fill(tempVerticalCoords, -1);
            }
        return matchFlag;

        }

    /*Clears matched gems and cascades gems downwards.
    * Returns: Total gems cleared in this specific match*/
    void clear() {
        // Clear the matched gems
        for (int i = 0; clearedCoords[i] != -1; i += 2) {
            int clearX = clearedCoords[i];
            int clearY = clearedCoords[i + 1];
            gameBoard[clearY][clearX] = null;
            score++;

        }
    }

    void cascade () {
        int pullDownCoord;
        for (int i = (columns - 1); i >= 0; i--){
            for (int j = (rows - 1); j >= 0; j--){
                if (gameBoard[j][i] == null){
                    pullDownCoord = j;
                    while (gameBoard[pullDownCoord][i] == null && pullDownCoord > 0){
                        pullDownCoord--;
                    }
                    if (pullDownCoord == 0 && gameBoard[pullDownCoord][i] == null){
                        int gemNum = randGem.nextInt(numGems);
                        gameBoard[j][i] = playSet.get(gemNum);
                    }
                    else {
                        gameBoard[j][i] = gameBoard[pullDownCoord][i];
                        gameBoard[pullDownCoord][i] = null;
                    }
                    LinkedList<Integer> newCoords = new LinkedList<>();
                    newCoords.add(j);
                    newCoords.add(i);
                    LinkedList<Integer> tempNewCoords = (LinkedList) newCoords.clone();
                    matchCoords.add(tempNewCoords);
                    newCoords.clear();

                }
            }
        }
    }
}


