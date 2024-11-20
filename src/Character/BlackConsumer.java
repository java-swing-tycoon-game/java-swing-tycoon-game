package Character;

import java.awt.*;

public class BlackConsumer extends Npc {
    public BlackConsumer() {
        super();
        moveToDest(new Place(245, 285, 75, 300, 220));
    }

    //BlackConsumer 전용 그래픽 효과 추가
    @Override
    protected void paintComponent(Graphics g) {
        // 부모 클래스의 draw 호출
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // 추가적인 오라 효과
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(getX() - 20, getY() - 20, 60, 60); // NPC 주변 원 그리기
    }
}
