package GameManager;

import Character.*;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class NpcManager {
    private final JLayeredPane parentPanel;

    // 플레이어
    private Player player;

    // npc(여러명이라 리스트)
    private List<Npc> npcList;

    private int maxNpc; // 데이별로 maxNpc 다름
    private int npcCount = 0;

    // bc는 한 번에 1명만 등장
    private static boolean bcActive = false;

    // 장소와 들어간 npc 관리
    private static ArrayList<Place> room = null;
    private final ArrayList<Place> waitRoom;
    private static Map<Place, Npc> roomToNpcMap = Map.of();
    private static Map<Place, Npc> waitRoomToNpcMap = Map.of();

    private static Timer spawnTimer;
    private static Timer moveRoomTimer;

    private Npc npc = null;

    // 병렬 처리
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    // 생성자
    public NpcManager(JLayeredPane parentPanel, Player player, int maxNpc) {
        this.parentPanel = parentPanel;
        this.player = player;
        this.maxNpc = maxNpc;

        this.npcList = new ArrayList<>();
        this.room = Npc.places
                .stream()
                .filter(place -> place.getNum() == 2) // 룸만 넣기
                .collect(Collectors.toCollection(ArrayList::new));
        this.waitRoom = Npc.places
                .stream()
                .filter(place -> place.getNum() == 3) // 대기존 2개
                .collect(Collectors.toCollection(ArrayList::new));

        this.roomToNpcMap = new HashMap<>();
        this.waitRoomToNpcMap = new HashMap<>();

        startNpcSpawnTimer();
    }

    // bc 존재 여부
    public static void setBcActive(boolean active) {
        bcActive = active;
    }

    // 맵에 NPC 추가
    private static void setNpcToMap(Map<Place, Npc> map, Place place, Npc npc) {
        if (!map.containsKey(place)) { // 중복 방지
            map.put(place, npc);
        }
    }
    // 맵이 차있는지 리턴
    private static boolean getMapNpc(Map<Place, Npc> map, Place place) {
        return map.containsKey(place);
    }

    // value인 npc 값으로 맵 뒤지기
    private static Place findNpc(Map<Place, Npc> map, Npc npc) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(npc))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    // 맵에서 NPC 제거
    private static void removeNpcFromMap(Map<Place, Npc> map, Place place) {
        if (map.containsKey(place)) {
            map.remove(place);
            System.out.println("NPC 제거 완료: " + place);
        }
    }

    // npc 삭제
    public void removeNpc(Npc npc) {
        executor.submit(() -> {
            npcList.remove(npc);
            SwingUtilities.invokeLater(() -> {
                parentPanel.remove(npc);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            ClickManager.removeClickEventList(npc);
            if (npc instanceof BlackConsumer) {
                bcActive = false; // 블랙 컨슈머 플래그 초기화

                // 다른거 클릭 복원
                ClickManager.onlyBcClick = false;
                ClickManager.clearClickEventList();

                for (Npc otherNpc : npcList) {
                    ClickManager.setClickEventList(otherNpc);
                }
            }
            moveRoomTimer.start();
        });
    }

    // npc 완료 후 맵에서 삭제하고 사라지기, npc class용
    public static void finishNpc(Npc npc) {
        if(!npc.getActive()) {
            if(findNpc(roomToNpcMap, npc) != null) {
                removeNpcFromMap(roomToNpcMap, findNpc(roomToNpcMap, npc));
            }
            else {
                removeNpcFromMap(waitRoomToNpcMap, findNpc(waitRoomToNpcMap, npc));
            }
        }

        // UI에서 NPC 제거
        npc.removeFromParent();
    }

    // 생성 시작
    private void startNpcSpawnTimer() {
        spawnNpc(); // 첫 1회는 바로 등장
        spawnTimer = new Timer(15000, e -> {
            if (npcList.size() < maxNpc) {
                spawnNpc();
            }
        });
        spawnTimer.start();
    }

    // 생성
    private void spawnNpc() {
        executor.submit(() -> {
            // 10% 확률로 블랙 컨슈머 생성
            if (Math.random() < 0.1) {
                npc = createBc(npc);
            }
            // 일반 npc
            else {
                npc = createNpc(npc);
            }
            ClickManager.setClickEventList(npc);
        });
    }

    private Npc createBc(Npc npc) {
        if (bcActive) {
            return null; // 기존 블랙 컨슈머가 있을 경우 생성 중단
        }

        npc = new BlackConsumer();
        bcActive = true;
        ClickManager.onlyBcClick = true;
        addNpcPanel(npc, 200);
        moveBcToPlayer(npc);

        return npc;
    }

    private Npc createNpc(Npc npc) {
        npc = new Npc(player);

        npcList.add(npc);
        npcCount++;
        addNpcPanel(npc, 100);
        moveNpcToWait(npc);

        return npc;
    }

    // npc를 화면에 띄운다
    private void addNpcPanel(Npc npc , int zIndex)
    {
        SwingUtilities.invokeLater(() -> {
            npc.setBounds(0, 0, 1024, 768);
            npc.setOpaque(false);
            parentPanel.add(npc, Integer.valueOf(zIndex));
        });
    }

    // bc를 화면 중앙 bc위치로 이동
    private void moveBcToPlayer(Npc npc) {
        executor.submit(() -> npc.moveToDest(Move.places.getLast(), false, ()-> {
            npc.setupRequest();

            // bc 등장 시 다른 클릭 이벤트 제거
            ClickManager.clearClickEventList();
            ClickManager.setClickEventList(npc); // bc만 추가
            ClickManager.onlyBcClick = true;
        }));
    }

    // 대기 구역으로 이동
    private void moveNpcToWait(Npc npc) {
        // 빈 대기 구역 찾기
        Optional<Place> emptyWaitRoom = waitRoom.stream()
                .filter(place -> !getMapNpc(waitRoomToNpcMap, place))
                .findFirst();

        if (emptyWaitRoom.isPresent()) {
            Place targetWaitRoom = emptyWaitRoom.get();
            setNpcToMap(waitRoomToNpcMap, targetWaitRoom, npc); // NPC를 대기 구역에 배치
            new bgmManager("assets/bgm/door.wav", false).toggleMusic();

            // 대기 구역으로 이동
            npc.moveToTarget(targetWaitRoom, null);
            checkAndMoveToRoom(npc,targetWaitRoom);
        } else {
            System.out.println("대기 구역이 모두 가득 찼습니다. NPC 생성 중지.");
            spawnTimer.stop(); // 모든 대기 구역이 차 있으면 타이머 정지
            removeNpc(npc);
        }
    }

    // 대기 했는지 확인 후 룸으로 보내기 시도
    private void checkAndMoveToRoom(Npc npc, Place targetWaitRoom) {
        moveRoomTimer = new Timer(5000, e -> {
            if (npc.getRequestCount() >= 1) { // 요청이 1번 이상 발생 후
                removeNpcFromMap(waitRoomToNpcMap, targetWaitRoom);
                spawnTimer.start();
                moveRoomTimer.stop();
            }
        });
    }

    // 룸으로 보냄
    static int n = 1;
    public static void moveNpcToRoom(Npc npc) {
        List<Place> emptyRooms = room.stream()
                .filter(place -> !getMapNpc(roomToNpcMap,place))
                .toList();

        if (!emptyRooms.isEmpty()) {
            Place targetRoom = emptyRooms.get(n % emptyRooms.size());

            // 이전 룸 찾기 및 제거
            Place currentRoom = roomToNpcMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(npc))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            if (currentRoom != null) {
                roomToNpcMap.remove(currentRoom);
                System.out.println("이전 룸 비워짐: " + currentRoom);
            }

            setNpcToMap(roomToNpcMap,targetRoom, npc); // 룸에 추가

            npc.moveToDest(targetRoom, true, () -> {
                spawnTimer.start();
            });
        } else {
            System.out.println("대기 구역에서 계속 대기");
            moveRoomTimer.stop();
        }
        n++;
    }

}
