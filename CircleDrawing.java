import java.awt.*;
import javax.swing.*;
import java.util.Scanner;

public class CircleDrawing extends JPanel {

    static int algorithmChoice;
    static int styleChoice;

    // --------------------------------------------------
    // Plot one pixel
    // --------------------------------------------------
    void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 2, 2);
    }

    // --------------------------------------------------
    // DDA Circle Drawing Algorithm
    // --------------------------------------------------
    void drawDDACircle(Graphics g, int xc, int yc, int r, int style) {

        double theta = 0;
        double step = 1.0 / r;
        int count = 0;

        while (theta <= 2 * Math.PI) {

            int x = (int) Math.round(xc + r * Math.cos(theta));
            int y = (int) Math.round(yc + r * Math.sin(theta));

            boolean draw = false;

            if (style == 1) {
                // Solid
                draw = true;
            }
            else if (style == 2) {
                // Dotted
                if (count % 5 == 0) {
                    draw = true;
                }
            }
            else if (style == 3) {
                // Dashed
                if (count % 12 < 7) {
                    draw = true;
                }
            }

            if (draw) {
                plot(g, x, y);
            }

            theta = theta + step;
            count++;
        }
    }

    // --------------------------------------------------
    // Bresenham Circle Drawing Algorithm
    // --------------------------------------------------
    void drawBresenhamCircle(Graphics g, int xc, int yc,
                             int r, int style) {

        int x = 0;
        int y = r;

        int d = 3 - (2 * r);
        int count = 0;

        while (x <= y) {

            boolean draw = false;

            if (style == 1) {
                // Solid
                draw = true;
            }
            else if (style == 2) {
                // Dotted
                if (count % 3 == 0) {
                    draw = true;
                }
            }
            else if (style == 3) {
                // Dashed
                if (count % 6 < 4) {
                    draw = true;
                }
            }

            if (draw) {
               plotEightPoints(g, xc, yc, x, y);
            }

            if (d < 0) {
                d = d + (4 * x) + 6;
            }
            else {
                d = d + 4 * (x - y) + 10;
                y--;
            }

            x++;
            count++;
        }
    }

    // --------------------------------------------------
    // Midpoint Circle Drawing Algorithm
    // --------------------------------------------------
    void drawMidpointCircle(Graphics g, int xc, int yc,
                            int r, int style) {

        int x = 0;
        int y = r;

        int p = 1 - r;
        int count = 0;

        while (x <= y) {

            boolean draw = false;

            if (style == 1) {
                // Solid
                draw = true;
            }
            else if (style == 2) {
                // Dotted
                if (count % 3 == 0) {
                    draw = true;
                }
            }
            else if (style == 3) {
                // Dashed
                if (count % 6 < 4) {
                    draw = true;
                }
            }

            //if (draw) {
            //    plotEightPoints(g, xc, yc, x, y);
            //}

            x++;

            if (p < 0) {
                p = p + (2 * x) + 1;
            }
            else {
                y--;
                p = p + (2 * (x - y)) + 1;
            }

            count++;
        }
    }

    // --------------------------------------------------
    // Plot 8 Symmetrical Points
    // --------------------------------------------------
    void plotEightPoints(Graphics g, int xc, int yc,
                         int x, int y) {

       
    }

    // --------------------------------------------------
    // Display Circle
    // --------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setFont(new Font("Arial", Font.BOLD, 20));

        String algorithm = "";
        String style = "";

        if (algorithmChoice == 1) {
            algorithm = "DDA Circle";
        }
        else if (algorithmChoice == 2) {
            algorithm = "Bresenham Circle";
        }
        else {
            algorithm = "Midpoint Circle";
        }

        if (styleChoice == 1) {
            style = "Solid";
        }
        else if (styleChoice == 2) {
            style = "Dotted";
        }
        else {
            style = "Dashed";
        }

        g.drawString(algorithm + " - " + style, 250, 50);

        int xc = 400;
        int yc = 330;
        int radius = 150;

        if (algorithmChoice == 1) {

            drawDDACircle(g, xc, yc, radius, styleChoice);

        }
        else if (algorithmChoice == 2) {

            drawBresenhamCircle(g, xc, yc, radius, styleChoice);

        }
        else {

            drawMidpointCircle(g, xc, yc, radius, styleChoice);
        }
    }

    // --------------------------------------------------
    // Main Method
    // --------------------------------------------------
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("--------------------------------------");
        System.out.println("       CIRCLE DRAWING PROGRAM");
        System.out.println("--------------------------------------");

        // Algorithm menu
        System.out.println("\nSelect Circle Drawing Algorithm:");
        System.out.println("1. DDA Circle Drawing Algorithm");
        System.out.println("2. Bresenham Circle Drawing Algorithm");
        System.out.println("3. Midpoint Circle Drawing Algorithm");

        System.out.print("Enter your choice: ");
        algorithmChoice = sc.nextInt();

        // Style menu
        System.out.println("\nSelect Circle Style:");
        System.out.println("1. Solid");
        System.out.println("2. Dotted");
        System.out.println("3. Dashed");

        System.out.print("Enter your choice: ");
        styleChoice = sc.nextInt();

        // Validation
        if (algorithmChoice < 1 || algorithmChoice > 3) {

            System.out.println("Invalid algorithm choice!");
            sc.close();
            return;
        }

        if (styleChoice < 1 || styleChoice > 3) {

            System.out.println("Invalid style choice!");
            sc.close();
            return;
        }

        // Create window
        JFrame frame = new JFrame("Circle Drawing Algorithms");

        CircleDrawing panel = new CircleDrawing();

        frame.add(panel);

        frame.setSize(800, 650);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        sc.close();
    }
}