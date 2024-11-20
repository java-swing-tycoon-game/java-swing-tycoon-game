package Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Request {
    private static final String[] itemRequestList = {"assets/img/item/cup.png", "assets/img/item/photoCard.png", "assets/img/item/popcorn.png", "assets/img/item/doll.png", "assets/img/item/bag.png", "assets/img/item/album.png"};
    private static final String[] placeRequestList = {"movie", "goods", "makeTop"};
    private static final Image request = new ImageIcon("assets/img/npc/request.png").getImage();

    private Image requestItem;
    private boolean active;
    private Timer requestTimer;

    public Request() {
        this.active = false;
        requestTimer = new Timer(5000, e -> {
            makeRequest();
        });
        requestTimer.start();
    }

    public void makeRequest() {
        if (!active) {
            this.requestItem = setRequestItem();
            this.active = true;
            requestTimer.stop();
        }
    }

    public void completeRequest() {
        if (active) {
            this.active = false;
            requestTimer.start();
        }
    }

    private Image setRequestItem() {
//        if(대기존에 있으면) {
//            return setWaitingRequest();
//        }
//        else if(무비에 있으면) {
//            return setMovieRequest();
//        }
//        else if(굿즈에 있으면) {
//            return setGoodsRequest();
//        }
        return setWaitingRequest();
    }

    // 대기 중 요청을 랜덤으로 선택
    private Image setWaitingRequest() {
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(2)]).getImage();
    }

    // 무비 중 요청을 랜덤으로 선택
    private Image setMovieRequest() {
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(2, 4)]).getImage();
    }

    // 굿즈 중 요청을 랜덤으로 선택
    private Image setGoodsRequest() {
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(4, itemRequestList.length)]).getImage();
    }

    public boolean isActive() {
        return active;
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
