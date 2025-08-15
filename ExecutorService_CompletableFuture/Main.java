import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CfPlayground {

    // === 실행기(Executor) ===
    // I/O 전용 스레드풀 (스레드 이름 접두어 io-)
    static final ExecutorService IO = new ThreadPoolExecutor(
            8, 8, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            new NamedThreadFactory("io-"),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    // 참고: 자바 21이면 가상 스레드로 바꿔서 체험해보세요 (대량 I/O 동시성에 유리)
    // static final ExecutorService IO = Executors.newThreadPerTaskExecutor(
    //        Thread.ofVirtual().name("v-io-", 0).factory());

    public static void main(String[] args) throws Exception {
        log("=== CompletableFuture 미니 랩 시작 ===");

        demoThenApply_vs_thenCompose();
        demoThenCombine();
        demoAllOf();
        demoAnyOf();
        demoTimeouts();
        demoExceptions();

        shutdown(IO);
        log("=== 끝 ===");
    }

    // ---------------- 데모 1: thenApply vs thenCompose ----------------
    static void demoThenApply_vs_thenCompose() {
        log("\n[1] thenApply vs thenCompose");

        // thenApply: T -> U (동기 변환, '겉'만 변환)
        CompletableFuture<String> nameUpper =
                userAsync("u-101")
                        .thenApply(name -> name.toUpperCase()); // 반환은 바로 String

        log("thenApply 결과: " + nameUpper.join());

        // thenCompose: T -> CompletableFuture<U> (비동기 평탄화/연쇄)
        // 유저를 찾고 → 그 유저로 최신 주문을 '비동기'로 조회
        CompletableFuture<String> latestOrder =
                userAsync("u-101")
                        .thenCompose(user -> latestOrderAsync(user)); // 평탄화되어 최종 타입은 CompletableFuture<String>

        log("thenCompose 결과(최신 주문): " + latestOrder.join());
    }

    // ---------------- 데모 2: thenCombine ----------------
    static void demoThenCombine() {
        log("\n[2] thenCombine (독립 작업 두 개 합치기)");

        CompletableFuture<String> productF = productAsync("p-7");
        CompletableFuture<Integer> stockF  = stockAsync("p-7");

        CompletableFuture<String> viewF =
                productF.thenCombine(stockF,
                        (p, s) -> "뷰: " + p + " / 재고: " + s);

        log(viewF.join());
    }

    // ---------------- 데모 3: allOf ----------------
    static void demoAllOf() {
        log("\n[3] allOf (여러 개를 모두 기다려서 모으기)");

        List<String> ids = List.of("u-1","u-2","u-3","u-4","u-5");
        List<CompletableFuture<Integer>> pointsF = ids.stream()
                .map(CfPlayground::pointAsync) // 각 유저의 포인트 (비동기)
                .collect(Collectors.toList());

        CompletableFuture<Void> all =
                CompletableFuture.allOf(pointsF.toArray(new CompletableFuture[0]));

        // allOf는 Void라서, 실제 값은 각 Future에서 join으로 꺼냅니다.
        int total = all.thenApply(v ->
                pointsF.stream().mapToInt(CompletableFuture::join).sum()
        ).join();

        log("포인트 총합 = " + total);
    }

    // ---------------- 데모 4: anyOf ----------------
    static void demoAnyOf() {
        log("\n[4] anyOf (가장 빠른 하나 채택)");

        // 캐시 / DB / 외부 API 중 더 빨리 오는 것 사용
        CompletableFuture<String> cache = maybeCacheAsync("key"); // 빠를 수도, 없을 수도
        CompletableFuture<String> db    = dbAsync("key");
        CompletableFuture<String> api   = apiAsync("key");

        Object fastest = CompletableFuture.anyOf(
                // 실패를 기본값으로 바꿔서 "성공한 것 중 가장 빠른 하나"처럼 동작시키기
                cache.exceptionally(ex -> null),
                db.exceptionally(ex -> null),
                api.exceptionally(ex -> null)
        ).join();

        String result = (String) fastest;
        if (result == null) result = "기본값(fallback)";
        log("가장 빠른 결과 = " + result);
    }

    // ---------------- 데모 5: 타임아웃 ----------------
    static void demoTimeouts() {
        log("\n[5] 타임아웃: orTimeout vs completeOnTimeout");

        // orTimeout: 기한 내 미완료 → 실패(예외)
        String v1 = slowAsync("느린작업", 800)
                .orTimeout(300, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> "타임아웃-폴백")
                .join();
        log("orTimeout → " + v1);

        // completeOnTimeout: 기한 내 미완료 → 기본값으로 '성공' 처리
        String v2 = slowAsync("느린작업2", 800)
                .completeOnTimeout("기본값", 300, TimeUnit.MILLISECONDS)
                .join();
        log("completeOnTimeout → " + v2);
    }

    // ---------------- 데모 6: 예외처리 ----------------
    static void demoExceptions() {
        log("\n[6] 예외: exceptionally / handle / whenComplete");

        // 6-1) exceptionally: 실패를 값으로 복구
        String a = failingAsync("boom")
                .exceptionally(ex -> {
                    log("exceptionally: " + ex.getClass().getSimpleName());
                    return "복구됨";
                })
                .join();
        log("exceptionally 결과 = " + a);

        // 6-2) handle: 성공/실패 모두에서 새 값 생성
        String b = failingAsync("boom2")
                .handle((val, ex) -> ex == null ? val : "기본값-핸들")
                .join();
        log("handle 결과 = " + b);

        // 6-3) whenComplete: 부수효과만, 예외는 그대로 전파
        String c = failingAsync("boom3")
                .whenComplete((val, ex) -> log("whenComplete: exNull? " + (ex == null)))
                .exceptionally(ex -> "마지막-폴백")
                .join();
        log("whenComplete 결과 = " + c);
    }

    // ---------------- Helpers: 비동기 시뮬레이션 ----------------
    static CompletableFuture<String> userAsync(String id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(250);
            log("userAsync 실행");
            return "user(" + id + ")";
        }, IO);
    }

    static CompletableFuture<String> latestOrderAsync(String user) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(300);
            log("latestOrderAsync 실행");
            return "order#" + (100 + new Random().nextInt(900)) + " for " + user;
        }, IO);
    }

    static CompletableFuture<String> productAsync(String pid) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(200);
            log("productAsync 실행");
            return "상품[" + pid + "]";
        }, IO);
    }

    static CompletableFuture<Integer> stockAsync(String pid) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(350);
            log("stockAsync 실행");
            return 42;
        }, IO);
    }

    static CompletableFuture<Integer> pointAsync(String uid) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(100 + new Random().nextInt(300));
            return 10 + new Random().nextInt(90);
        }, IO);
    }

    static CompletableFuture<String> maybeCacheAsync(String key) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(50);
            // 50% 확률로 캐시에 없음(예외로 표현)
            if (new Random().nextBoolean()) throw new NoSuchElementException("cache miss");
            return "CACHE_VALUE";
        }, IO);
    }

    static CompletableFuture<String> dbAsync(String key) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return "DB_VALUE";
        }, IO);
    }

    static CompletableFuture<String> apiAsync(String key) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(250);
            return "API_VALUE";
        }, IO);
    }

    static CompletableFuture<String> slowAsync(String label, int ms) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(ms);
            return label + "-OK";
        }, IO);
    }

    static CompletableFuture<String> failingAsync(String label) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(100);
            throw new RuntimeException("실패: " + label);
        }, IO);
    }

    // ------------- 유틸 -------------
    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    static void log(String msg) {
        System.out.printf("%tT [%s] %s%n", new Date(), Thread.currentThread().getName(), msg);
    }

    static void shutdown(ExecutorService ex) {
        ex.shutdown();
        try { ex.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
    }

    // 스레드 이름 접두어를 붙이는 ThreadFactory
    static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger seq = new AtomicInteger(1);
        private final ThreadFactory base = Executors.defaultThreadFactory();
        NamedThreadFactory(String prefix) { this.prefix = prefix; }
        @Override public Thread newThread(Runnable r) {
            Thread t = base.newThread(r);
            t.setName(prefix + seq.getAndIncrement());
            return t;
        }
    }
}
