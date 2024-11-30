package Scenes;

import javax.swing.*;
import java.awt.*;

public class Buy extends JFrame {
    private boolean nextButtonClicked = false; // 다음으로 버튼 상태
    private Runnable onDisposeAction; // dispose 시 실행할 동작

    public Buy() {
        showPopup();
    }

    public void setOnDisposeAction(Runnable onDisposeAction) {
        this.onDisposeAction = onDisposeAction;
    }

    public boolean isNextButtonClicked() {
        return nextButtonClicked;
    }

    private void showPopup() {
        setTitle("아이템 구매");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel message = new JLabel("아이템을 구매하세요!", SwingConstants.CENTER);
        JButton nextButton = new JButton("다음으로");

        nextButton.addActionListener(e -> {
            nextButtonClicked = true;
            dispose();
        });

        panel.add(message, BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);
        add(panel);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onDisposeAction != null) {
                    onDisposeAction.run();
                }
            }
        });
    }
}
