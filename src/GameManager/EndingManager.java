package GameManager;

import Scenes.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class EndingManager extends JDialog {
    private JLabel endingLabel;
    private int totalCoin;

    private DayManager dayManager;  // DayManager 인스턴스
    private CoinManager coinManager;  // CoinManager 인스턴스

    public EndingManager(JFrame parent, DayManager dayManager, CoinManager coinManager) {
        super(parent, true); // Modal Dialog로 설정
        this.dayManager = dayManager;
        this.coinManager = coinManager;
        this.totalCoin = coinManager.getCoinAmount(); // 총 코인 금액

        setSize(1038, 805);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        setLayout(new BorderLayout());

        // 엔딩 이미지를 담을 JLayeredPane 생성
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

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
        backToMainButton.setBounds(getWidth() / 2 - 125, getHeight() - 150, 250, 70);

        // 버튼 클릭 리스너 설정
        backToMainButton.addActionListener((ActionEvent e) -> {
            closeAllWindowsAndRestart(); // 애플리케이션을 새로 시작
        });

        // 버튼을 JLayeredPane에 추가
        layeredPane.add(backToMainButton, JLayeredPane.POPUP_LAYER);

        // JLayeredPane을 JDialog에 추가
        add(layeredPane, BorderLayout.CENTER);
    }

    private void updateEndingImage() {
        String imagePath = "";
        int day = dayManager.getDay(); // 현재 Day
        boolean isSuccess = (totalCoin >= 100 && day >= 7); // 성공 조건 변경

        if (isSuccess) {
            imagePath = "assets/img/ending-clear.png";
        } else {
            imagePath = "assets/img/ending-fail.png";
        }

        // 엔딩 이미지를 설정
        endingLabel = new JLabel(new ImageIcon(imagePath));
        endingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endingLabel.setVerticalAlignment(SwingConstants.CENTER);
    }


    private void closeAllWindowsAndRestart() {
        // 모든 창 닫기
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            window.dispose();
        }

        // 새로운 Main 실행
        SwingUtilities.invokeLater(() -> {
            Main mainScreen = new Main();
            mainScreen.setVisible(true);
        });
    }
}
