package GameManager;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class bgmManager {
    private Clip clip;
    private boolean isPlaying = false; // 재생 상태
    private final ImageIcon musicOnIcon = new ImageIcon("assets/img/bgmOn.png");
    private final ImageIcon musicOffIcon = new ImageIcon("assets/img/bgmOff.png");
    private JLabel musicLabel;

    public bgmManager(String filePath, boolean loop) {
        try {
            // 파일 로드
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            if(loop) clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생 설정
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !isPlaying) {
            clip.start(); // 재생
            isPlaying = true;
            updateIcon(); // 아이콘 업데이트
        }
    }

    public void stop() {
        if (clip != null && isPlaying) {
            clip.stop(); // 정지
            isPlaying = false;
            updateIcon(); // 아이콘 업데이트
        }
    }

    public void toggleMusic() {
        if (isPlaying) {
            stop(); // 정지
        } else {
            play(); // 재생
        }
    }

    public JLabel createMusicLabel() {
        musicLabel = new JLabel(musicOnIcon); // 초기 상태를 On 이미지로 설정
        return musicLabel;
    }

    public void updateIcon() {
        if (musicLabel != null) {
            musicLabel.setIcon(isPlaying ? musicOnIcon : musicOffIcon);
        }
    }
}
