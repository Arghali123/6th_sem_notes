// package Event_Handling;

// import java.awt.event.KeyEvent;
// import java.awt.event.KeyListener;
// import javax.swing.JFrame;
// import javax.swing.JLabel;

// public class KeyEventExample {
//     public static void main(String[] args) {
//         JFrame frame = new JFrame();
//         frame.setSize(700, 600);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setLayout(null);
        
//         JLabel label = new JLabel("Move here");
//         label.setBounds(200, 200, 100, 20);
//         frame.add(label);

//         label.addKeyListener(new KeyListener() {
//             @Override
//             public void keyPressed(KeyEvent e) { // Moved logic here
//                 int key = e.getKeyCode();
//                 int x = label.getX();
//                 int y = label.getY();

//                 if (key == KeyEvent.VK_UP) {
//                     y = y - 10;
//                 }
//                 if (key == KeyEvent.VK_DOWN) {
//                     y = y + 10;
//                 }
//                 if (key == KeyEvent.VK_LEFT) {
//                     x = x - 10;
//                 }
//                 if (key == KeyEvent.VK_RIGHT) {
//                     x = x + 10;
//                 }

//                 label.setLocation(x, y);
//             }

//             @Override
//             public void keyTyped(KeyEvent e) {
                
//             }

//             @Override
//             public void keyReleased(KeyEvent e) {
                
//             }
//         });

//         frame.setVisible(true);
//         label.setFocusable(true);
//         label.requestFocus();
//     }
// }


package AdapterClass;


import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Layout Management");
        frame.setSize(700, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // JTextField textfield = new JTextField(40);
        // frame.add(textfield);

        int X = 50;
        int Y = 50;

        JLabel label = new JLabel("move me");
        label.setBounds(X, Y, 100, 50);
        frame.add(label);

        label.addKeyListener(new KeyAdapter() {
            
        
            public void keyTyped(KeyEvent e) {
                System.out.println("hello");
            }

            public void keyPressed(KeyEvent e) {
                System.out.println("Hellloo");
                int X = label.getX();
                int Y = label.getY();
                int key = e.getKeyCode();
                System.out.println(key);

                if (key == KeyEvent.VK_UP) {
                    Y = Y - 10;
                }
                if (key == KeyEvent.VK_DOWN) {
                    Y = Y + 10;
                }
                if (key == KeyEvent.VK_LEFT) {
                    X = X - 10;
                }
                if (key == KeyEvent.VK_RIGHT) {
                    X = X + 10;
                }
                label.setLocation(X, Y);
            }

            // public void keyReleased(KeyEvent e) {

            // }
        });
        
        frame.setVisible(true);
        label.requestFocusInWindow();
    }
}