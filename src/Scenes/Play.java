package Scenes;

import Character.Player;
import GameManager.StartManager;
import GameManager.*;
import Goods.Goods;
import Items.ItemPanel;
import Character.bcAns;
import Character.MovePlayer;
import Items.LightStick;
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
    private static DayManager dayManager;
    private ProgressPaneManager progressPaneManager;
    public static boolean[] itemArray = {true, false, false, false}; // 기본값 false
    private String[] itemIcons = {
            "assets/img/item/itemCircle.png",
            "assets/img/item/sloganItem.png",
            "assets/img/item/stickItem.png",
            "assets/img/item/tshirtItem.png"
    };
    private ItemPanel itemPanel;

    private ItemManager itemManager; // ItemManager 인스턴스를 여기에 추가

    public Play() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");
        instance = this;
        setMainPanel();
        showCharacter();
        ItemUse();

        playBgm();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this.progressPaneManager = new ProgressPaneManager(this);

        setSize(1038, 805);
        setVisible(true);
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
        Player player = new Player(itemManager);
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

        // top 패널을 layeredPane에 추가
        JPanel top = showTop();
        top.setBounds(65, 0, 900, 100); // 위치와 크기를 설정하여 mapPanel과 겹치도록 설정
        mainPanel.add(top, Integer.valueOf(100));  // 위쪽 레이어

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

        ProgressPaneManager progressManager = new ProgressPaneManager();
        JPanel dayPanel = progressManager.getDayPanel();


        itemPanel = new ItemPanel();
        top.add(dayPanel, BorderLayout.WEST);
        top.add(itemPanel, BorderLayout.EAST);

        return top;
    }

    JPanel showBottom()
    {
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BorderLayout());


        time = new JLabel(new ImageIcon("assets/img/time.png"));
        time.setBounds(-2, 9, time.getIcon().getIconWidth(), time.getIcon().getIconHeight());

        timePanel = new JPanel();
        timePanel.setOpaque(false);
        timePanel.add(time);

        bottom.add(timePanel, BorderLayout.WEST);

        ProgressPaneManager progressManager = new ProgressPaneManager();
        JPanel progressPane = progressManager.getProgressPane();
        bottom.add(progressPane, BorderLayout.CENTER);

        coinManager = new CoinManager();


        bottom.add(coinManager.getCoinPanel(), BorderLayout.EAST);

        return bottom;
    }


    void updateCoinAmount(int amount) {
        coinManager.updateCoinAmount(amount);
    }

    void ItemUse(){
        if(itemArray[1] == true){
            bcAns.stop();
        }
        else if (itemArray[2] == true){
          // MovePlayer.fastMove();
        }
        else if (itemArray[3] == true){
        LightStick.use();
        }
        else{

        }
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
        new StartManager();
    }
}