package Character;

import Scenes.Deco;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Request {
    protected Npc npc;

    private static final String[] itemRequestList = {"assets/img/item/cup.png", "assets/img/item/photoCard.png", "assets/img/item/popcorn.png", "assets/img/item/doll.png", "assets/img/item/bag.png", "assets/img/item/album.png", "assets/img/item/deco.png"};
    protected Image request = new ImageIcon("assets/img/npc/request.png").getImage();

    private Image requestItem; // 요청 말풍선에 뜨는 이미지
    private final ArrayList<Place> zone; // 요청과 연관된 장소

    protected Timer requestTimer; // 시간제한을 위한 타이머
    protected boolean active; // 개별 요청의 완료 여부

    public Request(Npc npc, ArrayList<Place> places) {
        this.npc = npc;
        active = false;
        zone = new ArrayList<>();
        setZone(places);

        requestTimer = new Timer(5000, e -> {
            makeRequest();
        });
        requestTimer.start();
    }

    protected void setZone(ArrayList<Place> places)
    {
        zone.add(places.get(6)); // 굿즈
        zone.add(places.get(7)); // 꾸미기
        zone.add(places.get(8)); // 무비
        //zone.add(places.get(9)); // 대기존1
        //zone.add(places.get(10)); // 대기존2
    }

    public void makeRequest() {
        if (!active) {
            requestItem = setRequestItem(npc.characterX, npc.characterY);
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
        System.out.println("셋리퀘아이템에서의 npc 좌표: " + x + ", " + y);
        if (zone.get(0).contains(x, y)) {
            System.out.println("굿즈존에 들어왔습니다.");
            return setGoodsRequest();
        }
        else if (zone.get(1).contains(x, y)) {
            System.out.println("꾸미기에 들어왔습니다.");
            return setDecoRequest();
        }
        else if(zone.get(2).contains(x, y)) {
            System.out.println("무비에 들어왔습니다.");
            return setMovieRequest();
        }
        return setWaitingRequest();
    }

    // 대기 중 요청을 랜덤으로 선택
    private Image setWaitingRequest() {
        System.out.println("대기 요청 발생");
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(2)]).getImage();
    }

    // 무비 중 요청을 랜덤으로 선택
    private Image setMovieRequest() {
        System.out.println("무비 요청 발생");
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(2, 4)]).getImage();
    }

    // 굿즈 중 요청을 랜덤으로 선택
    private Image setGoodsRequest() {
        System.out.println("굿즈 요청 발생");
        Random random = new Random();
        return new ImageIcon(itemRequestList[random.nextInt(4, itemRequestList.length)]).getImage();
    }

    // 데코 요청
    private Image setDecoRequest() {
        System.out.println("꾸미기 요청 발생");
        return new ImageIcon(itemRequestList[6]).getImage();
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