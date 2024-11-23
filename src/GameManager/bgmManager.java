package GameManager;

import javax.sound.sampled.*;
        import java.io.File;
import java.io.IOException;

public class bgmManager {
    private static Clip clip;
    private static boolean isPlaying = false; // 재생 상태

    public bgmManager(String filePath) {
        try {
            // 파일 로드
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생 설정
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void toggleMusic() {
        if (clip != null) {
            if (isPlaying) {
                clip.stop(); // 정지
            } else {
                clip.start(); // 재생
            }
            isPlaying = !isPlaying; // 재생 상태
        }
    }
}
