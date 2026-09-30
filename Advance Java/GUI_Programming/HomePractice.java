import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class HomePractice {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Home Practice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 420);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JLabel comboLabel = new JLabel("Favorite Languages");
        String[] languages = { "Java", "Python", "C++", "JavaScript" };
        JComboBox<String> comboBox = new JComboBox<>(languages);
        comboBox.setPreferredSize(new Dimension(150, 25));

        JLabel radioLabel = new JLabel("Experience Label");
        JRadioButton rbBeginner = new JRadioButton("Beginner");
        JRadioButton rbPro = new JRadioButton("Pro", true);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(rbBeginner);
        radioGroup.add(rbPro);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        radioPanel.add(rbBeginner);
        radioPanel.add(rbPro);

        JCheckBox checkBox = new JCheckBox("Subscribe to Developer Newsletter");

        JLabel sliderLabel = new JLabel("Weekly Coding hours: 20");
        JSlider slider = new JSlider(0, 50, 20);

        String name = JOptionPane.showInputDialog("What is your name?");
        System.out.println("Hello, " + name);

        String ageInput = JOptionPane.showInputDialog("Enter your age:");
        int age = Integer.parseInt(ageInput);
        

        frame.add(comboLabel);
        frame.add(comboBox);
        frame.add(radioLabel);
        frame.add(radioPanel);
        frame.add(checkBox);
        frame.add(sliderLabel);
        frame.add(slider);

        frame.setVisible(true);
    }
}
