package GameManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    /////////// 폰트 관련 ///////////
    private static Map<String, Font> fontCache = new HashMap<>();
    // 폰트 로드
    public static Font loadFont(float size) {
        // 캐시에서 폰트가 이미 로드되었는지 확인
        String cacheKey = "assets/font/ChangwonDangamAsac-Bold_0712.ttf" + ":" + size;
        if (fontCache.containsKey(cacheKey)) {
            return fontCache.get(cacheKey);
        }

        Font customFont = null;
        try {
            // 폰트 파일 로드
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/ChangwonDangamAsac-Bold_0712.ttf"));
            customFont = customFont.deriveFont(size); // 폰트 크기 설정

            // 폰트를 캐시
            fontCache.put(cacheKey, customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트 로드 실패, 기본 폰트를 사용합니다.");
            customFont = new Font("Arial", Font.PLAIN, 18);
        }

        return customFont;
    }
}
