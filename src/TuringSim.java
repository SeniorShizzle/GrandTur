/**
 * GrandTur™ Turing Simulator - Love life. Love Turing.
 * Created by Esteban Valle on 7 October 2015
 */
public class TuringSim {

    private String grandTurLogo =
            "   _____                     _ _______         \n" +
            "  / ____|                   | |__   __|        \n" +
            " | |  __ _ __ __ _ _ __   __| |  | |_   _ _ __ \n" +
            " | | |_ | '__/ _` | '_ \\ / _` |  | | | | | '__|TM\n" +
            " | |__| | | | (_| | | | | (_| |  | | |_| | |   \n" +
            "  \\_____|_|  \\__,_|_| |_|\\__,_|  |_|\\__,_|_|   \n" +
            "                                               \n";


    public enum TuringState {
        HALT_ACCEPTED, HALT_REJECTED, NOT_STARTED, ERROR, NOT_WRITTEN,

        SeekingLeft,
        SeekingRight,

        SeekingNewRight,
        SeekingNewLeft,

        SeekingUnvisitedRight,

        SeekingALeft,
        SeekingBLeft,

        SeekingaRight,
        SeekingbRight,

        MoveOneRight,
        MoveOneLeft,

        FOUND_MIDDLE,

    }

    private boolean hasWrittenTape = false;

    /**
     * The main turing tape.
     */
    public TuringTape tape;

    /**
     * The current TuringState of the machine
     */
    private TuringState currentState = TuringState.NOT_STARTED;

    /**
     * The number of movements the machine has executed
     */
    private long iterations;

    /**
     * If the Turing machine should be verbose. Follow along!
     */
    public boolean verbose = false;

    /**
     * Creates a new TuringSim - GrandTur™
     */
    TuringSim(){
        System.out.println("\n\n\nWelcome to\n\n\n" + grandTurLogo + "\n\n\n");
        tape = new TuringTape();
        currentState = TuringState.NOT_WRITTEN;
        iterations = 0;
    }

    /**
     * Writes an input string to a TuringTape
     * @param vals the array of characters to write
     */
    void writeInput(char[] vals) throws UnsupportedOperationException, NullPointerException{
        if (hasWrittenTape) throw new UnsupportedOperationException("Tape already written.");
        if (vals == null) throw new NullPointerException("Write string is null.");

        for (int i = 0; i < vals.length; i++){
            char c = vals[i];

            if (c != 'a' && c != 'b') {
                // the machine accepts only a's and b's
                throw new UnsupportedOperationException("Illegal character entered to tape.");
            }

            try {
                tape.write(c);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                //TODO: Exit
            } finally {
                if (i < vals.length - 1) tape.moveRight(); // move the tape to the right
            }
        }

        hasWrittenTape = true;


    }

    /**
     * Returns TRUE if the machine accepts the given string
     * @return TRUE if the machine accepts the given string
     */
    boolean accepts() throws IllegalStateException{
        if (!hasWrittenTape) throw new IllegalStateException("Tape has not been written.");

        currentState = TuringState.SeekingLeft;

        while (true) {
            char c = 'z';
            try {
                c = (char)tape.read();
            } catch (Exception e){
                System.out.println(e.getLocalizedMessage());
                currentState = TuringState.ERROR;
            }

            switch (currentState) {
                case HALT_ACCEPTED:
                    return true;
                case HALT_REJECTED:
                    return false;

                case SeekingLeft:
                    if (c != 0) tape.moveLeft();
                    else {
                        currentState = TuringState.MoveOneRight;
                        tape.moveRight();
                    }

                    break;
                case SeekingRight:
                    if (c != 0) tape.moveRight();
                    else {
                        currentState = TuringState.MoveOneLeft;
                        tape.moveLeft();
                    }
                    break;

                case SeekingNewLeft:
                    if (c == 'X' || c == 'Y' || c == 0) { // we've gone too far
                        currentState = TuringState.MoveOneRight;
                        tape.moveRight();
                    } else {
                        tape.moveLeft();
                    }
                    break;
                case SeekingNewRight:
                    if (c == 'x' || c == 'y' || c == 0) { // we've gone too far
                        currentState = TuringState.MoveOneLeft;
                        tape.moveLeft();
                    } else {
                        tape.moveRight();
                    }
                    break;



                case SeekingALeft:
                    // Looking left for a Z, then go right one
                    if (c == 'Z' || c == 0) {
                        currentState = TuringState.SeekingaRight;
                        tape.moveRight();
                    } else {
                        tape.moveLeft();
                    }
                    break;

                case SeekingaRight:
                    // Searching for the first input of b, if not found, not a match
                    if (c == 'X') {
                        tape.write('Z');
                        currentState = TuringState.SeekingUnvisitedRight;
                        tape.moveRight();

                    } else {
                        currentState = TuringState.HALT_REJECTED;
                        tape.moveRight();
                    }
                    break;

                case SeekingBLeft:
                    // Looking left for a Z, then go right one
                    if (c == 'Z' || c == 0) {
                        currentState = TuringState.SeekingbRight;
                        tape.moveRight();
                    } else {
                        tape.moveLeft();
                    }
                    break;

                case SeekingbRight:
                    // Searching for the first input of b, if not found, not a match
                    if (c == 'Y') {
                        tape.write('Z');
                        currentState = TuringState.SeekingUnvisitedRight;
                        tape.moveRight();

                    } else {
                        currentState = TuringState.HALT_REJECTED;
                        tape.moveRight();
                    }
                    break;

                case SeekingUnvisitedRight:
                    if (c == 0) {
                        currentState = TuringState.HALT_ACCEPTED;
                        return true;
                    }
                    if (c == 'X' || c == 'Y') tape.moveRight();
                    else if (c == 'z') {
                        // we need to go farther
                        tape.moveRight();
                    } else {
                        if (c == 'y') currentState = TuringState.SeekingBLeft;
                        if (c == 'x') currentState = TuringState.SeekingALeft;
                        tape.write('z');
                        tape.moveLeft();
                    }
                    break;

                case MoveOneLeft:
                    if (c == 'a') tape.write((int) 'x');
                    else if (c == 'b') tape.write((int) 'y');
                    else if (c == 'X' || c == 'Y') {
                        currentState = TuringState.HALT_REJECTED;
                        break;
                    }
                    currentState = TuringState.SeekingNewLeft;
                    tape.moveLeft();
                    break;

                case MoveOneRight:
                    if (c == 'a') tape.write((int)'X');
                    else if (c == 'b') tape.write((int)'Y');
                    else if (c == 'y' || c == 'x'){
                        currentState = TuringState.FOUND_MIDDLE;
                        break;
                    }
                    currentState = TuringState.SeekingNewRight;
                    tape.moveRight();
                    break;

                case ERROR:
                    return false;

                default:
                    return false;

                case FOUND_MIDDLE:
                    // It will always find the middle on the right side of the string
                    //System.out.println("WERE THERE!");
                    if (c == 'x'){
                        currentState = TuringState.SeekingALeft;
                    } else if (c == 'y') {
                        currentState = TuringState.SeekingBLeft;
                    } else {
                        // ERROR OCCURRED
                    }
                    tape.write('z'); // z is for right-half already-visited
                    tape.moveLeft();
                    break;
            }

            iterations++;
            if (verbose) System.out.println(tape.toString());
        }

    }

    /**
     * Returns the current TuringState
     * @return the TuringState value of the current process
     */
    public TuringState getCurrentState(){
        return this.currentState;
    }

    /**
     * The number of iterations the Turing Machine has executed.
     * @return the number of iterations the Turing Machine has executed.
     */
    public long getIterations(){
        return iterations;
    }


    /**
     * Returns the string representation of the turing machine.
     * @return a String containing the state of the turing tape.
     */
    @Override
    public String toString(){
        return "Turing Machine with Tape:\n\t" + tape.toString();
    }


}


/* General Turing Theory: How To Check Equal Concatenated Strings
 *
 * 1) Find the leftmost cell
 * 2) Replace a->X b->Y (set SeekingaRight or SeekingbRight)
 * 3) Search rightward to find the last cell
 *      a) SeekingaRight: if find b, halt reject
 *      b) SeekingaRight: if find a, replace with a->x b->y, set MoveLeftOne
 * 4) MoveLeftOne moves left one space, replace a->x, b->y (set SeekingALeft, SeekingBLeft)
 * 5) Search leftward to find the next capital, don't change; move right set SeekingaRight or SeekingbRight
 * 6) SeekingaRight: if a is found, replace with a->X, b->Y, set to SeekingARight
 *    SeekingaRight: if b is found, halt reject
 * 7) Move right to find next capital, don't change; move left, set SeekingaLeft or SeekingbLeft
 * 8) Repeat steps 5-7 until you're in the middle
 * 9) You know you're finished because when you move left, the first space will be a captial letter
 * 10)If you move left and haven't found a capital, you have more work to do (state for this?)
 *
 */
