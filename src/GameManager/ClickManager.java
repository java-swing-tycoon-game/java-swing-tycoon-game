package GameManager;

import Character.BlackConsumer;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClickManager extends MouseAdapter {
    public static List<ClickEvent> ClickEventList = new ArrayList<>();
    public static boolean onlyBcClick = false;

    // 클릭 가능한 이벤트들 우선 순위 두고 저장
    public static void setClickEventList(ClickEvent event) {
        if (event != null) {
            ClickEventList.add(event);
            ClickEventList.sort((a, b) -> b.getPriority() - a.getPriority());
        }
    }

    // 클릭 가능한 이벤트 삭제
    public static void removeClickEventList(ClickEvent event) {
        ClickEventList.remove(event);
    }

    // 클릭 이벤트 처리
    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();
        if (onlyBcClick) {
            // bc만 클릭 가능
            for (ClickEvent click : ClickEventList) {
                if (click instanceof BlackConsumer && click.setBounds().contains(clickPoint)) {
                    click.onClick(clickPoint);
                    return;
                }
            }
        } else {
            for (ClickEvent click : ClickEventList) {
                if (click != null && click.isEnabled() && click.setBounds().contains(clickPoint)) {
                    click.onClick(clickPoint);
                }
            }
        }
    }

    // 블랙컨슈머 등장 시 다른 클릭 이벤트 비활성화
    public static void disableAllExceptBlackConsumer() {
        for (ClickEvent event : ClickEventList) {
            if (!(event instanceof BlackConsumer)) {
                event.setEnabled(false);
            }
        }
    }

    // 블랙컨슈머 제거 후 클릭 이벤트 복원
    public static void enableAllEvents() {
        for (ClickEvent event : ClickEventList) {
            event.setEnabled(true);
        }
    }
}
