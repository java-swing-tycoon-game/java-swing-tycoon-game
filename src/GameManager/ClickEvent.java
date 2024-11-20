package GameManager;

import java.awt.*;

public interface ClickEvent {
    Rectangle setBounds(); // 클릭 범위 설정
    void onClick(Point clickPoint); // 클릭되면 할 일
}