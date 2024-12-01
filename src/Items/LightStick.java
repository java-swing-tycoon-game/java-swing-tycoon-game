package Items;

public class LightStick {
    private static int timeIncrease; // 응원봉 사용 시 증가하는 시간
    private static int totalTime;    // 응원봉 사용으로 누적된 총 시간

    public LightStick() {
        this.timeIncrease = 5; // 기본 증가 시간 (5초)
        this.totalTime = 0;     // 총 시간 초기화
    }

    public int getTimeIncrease() {
        return timeIncrease;
    }

    public static void use() {
        totalTime += timeIncrease; // 응원봉 사용 시 누적 시간
        System.out.println("응원봉 사용.." + totalTime);
    }
}
