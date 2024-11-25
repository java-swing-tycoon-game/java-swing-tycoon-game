package GameManager;

import Character.*;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.stream.Collectors;

public class NpcManager {
    private JLayeredPane parentPanel;
    private List<Npc> npcList;
    private int maxNpcs;
    private int npcCount = 0;
    private int blackConsumerCount = 0;

    private static boolean bcActive = false;

    private final ArrayList<Place> room;
    private final ArrayList<Place> waitRoom;
    private final Map<Place, Npc> placeToNpcMap;

    private ClickManager clickManager;
    private Timer spawnTimer;

    public NpcManager(JLayeredPane parentPanel, int maxNpcs, ClickManager clickManager) {
        this.parentPanel = parentPanel;
        this.maxNpcs = maxNpcs;
        this.clickManager = clickManager;

        this.npcList = new ArrayList<>();
        this.room = Place.createPlaces()
                .stream()
                .filter(place -> place.getNum() == 2) // 룸만 넣기
                .collect(Collectors.toCollection(ArrayList::new));
        this.waitRoom = Place.createPlaces()
                .stream()
                .filter(place -> place.getNum() == 3) // 대기존 2개
                .collect(Collectors.toCollection(ArrayList::new));

        this.placeToNpcMap = new HashMap<>();

        startNpcSpawnTimer();
    }

    private void startNpcSpawnTimer() {
        spawnNpc();
        spawnTimer = new Timer(3000, e -> {
            if (npcList.size() < maxNpcs) {
                spawnNpc();
            }
        });
        spawnTimer.start();
    }

    private void spawnNpc() {
        Npc npc;

        // 20% 확률로 블랙 컨슈머 생성
        if (Math.random() < 0.2) {
            if (bcActive) {
                System.out.println("블랙 컨슈머가 이미 존재합니다. 새 블랙 컨슈머 생성 중단.");
                return; // 기존 블랙 컨슈머가 있을 경우 생성 중단
            }

            npc = new BlackConsumer();
            blackConsumerCount++;
            bcActive = true;
            addBcPanel(npc);
        } else { // 일반 npc
            npc = new Npc();
            npcCount++;
            npcList.add(npc);
            addNpcPanel(npc);
            moveNpcToWait(npc);
        }
        //System.out.println(npcList);
        clickManager.setClickList(npc);
    }

    private void addBcPanel(Npc npc)
    {
        npc.setBounds(0, 0, 1024, 768);
        npc.setOpaque(false);
        parentPanel.add(npc, Integer.valueOf(200));
    }

    private void addNpcPanel(Npc npc)
    {
        npc.setBounds(0, 0, 1024, 768);
        npc.setOpaque(false);
        parentPanel.add(npc, Integer.valueOf(100));
    }

    private void moveNpcToWait(Npc npc) {
        // 빈 대기 구역 찾기
        Optional<Place> emptyWaitRoom = waitRoom.stream()
                .filter(place -> !isPlaceOccupied(place))
                .findFirst();

        if (emptyWaitRoom.isPresent()) {
            Place targetWaitRoom = emptyWaitRoom.get();
            assignNpcToPlace(npc, targetWaitRoom); // NPC를 대기 구역에 배치
            npc.moveToTarget(targetWaitRoom, null); // 대기 구역으로 이동
            System.out.println("NPC가 대기 구역으로 이동합니다");

            // 10초 대기 후 비어있는 룸으로 이동 시도
            Timer waitTimer = new Timer(5000, e -> moveNpcToRoom(npc));
            waitTimer.setRepeats(false);
            waitTimer.start();
        } else {
            System.out.println("대기 구역이 모두 가득 찼습니다. NPC 생성 중지.");
            spawnTimer.stop(); // 모든 대기 구역이 차 있으면 타이머 정지
            removeNpc(npc);
        }
    }

    private void moveNpcToRoom(Npc npc) {
        // 빈 룸 찾기
        List<Place> emptyRooms = room.stream()
                .filter(place -> !isPlaceOccupied(place))
                .collect(Collectors.toList());

        if (!emptyRooms.isEmpty()) {
            // 랜덤으로 빈 룸 선택
            Random random = new Random();
            Place targetRoom = emptyRooms.get(random.nextInt(emptyRooms.size()));

            // NPC를 해당 룸으로 이동
            assignNpcToPlace(npc, targetRoom);
            npc.moveToDest(targetRoom, true, null); // 룸으로 이동
            System.out.println("NPC가 룸으로 이동합니다. npc 좌표: " + npc.characterX);
        } else {
            System.out.println("모든 룸이 가득 찼습니다. NPC는 대기 구역에서 계속 대기합니다.");
            // 대기 상태 유지
            Timer recheckTimer = new Timer(10000, e -> moveNpcToRoom(npc));
            recheckTimer.setRepeats(false);
            recheckTimer.start();
        }
    }

    // room이 가득 참
    public boolean areAllPlacesFull() {
        return room.stream().allMatch(this::isPlaceOccupied);
    }

    public boolean isPlaceOccupied(Place place) {
        return placeToNpcMap.containsKey(place);
    }

    public void assignNpcToPlace(Npc npc, Place place) {
        placeToNpcMap.put(place, npc);
    }

    public void removeNpc(Npc npc) {
        npcList.remove(npc);
        parentPanel.remove(npc);
        parentPanel.revalidate();
        parentPanel.repaint();
        if (npc instanceof BlackConsumer) {
            bcActive = false; // 블랙 컨슈머 플래그 초기화
        }
    }

    // 플래그를 외부에서 제어할 수 있도록 추가
    public static void setBcActive(boolean active) {
        bcActive = active;
    }

    public List<Place> getRoom() {
        return room;
    }
}
