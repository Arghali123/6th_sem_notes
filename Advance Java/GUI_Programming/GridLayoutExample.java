import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.GridLayout;

public class GridLayoutExample {
 public static void main(String[] args) {
    JFrame frame=new JFrame("Grid Layout Example");
    frame.setSize(700,600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    frame.setLayout(new GridLayout(2,3));

    JButton button1=new JButton("Button1");
    JButton button2=new JButton("Button2");
    JButton button3=new JButton("Button3");
    JButton button4=new JButton("Button4");
    JButton button5=new JButton("Button5");
    JButton button6=new JButton("Button6");
    
    frame.add(button1);
    frame.add(button2);
    frame.add(button3);
    frame.add(button4);
    frame.add(button5);
    frame.add(button6);

    frame.setVisible(true);
 }
}