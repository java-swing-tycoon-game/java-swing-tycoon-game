import javax.swing.*;
import java.awt.*;

public class Play extends JFrame {
    Play() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");

        setLayout(new BorderLayout());
        showBackground();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);

        setVisible(true);
        pack();
    }

    void showBackground()
    {
        JLabel bgImageLabel = new JLabel(new ImageIcon("assets/img/edge.png"));
        setContentPane(bgImageLabel);

        JLabel mapImageLabel = new JLabel(new ImageIcon("assets/img/map.png"));
        setContentPane(mapImageLabel);
    }
}
