package GameManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RankingManager {
    private static final String RANKING_FILE = "assets/txt/ranking.txt";
    private final List<RankingEntry> rankings;

    public RankingManager() {
        rankings = new ArrayList<>();
        loadRankings(); // 초기화 시 파일에서 랭킹 로드
    }

    public void addRanking(String nickname, int score) {
        if (isDuplicateNickname(nickname)) {
        } else {
            // 새 닉네임 추가
            rankings.add(new RankingEntry(nickname, score));

            // 점수 순으로 정렬
            rankings.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

            // 3등까지만 저장
            if (rankings.size() > 3) {
                rankings.remove(3);
            }

            saveRankings(); // 랭킹 변경 시 저장
        }
    }

    public boolean isDuplicateNickname(String nickname) {
        for (RankingEntry entry : rankings) {
            if (entry.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public List<RankingEntry> getTopRankings() {
        return new ArrayList<>(rankings);
    }

    private void saveRankings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RANKING_FILE))) {
            for (RankingEntry entry : rankings) {
                writer.write(entry.getNickname() + "," + entry.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRankings() {
        File file = new File(RANKING_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String nickname = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    rankings.add(new RankingEntry(nickname, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class RankingEntry {
        private final String nickname;
        private int score;

        public RankingEntry(String nickname, int score) {
            this.nickname = nickname;
            this.score = score;
        }

        public String getNickname() {
            return nickname;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
