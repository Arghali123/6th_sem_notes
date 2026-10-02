import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageInsertion {
    public static void main(String[] args) {
        JFrame frame=new JFrame();
        frame.setSize(1000,900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon img=new ImageIcon("messi.png");

        //Put the image in jlabel or jpanel
        JLabel label=new JLabel(img);
        
        frame.add(label);
        frame.setVisible(true);
    }
}
