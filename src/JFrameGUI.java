import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JFrameGUI extends JPanel {
    public BufferedImage buffer;
    public Graphics2D graphics_2D;
    public int x = 20, y = 40; // cursor position
    public FontMetrics fontMetrics;

    public int cursorX = 20, cursorY = 40;
    public boolean cursorVisible = true;


    public JFrameGUI() {
        buffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        graphics_2D = buffer.createGraphics();

        graphics_2D.setColor(Color.lightGray); // sets everything to white
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());


        graphics_2D.setFont(new Font("Monospaced", Font.PLAIN, 16)); // white color by default
        graphics_2D.setColor(Color.BLACK); // setting the text to black color
        fontMetrics = graphics_2D.getFontMetrics();

        // timer for vertical blinking cursor
        Timer timer = new Timer(500, e -> {
            cursorVisible = !cursorVisible;
            repaint();
        });
        timer.start();
    }

    public void cursor_moveRightOnly_withinText(char ch) {
        cursorX += fontMetrics.charWidth(ch); // move cursor right by the width of the character
        x += fontMetrics.charWidth(ch); // move cursor right by the width of the character
    }

    public void cursor_moveLeftOnly_withinText(char ch) {
        cursorX -= fontMetrics.charWidth(ch); // move cursor left by the width of the character
        x -= fontMetrics.charWidth(ch); // move cursor left by the width of the character
    }

    public void addChar(char ch) {
        graphics_2D.drawString(String.valueOf(ch), x, y);
        cursorX += fontMetrics.charWidth(ch); // move cursor right by the width of the character
        x += fontMetrics.charWidth(ch); // move cursor right by the width of the character
        repaint();
    }
    public void deleteChar(char ch) {

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);

        // vertical blinking cursor
        if (cursorVisible) {
            g.setColor(Color.BLACK);
            g.drawLine(cursorX, cursorY - fontMetrics.getAscent(), cursorX, cursorY);
        }
    }




    public void redrawAllText() {
        // clearing everything
        graphics_2D.setColor(Color.lightGray); // sets everything to white
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

        // reset drawing color to black
        graphics_2D.setColor(Color.BLACK);

        // reset cursor position
        x = 20;
        y = 40;

        // redrawing from scratch by traversing the LinkedList
        for (int loopIndex = Main.positionIndex_withinMainArray_firstElementOfText;
             loopIndex != Main.positionIndex_withinMainArray_lastElementOfText;
             loopIndex = Main.rightwardPointer.get(loopIndex)) {

            if (loopIndex != Main.positionIndex_withinMainArray_firstElementOfText) {
                char ch = Main.mainArray.get(loopIndex);
                addChar(ch);
            }

        }
    }
}