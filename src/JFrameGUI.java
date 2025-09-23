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
        redrawAllText_withWordWrap();
    }

    public void cursor_moveLeftOnly_withinText(char ch) {
        cursorX -= fontMetrics.charWidth(ch); // move cursor left by the width of the character
        redrawAllText_withWordWrap();
    }

    public void addChar(char ch) {
        cursorX += fontMetrics.charWidth(ch); // move cursor right by the width of the character
        redrawAllText_withWordWrap();
    }
    //public void deleteChar(char ch) {}


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


    public void redrawAllText_withWordWrap() { // redraw all text with word-wrapping
        graphics_2D.setColor(Color.lightGray);
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

        graphics_2D.setColor(Color.BLACK);
        int drawX = 20;
        int drawY = 40;

        // traverse the LinkedList from the left-edge to the right-edge
        for (int loopIndex = Main.rightwardPointer.get(Main.positionIndex_withinMainArray_firstElementOfText);
             loopIndex != Main.positionIndex_withinMainArray_lastElementOfText;
             loopIndex = Main.rightwardPointer.get(loopIndex))
        {

            char ch = Main.mainArray.get(loopIndex);

            // recording the GUI-cursor-position IF this loopIndex == cursorIndex_withinMainArray
            if (loopIndex == Main.positionIndex_withinMainArray_cursor) {
                cursorX = drawX;
                cursorY = drawY;
            }

            // word-wrapping
            if ((drawX + fontMetrics.charWidth(ch)) > (buffer.getWidth() - 20)) { // keep a 20px margin on the right
                drawX = 20;
                drawY += fontMetrics.getHeight();
            }

            // performing the actual drawing
            graphics_2D.drawString(String.valueOf(ch), drawX, drawY);

            // increment drawX for next drawing-cycle
            drawX += fontMetrics.charWidth(ch);
            repaint();
        }

        // recording the GUI-cursor-position IF the cursorIndex_withinMainArray == lastElementIndex_withinMainArray
        if (Main.positionIndex_withinMainArray_cursor == Main.positionIndex_withinMainArray_lastElementOfText) {
            cursorX = drawX;
            cursorY = drawY;
        }
    }
}