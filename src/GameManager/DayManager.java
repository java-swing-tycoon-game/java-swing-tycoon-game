package GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DayManager {
    private int day = 1; // 현재 Day

    private ImageDayPanel dayPanel;

    public DayManager() {
        this.dayPanel = new ImageDayPanel();
    }

    public JPanel getDayPanel() {
        return dayPanel;
    }

    public int getDay() {
        return day;
    }

    public void nextDay() {
        day++;
        dayPanel.updateDayImage(day);
    }

    public class ImageDayPanel extends JPanel {
        private JLabel dayLabel;

        public ImageDayPanel() {
            setLayout(new FlowLayout());
            dayLabel = new JLabel();
            updateDayImage(day);
            add(dayLabel);
        }

        public void updateDayImage(int day) {
            try {
                String imagePath = "assets/img/day" + day + ".png";
                BufferedImage dayImage = ImageIO.read(new File(imagePath));
                dayLabel.setIcon(new ImageIcon(dayImage));
            } catch (IOException ex) {
                System.err.println("Day 이미지 로드 실패: " + ex.getMessage());
                dayLabel.setText("Day " + day); // 이미지 로드 실패 시 텍스트 표시
            }
        }
    }
}
