import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExamples {

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Початок демонстрації методів CompletableFuture.");

        // 1. Демонстрація anyOf() - виведення результату першого успішно завершеного завдання
        demonstrateAnyOf();

        System.out.println("\n--- Розпочинаємо сценарій бронювання квитка ---");

        // 2. Сценарій бронювання квитка з використанням thenCompose() та thenCombine()
        bookTicket();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Демонстрація методів CompletableFuture завершена.");
    }

    private static void demonstrateAnyOf() throws InterruptedException, ExecutionException {
        System.out.println("Демонстрація anyOf():");

        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            sleep(random.nextInt(500));
            if (random.nextBoolean()) {
                return "Результат завдання 1";
            } else {
                throw new RuntimeException("Помилка в завданні 1");
            }
        }, executor);

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            sleep(random.nextInt(500));
            if (random.nextBoolean()) {
                return "Результат завдання 2";
            } else {
                throw new RuntimeException("Помилка в завданні 2");
            }
        }, executor);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            sleep(random.nextInt(500));
            return "Результат завдання 3";
        }, executor);

        CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(task1, task2, task3);

        firstCompleted.thenAccept(result -> {
            System.out.println("Перше успішно завершене завдання з результатом: " + result);
        }).exceptionally(ex -> {
            System.out.println("Усі завдання завершилися з помилкою або не повернули результат.");
            return null;
        }).get();
    }

    private static void bookTicket() throws InterruptedException, ExecutionException {
        System.out.println("Перевірка наявності місць...");
        CompletableFuture<Boolean> availabilityFuture = checkAvailability();

        System.out.println("Пошук найкращої ціни...");
        CompletableFuture<Double> priceFuture = availabilityFuture
                .thenCompose(available -> {
                    if (available) {
                        return findBestPrice();
                    } else {
                        System.out.println("На жаль, місць немає.");
                        return CompletableFuture.completedFuture(null);
                    }
                });

        System.out.println("Завершення бронювання...");
        CompletableFuture<String> bookingResultFuture = priceFuture
                .thenCompose(price -> {
                    if (price != null) {
                        return completeBooking(price);
                    } else {
                        return CompletableFuture.completedFuture("Бронювання неможливе.");
                    }
                });

        System.out.println("Очікування завершення бронювання...");
        System.out.println("Результат бронювання: " + bookingResultFuture.get());
    }

    private static CompletableFuture<Boolean> checkAvailability() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
            boolean available = random.nextBoolean();
            System.out.println("Наявність місць перевірено. Доступно: " + available);
            return available;
        }, executor);
    }

    private static CompletableFuture<Double> findBestPrice() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(ThreadLocalRandom.current().nextInt(500, 1500));
            double price = 100.0 + random.nextDouble() * 400.0;
            System.out.println("Найкраща ціна знайдена: " + String.format("%.2f", price));
            return price;
        }, executor);
    }

    private static CompletableFuture<String> completeBooking(double price) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(ThreadLocalRandom.current().nextInt(800, 1800));
            String bookingConfirmation = "Бронювання успішно завершено. Ціна: " + String.format("%.2f", price);
            System.out.println(bookingConfirmation);
            return bookingConfirmation;
        }, executor);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}