import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class ShapeExample extends JPanel
{

        @Override 
        protected void paintComponent(Graphics g) {
       
            super.paintComponent(g);

            Graphics2D graphics2d=(Graphics2D)g;

            //Line 
            graphics2d.drawLine(50,50,250,250);
        }
    public static void main(String[] args)
    {
       JFrame frame=new JFrame("Shape Example");
       frame.setSize(700,600);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       frame.add(new ShapeExample());

       frame.setVisible(true);
    }
}