package Character;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Request {
    private static final String[] itemRequestList = {"assets/img/item/slogan.png", "assets/img/item/tshirts.png", "assets/img/item/stick.png", "assets/img/item/doll.png", "assets/img/item/bag.png", "assets/img/item/album.png"};
    private static final String[] placeRequestList = {"goods", "makeTop", "movie"};
    private static final Image request = new ImageIcon("assets/img/npc/request.png").getImage();

    private Image requestItem;
    private boolean active;

    public Request() {
        this.requestItem = setRandomRequest();
        this.active = true;
    }

    // 요청을 랜덤으로 선택
    private static Image setRandomRequest() {
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(itemRequestList.length)]).getImage();
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

            // 요청 아이템 이미지 표시
            g2d.drawImage(requestItem, balloonX, balloonY, null);
        }
    }
}
