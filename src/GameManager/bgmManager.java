package GameManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class bgmManager {
    private Clip clip;
    private boolean isPlaying = false; // 재생 상태

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
        }
    }

    public void stop() {
        if (clip != null && isPlaying) {
            clip.stop(); // 정지
            isPlaying = false;
        }
    }

    public void toggleMusic() {
        if (isPlaying) {
            stop(); // 정지
        } else {
            play(); // 재생
        }
    }
}
