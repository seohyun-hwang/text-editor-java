import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JFrameGUI extends JPanel {
    public BufferedImage buffer;
    public Graphics2D graphics_2D;
    public int cursorX = 20, cursorY = 40; // cursor position
    public FontMetrics fontMetrics;

    public boolean cursorVisible = true;


    public JFrameGUI() {
        buffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        graphics_2D = buffer.createGraphics();

        graphics_2D.setColor(Color.lightGray); // sets everything to white
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());


        graphics_2D.setFont(new Font("Monospaced", Font.PLAIN, 16)); // white color by default
        graphics_2D.setColor(Color.BLACK); // setting the text to black color
        fontMetrics = graphics_2D.getFontMetrics();

        Timer timer = new Timer(200, e -> {
            cursorVisible = !cursorVisible;
            repaint();
        });
        timer.start();
    }

    public void cursor_moveRightOnly_withinText(char ch) {
        cursorX += fontMetrics.charWidth(ch); // move cursor right by the width of the character
        wordWrapping();
    }

    public void cursor_moveLeftOnly_withinText(char ch) {
        cursorX -= fontMetrics.charWidth(ch); // move cursor left by the width of the character
        wordWrapping();
    }

    public void addChar(char ch) {
        graphics_2D.drawString(String.valueOf(ch), cursorX, cursorY);
        cursorX += fontMetrics.charWidth(ch); // move cursor right by the width of the character
        repaint();
        wordWrapping();
    }
    public void deleteChar(char ch) {
        cursorX -= fontMetrics.charWidth(ch); // move cursor left by the width of the character
        wordWrapping();
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



    public void wordWrapping() {
        int panelWidth = buffer.getWidth() - 20; // margin
        int drawX = 20;
        int drawY = 40;

        for (int loopIndex = Main.rightwardPointer.get(Main.positionIndex_withinMainArray_firstElementOfText);
             loopIndex != Main.positionIndex_withinMainArray_lastElementOfText;
             loopIndex = Main.rightwardPointer.get(loopIndex)) {

            char ch = Main.mainArray.get(loopIndex);
            int charWidth = fontMetrics.charWidth(ch);

            // wrap to next line if overflow
            if (drawX + charWidth > panelWidth) {
                drawX = 20;
                drawY += fontMetrics.getHeight();
            }

            graphics_2D.drawString(String.valueOf(ch), drawX, drawY);

            if (loopIndex == Main.positionIndex_withinMainArray_cursor) {
                cursorX = drawX + charWidth;
                cursorY = drawY;
            }

            drawX += charWidth;
        }
    }
    public void redrawAllText() {
        // clearing everything
        graphics_2D.setColor(Color.lightGray); // sets everything to white
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

        // reset drawing color to black
        graphics_2D.setColor(Color.BLACK);


        // resetting the drawing position
        int drawX = 20;
        cursorX = 20;
        cursorY = 40;

        // redrawing from scratch by traversing the LinkedList
        for (int loopIndex = Main.rightwardPointer.get(Main.positionIndex_withinMainArray_firstElementOfText);
             loopIndex != Main.positionIndex_withinMainArray_lastElementOfText;
             loopIndex = Main.rightwardPointer.get(loopIndex)) {

            char ch = Main.mainArray.get(loopIndex);
            graphics_2D.drawString(String.valueOf(ch), drawX, cursorY);

            if (loopIndex == Main.positionIndex_withinMainArray_cursor) {
                cursorX = drawX + fontMetrics.charWidth(ch); // cursor after this char
            }

            drawX += fontMetrics.charWidth(ch);
            wordWrapping();
        }
        repaint();
    }
}