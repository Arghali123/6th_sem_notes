package Event_Handling;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;

public class ActionEventExample {
    public static void main(String[] args) {
    JFrame frame=new JFrame("ActionEvent Example");
    frame.setSize(700,600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new FlowLayout());

    JButton clickMe=new JButton("Click me");

    clickMe.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e){
                System.out.println("Button Clicked");
               }

    });

    frame.add(clickMe);

    frame.setVisible(true);
    }
}
