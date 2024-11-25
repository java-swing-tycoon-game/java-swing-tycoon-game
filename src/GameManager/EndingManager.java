package GameManager;

import javax.swing.*;
import java.awt.*;

public class EndingManager extends JFrame {
    private JLabel endingLabel;
    private int day;
    private int totalCoin;

    public EndingManager(int day, int totalCoin) {
        this.day = day;
        this.totalCoin = totalCoin;

        // 엔딩 화면 설정
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다 - Ending");
        setSize(1038, 805);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 엔딩 이미지 로딩
        updateEndingImage();

        // 엔딩 이미지를 JFrame에 추가
        add(endingLabel, BorderLayout.CENTER);
    }

    private void updateEndingImage() {
        String imagePath = "";

        // 날짜와 코인 금액에 따른 엔딩 결정
        if (day == 7) {  // 최종 단계
            imagePath = (totalCoin >= 100) ? "assets/img/ending-clear.png" : "assets/img/ending-fail.png";
        }
        else if (day == 1 && totalCoin < 5) {
            imagePath = "assets/img/ending-fail.png";
        }
        else if (day == 2 && totalCoin < 10) {
            imagePath = "assets/img/ending-fail.png";
        }
        else if (day == 3 && totalCoin < 20) {
            imagePath = "assets/img/ending-fail.png";
        }
        else if (day == 4 && totalCoin < 50) {
            imagePath = "assets/img/ending-fail.png";
        }
        else if (day == 5 && totalCoin < 75) {
            imagePath = "assets/img/ending-fail.png";
        }
        else if (day == 6 && totalCoin < 90) {
            imagePath = "assets/img/ending-fail.png";
        }

        endingLabel = new JLabel(new ImageIcon(imagePath));
    }
}
