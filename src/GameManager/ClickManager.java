package GameManager;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClickManager extends MouseAdapter {
    public static List<ClickEvent> ClickEventList = new ArrayList<>();

    // 클릭 가능한 이벤트들 우선 순위 두고 저장
    public static void setClickEventList(ClickEvent event) {
        ClickEventList.add(event);
        ClickEventList.sort((a, b) -> b.getPriority() - a.getPriority());
    }

    // 클릭 가능한 이벤트 삭제
    public static void removeClickEventList(ClickEvent event) {
        ClickEventList.remove(event);
    }

    // 클릭 이벤트 처리
    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();
        for (ClickEvent click : ClickEventList) {
            if (click.setBounds().contains(clickPoint)) {
                click.onClick(clickPoint);
                //break;
            }
        }
    }
}
