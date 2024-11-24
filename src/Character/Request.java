package Character;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Request {
    private static final String[] itemRequestList = {"assets/img/item/cup.png", "assets/img/item/photoCard.png", "assets/img/item/popcorn.png", "assets/img/item/doll.png", "assets/img/item/bag.png", "assets/img/item/album.png"};
    protected Image request = new ImageIcon("assets/img/npc/request.png").getImage();

    private Image requestItem; // 요청 말풍선에 뜨는 이미지
    private final ArrayList<Place> zone; // 요청과 연관된 장소

    protected Timer requestTimer; // 시간제한을 위한 타이머
    protected boolean active; // 개별 요청의 완료 여부

    public Request(int x, int y, ArrayList<Place> places) {
        active = false;
        zone = new ArrayList<>();
        setZone(places);

        requestTimer = new Timer(5000, e -> {
            makeRequest(x, y);
        });
        requestTimer.start();
    }

    protected void setZone(ArrayList<Place> places)
    {
        zone.add(places.get(6));
        zone.add(places.get(7));
        zone.add(places.get(8));
        zone.add(places.get(9));
        zone.add(places.get(10));
    }

    public void makeRequest(int x, int y) {
        if (!active) {
            requestItem = setRequestItem(x, y);
            active = true;
            requestTimer.stop();
        }
    }

    public void completeRequest() {
        if (active) {
            this.active = false;
            requestTimer.start();
        }
    }

    private Image setRequestItem(int x, int y) {
        if (zone.get(0).contains(x, y)) {
            System.out.println("영화");
            return setMovieRequest();
        }
        else if (zone.get(1).contains(x, y)) {
            System.out.println("굿즈");
            return setGoodsRequest();
        }
        else if(zone.get(2).contains(x, y)) {
            System.out.println("대기존1");
            return setWaitingRequest();
       }
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
