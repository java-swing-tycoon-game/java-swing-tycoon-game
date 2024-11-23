package Character;

import java.awt.*;

public class BlackConsumer extends Npc {
    public BlackConsumer() {
        super();
    }

 @Override
    // 요청을 만들러 간다. bc 때문에 분리해봄
    protected void moveToRequest() {
     moveToDest(new Place(512, 330, 0, 512, 330));
     setupRequest();
    }

    @Override
    protected void setupRequest() {
        // 요청 생성
        request = new bcRequest(characterX,characterY);
    }

    @Override // npc 범위만 클릭해서 요청 수행하도록
    public Rectangle setBounds() {
        int imageWidth = 120;
        int imageHeight = 150;
        clickBounds = new Rectangle(characterX, characterY, imageWidth, imageHeight);
        return clickBounds;
    }

    @Override // 요청 완료 처리
    public void onClick(Point clickPoint) {
        if (request != null) {
            request.completeRequest(); // 클릭 시 요청 완료
            active = false;
            repaint();
        }
    }

    //BlackConsumer 전용 그래픽 효과 추가
    @Override
    protected void paintComponent(Graphics g) {
        // 부모 클래스의 draw 호출
        super.paintComponent(g);
    }
}
