import javax.swing.*;
import java.awt.event.*;

public class JFrameSetup extends JFrame implements KeyListener, WindowListener {

    JFrame jframe;
    JFrameGUI jframeGUI = new JFrameGUI();

    // used later in KeyListener
    boolean cursor_moveLeftOnly_withinGUI_boolean = true;
    boolean cursor_moveRightOnly_withinGUI_boolean = true;


    JFrameSetup() throws InterruptedException {
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



    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == ' ' || e.getKeyChar() == ',' || e.getKeyChar() == '.' || e.getKeyChar() == '!' || e.getKeyChar() == '?' || e.getKeyChar() == '(' || e.getKeyChar() == ')') { // insertion-key typed (alphanumeric character)
            Main.insertChar_withinText(e.getKeyChar());
            jframeGUI.addChar(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            char ch = Main.mainArray.get(Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor));
            Main.deleteChar_withinText();
            if (Main.activateJFrameGUI) {
                jframeGUI.redrawAllText_withWordWrap();
                jframeGUI.cursor_moveLeftOnly_withinText(ch);
                Main.activateJFrameGUI = false;
            }
        } else if (e.getKeyCode() == 37) {
            System.out.println("Hello1");
            if (Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor) != null && Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor) != Main.positionIndex_withinMainArray_firstElementOfText) {
                if (Main.leftwardPointer.get(Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor)) != Main.positionIndex_withinMainArray_firstElementOfText) {
                    Main.cursor_moveLeftOnly_withinText_void();
                    jframeGUI.cursor_moveLeftOnly_withinText(Main.cursor_moveLeftOnly_withinText_char());
                }
                else if (cursor_moveLeftOnly_withinGUI_boolean) {
                    jframeGUI.cursor_moveLeftOnly_withinText(Main.cursor_moveLeftOnly_withinText_char());
                    cursor_moveLeftOnly_withinGUI_boolean = false;
                }
                cursor_moveRightOnly_withinGUI_boolean = true;
            }
        } else if (e.getKeyCode() == 39) {
            System.out.println("Hello2");
            System.out.println("Cursor: " + Main.positionIndex_withinMainArray_cursor);
            if (Main.positionIndex_withinMainArray_cursor != Main.positionIndex_withinMainArray_lastElementOfText) {
                char ch = Main.mainArray.get(Main.positionIndex_withinMainArray_cursor);
                jframeGUI.cursor_moveRightOnly_withinText(ch);
                Main.cursor_moveRightOnly_withinText_void();
                cursor_moveLeftOnly_withinGUI_boolean = true;
            }
            else if (cursor_moveRightOnly_withinGUI_boolean) {
                char ch = Main.mainArray.get(Main.leftwardPointer.get(Main.positionIndex_withinMainArray_cursor));
                jframeGUI.cursor_moveRightOnly_withinText(ch);
                cursor_moveRightOnly_withinGUI_boolean = false;
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