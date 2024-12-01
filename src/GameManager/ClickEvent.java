package GameManager;

import java.awt.*;

public interface ClickEvent {
    Rectangle setBounds(); // 클릭 범위 설정
    void onClick(Point clickPoint); // 클릭되면 할 일
    default int getPriority() { // 우선순위 기본값
        return 0;
    }
    boolean isEnabled(); // 추가
    void setEnabled(boolean enabled); // 추가
}