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
        mapPanel.setSize(958,667);

        JLabel mapImage = new JLabel(new ImageIcon("assets/img/map.png"));

        mapPanel.add(mapImage);
        bgImage.add(mapPanel, BorderLayout.CENTER);

        showTop();
        showBottom();
    }

    void showTop()
    {
        //상단 패널
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(0 , 50, 0 , 50));

        // 데이
        JPanel dayPanel = new JPanel();
        dayPanel.setOpaque(false);
        dayPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel day = new JLabel(new ImageIcon("assets/img/day.png"));
        dayPanel.add(day);

        // 아이템
        JPanel itemPanel = new JPanel();
        itemPanel.setOpaque(false);
        itemPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel item = new JLabel(new ImageIcon("assets/img/item.png"));
        JLabel itemCircle = new JLabel(new ImageIcon("assets/img/itemCircle.png"));
        itemCircle.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));
        itemPanel.add(item);
        itemPanel.add(itemCircle);

        top.add(dayPanel, BorderLayout.WEST);
        top.add(itemPanel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
    }

    void showBottom()
    {
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
        coinImage.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));
        Font font = new Font("궁서", Font.BOLD, 30);
        JLabel coinTxt = new JLabel("x 1 만원");
        coinTxt.setFont(font);
        coinTxt.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));

        timePanel.add(time);
        timePanel.add(timeBar);
        coinPanel.add(coin);
        coinPanel.add(coinImage);
        coinPanel.add(coinTxt);

        bottom.add(timePanel, BorderLayout.WEST);
        bottom.add(coinPanel, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }
}