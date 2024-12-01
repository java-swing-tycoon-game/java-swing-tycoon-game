package GameManager;

import Scenes.Main;
import Scenes.Play;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndingManager extends JFrame {
    private JLabel endingLabel;
    private int totalCoin;

    private DayManager dayManager;  // DayManager 인스턴스
    private CoinManager coinManager;  // CoinManager 인스턴스

    public EndingManager(DayManager dayManager, CoinManager coinManager) {
        this.dayManager = dayManager;
        this.coinManager = coinManager;
        this.totalCoin = coinManager.getCoinAmount();  // 총 코인 금액

        // 엔딩 화면 설정
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다 - Ending");
        setSize(1038, 805);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 엔딩 이미지를 담을 JLayeredPane 생성
        JLayeredPane layeredPane = new JLayeredPane();

        // 엔딩 이미지 로딩
        updateEndingImage();

        // 엔딩 이미지를 JLayeredPane에 추가
        endingLabel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(endingLabel, JLayeredPane.DEFAULT_LAYER);

        // 메인으로 돌아가기 버튼 생성
        JButton backToMainButton = new JButton(new ImageIcon("assets/img/gotomain.png"));
        backToMainButton.setBorderPainted(false);
        backToMainButton.setContentAreaFilled(false);

        // 버튼 크기 조정
        backToMainButton.setPreferredSize(new Dimension(150, 60));  // 예시로 크기를 설정
        backToMainButton.setBounds(getWidth() / 2 + 200, getHeight() - 150, 250, 70);  // 버튼 위치 설정

        // 버튼 클릭 리스너 설정
        backToMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 버튼 클릭 시 Main 화면으로
                goToMainScreen();
            }
        });

        // 버튼을 JLayeredPane에 추가
        layeredPane.add(backToMainButton, JLayeredPane.POPUP_LAYER);

        // JLayeredPane을 JFrame에 추가
        add(layeredPane);

    }

    private void updateEndingImage() {
        String imagePath = "";
        int day = dayManager.getDay();  // 현재 Day
        boolean isSuccess = (totalCoin >= 100 && day == 7); // 최종 성공 조건

        if (isSuccess) {
            imagePath = "assets/img/ending-clear.png";
        } else {
            imagePath = "assets/img/ending-fail.png";
        }

        // Game Over 시 출력
        endingLabel = new JLabel(new ImageIcon(imagePath));
    }

    // 메인 화면으로 돌아가는 메서드
    private void goToMainScreen() {
        // EndingManager 창을 닫고 Main 화면을 띄움
        this.dispose();
        Scenes.Main mainScreen = new Main();
        mainScreen.setVisible(true);
    }
}
