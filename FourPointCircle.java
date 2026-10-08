import java.awt.*;
import javax.swing.*;

public class FourPointCircle extends JPanel {

    void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 3, 3);
    }

    // Bresenham Circle Algorithm
    void drawCircle(Graphics g, int xc, int yc, int r) {

        int x = 0;
        int y = r;

        int d = 3 - 2 * r;

        while (x <= y) {

            // Only 4 symmetric points
            plot(g, xc + x, yc + y);
            plot(g, xc + x, yc - y);
            plot(g, xc + y, yc + x);
            plot(g, xc + y, yc - x);
            

            if (d < 0) {
                d = d + 4 * x + 6;
            }
            else {
                d = d + 4 * (x - y) + 10;
                y--;
                
            }
            x++;
            
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString("Bresenham Circle - 4 Point Symmetry",
                     220, 50);

        drawCircle(g, 400, 330, 150);
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("4 Point Circle");

        FourPointCircle panel = new FourPointCircle();

        frame.add(panel);

        frame.setSize(800, 650);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}