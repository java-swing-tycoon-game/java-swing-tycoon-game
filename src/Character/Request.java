package Character;

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

    public Request(Npc npc) {
        this.npc = npc;
        active = false;
        zone = new ArrayList<>();
        setZone();

        requestTimer = new Timer(5000, e -> {
            makeRequest();
        });
        requestTimer.start();
    }

    public boolean getActive() {
        return active;
    }

    // room 3곳 저장
    protected void setZone()
    {
        for (int i = 0; i < Move.places.size(); i++)
            if(Move.places.get(i).getNum() == 2)
                zone.add(Move.places.get(i));
    }

    public void makeRequest() {
        if (!active) {
            requestItem = setRequestItem(npc.characterX, npc.characterY);
            active = true;
            requestTimer.stop();
        }
    }

    // 요청 완료
    public void completeRequest() {
        if (active) {
            active = false;
            requestTimer.start();
        }
    }

    public Image getRequestItem() {
        return requestItem;
    }

    // 장소에 맞춰 요청할 아이템 이미지 세팅
    private Image setRequestItem(int x, int y) {
        if (zone.get(0).contains(x, y)) {
            return setGoodsRequest();
        }
        else if (zone.get(1).contains(x, y)) {
            return setDecoRequest();
        }
        else if(zone.get(2).contains(x, y)) {
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
        return new ImageIcon(itemRequestList[random.nextInt(4, 6)]).getImage();
    }

    // 데코 요청
    private Image setDecoRequest() {
        System.out.println("꾸미기 요청 발생");
        return new ImageIcon(itemRequestList[6]).getImage();
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