import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JFrameGUI extends JPanel {
    public BufferedImage buffer;
    public Graphics2D graphics_2D;
    public int x = 20, y = 40; // cursor position
    public FontMetrics fontMetrics;

    public JFrameGUI() {
        buffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        graphics_2D = buffer.createGraphics();

        graphics_2D.setColor(Color.lightGray); // sets everything to white
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());


        graphics_2D.setFont(new Font("Monospaced", Font.PLAIN, 16)); // white color by default
        graphics_2D.setColor(Color.BLACK); // setting the text to black color
        fontMetrics = graphics_2D.getFontMetrics();
    }

    public void addChar(char c) {
        graphics_2D.drawString(String.valueOf(c), x, y);
        x += fontMetrics.charWidth(c); // move cursor right by the width of the character
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }




    public void redrawAllText() {
        // clear the buffer
        graphics_2D.setColor(Color.lightGray); // sets everything to white
        graphics_2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

        // reset drawing color to black
        graphics_2D.setColor(Color.BLACK);

        // reset cursor position
        x = 20;
        y = 40;

        // walk through your text data structure and redraw
        for (int loopIndex = Main.positionIndex_withinMainArray_firstElementOfText;
             loopIndex != Main.positionIndex_withinMainArray_lastElementOfText;
             loopIndex = Main.rightwardPointer.get(loopIndex)) {

            if (loopIndex != Main.positionIndex_withinMainArray_firstElementOfText) {
                char ch = Main.mainArray.get(loopIndex);
                graphics_2D.drawString(String.valueOf(ch), x, y);
                x += fontMetrics.charWidth(ch);
                repaint();
            }

        }
    }
}