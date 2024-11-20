package Scenes;

import Character.Player;
import Character.Npc;
import Character.BlackConsumer;
import GameManager.FontManager;
import GameManager.*;

import javax.swing.*;
import java.awt.*;

public class Play extends JFrame {
    private JLabel time; // 시간바
    private JPanel timePanel;
    private int coinAmount = 0;  // 초기 코인 금액

    public Play() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");

        setLayout(new BorderLayout());
        showCharacter();
        showBackground();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1038, 805);
        setVisible(true);

        // startDayTimer();  // 타이머
    }

    void showCharacter()
    {
        // Npc 생성
        Npc npc = new Npc();
        npc.setBounds(0, 0, 1024, 768);
        npc.setOpaque(false);
        add(npc);

        // 악성 Npc 생성
//        BlackConsumer bc = new BlackConsumer();
//        bc.setBounds(0, 0, 1024, 768);
//        bc.setOpaque(false);
//        layeredPane.add(bc, Integer.valueOf(2));  // map 위에 오도록 우선순위 설정

        // Player 생성
        Player player = new Player();
        player.setBounds(0, 0, 1024, 768);
        add(player);

        // 캐릭터들 클릭 되도록
        ClickManager clickManager = new ClickManager();

        clickManager.setClickList(npc);
        clickManager.setClickList(player);

        addMouseListener(clickManager);
    }

    void showBackground()
    {
        // 겹칠 패널 생성
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setLayout(null);

        // mapPanel을 layeredPane에 추가
        JPanel map = showMap();
        map.setBounds(0, 0, 1024, 768);
        layeredPane.add(map, Integer.valueOf(0));

        // top 패널을 layeredPane에 추가
        JPanel top = showTop();
        top.setBounds(65, 0, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        layeredPane.add(top, Integer.valueOf(100));  // 위쪽 레이어

        // bottom 패널을 layeredPane에 추가
        JPanel bottom = showBottom();
        bottom.setBounds(65, 700, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        layeredPane.add(bottom, Integer.valueOf(100));  // 위쪽 레이어

        add(layeredPane, BorderLayout.CENTER);
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
        time = new JLabel(new ImageIcon("assets/img/time.png"));
        time.setBounds(-2, 9, time.getIcon().getIconWidth(), time.getIcon().getIconHeight()); // TIME 이미지

        timePanel = new JPanel();
        timePanel.setOpaque(false);    // 해당 패널 배경 투명도
        timePanel.add(time); // TIME 이미지 따로 먼저 추가

        // ProgressPane 추가
        ProgressPaneManager progressManager = new ProgressPaneManager();
        JPanel progressPane = progressManager.getProgressPane();
        bottom.add(progressPane, BorderLayout.CENTER);  // progressPane을 하단 패널 중앙에 추가

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
        bottom.add(timePanel, BorderLayout.WEST);

        // coin 머시깽이들
        coinPanel.add(coin);
        coinPanel.add(coinImage);
        coinPanel.add(coinTxt);
        bottom.add(coinPanel, BorderLayout.EAST);

        return bottom;
    }

    // 코인 금액 변경될 때 함수
    void updateCoinAmount(int amount) {
        coinAmount += amount;  // 코인 금액 +/-
        JLabel coinTxt = (JLabel) ((JPanel) ((BorderLayout) getContentPane().getLayout())
                .getLayoutComponent(BorderLayout.EAST)).getComponent(2);
        // coinTxt를 찾아서 getComponent()이벤트를 발생한 컴포넌트 반환하기 (인덱스 2: coinTxt)

        coinTxt.setText("x " + coinAmount + " 만원");  // 코인 금액 업데이트
    }

    /*
    추후에 수정해야 할 것 같아서! 그냥 냅둬주세요!!
    // 하단 시간바 크기 조정
    void updateTimeBar() {

    }

    // 응원봉 추가했을 때 타이머 변동
    void useLStick() {
        if (realTime + LStick.getTimeIncrease() <= 60) { // 최대 60초 = realTime으로 정해둔 거
            LStick.use();
            realTime += LStick.getTimeIncrease();
            updateTimeBar();
        }
        else realTime = 60;
    }*/

    public static void main(String[] args) {
        new Play();
    }
}