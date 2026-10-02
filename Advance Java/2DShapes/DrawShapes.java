import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;


public class DrawShapes extends JPanel
{
    @Override 
    protected  void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d=(Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //1. Rectangle
        g2d.setColor(new Color(230, 80, 80));
        g2d.fillRect(50, 50, 180, 100); 
        g2d.setColor(Color.BLACK);
        g2d.drawRect(50, 50, 180, 100);
        g2d.drawString("Rectangle (180x100)", 50, 170);

        // 2. Draw Square 
        g2d.setColor(Color.yellow);
        g2d.fillRect(280, 50, 110, 110); // (x, y, side, side)
        g2d.setColor(Color.BLACK);
        g2d.drawRect(280, 50, 110, 110);
        g2d.drawString("Square (110x110)", 280, 170);

        // 3. Draw Circle 
        g2d.setColor(Color.BLUE);
        g2d.fillOval(440, 50, 110, 110); // (x, y, diameter_x, diameter_y)
        g2d.setColor(Color.BLACK);
        g2d.drawOval(440, 50, 110, 110);
        g2d.drawString("Circle (Diameter: 110)", 440, 170);

        
        // 4. Draw and fill the ellipse (oval)
        
        g2d.setColor(Color.GREEN);
        g2d.fillOval(50, 230, 300, 150);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(50, 230, 300, 150);
        

    }
    public static void main(String[] args) {
        JFrame frame=new JFrame("Making shapes");
        DrawShapes panel=new DrawShapes();

        frame.add(panel);
        frame.setSize(700,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center window on screen
        frame.setVisible(true);
    }
}