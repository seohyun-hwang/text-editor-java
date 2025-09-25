import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // general support variables (within Main class)
    static int positionIndex_withinMainArray_cursor;
    static final int positionIndex_withinMainArray_linkedListHead = 0;
    static final int positionIndex_withinMainArray_linkedListTail = 1;

    // DoublyLinkedList (for text-character sequencing)
    // text is represented as a series of characters in a LinkedList exclusively between the head and tail.
    static ArrayList<Character> mainArray = new ArrayList<>();
    static ArrayList<Integer> leftwardPointer_withinLinkedList = new ArrayList<>();
    static ArrayList<Integer> rightwardPointer_withinLinkedList = new ArrayList<>();

    // support variables (for GUI classes)
    static boolean cursorGUI_shouldMoveLeft = false;
    static boolean cursorGUI_shouldMoveRight = false;
    static boolean cursorGUI_shouldAdaptToDeletion = false;

    // undo/redo
    static ArrayList<Byte> latestActionType = new ArrayList<>(); // 1: insert; -1: deletion
    static ArrayList<Integer> positionIndices_withinMainArray_undoRedoLocation = new ArrayList<>();
    static int undoCount = 0;

    // a stopwatch tool just for fun
    static long nanoTime = System.nanoTime();

    public static void main(String[] args) {
        // setting up the head (first element)
        System.out.println("length of mainArray (firstElementSetup): " + mainArray.size());
        mainArray.add(null);
        leftwardPointer_withinLinkedList.add(null);
        rightwardPointer_withinLinkedList.add(1);
        System.out.println("length of mainArray (firstElementSetup): " + mainArray.size());

        // setting up the tail (last element)
        System.out.println("length of mainArray (lastElementSetup): " + mainArray.size());
        mainArray.add(null);
        leftwardPointer_withinLinkedList.add(0);
        rightwardPointer_withinLinkedList.add(null);
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
                Main.insertChar_withinLinkedList(ch);
            }

            positionIndex_withinMainArray_cursor = positionIndex_withinMainArray_linkedListTail;
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: FileNotFound");
            e.printStackTrace();
        }
    }

    public static void saveAndQuit() {
        System.out.println("Time progressed since program start: " + (System.nanoTime() - nanoTime) + " nanoseconds.");
        nanoTime = System.nanoTime();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saveProgress.txt"))) {

            for (int loopIndex = rightwardPointer_withinLinkedList.get(positionIndex_withinMainArray_linkedListHead);
                 loopIndex != positionIndex_withinMainArray_linkedListTail;
                 loopIndex = rightwardPointer_withinLinkedList.get(loopIndex))
            {

                writer.write(mainArray.get(loopIndex));

            }
            System.out.println("Character array written to saveProgress.txt.");
            System.out.println("Time elapsed throughout the file-saving process: " + (System.nanoTime() - nanoTime) + " nanoseconds.");

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        System.exit(0);
    }

    public static int subject_moveRightOnly_withinLinkedList_intReturn(int positionIndex_withinMainArray_subject) {
        System.out.println("Right-pointer of subject: " + rightwardPointer_withinLinkedList.get(positionIndex_withinMainArray_subject));
        System.out.println("Subject position: " + positionIndex_withinMainArray_subject);
        if (positionIndex_withinMainArray_subject != positionIndex_withinMainArray_linkedListTail) {
            positionIndex_withinMainArray_subject = rightwardPointer_withinLinkedList.get(positionIndex_withinMainArray_subject);

            cursorGUI_shouldMoveRight = true;
            return positionIndex_withinMainArray_subject;
        }
        return positionIndex_withinMainArray_subject;
    }
    public static int subject_moveLeftOnly_withinLinkedList_intReturn(int positionIndex_withinMainArray_subject) {
        System.out.println("Left-pointer of subject: " + leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_subject));
        System.out.println("Subject position: " + positionIndex_withinMainArray_subject);
        if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_subject) != positionIndex_withinMainArray_linkedListHead) {
            positionIndex_withinMainArray_subject = leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_subject);

            cursorGUI_shouldMoveLeft = true;
            return positionIndex_withinMainArray_subject;
        }
        return positionIndex_withinMainArray_subject;
    }

    public static void insertChar_withinLinkedList(char insertedChar) { // always inserting to the left of the cursor-position
        mainArray.add(insertedChar);
        leftwardPointer_withinLinkedList.add(null);
        rightwardPointer_withinLinkedList.add(null);

        positionIndices_withinMainArray_undoRedoLocation.add(mainArray.size() - 1);
        latestActionType.add((byte) 1);
        undoCount = 0;

        System.out.println("length of mainArray (inserting): " + mainArray.size());
        if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor) == positionIndex_withinMainArray_linkedListHead) { // if cursor is at the left-edge of the document

            // back-and-forth pointer-pair: firstElement <> newElement
            rightwardPointer_withinLinkedList.set(positionIndex_withinMainArray_linkedListHead, mainArray.size() - 1);
            leftwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_linkedListHead);

            if (positionIndex_withinMainArray_cursor != positionIndex_withinMainArray_linkedListTail) {
                // back-and-forth pointer-pair: newElement <> cursorElement
                leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_cursor, mainArray.size() - 1);
                rightwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_cursor);
            }
        }
        if (positionIndex_withinMainArray_cursor == positionIndex_withinMainArray_linkedListTail) { // if cursor is at the right-edge of the document
            int left2_fromPositionIndex_withinMainArray_lastElement = leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor);

            // back-and-forth pointer-pair repairing: lastLeft2Element <> newElement
            leftwardPointer_withinLinkedList.set(mainArray.size() - 1, left2_fromPositionIndex_withinMainArray_lastElement);
            rightwardPointer_withinLinkedList.set(left2_fromPositionIndex_withinMainArray_lastElement, mainArray.size() - 1);

            // back-and-forth pointer-pair: newElement <> lastElement
            leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_linkedListTail, mainArray.size() - 1);
            rightwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_linkedListTail);


        }
        else {
            if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor) != positionIndex_withinMainArray_linkedListHead) {
                // setting up the cursorLeft2Element
                subject_moveLeftOnly_withinLinkedList_intReturn(positionIndex_withinMainArray_cursor);
                int left2_fromPositionIndex_withinMainArray_cursor = positionIndex_withinMainArray_cursor;
                subject_moveRightOnly_withinLinkedList_intReturn(positionIndex_withinMainArray_cursor);

                // back-and-forth pointer-pair: newElement <> cursorElement
                leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_cursor, mainArray.size() - 1);
                rightwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_cursor);

                // back-and-forth pointer-pair: cursorLeft2Element <> newElement
                rightwardPointer_withinLinkedList.set(left2_fromPositionIndex_withinMainArray_cursor, mainArray.size() - 1);
                leftwardPointer_withinLinkedList.set(mainArray.size() - 1, left2_fromPositionIndex_withinMainArray_cursor);
            }
        }

    }
    public static void deleteChar_withinLinkedList() { // always deleting to the left of the cursor-position

        int positionIndex_withinMainArray_undoRedoLocation = leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor);

        if (leftwardPointer_withinLinkedList.get(leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor)) != null) {
            if (leftwardPointer_withinLinkedList.get(leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor)) > positionIndex_withinMainArray_linkedListHead) {

                int left1_fromPositionIndex_withinMainArray_cursor = leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor);
                if (left1_fromPositionIndex_withinMainArray_cursor == positionIndex_withinMainArray_linkedListHead) {
                    return; // nothing to delete
                }

                int left2_fromPositionIndex_withinMainArray_cursor = leftwardPointer_withinLinkedList.get(left1_fromPositionIndex_withinMainArray_cursor);
                // back-and-forth pointer-pair repairing: cursorLeft2Element <> cursorElement
                leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_cursor, left2_fromPositionIndex_withinMainArray_cursor);
                rightwardPointer_withinLinkedList.set(left2_fromPositionIndex_withinMainArray_cursor, positionIndex_withinMainArray_cursor);


                positionIndices_withinMainArray_undoRedoLocation.add(positionIndex_withinMainArray_undoRedoLocation);
                latestActionType.add((byte) -1);
                undoCount = 0;
                cursorGUI_shouldAdaptToDeletion = true;
            }
            else {
                if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor) > positionIndex_withinMainArray_linkedListHead) {
                    // back-and-forth pointer-pair repairing: firstElement <> cursorElement
                    leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_cursor, positionIndex_withinMainArray_linkedListHead);
                    rightwardPointer_withinLinkedList.set(positionIndex_withinMainArray_linkedListHead, positionIndex_withinMainArray_cursor);


                    positionIndices_withinMainArray_undoRedoLocation.add(positionIndex_withinMainArray_undoRedoLocation);
                    latestActionType.add((byte) -1);
                    undoCount = 0;
                    cursorGUI_shouldAdaptToDeletion = true;
                }
            }
        }
    }

    public static void undo_anAction() { //
        if (mainArray.size() > 2) { // perform the undo-action ONLY IF there's more than just the left-edge and right-edge of the text
            undoCount++;

            if (latestActionType.get((latestActionType.size() - 1) - undoCount) == 1) { // if the latest non-arrow-key action is an insertion
                // a pointer to the place where the action was done
                int positionIndex_withinMainArray_actionLocation = positionIndices_withinMainArray_undoRedoLocation.get((positionIndices_withinMainArray_undoRedoLocation.size() - 1) - undoCount);

                if (leftwardPointer_withinLinkedList.get(leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_actionLocation)) > positionIndex_withinMainArray_linkedListHead) {

                    int left1_fromPositionIndex_withinMainArray_cursor = leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_actionLocation);
                    if (left1_fromPositionIndex_withinMainArray_cursor == positionIndex_withinMainArray_linkedListHead) {
                        return; // nothing to delete
                    }

                    int left2_fromPositionIndex_withinMainArray_cursor = leftwardPointer_withinLinkedList.get(left1_fromPositionIndex_withinMainArray_cursor);
                    // back-and-forth pointer-pair repairing: cursorLeft2Element <> actionLocationElement
                    leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_actionLocation, left2_fromPositionIndex_withinMainArray_cursor);
                    rightwardPointer_withinLinkedList.set(left2_fromPositionIndex_withinMainArray_cursor, positionIndex_withinMainArray_actionLocation);

                    cursorGUI_shouldMoveLeft = true;
                }
                else {
                    if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_actionLocation) > positionIndex_withinMainArray_linkedListHead) {
                        // back-and-forth pointer-pair repairing: firstElement <> actionLocationElement
                        leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_actionLocation, positionIndex_withinMainArray_linkedListHead);
                        rightwardPointer_withinLinkedList.set(positionIndex_withinMainArray_linkedListHead, positionIndex_withinMainArray_actionLocation);

                        cursorGUI_shouldMoveLeft = true;
                    }
                }
            }
            else if (latestActionType.get(latestActionType.size() - 1 - undoCount) == -1) { // if the latest non-arrow-key action is a deletion
                // a pointer to the place where the action was done
                int positionIndex_withinMainArray_actionLocation = positionIndices_withinMainArray_undoRedoLocation.get((positionIndices_withinMainArray_undoRedoLocation.size() - 1) - undoCount);

                if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_actionLocation) == positionIndex_withinMainArray_linkedListHead) { // if cursor is at the left-edge of the document

                    // back-and-forth pointer-pair: firstElement <> newElement
                    rightwardPointer_withinLinkedList.set(positionIndex_withinMainArray_linkedListHead, mainArray.size() - 1);
                    leftwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_linkedListHead);

                    if (positionIndex_withinMainArray_actionLocation != positionIndex_withinMainArray_linkedListTail) {
                        // back-and-forth pointer-pair: newElement <> actionLocationElement
                        leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_actionLocation, mainArray.size() - 1);
                        rightwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_actionLocation);
                    }
                }
                if (positionIndex_withinMainArray_actionLocation == positionIndex_withinMainArray_linkedListTail) { // if cursor is at the right-edge of the document
                    int left2_fromPositionIndex_withinMainArray_lastElement = leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_cursor);

                    // back-and-forth pointer-pair repairing: lastLeft2Element <> newElement
                    leftwardPointer_withinLinkedList.set(mainArray.size() - 1, left2_fromPositionIndex_withinMainArray_lastElement);
                    rightwardPointer_withinLinkedList.set(left2_fromPositionIndex_withinMainArray_lastElement, mainArray.size() - 1);

                    // back-and-forth pointer-pair: newElement <> lastElement
                    leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_linkedListTail, mainArray.size() - 1);
                    rightwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_linkedListTail);


                }
                else {
                    if (leftwardPointer_withinLinkedList.get(positionIndex_withinMainArray_actionLocation) != positionIndex_withinMainArray_linkedListHead) {
                        // setting up the cursorLeft2Element
                        positionIndex_withinMainArray_actionLocation = subject_moveLeftOnly_withinLinkedList_intReturn(positionIndex_withinMainArray_actionLocation);
                        int left2_fromPositionIndex_withinMainArray_actionLocation = positionIndex_withinMainArray_actionLocation;
                        positionIndex_withinMainArray_actionLocation = subject_moveRightOnly_withinLinkedList_intReturn(positionIndex_withinMainArray_actionLocation);

                        // back-and-forth pointer-pair: newElement <> actionLocationElement
                        leftwardPointer_withinLinkedList.set(positionIndex_withinMainArray_actionLocation, mainArray.size() - 1);
                        rightwardPointer_withinLinkedList.set(mainArray.size() - 1, positionIndex_withinMainArray_actionLocation);

                        // back-and-forth pointer-pair: actionLocationLeft2Element <> newElement
                        rightwardPointer_withinLinkedList.set(left2_fromPositionIndex_withinMainArray_actionLocation, mainArray.size() - 1);
                        leftwardPointer_withinLinkedList.set(mainArray.size() - 1, left2_fromPositionIndex_withinMainArray_actionLocation);
                    }
                }
            }
        }
        else {
            System.out.println("Nothing to undo.");
        }
    }
    public static void redo_anUndoneAction() {
        if (undoCount > 0) { // perform the redo-action ONLY IF there are any undo actions to work with
            undoCount--;

            if (latestActionType.get(latestActionType.size() - 1) == -1 - undoCount) { // if the latest non-arrow-key action is a deletion

            }
            else if (latestActionType.get(latestActionType.size() - 1 - undoCount) == 1) { // if the latest non-arrow-key action is an insertion

            }
        }
        else {
            System.out.println("Nothing to redo.");
        }
    }
}