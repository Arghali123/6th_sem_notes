import javax.swing.JButton;
import javax.swing.JFrame;



import java.awt.BorderLayout;

public class BorderLayoutExample {
    public static void main(String[] args) {
        JFrame frame=new JFrame("Border Layout example");
        frame.setSize(700,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());

        JButton header=new JButton("Header");
        frame.add(header,BorderLayout.NORTH);

        JButton footer=new JButton("Footer");
        frame.add(footer,BorderLayout.SOUTH);


        JButton leftSideBar=new JButton("Left Sidebar");
        frame.add(leftSideBar,BorderLayout.WEST);

        JButton rightSideBar=new JButton("Right Sidebar");
        frame.add(rightSideBar,BorderLayout.EAST);

        JButton mainButton=new JButton("Main Body");
        frame.add(mainButton,BorderLayout.CENTER);



        frame.setVisible(true);
    }
}
