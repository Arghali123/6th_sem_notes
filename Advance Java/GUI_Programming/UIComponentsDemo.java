import javax.swing.*;
import java.awt.*;

public class UIComponentsDemo {
    public static void main(String[] args) {
        // Create the main window frame
        JFrame frame = new JFrame("Interactive Swing Components");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 420);
        frame.setLocationRelativeTo(null); // Center on screen

        // Set FlowLayout with alignment and gaps
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        // Styling defaults
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);

        // 1. JComboBox
        JLabel comboLabel = new JLabel("Favorite Language:");
        comboLabel.setFont(labelFont);
        String[] languages = {"Java", "Python", "C++", "JavaScript"};
        JComboBox<String> comboBox = new JComboBox<>(languages);
        comboBox.setPreferredSize(new Dimension(150, 25));

        // 2. JRadioButton (Grouped)
        JLabel radioLabel = new JLabel("Experience Level:");
        radioLabel.setFont(labelFont);
        JRadioButton rbBeginner = new JRadioButton("Beginner");
        JRadioButton rbPro = new JRadioButton("Pro", true);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(rbBeginner);
        radioGroup.add(rbPro);

        // Panel to group radio buttons together neatly
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        radioPanel.add(rbBeginner);
        radioPanel.add(rbPro);

        // 3. JCheckBox
        JCheckBox checkBox = new JCheckBox("Subscribe to Developer Newsletter");
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // 4. JSlider
        JLabel sliderLabel = new JLabel("Weekly Coding Hours: 20");
        sliderLabel.setFont(labelFont);
        JSlider slider = new JSlider(0, 50, 20);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(300, 50));

        // Update slider label on drag
        slider.addChangeListener(e -> 
            sliderLabel.setText("Weekly Coding Hours: " + slider.getValue())
        );

        // 5. JButton & JOptionPane
        JButton submitButton = new JButton("Submit Profile");
        submitButton.setBackground(new Color(50, 120, 220));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(200, 35));

        // Display JOptionPane on click
        submitButton.addActionListener(e -> {
            String selectedLang = (String) comboBox.getSelectedItem();
            String level = rbBeginner.isSelected() ? "Beginner" : "Pro";
            boolean subscribed = checkBox.isSelected();
            int hours = slider.getValue();

            String message = String.format(
                "Profile Summary:\n• Language: %s\n• Level: %s\n• Hours/Week: %d\n• Newsletter: %s",
                selectedLang, level, hours, (subscribed ? "Yes" : "No")
            );

            JOptionPane.showMessageDialog(frame, message, "Submission Successful", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add all components to the frame in order
        frame.add(comboLabel);
        frame.add(comboBox);
        frame.add(radioLabel);
        frame.add(radioPanel);
        frame.add(checkBox);
        frame.add(sliderLabel);
        frame.add(slider);
        frame.add(submitButton);

        // Display frame
        frame.setVisible(true);
    }
}