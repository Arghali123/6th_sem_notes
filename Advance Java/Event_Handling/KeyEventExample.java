package Event_Handling;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class KeyEventExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        JLabel label = new JLabel("Move here");
        label.setBounds(200, 200, 100, 20);
        frame.add(label);

        label.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) { // Moved logic here
                int key = e.getKeyCode();
                int x = label.getX();
                int y = label.getY();

                if (key == KeyEvent.VK_UP) {
                    y = y - 10;
                }
                if (key == KeyEvent.VK_DOWN) {
                    y = y + 10;
                }
                if (key == KeyEvent.VK_LEFT) {
                    x = x - 10;
                }
                if (key == KeyEvent.VK_RIGHT) {
                    x = x + 10;
                }

                label.setLocation(x, y);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });

        frame.setVisible(true);
        label.setFocusable(true);
        label.requestFocus();
    }
}
