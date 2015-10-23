/**
 * An infinite tape for the turing machine. Creates and instantiates cells
 * dynamically for increased performance.
 * Created by Esteban Valle on 7 October 2015
 */
public class TuringTape {

    /**
     * A single cell in the Turing Machine.
     */
    private class TuringCell{
        public int value;
        public TuringCell left;
        public TuringCell right;

        public TuringCell(){
            this.value = 0;
        }

        public TuringCell(TuringCell left, TuringCell right, int value){
            this.left = left;
            this.right = right;
            this.value = value;
        }
    }


    private TuringCell activeCell;

    private int cellCount;

    private TuringCell leftmostCell;
    private TuringCell rightmostCell;


    /**
     * creates a TuringTape with cells initialized to 0
     */
    public TuringTape() {
        // create a new active cell
        this.activeCell = new TuringCell(null, null, 0);
        cellCount = 1;

        leftmostCell = this.activeCell;
        rightmostCell = this.activeCell;
    }

    /**
     * moves the tape head one cell to the left
     */
    public void moveLeft() {
        if (activeCell.left == null) {
            TuringCell cell = new TuringCell();
            cellCount++;

            activeCell.left = cell;
            cell.right = activeCell;
            leftmostCell = cell;
        }

        activeCell = activeCell.left;

    }

    /**
     * moves the tape head one cell to the right
     */
    public void moveRight() {
        if (activeCell.right == null) {
            TuringCell cell = new TuringCell();
            cellCount++;

            activeCell.right = cell;
            cell.left = activeCell;
            rightmostCell = cell;
        }

        activeCell = activeCell.right;
    }

    /**
     * returns the value contained by active cell
     * @return the value contained by the active cell
     */
    public int read() throws TuringTapeError{
        if (activeCell == null) throw new TuringTapeError("No active cell");
        return activeCell.value;
    }

    /**
     * writes the value to the active cell.
     * @param a
     */
    public void write(int a){
        //if (activeCell == null) throw new TuringTapeError("No active cell initialized.");

        activeCell.value = a;

    }

    /**
     * TuringTapeError is thrown when something has gone wrong with the turing tape.
     */
    public class TuringTapeError extends Exception{
        public TuringTapeError(String description){
            super(description);
        }
    }

    /**
     * Returns the count of cells on the turing tape
     * @return the count of cells on the turing tape
     */
    public int getCellCount(){
        return this.cellCount;
    }

    /**
     * Jumps to the left edge of the tape without cheating
     */
    public void jumpToLeftEdge(){
        TuringCell currentCell = activeCell;

        while (currentCell != null && currentCell.value != 0) {
            moveLeft();
        }
        moveRight(); // the loop will land us on a blank cell
    }

    /**
     * Jumps to the right edge of the tape without cheating
     */
    public void jumpToRightEdge(){
        TuringCell currentCell = activeCell;

        while (currentCell != null && currentCell.value != 0){
            moveRight();
        }
        moveLeft(); // the loop will land us on a blank cell
    }

    @Override
    public String toString(){
        String ret = "TuringTape: (" + cellCount + " cells) | - ";
        TuringCell currentCell = leftmostCell;

        while (currentCell != null){
            if (currentCell == activeCell) {
                ret += "(" + (char)currentCell.value + ")- ";
            }
            else {
                ret += (char) currentCell.value + " - ";
            }
            currentCell = currentCell.right;
        }

        return ret + "|";
    }


}


