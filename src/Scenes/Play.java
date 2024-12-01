package Scenes;

import Character.Player;
import GameManager.StartManager;
import GameManager.*;
import Goods.Goods;
import Items.ItemPanel;
import Character.bcAns;
import Character.MovePlayer;
import Items.LightStick;
import Items.Tshirt;

import javax.swing.*;
import java.awt.*;

public class Play extends JFrame {
    private static JLayeredPane mainPanel;
    public static Play instance;

    private JLabel time; // 시간바
    private JPanel timePanel;
    private int coinAmount = 0;  // 초기 코인 금액

    private bgmManager bgm;
    private CoinManager coinManager;
    private NpcManager npcManager;
    private ClickManager clickManager;
    private DayManager dayManager;
    private ProgressPaneManager progressPaneManager;
    public static boolean[] itemArray = {true, false, false, false}; // 기본값 false
    private String[] itemIcons = {
            "assets/img/item/itemCircle.png",
            "assets/img/item/sloganItem.png",
            "assets/img/item/stickItem.png",
            "assets/img/item/tshirtItem.png"
    };
    private ItemPanel itemPanel;

    public static Player player;

    private ItemManager itemManager; // ItemManager 인스턴스를 여기에 추가

    public Play() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dayManager = DayManager.getInstance();
        progressPaneManager = new ProgressPaneManager(dayManager); // ProgressPaneManager 초기화

        setMainPanel();
        showCharacter();

        instance = this;
        playBgm();

        setSize(1038, 805);
        setLocationRelativeTo(null);
        setVisible(true);

        progressPaneManager.startDayTimer(); // 게임 시작과 함께 Day 타이머 시작
    }

    public JLayeredPane getMainPanel() {
        return mainPanel;
    }

    void setupClickManager() {
        clickManager = new ClickManager(); // ClickManager 초기화
        mainPanel.addMouseListener(clickManager); // MouseListener 등록
    }

    void playBgm() {
        bgm = new bgmManager("assets/bgm/playBgm.wav", true);
        bgm.toggleMusic(); // 음악 자동 재생

        JLabel musicLabel = bgm.createMusicLabel();

        JPanel musicPanel = new JPanel();
        musicPanel.setOpaque(false);
        musicPanel.setLayout(new BorderLayout());
        musicPanel.add(musicLabel, BorderLayout.CENTER);
        musicPanel.setBounds(940, -5, 100, 100);

        mainPanel.add(musicPanel, Integer.valueOf(100));

        // m 누르면 재생/정지
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_M) {
                    bgm.toggleMusic(); // M 버튼으로 on/off
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    void showCharacter()
    {
        setupClickManager();

        // ItemManager 생성
        ItemManager itemManager = ItemManager.getInstance();

        // Player 생성
        player = new Player(itemManager);
        player.setBounds(0, 0, 1024, 768);
        player.setOpaque(false);
        mainPanel.add(player, Integer.valueOf(110));
        ClickManager.setClickEventList(player);

        // npc 생성
        npcManager = new NpcManager(mainPanel, player, 5);

        // Goods 생성
        Goods goods = new Goods(itemManager);
        goods.setBounds(0, 0, 1024, 768);
        goods.setOpaque(false);
        mainPanel.add(goods, Integer.valueOf(100));
    }

    void setMainPanel() {

        mainPanel = new JLayeredPane();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        showBackground();

        setContentPane(mainPanel);
    }

    void showBackground()
    {
        // mapPanel을 layeredPane에 추가
        JPanel map = showMap();
        map.setBounds(0, 0, 1024, 768);
        mainPanel.add(map, Integer.valueOf(0));

        // place 패널을 layeredPane에 추가
        SimplePlaceManager SimplePlaceManager = new SimplePlaceManager();
        SimplePlaceManager.setOpaque(false);
        SimplePlaceManager.setBounds(0, 0, 1024, 768); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        mainPanel.add(SimplePlaceManager, Integer.valueOf(50));  // 위쪽 레이어
        SimplePlaceManager.setPlaceVisible(1, true);

        // top 패널을 layeredPane에 추가
        JPanel top = showTop();
        top.setBounds(65, 0, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        mainPanel.add(top, Integer.valueOf(100));  // 위쪽 레이어

        // bottom 패널을 layeredPane에 추가
        JPanel bottom = showBottom();
        bottom.setBounds(65, 700, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        mainPanel.add(bottom, Integer.valueOf(100));  // 위쪽 레이어
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

    JPanel showTop() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BorderLayout());

        // 데이
        JPanel dayPanel = dayManager.getDayPanel();

        itemPanel = new ItemPanel(progressPaneManager);
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

        // timePanel 관련 요소들 추가하기
        bottom.add(timePanel, BorderLayout.WEST);

        // ProgressPane 추가
        JPanel progressPane = progressPaneManager.getProgressPane();
        bottom.add(progressPane, BorderLayout.CENTER);  // progressPane을 하단 패널 중앙에 추가

        // 코인 관리
        coinManager = new CoinManager();  // CoinManager 객체 생성

        // 코인 관련 요소들 추가하기
        bottom.add(coinManager.getCoinPanel(), BorderLayout.EAST);

        return bottom;
    }

    // 코인 금액 변경될 때 함수
    void updateCoinAmount(int amount) {
        CoinManager.updateCoinAmount(amount);  // CoinManager를 통해 코인 금액 업데이트
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

     */

    public static void main(String[] args) {
        DayManager dayManager = DayManager.getInstance();

        // Day 1 시작
        new StartManager(dayManager, true);

        // Day 2 이후 (예시)
        new StartManager(dayManager, false);
    }
}