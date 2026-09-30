package GUI_Programming;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class GuiComponents {
    public static void main(String[] args) {
        //1. Create frame
        JFrame frame=new JFrame("GUI Components example");

        //2. Create default size of the frame
        frame.setSize(700,600);

        //3. Set Layout
        frame.setLayout(null);

        // //4. Add button components
        // JButton firstButton=new JButton("First button");
        // firstButton.setBounds(100,0,150,50);

        // JButton secondButton=new JButton("Second button");
        // secondButton.setBounds(100,60,150,50);

        // JButton thirdButton=new JButton("Third button");
        // thirdButton.setBounds(100,120,150,50);

        // frame.add(firstButton);
        // frame.add(secondButton);
        // frame.add(thirdButton);

        // JLabel label1=new JLabel("Write");
        // label1.setBounds(500,70,150,30);
        // JTextArea area=new JTextArea("Do something...");
        // area.setBounds(500,100,150,30);

        // frame.add(label1);
        // frame.add(area);

        //Checkbox componet
        JLabel checkboxLabel=new JLabel("Hobbies");
        checkboxLabel.setBounds(100,0,150,30);

        JCheckBox cricket=new JCheckBox("Cricket");
        cricket.setBounds(50,50,150,50);

        JCheckBox football=new JCheckBox("Football");
        football.setBounds(210,50,150,50);

        JCheckBox chess=new JCheckBox("Chess");
        JCheckBox tabbleTannis=new JCheckBox("Table Tennis");

        frame.add(checkboxLabel);
        frame.add(cricket);
        frame.add(football);
        frame.add(chess);
        frame.add(tabbleTannis);

        frame.setVisible(true);
    }
}
