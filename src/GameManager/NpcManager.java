package GameManager;

import Character.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class NpcManager {
    private JLayeredPane parentPanel;

    private List<Npc> npcs;
    private int maxNpcs;
    private int npcCount = 0;
    private int blackConsumerCount = 0;

    private ClickManager clickManager; // 클릭 매니저

    public NpcManager(JLayeredPane parentPanel, int maxNpcs, ClickManager clickManager) {
        this.parentPanel = parentPanel;
        this.npcs = new ArrayList<>();
        this.maxNpcs = maxNpcs;
        this.clickManager = clickManager; // ClickManager 연결

        startNpcSpawnTimer();
    }

    private void startNpcSpawnTimer() {
        spawnNpc();
        Timer spawnTimer = new Timer(13000, e -> {
            if (npcs.size() < maxNpcs) {
                spawnNpc();
            }
        });
        spawnTimer.start();
    }

    private void spawnNpc() {
        Npc npc;

        if (Math.random() < 0.8) { // 30% 확률로 BlackConsumer 생성
            npc = new BlackConsumer();
            blackConsumerCount++;

        } else {
            npc = new Npc();
            npcCount++;
        }

        npc.setBounds(0, 0, 1024, 768);
        npc.setOpaque(false);
        parentPanel.add(npc, Integer.valueOf(200));

        npcs.add(npc);
        clickManager.setClickList(npc);
    }

    public void removeNpc(Npc npc) {
        npcs.remove(npc);
        parentPanel.remove(npc);
        parentPanel.revalidate();
        parentPanel.repaint();
    }

    public int getNpcCount() {
        return npcCount;
    }

    public int getBlackConsumerCount() {
        return blackConsumerCount;
    }

    public List<Npc> getNpcs() {
        return npcs;
    }
}
