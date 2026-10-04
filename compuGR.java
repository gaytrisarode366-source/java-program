import java.awt.*;//abstract window toolkit(Classes: Graphics, Color, Font)
import javax.swing.*;//swing package GUI(Classes: JFrame, JPanel)

public class compuGR extends JPanel {//extends JPanel to create a custom panel for drawing

    //DDA Line Drawing Algorithm
    void drawDDA(Graphics g, int x1, int y1,
                 int x2, int y2, String style) {//method to draw a line beetween two points (x1, y1) and (x2, y2) 

        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));//math.abs(-100):100;(math.max(100, 50):100)

        float xInc = (float) dx / steps;
        float yInc = (float) dy / steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {

            int px = Math.round(x);//math.round(2.3):2;(math.round(2.7):3) because pixel value in integer
            int py = Math.round(y);
            // DOTTED LINE
            if (style.equals("dotted")) {

                if (i % 6 == 0) {
                    g.fillRect(px, py, 2, 2);
                }
            }
            // THICK LINE
            else if (style.equals("thick")) {
                g.fillRect(px, py, 5, 5);
            }
            x = x + xInc;
            y = y + yInc;
        }
    }
    // BRESENHAM LINE DRAWING ALGORITHM
    void drawBresenham(Graphics g, int x1, int y1,
                       int x2, int y2, String style) {

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int err = dx - dy;
        int count = 0;
        while (true) {

            // DASHED LINE
            if (style.equals("dashed")) {

                if ((count / 8) % 2 == 0) {
                    g.fillRect(x1, y1, 3, 3);
                }
            }
            // SOLID LINE
            else if (style.equals("solid")) {

                g.fillRect(x1, y1, 3, 3);
            }
            // End condition
            if (x1 == x2 && y1 == y2) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
            count++;
        }
    }
    // RECTANGLE USING DDA
    void drawRectangleDDA(Graphics g, int left, int top,
                          int right, int bottom,
                          String style) {

        // Top
        drawDDA(g, left, top, right, top, style);

        // Right
        drawDDA(g, right, top, right, bottom, style);

        // Bottom
        drawDDA(g, right, bottom, left, bottom, style);

        // Left
        drawDDA(g, left, bottom, left, top, style);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        // OUTER RECTANGLE  DDA + DOTTED
        drawRectangleDDA(
             g,
                80, 80,
                620, 300,
                "dotted"
        );
        // DIAMOND BRESENHAM  Top point
        int topX = 350;
        int topY = 75;

        // Right point
        int rightX = 620;
        int rightY = 190;

        // Bottom point
        int bottomX = 350;
        int bottomY = 304;

        // Left point
        int leftX = 80;
        int leftY = 190;

        // Upper-right - DASHED
        drawBresenham(
                g,
                topX, topY,
                rightX, rightY,
                "solid"
        );
        // Lower-right - SOLID
        drawBresenham(
                g,
                rightX, rightY,
                bottomX, bottomY,
                "solid"
        );
        // Lower-left - DASHED
        drawBresenham(
                g,
                bottomX, bottomY,
                leftX, leftY,
                "solid"
        );
        // Upper-left - SOLID
        drawBresenham(
                g,
                leftX, leftY,
                topX, topY,
                "solid"
        );
        // INNER RECTANGLE DDA + THICK
        drawRectangleDDA(
                g,
                219, 129,
                479, 249,
                "thick"
        );
        g.setFont(new Font("Arial", Font.BOLD, 18));

        g.drawString(
                "DDA - Dotted & Thick Rectangle",
                180, 370
        );
        g.drawString(
                "Bresenham - Solid Diamond",
                170, 400
        );
    }
    public static void main(String[] args) {

        JFrame frame = new JFrame(
                "Computer Graphics"
        );
        compuGR panel = new compuGR();
        frame.add(panel);
        frame.setSize(720, 480);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}