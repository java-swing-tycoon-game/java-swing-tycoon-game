package GameManager;

import Character.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static Scenes.Play.player;

@SuppressWarnings("ALL")
public class NpcManager {
    private static JLayeredPane parentPanel = null;

    // npc(여러명이라 리스트)
    private static List<Npc> npcList;

    private static int maxNpc; // 데이별로 maxNpc 다름

    // bc는 한 번에 1명만 등장
    private static boolean bcActive = false;

    // 장소와 들어간 npc 관리
    private static ArrayList<Place> room = null;
    private static ArrayList<Place> waitRoom = null;
    private static Map<Place, Npc> roomToNpcMap = Map.of();
    private static Map<Place, Npc> waitRoomToNpcMap = Map.of();

    private static Timer spawnTimer;
    private static Timer moveRoomTimer;

    private Npc npc = null;
    private static BlackConsumer bc = null;
    private static double bcChance = 0; // 진상 생성 확률

    // 병렬 처리
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    // 생성자
    public NpcManager(JLayeredPane parentPanel, int maxNpc) {
        NpcManager.parentPanel = parentPanel;
        NpcManager.maxNpc = maxNpc;

        npcList = new ArrayList<>();
        room = Npc.places
                .stream()
                .filter(place -> place.getNum() == 2) // 룸만 넣기
                .collect(Collectors.toCollection(ArrayList::new));
        waitRoom = Npc.places
                .stream()
                .filter(place -> place.getNum() == 3) // 대기존 2개
                .collect(Collectors.toCollection(ArrayList::new));

        roomToNpcMap = new HashMap<>();
        waitRoomToNpcMap = new HashMap<>();

        startNpcSpawnTimer();
    }

    // bc 존재 여부
    public static void setBcActive(boolean active) {
        bcActive = active;
    }

    // 맵에 NPC 추가
    private static synchronized void setNpcToMap(Map<Place, Npc> map, Place place, Npc npc) {
        if (!map.containsKey(place)) {
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
    private static synchronized void removeNpcFromMap(Map<Place, Npc> map, Place place) {
        map.remove(place);
    }

    // NPC를 맵에서 제거
    private static synchronized void removeNpcFromAllMaps(Npc npc) {
        Place npcPlace = findNpc(roomToNpcMap, npc);
        if (npcPlace != null) {
            removeNpcFromMap(roomToNpcMap, npcPlace);
        } else {
            npcPlace = findNpc(waitRoomToNpcMap, npc);
            if (npcPlace != null) {
                removeNpcFromMap(waitRoomToNpcMap, npcPlace);
            }
        }
    }

    // NPC UI 및 리스트에서 완전히 제거
    public static synchronized void removeNpc(Npc npc) {
        executor.submit(() -> {
            // 맵에서 NPC 제거
            removeNpcFromAllMaps(npc);

            // UI 및 리스트에서 NPC 제거
            SwingUtilities.invokeLater(() -> {
                if (npcList.contains(npc)) {
                    npcList.remove(npc);
                }
                if (parentPanel != null) {
                    parentPanel.remove(npc);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                }

                // 클릭 이벤트 리스트에서 제거
                ClickManager.removeClickEventList(npc);

                // 블랙 컨슈머 관련 상태 초기화
                if (npc instanceof BlackConsumer) {
                    bcActive = false;
                    ClickManager.onlyBcClick = false;
                    ClickManager.enableAllEvents();
                    bc = null;
                    System.out.println("블랙컨슈머 제거됨. 클릭 복구");
                }
            });

            // 타이머 재시작
            if (moveRoomTimer != null) {
                moveRoomTimer.start();
            }
        });
    }

    // NPC 완료 처리 (맵에서 제거 후 UI 삭제 호출)
    public static void finishNpc(Npc npc) {
        synchronized (npcList) {
            removeNpcFromAllMaps(npc);
            if (npcList.contains(npc)) {
                npcList.remove(npc);
            }
        }

        SwingUtilities.invokeLater(() -> {
            if (npc.getParent() != null) {
                npc.removeFromParent();
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        });
    }


    // 생성 시작
    public void startNpcSpawnTimer() {
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
    private void spawnNpc() {
        executor.submit(() -> {
            if(player.isMoving) {// 일반 npc
                npc = createNpc();
                bcChance += 0.05;
            }
            else {
                // 플레이어 멈춰있고 확률로 블랙 컨슈머 생성
                if (Math.random() < bcChance  && !bcActive) {
                    bc = createBc();
                    bcChance -= 0.07;
                }
                else {
                    npc = createNpc();
                    ClickManager.setClickEventList(npc);
                    bcChance += 0.05;
                }
            }
            ClickManager.setClickEventList(npc);
        });
    }

    private static BlackConsumer createBc() {
        BlackConsumer bc = new BlackConsumer();
        bcActive = true;
        ClickManager.onlyBcClick = true;

        // 다른 클릭 이벤트 비활성화
        ClickManager.disableAllExceptBlackConsumer();

        addNpcPanel(bc, 200);
        moveBcToPlayer(bc);
        bc.bcAuto();

        return bc;
    }

    private static Npc createNpc() {
        boolean hasEmptyWaitRoom = waitRoom.stream()
                .anyMatch(place -> !getMapNpc(waitRoomToNpcMap, place));

        if (!hasEmptyWaitRoom) {
            System.out.println("대기 구역이 모두 가득 찼습니다. NPC 생성 중지.");
            return null;
        }

        Npc npc = new Npc(); // 새 Npc 객체 생성

        npcList.add(npc);

        // 클릭 이벤트 등록
        ClickManager.setClickEventList(npc);

        addNpcPanel(npc, 110);
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
        int currentDay = DayManager.getDay();

        // 현재 날짜에 따라 이동 가능한 룸 필터링
        List<Place> validRooms = switch (currentDay) {
            case 1 -> List.of(room.get(2)); // 1일차: 2번 인덱스 룸만
            case 2 -> List.of(room.get(0), room.get(2)); // 2일차: 0번, 2번 인덱스 룸
            default -> room; // 3일차 이후: 모든 룸
        };

        List<Place> emptyRooms = validRooms.stream()
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

            npc.moveToDest(targetRoom, true, () -> {
                spawnTimer.start();
            });

            if (currentRoom != null) {
                roomToNpcMap.remove(currentRoom);
                System.out.println("이전 룸 비워짐: " + currentRoom);
            }

            setNpcToMap(roomToNpcMap,targetRoom, npc); // 룸에 추가
        } else {
            System.out.println("대기 구역에서 계속 대기");
            moveRoomTimer.stop();
        }
        n++;
    }

    /////////// 데이에 맞추어 변화 시도 ///////////
    // 모든 NPC 상태 초기화
    public static void resetAllNpcStates() {
        for (Npc npc : npcList) {
            npc.resetNpc(); // 모든 NPC 상태 초기화
        }
        System.out.println("모든 NPC 상태 초기화 완료");
    }

    // 모든 NPC 제거
    public static void clearAllNpcs() {
        executor.submit(() -> {
            // NPC 제거
            for (Npc npc : new ArrayList<>(npcList)) {
                // NPC가 활성화되어 있더라도 강제 제거
                npc.setActive(false); // 비활성화 처리
                finishNpc(npc);       // 맵에서 삭제
                removeNpc(npc);
            }

            // 블랙컨슈머 강제 종료
            forceRemoveBlackConsumer();

            // 상태 초기화
            npcList.clear();
            roomToNpcMap.clear();
            waitRoomToNpcMap.clear();
            bcActive = false;

            // parentPanel에서 NPC 컴포넌트만 제거
            SwingUtilities.invokeLater(() -> {
                if (parentPanel != null) {
                    // NPC만 선택적으로 제거
                    for (Component comp : parentPanel.getComponents()) {
                        if (comp instanceof Npc || comp instanceof BlackConsumer) {
                            parentPanel.remove(comp);
                        }
                    }
                    parentPanel.revalidate();
                    parentPanel.repaint();
                }
            });

            System.out.println("모든 NPC 제거 완료");
        });
    }

    // 블랙컨슈머 강제 종료 메서드
    public static void forceRemoveBlackConsumer() {
        if (bcActive && bc != null) {
            System.out.println("블랙컨슈머 게임 진행 중 강제 종료");

            SwingUtilities.invokeLater(() -> {
                parentPanel.revalidate(); // 레이아웃 갱신
                parentPanel.repaint(); // 화면 다시 그리기
            });

            removeNpc(bc); // BC 객체를 리스트와 상태에서 제거
            bcActive = false; // 상태 초기화
            ClickManager.onlyBcClick = false;
            bc = null; // BC 객체 초기화
        }
    }

    // 데이 변경 시 호출
    public static void onDayChange(int day) {
        System.out.println("데이 변경: " + day);

        // 기존 NPC 초기화
        clearAllNpcs();
        maxNpc = 10;

        // NpcManager 설정 갱신
        initializeManager(parentPanel, maxNpc);

        // 새로운 NPC 생성 시작
        //startNpcSpawnTimer();

        System.out.println("데이 " + day + " 초기화 완료. 최대 NPC: " + maxNpc + ", 블랙컨슈머 확률: " + bcChance);
    }


    // NpcManager 초기화 메서드
    public static void initializeManager(JLayeredPane panel, int maxNpc) {
        parentPanel = panel;
        NpcManager.maxNpc = maxNpc;

        npcList = new ArrayList<>();

        // Null 체크 및 초기화
        if (Npc.places == null) {
            System.err.println("Npc.places가 초기화되지 않았습니다.");
            Npc.places = Place.createPlaces(); // Npc.places 초기화
        }

        room = Npc.places.stream()
                .filter(place -> place.getNum() == 2)
                .collect(Collectors.toCollection(ArrayList::new));
        waitRoom = Npc.places.stream()
                .filter(place -> place.getNum() == 3)
                .collect(Collectors.toCollection(ArrayList::new));
        roomToNpcMap = new HashMap<>();
        waitRoomToNpcMap = new HashMap<>();

        System.out.println("NpcManager 초기화 완료. 최대 NPC: " + maxNpc + ", 블랙컨슈머 확률: " + bcChance);
    }
}
