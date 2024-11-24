package Character;

import javax.swing.*;
import java.awt.*;

public class bcAns {
    private String targetText; // 목표 텍스트
    public static String userInput = ""; // 사용자 입력 텍스트

    static boolean ansActive; // 타자 게임 활성화 여부
    private boolean isSuccess; // 성공 여부

    private Image request = new ImageIcon("assets/img/npc/bcAns.png").getImage();

    public bcAns(String targetText) {
        this.targetText = targetText;
        ansActive = false;
        isSuccess = false;
    }

    public void stop() {
        ansActive = false;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void processInput(char keyChar) {
        if (ansActive) {
            userInput += keyChar; // 사용자 입력 추가
            System.out.println("사용자입력: " + userInput);

            if (targetText.startsWith(userInput)) {
                if (userInput.equals(targetText)) {
                    isSuccess = true;
                    stop(); // 성공 시 게임 종료
                }
            } else {
                userInput = ""; // 틀린 경우 초기화
            }
        }
    }

    public void draw(Graphics2D g2d, int x, int y) {
        if (ansActive) {
            // 말풍선
            g2d.drawImage(request, x, y, null);

            // 답변 텍스트 출력
            g2d.drawString("입력할 문장: " + targetText, x + 10, y + 20);

            // 사용자 입력 텍스트 출력
            g2d.setColor(Color.red);
            g2d.drawString(userInput, x + 10, y + 40);
        }
    }
}
