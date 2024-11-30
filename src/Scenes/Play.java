package Scenes;

import GameManager.CoinManager;
import GameManager.DayManager;
import GameManager.ProgressPaneManager;
import GameManager.StartManager;

import javax.swing.*;
import java.awt.*;

public class Play extends JFrame {
    private static JLayeredPane mainPanel;
    private ProgressPaneManager progressPaneManager;
    private DayManager dayManager;

    public Play() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JLayeredPane();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        dayManager = new DayManager(); // DayManager 초기화
        progressPaneManager = new ProgressPaneManager(dayManager); // ProgressPaneManager 초기화

        setupUI();

        setSize(1038, 805);
        setVisible(true);

        progressPaneManager.startDayTimer(); // 게임 시작과 함께 Day 타이머 시작
    }

    private void setupUI() {
        // 상단 UI: Day Panel
        JPanel dayPanel = dayManager.getDayPanel();
        dayPanel.setBounds(65, 0, 900, 100);
        mainPanel.add(dayPanel, Integer.valueOf(100));

        // 하단 UI: Progress Bar
        JPanel progressPane = progressPaneManager.getProgressPane();
        progressPane.setBounds(65, 700, 900, 100);
        mainPanel.add(progressPane, Integer.valueOf(100));
    }

    public static void main(String[] args) {
        new StartManager();
    }
}
