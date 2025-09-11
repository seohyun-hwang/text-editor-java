import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // support variables
    static int positionIndex_withinMainArray_cursor = 1;
    static int positionIndex_withinMainArray_firstElementOfText;
    static int positionIndex_withinMainArray_lastElementOfText;
    static boolean activateJFrameGUI = false;


    // LinkedList
    static ArrayList<Character> mainArray = new ArrayList<>();
    static ArrayList<Integer> leftwardPointer = new ArrayList<>();
    static ArrayList<Integer> rightwardPointer = new ArrayList<>();

    // a stopwatch tool just for fun
    static long nanoTime;

    public static void main(String[] args) throws FileNotFoundException {
        // setting up the persistent-storage file
        File file = new File("save_progress.txt");
        openProgram(file);

        // setting up the first element
        System.out.println("length of mainArray (firstElementSetup): " + mainArray.size());
        mainArray.add(null);
        leftwardPointer.add(null);
        rightwardPointer.add(1);
        positionIndex_withinMainArray_firstElementOfText = 0; // final value
        System.out.println("length of mainArray (firstElementSetup): " + mainArray.size());

        // setting up the last element
        System.out.println("length of mainArray (lastElementSetup): " + mainArray.size());
        mainArray.add(null);
        leftwardPointer.add(0);
        rightwardPointer.add(null);
        positionIndex_withinMainArray_lastElementOfText = 1; // final value
        System.out.println("length of mainArray (lastElementSetup): " + mainArray.size());


        new JFrameSetup(); // JFrame: Java GUI software
    }

    public static void openProgram(File file) throws FileNotFoundException {
        Main.nanoTime = System.nanoTime();

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
        nanoTime = System.nanoTime() - nanoTime;
        System.out.println("Time progressed since program start: " + nanoTime + " nanoseconds.");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saveProgress.txt"))) {
            for (int loopIndex = Main.positionIndex_withinMainArray_firstElementOfText;
                 loopIndex != Main.positionIndex_withinMainArray_lastElementOfText;
                 loopIndex = Main.rightwardPointer.get(loopIndex)) {

                if (loopIndex != Main.positionIndex_withinMainArray_firstElementOfText) {
                    char ch = Main.mainArray.get(loopIndex);
                    writer.write(ch);
                }

            }
            System.out.println("Character array written to saveProgress.txt.");
            System.out.println("Time progressed throughout file-writing process: " + (System.nanoTime() - nanoTime) + " nanoseconds.");

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public static void cursor_moveRightOnly_withinText() {
        if (positionIndex_withinMainArray_cursor != positionIndex_withinMainArray_lastElementOfText) {
            positionIndex_withinMainArray_cursor = rightwardPointer.get(positionIndex_withinMainArray_cursor);
        }
    }
    public static void cursor_moveLeftOnly_withinText() {
        if (leftwardPointer.get(positionIndex_withinMainArray_cursor) != positionIndex_withinMainArray_firstElementOfText) {
            positionIndex_withinMainArray_cursor = leftwardPointer.get(positionIndex_withinMainArray_cursor);
        }
    }

    public static void insertChar_withinText(char insertedChar) {
        System.out.println("length of mainArray (inserting): " + mainArray.size());
        mainArray.add(insertedChar);
        leftwardPointer.add(null);
        rightwardPointer.add(null);

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
            if (leftwardPointer.get(positionIndex_withinMainArray_cursor) != positionIndex_withinMainArray_firstElementOfText) {
                // back-and-forth pointer-pair: cursorMinus2Element <> newElement
                cursor_moveLeftOnly_withinText();
                int minus2PositionIndex_withinMainArray_cursor = positionIndex_withinMainArray_cursor;
                cursor_moveRightOnly_withinText();
                rightwardPointer.set(minus2PositionIndex_withinMainArray_cursor, mainArray.size() - 1);
                leftwardPointer.set(mainArray.size() - 1, minus2PositionIndex_withinMainArray_cursor);
            }

            // back-and-forth pointer-pair: newElement <> lastElement
            leftwardPointer.set(positionIndex_withinMainArray_lastElementOfText, mainArray.size() - 1);
            rightwardPointer.set(mainArray.size() - 1, positionIndex_withinMainArray_lastElementOfText);
        }
        else {
            if (leftwardPointer.get(positionIndex_withinMainArray_cursor) != positionIndex_withinMainArray_firstElementOfText) {
                // setting up the cursorMinus2Element
                cursor_moveLeftOnly_withinText();
                int minus2PositionIndex_withinMainArray_cursor = positionIndex_withinMainArray_cursor;
                cursor_moveRightOnly_withinText();

                // back-and-forth pointer-pair: newElement <> cursorElement
                leftwardPointer.set(positionIndex_withinMainArray_cursor, mainArray.size() - 1);
                rightwardPointer.set(mainArray.size() - 1, positionIndex_withinMainArray_cursor);

                // back-and-forth pointer-pair: cursorMinus2Element <> newElement
                rightwardPointer.set(minus2PositionIndex_withinMainArray_cursor, mainArray.size() - 1);
                leftwardPointer.set(mainArray.size() - 1, minus2PositionIndex_withinMainArray_cursor);
            }
        }

    }
    public static void deleteChar_withinText() {
        System.out.println("Cursor element: " + positionIndex_withinMainArray_cursor);
        System.out.println("Left element: " + leftwardPointer.get(positionIndex_withinMainArray_cursor));
        System.out.println("LeftLeft element: " + leftwardPointer.get(leftwardPointer.get(positionIndex_withinMainArray_cursor)));

        if (leftwardPointer.get(leftwardPointer.get(positionIndex_withinMainArray_cursor)) != null) {
            if (leftwardPointer.get(leftwardPointer.get(positionIndex_withinMainArray_cursor)) > positionIndex_withinMainArray_firstElementOfText) {
                // setting up the cursorMinus2Element
                cursor_moveLeftOnly_withinText();
                cursor_moveLeftOnly_withinText();
                int minus2PositionIndex_withinMainArray_cursor = positionIndex_withinMainArray_cursor;
                cursor_moveRightOnly_withinText();
                cursor_moveRightOnly_withinText();

                // back-and-forth pointer-pair repairing: cursorMinus2Element <> cursorElement
                leftwardPointer.set(positionIndex_withinMainArray_cursor, minus2PositionIndex_withinMainArray_cursor);
                rightwardPointer.set(minus2PositionIndex_withinMainArray_cursor, positionIndex_withinMainArray_cursor);

                activateJFrameGUI = true;
            }
            else {
                if (leftwardPointer.get(positionIndex_withinMainArray_cursor) > positionIndex_withinMainArray_firstElementOfText) {
                    // back-and-forth pointer-pair repairing: firstElement <> cursorElement
                    leftwardPointer.set(positionIndex_withinMainArray_cursor, positionIndex_withinMainArray_firstElementOfText);
                    rightwardPointer.set(positionIndex_withinMainArray_firstElementOfText, positionIndex_withinMainArray_cursor);
                }
            }
        }
    }
}