package Character;

import java.awt.*;

public class BlackConsumer extends Npc {
    public BlackConsumer() {
        super();
        // 임시로 5 작성
        moveToDest(new Place(5, 512, 330, 0, 512, 330));
    }

    //BlackConsumer 전용 그래픽 효과 추가
    @Override
    protected void paintComponent(Graphics g) {
        // 부모 클래스의 draw 호출
        super.paintComponent(g);
    }
}
