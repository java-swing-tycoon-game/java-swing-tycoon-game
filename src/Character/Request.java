package Character;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Request {
    private static final String[] itemRequest = {"album", "bag", "cup"};
    private static final String[] placeRequest = {"goods", "makeTop", "movie"};
    private static final Image request = new ImageIcon("assets/img/npc/request.png").getImage();

    private String requestItem;
    private boolean active;

    public Request() {
        this.requestItem = getRandomRequest();
        this.active = true;
    }

    // 요청을 랜덤으로 선택
    private static String getRandomRequest() {
        Random random = new Random();
        return itemRequest[random.nextInt(itemRequest.length)];
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void draw(Graphics2D g2d, int x, int y) {
        if (active) {
            int balloonX = x - 70;
            int balloonY = y - 65;

            // 말풍선 이미지 그리기
            g2d.drawImage(request, balloonX, balloonY, null);

            // 요청 텍스트 표시
            g2d.setColor(Color.BLACK);
            g2d.drawString(requestItem, balloonX + 10, balloonY + 25);
        }
    }
}
