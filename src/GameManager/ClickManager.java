package GameManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClickManager extends MouseAdapter {
    private static List<ClickEvent> ClickEventList = new ArrayList<>();

    // 클릭 가능한 이벤트들 저장
    public void setClickList(ClickEvent click) {
        ClickEventList.add(click);
        System.out.println(click);
        System.out.println(ClickEventList);
    }

    // 클릭 가능한 이벤트 삭제
    public static void removeClickList(ClickEvent click) {
        ClickEventList.remove(click);
    }

    // 클릭 이벤트 처리
    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();
        for (ClickEvent click : ClickEventList) {
            if (click.setBounds().contains(clickPoint)) {
                click.onClick(clickPoint);
                break;
            }
        }
    }
}
