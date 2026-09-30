import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;


public class FlowLayoutExample {
    public static void main(String[] args) {
        JFrame frame=new JFrame("FlowLayout Example");
        frame.setSize(700,600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new FlowLayout());
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton firstButton=new JButton("First Button");
        JButton secondButton=new JButton("Second Button");
        JButton thirdButton=new JButton("Third Button");
        JButton fourthButton=new JButton("Fourth Button");

        frame.add(firstButton);
        frame.add(secondButton);
        frame.add(thirdButton);
        frame.add(fourthButton);

        frame.setVisible(true);

        
    }
}
