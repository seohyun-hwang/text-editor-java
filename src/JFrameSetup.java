import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class JFrameSetup extends JFrame implements KeyListener, WindowListener {

    JFrame jframe;
    JFrameGUI jframeGUI = new JFrameGUI();

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

        jframeGUI.redrawAllText();
    }



    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        if (Character.isLetterOrDigit(e.getKeyChar())) { // insertion-key typed (alphanumeric character)
            Main.insertChar_withinText(e.getKeyChar());
            jframeGUI.addChar(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            Main.deleteChar_withinText();
            if (Main.activateJFrameGUI) {
                jframeGUI.redrawAllText();
                Main.activateJFrameGUI = false;
            }
        } else if (e.getKeyCode() == 37) {
            System.out.println("Hello1");
            Main.cursor_moveLeftOnly_withinText();
        } else if (e.getKeyCode() == 39) {
            System.out.println("Hello2");
            Main.cursor_moveRightOnly_withinText();
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