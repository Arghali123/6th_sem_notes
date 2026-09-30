import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Calculator");
        frame.setSize(700, 600);
        frame.setLayout(null);

        JLabel input1 = new JLabel("Input1");
        input1.setBounds(50, 0, 150, 30);
        JTextField input1Field = new JTextField();
        input1Field.setBounds(50, 50, 150, 30);

        JLabel input2 = new JLabel("Input2");
        input2.setBounds(250, 0, 150, 30);
        JTextField input2Field = new JTextField();
        input2Field.setBounds(250, 50, 150, 30);

        JButton addBtn = new JButton("Add");
        addBtn.setBounds(50, 100, 100, 50);

        JButton subBtn = new JButton("Sub");
        subBtn.setBounds(250, 100, 100, 50);

        JLabel result = new JLabel("Result: ");
        result.setBounds(50, 200, 150, 50);

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = Integer.parseInt(input1Field.getText());
                int b = Integer.parseInt(input2Field.getText());
                int output = a + b;

                result.setText("Result: " + output);
                input1.setText("");
                input2.setText("");

                System.out.println(output);

            }
        });

        subBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = Integer.parseInt(input1Field.getText());
                int b = Integer.parseInt(input2Field.getText());
                int output = a - b;
                result.setText("Result: " + output);
                input1.setText("");
                input2.setText("");
                System.out.println(output);

            }
        });

        frame.add(input1);
        frame.add(input1Field);
        frame.add(input2);
        frame.add(input2Field);
        frame.add(addBtn);
        frame.add(subBtn);
        frame.add(result);

        frame.setVisible(true);

    }
}
