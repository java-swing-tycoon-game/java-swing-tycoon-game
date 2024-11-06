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
    }

    void showBackground()
    {
        // 배경 이미지
        JLabel bgImage = new JLabel(new ImageIcon("assets/img/edge.png"));
        setContentPane(bgImage);
        bgImage.setLayout(new BorderLayout());

        // 맵 이미지
        JPanel mapPanel = new JPanel();
        mapPanel.setOpaque(false);
        mapPanel.setLayout(new FlowLayout());
        mapPanel.setSize(958,667);

        JLabel mapImage = new JLabel(new ImageIcon("assets/img/map.png"));


        // 하단 패널
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(0 , 50, 0 , 50));

        // 시간
        JPanel timePanel = new JPanel();
        timePanel.setOpaque(false);
        timePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel time = new JLabel(new ImageIcon("assets/img/time.png"));
        JLabel timeBar = new JLabel(new ImageIcon("assets/img/timeBar.png"));

        // 코인
        JPanel coinPanel = new JPanel();
        coinPanel.setOpaque(false);
        coinPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel coin = new JLabel(new ImageIcon("assets/img/coin.png"));
        JLabel coinImage = new JLabel(new ImageIcon("assets/img/coinImage.png"));
        Font font = new Font("궁서", Font.BOLD, 30);
        JLabel coinTxt = new JLabel("x 1 만원");
        coinTxt.setFont(font);


        mapPanel.add(mapImage);

        timePanel.add(time);
        timePanel.add(timeBar);
        coinPanel.add(coin);
        coinPanel.add(coinImage);
        coinPanel.add(coinTxt);

        bottom.add(timePanel, BorderLayout.WEST);
        bottom.add(coinPanel, BorderLayout.EAST);

        bgImage.add(bottom, BorderLayout.SOUTH);
        bgImage.add(mapPanel, BorderLayout.CENTER);
    }
}