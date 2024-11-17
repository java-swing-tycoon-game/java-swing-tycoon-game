package Character;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Request {
    private static final String[] POSSIBLE_REQUESTS = {"Bring me an apple", "Find my hat", "Help me fix this"};
    private static final Image REQUEST_IMAGE = new ImageIcon("assets/img/npc/request.png").getImage();

    private String requestText;  // 개별 요청 텍스트
    private boolean active;      // 개별 활성 상태

    public Request() {
        this.requestText = getRandomRequest();
        this.active = true;
    }

    // 요청을 랜덤으로 선택
    private static String getRandomRequest() {
        Random random = new Random();
        return POSSIBLE_REQUESTS[random.nextInt(POSSIBLE_REQUESTS.length)];
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void draw(Graphics2D g2d, int x, int y) {
        if (active) {
            int balloonX = x - 20;
            int balloonY = y - 80;

            // 말풍선 이미지 그리기
            g2d.drawImage(REQUEST_IMAGE, balloonX, balloonY, null);

            // 요청 텍스트 표시
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(requestText, balloonX + 10, balloonY + 25);
        }
    }
}
