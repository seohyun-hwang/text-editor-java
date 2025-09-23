import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // general support variables (within Main class)
    static int positionIndex_withinMainArray_cursor;
    static final int positionIndex_withinMainArray_firstElementOfText = 0;
    static final int positionIndex_withinMainArray_lastElementOfText = 1;

    // DoublyLinkedList (for text-character sequencing)
    static ArrayList<Character> mainArray = new ArrayList<>();
    static ArrayList<Integer> leftwardPointer = new ArrayList<>();
    static ArrayList<Integer> rightwardPointer = new ArrayList<>();

    // support variables (for GUI classes)
    static boolean cursorGUI_shouldMoveLeft = false;
    static boolean cursorGUI_shouldMoveRight = false;
    static boolean cursorGUI_shouldAdaptToDeletion = false;

    // undo/redo
    static ArrayList<Byte> latestActionType = new ArrayList<>(); // 1: insert; -1: deletion
    static ArrayList<Integer> undoRedo_referencePointer = new ArrayList<>();
    static int undoCount = 0;

    // a stopwatch tool just for fun
    static long nanoTime = System.nanoTime();

    public static void main(String[] args) {
        // setting up the first element
        System.out.println("length of mainArray (firstElementSetup): " + mainArray.size());
        mainArray.add(null);
        leftwardPointer.add(null);
        rightwardPointer.add(1);
        System.out.println("length of mainArray (firstElementSetup): " + mainArray.size());

        // setting up the last element
        System.out.println("length of mainArray (lastElementSetup): " + mainArray.size());
        mainArray.add(null);
        leftwardPointer.add(0);
        rightwardPointer.add(null);
        System.out.println("length of mainArray (lastElementSetup): " + mainArray.size());

        // defining that the cursor-position should begin at the last element
        positionIndex_withinMainArray_cursor = 1;

        // setting up the program's interaction with a persistent-storage file
        File file = new File("saveProgress.txt");
        openProgram(file);

        new JFrameSetup(); // JFrame: Java GUI software
    }

    public static void openProgram(File file) {
        nanoTime = System.nanoTime() - nanoTime;
        System.out.println("Time progress since program start: " + nanoTime + " nanoseconds.");

        try {
            Scanner scanner = new Scanner(file); // instructs a scanner to read through the saveProgress.txt file
            scanner.useDelimiter(""); // scanner-delimiter set to an empty String
            while (scanner.hasNext()) {
                char ch = scanner.next().charAt(0); // going through file character-by-character
                Main.insertChar_withinText(ch);
            }

            positionIndex_withinMainArray_cursor = positionIndex_withinMainArray_lastElementOfText;
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: FileNotFound");
            e.printStackTrace();
        }
    }

    public static void saveAndQuit() {
        System.out.println("Time progressed since program start: " + (System.nanoTime() - nanoTime) + " nanoseconds.");
        nanoTime = System.nanoTime();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saveProgress.txt"))) {

            for (int loopIndex = rightwardPointer.get(positionIndex_withinMainArray_firstElementOfText);
                 loopIndex != positionIndex_withinMainArray_lastElementOfText;
                 loopIndex = rightwardPointer.get(loopIndex)) {

                writer.write(mainArray.get(loopIndex));

            }
            System.out.println("Character array written to saveProgress.txt.");
            System.out.println("Time elapsed throughout the file-saving process: " + (System.nanoTime() - nanoTime) + " nanoseconds.");

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        System.exit(0);
    }

    public static void cursor_moveRightOnly_withinText_void() {
        System.out.println("Right-pointer of cursor: " + rightwardPointer.get(positionIndex_withinMainArray_cursor));
        System.out.println("Cursor: " + positionIndex_withinMainArray_cursor);
        if (positionIndex_withinMainArray_cursor != positionIndex_withinMainArray_lastElementOfText) {
            positionIndex_withinMainArray_cursor = rightwardPointer.get(positionIndex_withinMainArray_cursor);
            cursorGUI_shouldMoveRight = true;
        }
    }
    public static void cursor_moveLeftOnly_withinText_void() {
        System.out.println("Left-pointer of cursor: " + leftwardPointer.get(positionIndex_withinMainArray_cursor));
        System.out.println("Cursor: " + positionIndex_withinMainArray_cursor);
        if (leftwardPointer.get(positionIndex_withinMainArray_cursor) != positionIndex_withinMainArray_firstElementOfText) {
            positionIndex_withinMainArray_cursor = leftwardPointer.get(positionIndex_withinMainArray_cursor);
            cursorGUI_shouldMoveLeft = true;
        }
    }

    public static void insertChar_withinText(char insertedChar) { // always inserting to the left of the cursor-position
        mainArray.add(insertedChar);
        leftwardPointer.add(null);
        rightwardPointer.add(null);

        undoRedo_referencePointer.add(mainArray.size() - 1);
        latestActionType.add((byte) 1);
        undoCount = 0;

        System.out.println("length of mainArray (inserting): " + mainArray.size());
        if (leftwardPointer.get(positionIndex_withinMainArray_cursor) == positionIndex_withinMainArray_firstElementOfText) { // if cursor is at the left-edge of the document

            // back-and-forth pointer-pair: firstElement <> newElement
            rightwardPointer.set(positionIndex_withinMainArray_firstElementOfText, mainArray.size() - 1);
            leftwardPointer.set(mainArray.size() - 1, positionIndex_withinMainArray_firstElementOfText);

            if (positionIndex_withinMainArray_cursor != positionIndex_withinMainArray_lastElementOfText) {
                // back-and-forth pointer-pair: newElement <> cursorElement
                leftwardPointer.set(positionIndex_withinMainArray_cursor, mainArray.size() - 1);
                rightwardPointer.set(mainArray.size() - 1, positionIndex_withinMainArray_cursor);
            }
        }
        if (positionIndex_withinMainArray_cursor == positionIndex_withinMainArray_lastElementOfText) { // if cursor is at the right-edge of the document
            int left2_fromPositionIndex_withinMainArray_lastElement = leftwardPointer.get(positionIndex_withinMainArray_cursor);

            // back-and-forth pointer-pair repairing: lastLeft2Element <> newElement
            leftwardPointer.set(mainArray.size() - 1, left2_fromPositionIndex_withinMainArray_lastElement);
            rightwardPointer.set(left2_fromPositionIndex_withinMainArray_lastElement, mainArray.size() - 1);

            // back-and-forth pointer-pair: newElement <> lastElement
            leftwardPointer.set(positionIndex_withinMainArray_lastElementOfText, mainArray.size() - 1);
            rightwardPointer.set(mainArray.size() - 1, positionIndex_withinMainArray_lastElementOfText);


        }
        else {
            if (leftwardPointer.get(positionIndex_withinMainArray_cursor) != positionIndex_withinMainArray_firstElementOfText) {
                // setting up the cursorLeft2Element
                cursor_moveLeftOnly_withinText_void();
                int left2_fromPositionIndex_withinMainArray_cursor = positionIndex_withinMainArray_cursor;
                cursor_moveRightOnly_withinText_void();

                // back-and-forth pointer-pair: newElement <> cursorElement
                leftwardPointer.set(positionIndex_withinMainArray_cursor, mainArray.size() - 1);
                rightwardPointer.set(mainArray.size() - 1, positionIndex_withinMainArray_cursor);

                // back-and-forth pointer-pair: cursorLeft2Element <> newElement
                rightwardPointer.set(left2_fromPositionIndex_withinMainArray_cursor, mainArray.size() - 1);
                leftwardPointer.set(mainArray.size() - 1, left2_fromPositionIndex_withinMainArray_cursor);
            }
        }

    }
    public static void deleteChar_withinText() { // always deleting to the left of the cursor-position

        if (leftwardPointer.get(leftwardPointer.get(positionIndex_withinMainArray_cursor)) != null) {
            undoRedo_referencePointer.add(positionIndex_withinMainArray_cursor);
            latestActionType.add((byte) -1);
            undoCount = 0;

            if (leftwardPointer.get(leftwardPointer.get(positionIndex_withinMainArray_cursor)) > positionIndex_withinMainArray_firstElementOfText) {

                int left1_fromPositionIndex_withinMainArray_cursor = leftwardPointer.get(positionIndex_withinMainArray_cursor);
                if (left1_fromPositionIndex_withinMainArray_cursor == positionIndex_withinMainArray_firstElementOfText) {
                    return; // nothing to delete
                }

                int left2_fromPositionIndex_withinMainArray_cursor = leftwardPointer.get(left1_fromPositionIndex_withinMainArray_cursor);
                // back-and-forth pointer-pair repairing: cursorLeft2Element <> cursorElement
                leftwardPointer.set(positionIndex_withinMainArray_cursor, left2_fromPositionIndex_withinMainArray_cursor);
                rightwardPointer.set(left2_fromPositionIndex_withinMainArray_cursor, positionIndex_withinMainArray_cursor);

                cursorGUI_shouldAdaptToDeletion = true;
            }
            else {
                if (leftwardPointer.get(positionIndex_withinMainArray_cursor) > positionIndex_withinMainArray_firstElementOfText) {
                    // back-and-forth pointer-pair repairing: firstElement <> cursorElement
                    leftwardPointer.set(positionIndex_withinMainArray_cursor, positionIndex_withinMainArray_firstElementOfText);
                    rightwardPointer.set(positionIndex_withinMainArray_firstElementOfText, positionIndex_withinMainArray_cursor);

                    cursorGUI_shouldAdaptToDeletion = true;
                }
            }
        }
    }

    public static void undo() { //
        if (mainArray.size() > 2) { // perform the undo-action ONLY IF there's more than just the left-edge and right-edge of the text
            if (latestActionType.get(latestActionType.size() - 1 - undoCount) == -1) { // if the latest non-arrow-key action is a deletion
                int focusPointer = undoRedo_referencePointer.get(undoRedo_referencePointer.size() - 1 - undoCount);

                if (leftwardPointer.get(leftwardPointer.get(focusPointer)) > positionIndex_withinMainArray_firstElementOfText) {

                    int minus1PositionIndex_withinMainArray_cursor = leftwardPointer.get(focusPointer);
                    if (minus1PositionIndex_withinMainArray_cursor == positionIndex_withinMainArray_firstElementOfText) {
                        return; // nothing to delete
                    }

                    int minus2PositionIndex_withinMainArray_cursor = leftwardPointer.get(minus1PositionIndex_withinMainArray_cursor);
                    // back-and-forth pointer-pair repairing: cursorLeft2Element <> cursorElement
                    leftwardPointer.set(focusPointer, minus2PositionIndex_withinMainArray_cursor);
                    rightwardPointer.set(minus2PositionIndex_withinMainArray_cursor, focusPointer);

                    cursorGUI_shouldMoveLeft = true;
                }
                else {
                    if (leftwardPointer.get(focusPointer) > positionIndex_withinMainArray_firstElementOfText) {
                        // back-and-forth pointer-pair repairing: firstElement <> cursorElement
                        leftwardPointer.set(focusPointer, positionIndex_withinMainArray_firstElementOfText);
                        rightwardPointer.set(positionIndex_withinMainArray_firstElementOfText, focusPointer);

                        cursorGUI_shouldMoveLeft = true;
                    }
                }
            }
            else if (latestActionType.get(latestActionType.size() - 1 - undoCount) == 1) { // if the latest non-arrow-key action is an insertion

            }
            undoCount++;
        }
        else {
            System.out.println("Nothing to undo.");
        }
    }
    public static void redo() {
        if (undoCount > 0) { // perform the redo-action ONLY IF there are any undo actions to work with
            if (latestActionType.get(latestActionType.size() - 1) == -1 - undoCount) { // if the latest non-arrow-key action is a deletion

            }
            else if (latestActionType.get(latestActionType.size() - 1 - undoCount) == 1) { // if the latest non-arrow-key action is an insertion

            }
            undoCount--;
        }
        else {
            System.out.println("Nothing to redo.");
        }
    }
}