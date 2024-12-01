package GameManager;

import javax.swing.*;
import java.awt.*;

public class CoinManager {
    private static int coinAmount = 100000;  // 초기 코인 금액
    private static JLabel coinTxt;      // 코인 금액을 표시
    private static JPanel coinPanel;    // 하단에 코인 요소들을 포함한 패널
    DayManager dayManager = DayManager.getInstance();

    int[] coins = {5, 10, 20, 50, 75, 90, 100};

    public CoinManager() {
        coinPanel = new JPanel();
        coinPanel.setOpaque(false);
        coinPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel coin = new JLabel(new ImageIcon("assets/img/coin.png"));
        JLabel coinImage = new JLabel(new ImageIcon("assets/img/coinImage.png"));
        coinImage.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));

        // 코인 텍스트 설정
        Font font = FontManager.loadFont(30);

        if (DayManager.getDay() == 1) coinAmount = 0;   // 초기화

        coinTxt = new JLabel("x " + coinAmount + "만원");  // 코인 금액 표시
        coinTxt.setFont(font);
        coinTxt.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));

        // 패널에 추가
        coinPanel.add(coin);
        coinPanel.add(coinImage);
        coinPanel.add(coinTxt);
    }

    public JPanel getCoinPanel() {
        return coinPanel;
    }

    public static int getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(int n) {coinAmount = n;}

    // 코인 금액 업데이트
    public static void updateCoinAmount(int amount) {
        coinAmount += amount;  // 코인 금액 +/- 업데이트
        coinTxt.setText("x " + coinAmount + "만원");  // 코인 금액 업데이트
        System.out.println(coinAmount);
    }
}