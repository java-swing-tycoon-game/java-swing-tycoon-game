package GameManager;

import Character.*;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static Scenes.Play.player;

public class NpcManager {
    private static JLayeredPane parentPanel = null;

    // npc(여러명이라 리스트)
    private static List<Npc> npcList;

    private static int maxNpc; // 데이별로 maxNpc 다름
    private static int npcCount = 0;

    // bc는 한 번에 1명만 등장
    private static boolean bcActive = false;

    // 장소와 들어간 npc 관리
    private static ArrayList<Place> room = null;
    private static ArrayList<Place> waitRoom = null;
    private static Map<Place, Npc> roomToNpcMap = Map.of();
    private static Map<Place, Npc> waitRoomToNpcMap = Map.of();

    private static Timer spawnTimer;
    private static Timer moveRoomTimer;

    private static Npc npc = null;
    private static BlackConsumer bc = null;

    // 병렬 처리
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    // 생성자
    public NpcManager(JLayeredPane parentPanel, Player player, int maxNpc) {
        this.parentPanel = parentPanel;
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
    public static void removeNpc(Npc npc) {
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
                bc = null;
                // 디버깅용 로그
                System.out.println("블랙컨슈머 제거됨. 클릭 복구");

                // 기존 클릭 이벤트 복구
                for (Npc otherNpc : npcList) {
                    ClickManager.setClickEventList(otherNpc);
                }
                ClickManager.setClickEventList(player); // 플레이어 클릭 복구
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
    static void startNpcSpawnTimer() {
        if (spawnTimer != null) {
            spawnTimer.stop(); // 기존 타이머 중지
        }

        spawnNpc(); // 첫 1회는 바로 등장
        spawnTimer = new Timer(15000, e -> {
            if (npcList.size() < maxNpc) {
                spawnNpc();
            }
        });
        spawnTimer.start();
    }

    // 생성
    private static void spawnNpc() {
        executor.submit(() -> {
            if(player.isMoving) {// 일반 npc
                npc = createNpc(npc);
            }
            else {
                // 플레이어 멈춰있고 50% 확률로 블랙 컨슈머 생성
                if (Math.random() < 0.1 && !bcActive) {
                    bc = createBc();
                    ClickManager.setClickEventList(bc);
                }
                else {
                    npc = createNpc(npc);
                    ClickManager.setClickEventList(npc);
                }
            }
            ClickManager.setClickEventList(npc);
        });
    }

    private static BlackConsumer createBc() {
        BlackConsumer bc = new BlackConsumer();
        bcActive = true;
        ClickManager.onlyBcClick = true;
        addNpcPanel(bc, 200);
        moveBcToPlayer(bc);
        bc.bcAuto();

        return bc;
    }

    private static Npc createNpc(Npc npc) {
        npc = new Npc();

        npcList.add(npc);
        npcCount++;
        addNpcPanel(npc, 100);
        moveNpcToWait(npc);

        return npc;
    }

    // npc를 화면에 띄운다
    private static void addNpcPanel(Npc npc, int zIndex)
    {
        SwingUtilities.invokeLater(() -> {
            npc.setBounds(0, 0, 1024, 768);
            npc.setOpaque(false);
            parentPanel.add(npc, Integer.valueOf(zIndex));
        });
    }

    // bc를 화면 중앙 bc위치로 이동
    private static void moveBcToPlayer(Npc npc) {
        executor.submit(() -> npc.moveToDest(Move.places.getLast(), false, ()-> {
            npc.setupRequest();
        }));
    }

    // 대기 구역으로 이동
    private static void moveNpcToWait(Npc npc) {
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
    private static void checkAndMoveToRoom(Npc npc, Place targetWaitRoom) {
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

    // 모든 NPC 제거
    public static void clearAllNpcs() {
        executor.submit(() -> {
            for (Npc npc : new ArrayList<>(npcList)) {
                removeNpc(npc); // 모든 NPC를 제거
            }
            forceRemoveBlackConsumer();

            // 상태 초기화
            npcList.clear();
            roomToNpcMap.clear();
            waitRoomToNpcMap.clear();
            npcCount = 0;
            bcActive = false;

            SwingUtilities.invokeLater(() -> {
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            System.out.println("모든 NPC 제거 완료");
        });
    }

    // 블랙컨슈머 강제 종료 메서드
    public static void forceRemoveBlackConsumer() {
        if (bcActive && bc != null) {
            System.out.println("블랙컨슈머 게임 진행 중 강제 종료");

            SwingUtilities.invokeLater(() -> {
                //parentPanel.remove(bc); // UI에서 블랙컨슈머 제거
                parentPanel.revalidate(); // 레이아웃 갱신
                parentPanel.repaint(); // 화면 다시 그리기
            });

            removeNpc(bc); // BC 객체를 리스트와 상태에서 제거
            bcActive = false; // 상태 초기화
            ClickManager.onlyBcClick = false;
            bc = null; // BC 객체 초기화
        }
    }
}
