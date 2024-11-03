import javax.swing.*;

public class Play extends JFrame {
    Play() {
        setTitle("Main");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 레이아웃 널로 설정 --> 위치 지정 마음대로

        ImageIcon bgImage = new ImageIcon("assets/img/edge.png"); // 배경
        JLabel bgImageLabel = new JLabel(bgImage);
        bgImageLabel.setBounds(0, 0, getWidth(), getHeight());

        ImageIcon map = new ImageIcon("assets/img/map.png"); // 맵 이미지
        JLabel mapImage = new JLabel(map);
        mapImage.setBounds(0, 0, getWidth(), getHeight());

        add(mapImage);
        add(bgImageLabel);

        setVisible(true);
    }
}
