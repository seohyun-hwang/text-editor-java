import javax.swing.*;
import java.awt.event.*;

public class JFrameSetup extends JFrame implements KeyListener, WindowListener {

    JFrame jframe;
    JFrameGUI jframeGUI = new JFrameGUI();
    char[] validTextCharacters = {' ', ',', '.', '!', '?', '(', ')', ';', '/', '-', '+', '@', '#', '%', '&', '*', '^', ':', '<', '>', '`', '~', '=', '_', '\\', '|'};


    JFrameSetup() {
        jframe = new JFrame();
        setTitle("Seohyun Hwang's basic text editor");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(jframeGUI);
        setVisible(true);
        addWindowListener(this);

        jframeGUI.addKeyListener(this);
        jframeGUI.setFocusable(true);
        jframeGUI.requestFocusInWindow();

        jframeGUI.redrawAllText_withWordWrap();
    }



    public static boolean isValid_nonalphanumericalTextCharacter(char ch, char[] validTextCharacters) {
        for (int i = 0; i < validTextCharacters.length; i++) {
            if (validTextCharacters[i] == ch) {
                return true;
            }
        }
        return false;
    }


    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        if (Character.isLetterOrDigit(e.getKeyChar()) || isValid_nonalphanumericalTextCharacter(e.getKeyChar(), validTextCharacters)) { // insertion-key typed (alphanumeric character or any other character defined as valid)
            Main.insertChar_withinText(e.getKeyChar());
            jframeGUI.addChar(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            char ch = Main.mainArray.get(Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor));
            Main.deleteChar_withinText();
            if (Main.cursorGUI_shouldAdaptToDeletion) {
                jframeGUI.cursor_moveLeftOnly_withinText(ch);
                jframeGUI.redrawAllText_withWordWrap();

                Main.cursorGUI_shouldMoveLeft = false;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Hello1");
            Main.cursor_moveLeftOnly_withinText_void();
            if (Main.cursorGUI_shouldMoveLeft) {
                jframeGUI.cursor_moveLeftOnly_withinText(Main.mainArray.get(Main.positionIndex_withinMainArray_cursor));

                Main.cursorGUI_shouldMoveLeft = false;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Hello2");
            System.out.println("Cursor: " + Main.positionIndex_withinMainArray_cursor);

            Main.cursor_moveRightOnly_withinText_void();
            if (Main.cursorGUI_shouldMoveRight) {
                if (Main.positionIndex_withinMainArray_cursor == Main.positionIndex_withinMainArray_lastElementOfText) {
                    jframeGUI.cursor_moveRightOnly_withinText(Main.mainArray.get(Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor)));
                }
                else {
                    jframeGUI.cursor_moveRightOnly_withinText(Main.mainArray.get(Main.positionIndex_withinMainArray_cursor));
                }

                Main.cursorGUI_shouldMoveRight = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right arrow key released!");
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left arrow key released!");
        }

        System.out.println("Released key-CHAR: " + e.getKeyChar());
        System.out.println("Released key-CODE: " + e.getKeyCode());
    }


    // WindowListener methods
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("JFrame closed.");
        Main.saveAndQuit();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}