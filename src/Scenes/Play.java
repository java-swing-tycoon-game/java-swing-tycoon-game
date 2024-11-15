package Scenes;

import Character.Player;
import Character.Npc;
import GameManager.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Play extends JFrame {
    private JLabel timeBarLabel, basicTimeBar, time; // 시간바
    private JPanel timePanel;
    private Timer dayTimer; // 각 데이의 타이머
    private int realTime = 60;  // 각 데이를 60초로 설정 (일단 임시로.. 데이 증가하면 여기를 같이 수정하면 될듯)

    private int coinAmount = 500;  // 초기 코인 금액

    Play() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");

        setLayout(new BorderLayout());
        showBackground();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1038, 805);
        setVisible(true);
    }

    void showBackground()
    {
        // 겹칠 패널 생성
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setLayout(null);  // null 레이아웃으로 절대 위치 설정 가능

        // mapPanel을 layeredPane에 추가
        JPanel map = showMap();
        map.setBounds(0, 0, 1024, 768);
        layeredPane.add(map, Integer.valueOf(0));

        Npc npc = new Npc();
        npc.setBounds(0, 0, 1024, 768);
        npc.setOpaque(false);
        layeredPane.add(npc, Integer.valueOf(2));  // map 위에 오도록 우선순위 설정

        // Player 객체 추가
        Player player = new Player();
        player.setBounds(0, 0, 1024, 768);
        layeredPane.add(player, Integer.valueOf(2));

        // top 패널을 layeredPane에 추가
        JPanel top = showTop();
        top.setBounds(65, 0, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        layeredPane.add(top, Integer.valueOf(100));  // 위쪽 레이어

        // bottom 패널을 layeredPane에 추가
        JPanel bottom = showBottom();
        bottom.setBounds(65, 700, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        layeredPane.add(bottom, Integer.valueOf(100));  // 위쪽 레이어

        add(layeredPane, BorderLayout.CENTER);

        startDayTimer();  // 타이머
    }

    JPanel showMap()
    {
        JPanel mapPanel = new JPanel();
        mapPanel.setOpaque(false);
        mapPanel.setLayout(new BorderLayout());

        ImageIcon map = new ImageIcon("assets/img/map.png");
        mapPanel.add(new JLabel(map), BorderLayout.CENTER);

        return mapPanel;
    }

    JPanel showTop()
    {
        //상단 패널
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BorderLayout());

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

        return top;
    }

    JPanel showBottom()
    {
        // 하단 패널
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BorderLayout());

        // 시간
        timePanel = new JPanel();
        timePanel.setOpaque(false);
        timePanel.setLayout(null);  // 시간바 겹치게 하기 위함

        time = new JLabel(new ImageIcon("assets/img/time.png"));
        time.setBounds(-2, 9, time.getIcon().getIconWidth(), time.getIcon().getIconHeight());  // TIME 이미지

        // 초기 시간바 크기 설정
        int initialWidth = 440;  // 초기 너비 400 고정
        ImageIcon timeBarIcon = new ImageIcon("assets/img/timeBar.png");

        timeBarLabel = new JLabel(new ImageIcon(timeBarIcon.getImage().getScaledInstance(initialWidth, timeBarIcon.getIconHeight(), Image.SCALE_SMOOTH)));
        timeBarLabel.setBounds(time.getIcon().getIconWidth() + 2, 15, initialWidth, timeBarLabel.getIcon().getIconHeight());

        basicTimeBar = new JLabel(new ImageIcon("assets/img/basicTimeBar.png"));
        basicTimeBar.setBounds(time.getIcon().getIconWidth() + 2, 10, 440, basicTimeBar.getIcon().getIconHeight());

        timePanel.setPreferredSize(new Dimension(initialWidth, timeBarLabel.getHeight()));

        // 코인
        JPanel coinPanel = new JPanel();
        coinPanel.setOpaque(false);
        coinPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel coin = new JLabel(new ImageIcon("assets/img/coin.png"));
        JLabel coinImage = new JLabel(new ImageIcon("assets/img/coinImage.png"));
        coinImage.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));

        // 코인 텍스트 확인 부분
        Font font = FontManager.loadFont(30);
        JLabel coinTxt = new JLabel("x " + coinAmount + "만원");  // 코인 금액 표시
        coinTxt.setFont(font);
        coinTxt.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));

        // timePanel 관련 요소들 추가하기
        timePanel.add(time);
        timePanel.add(timeBarLabel);
        timePanel.add(basicTimeBar);
        bottom.add(timePanel, BorderLayout.WEST);

        // coin 머시깽이들
        coinPanel.add(coin);
        coinPanel.add(coinImage);
        coinPanel.add(coinTxt);
        bottom.add(coinPanel, BorderLayout.EAST);

        return bottom;
    }

    // 타임 관련 함수들..
    void startDayTimer() {
        dayTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realTime--;
                updateTimeBar();  // 시간바 업데이트

                // 타이머 종료
                if (realTime <= 0) {
                    dayTimer.stop();  // 타이머 중지
                    resetDay();
                }
            }
        });
        dayTimer.start();
    }

    // 하단 시간바 크기 조정
    void updateTimeBar() {
        int newWidth = (int) (440 * (realTime / 60.0)); // 60초 기준으로 시간바 너비 계산

        // 시간바 이미지를 줄어든 크기로 설정
        ImageIcon resizedTimeBar = new ImageIcon(new ImageIcon("assets/img/timeBar.png")
                .getImage().getScaledInstance(newWidth, timeBarLabel.getHeight(), Image.SCALE_SMOOTH));
        timeBarLabel.setIcon(resizedTimeBar);

        // timePanel이랑 기본 시간바 크기는 고정 back으로 고정시켜두기
        basicTimeBar.setBounds(time.getIcon().getIconWidth() + 2, 10, 440, basicTimeBar.getIcon().getIconHeight());
        timePanel.setPreferredSize(new Dimension(440 + time.getIcon().getIconWidth(), timePanel.getHeight()));

        // timeBarLabel 위치 고정하고 너비만 조정
        timeBarLabel.setBounds(time.getIcon().getIconWidth() + 2, 15, newWidth, timeBarLabel.getHeight());

        timePanel.revalidate(); // 컴포넌트들 재배치하는 거
        timePanel.repaint();
    }



    // 데이 다시 60초로 리셋
    void resetDay() {
        realTime = 60;
        startDayTimer();  // 타이머 다시 시작
    }

    // 코인 금액 변경될 때 함수
    void updateCoinAmount(int amount) {
        coinAmount += amount;  // 코인 금액 +/-
        JLabel coinTxt = (JLabel) ((JPanel) ((BorderLayout) getContentPane().getLayout())
                .getLayoutComponent(BorderLayout.EAST)).getComponent(2);
        // coinTxt를 찾아서 getComponent()이벤트를 발생한 컴포넌트 반환하기 (인덱스 2: coinTxt)

        coinTxt.setText("x " + coinAmount + "만원");  // 코인 금액 업데이트
    }

    public static void main(String[] args) {
        new Play();
    }
}
