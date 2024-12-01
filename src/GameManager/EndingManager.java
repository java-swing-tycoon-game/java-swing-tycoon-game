package GameManager;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EndingManager extends JDialog {
    private JLabel endingLabel;
    private final int totalCoin;

    private final DayManager dayManager;  // DayManager 인스턴스
    private final CoinManager coinManager;  // CoinManager 인스턴스
    private final RankingManager rankingManager; // 랭킹 관리

    public EndingManager(JFrame parent, DayManager dayManager, CoinManager coinManager) {
        super(parent, true); // Modal Dialog로 설정
        // 성공 여부
        this.dayManager = dayManager;
        this.coinManager = coinManager;
        this.totalCoin = CoinManager.getCoinAmount(); // 총 코인 금액
        this.rankingManager = new RankingManager();

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

        // 닉네임 입력 및 저장 버튼 추가
        JTextField nicknameField = new JTextField();
        nicknameField.setBounds(50, getHeight() - 250, 200, 30);
        JLabel nicknameLabel = new JLabel("닉네임:");
        nicknameLabel.setBounds(50, getHeight() - 280, 200, 30);
        nicknameLabel.setForeground(Color.WHITE);
        JButton saveRankingButton = new JButton("저장");
        saveRankingButton.setBounds(260, getHeight() - 250, 100, 30);

        saveRankingButton.addActionListener((ActionEvent _) -> {
            String nickname = nicknameField.getText().trim();

            // 빈 닉네임 확인
            if (nickname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "닉네임을 입력해주세요!", "닉네임 누락", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 중복 닉네임 확인
            if (rankingManager.isDuplicateNickname(nickname)) {
                JOptionPane.showMessageDialog(this, "중복된 닉네임입니다. 다른 닉네임을 입력해주세요.", "중복 닉네임", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 닉네임 저장
            rankingManager.addRanking(nickname, totalCoin);

            // 저장 성공 알림
            JOptionPane.showMessageDialog(this, "닉네임이 성공적으로 저장되었습니다!", "저장 성공", JOptionPane.INFORMATION_MESSAGE);

            // 입력 필드 초기화
            nicknameField.setText("");

            // 랭킹 라벨 갱신
            updateRankingLabel(layeredPane);

            // UI 갱신 (선택 사항)
            revalidate();
            repaint();
        });

        // 랭킹 표시
        updateRankingLabel(layeredPane);

        // 닉네임 입력 필드 및 버튼을 JLayeredPane에 추가
        layeredPane.add(nicknameField, JLayeredPane.POPUP_LAYER);
        layeredPane.add(nicknameLabel, JLayeredPane.POPUP_LAYER);
        layeredPane.add(saveRankingButton, JLayeredPane.POPUP_LAYER);

        // 메인으로 돌아가기 버튼 생성
        JButton backToMainButton = new JButton(new ImageIcon("assets/img/gotomain.png"));
        backToMainButton.setBorderPainted(false);
        backToMainButton.setContentAreaFilled(false);

        // 버튼 크기 조정
        backToMainButton.setBounds(getWidth() / 2 - 125, getHeight() - 150, 250, 70);

        // 버튼 클릭 리스너 설정
        backToMainButton.addActionListener((ActionEvent _) -> {
            closeAllWindowsAndRestart(); // 애플리케이션을 새로 시작
        });

        // 버튼을 JLayeredPane에 추가
        layeredPane.add(backToMainButton, JLayeredPane.POPUP_LAYER);

        // JLayeredPane을 JDialog에 추가
        add(layeredPane, BorderLayout.CENTER);
    }

    private void updateEndingImage() {
        String imagePath;
        int day = DayManager.getDay(); // 현재 Day
        boolean isSuccess = (totalCoin >= 100 && day == 7); // 성공 조건 변경

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

    private JLabel rankingLabel = null;

    private void updateRankingLabel(JLayeredPane layeredPane) {
        if (rankingLabel == null) {
            rankingLabel = new JLabel();
            rankingLabel.setBounds(50, getHeight() - 250, 300, 200);
            layeredPane.add(rankingLabel, JLayeredPane.POPUP_LAYER);
        }

        List<RankingManager.RankingEntry> topRankings = rankingManager.getTopRankings();
        StringBuilder rankingText = new StringBuilder("<html><style>");
        rankingText.append("body { font-size: 20px; color: #FF67FA; }");
        rankingText.append("</style><u>TOP 3 랭킹</u><br>");

        for (int i = 0; i < topRankings.size(); i++) {
            RankingManager.RankingEntry entry = topRankings.get(i);
            rankingText.append((i + 1))
                    .append(". ")
                    .append(entry.getNickname())
                    .append(": ")
                    .append(entry.getScore())
                    .append("점<br>");
        }
        rankingText.append("</html>");

        rankingLabel.setText(rankingText.toString());
    }

    private void closeAllWindowsAndRestart() {
        // if (dayManager.getDay() == 1) play.playBgm().bgm.toggleMusic();

        // 모든 창 닫기
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            window.dispose();
        }

        // 상태 초기화
        dayManager.setDay(); // DayManager 상태 초기화
        coinManager.setCoinAmount(0); // CoinManager 상태 초기화

        // 새로운 Main 실행
    }
}
