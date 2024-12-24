# Демонстрація методів CompletableFuture в Java

Цей проект демонструє використання деяких важливих методів класу `CompletableFuture` в Java для асинхронного програмування.
Метою цього проекту є надання практичних прикладів використання методів `thenCompose()`, `thenCombine()`, `allOf()` та `anyOf()` класу `CompletableFuture`.


## Використані методи CompletableFuture

*   **`thenCompose(Function)`:** Використовується для створення ланцюжка асинхронних операцій, де результат першої операції використовується для запуску другої. Повертає новий `CompletableFuture`, який завершується результатом `CompletableFuture`, повернутого функцією.
*   **`thenCombine(CompletionStage, BiFunction)`:** Використовується для комбінування результатів двох незалежних `CompletableFuture`. Повертає новий `CompletableFuture`, який завершується результатом застосування заданої біфункції до результатів обох `CompletableFuture`.
*   **`allOf(CompletableFuture...)`:** Приймає масив `CompletableFuture` і повертає новий `CompletableFuture`, який завершується, коли всі вхідні `CompletableFuture` завершаться. Результати індивідуальних future можна отримати окремо.
*   **`anyOf(CompletableFuture...)`:** Приймає масив `CompletableFuture` і повертає новий `CompletableFuture`, який завершується, коли будь-який з вхідних `CompletableFuture` завершується (успішно або з помилкою).

## Як запустити

1. Переконайтеся, що у вас встановлено Java Development Kit (JDK).
2. Скомпілюйте файл `CompletableFutureExamples.java`:
   
    ```bash
    javac CompletableFutureExamples.java
    ```
4. Запустіть скомпільований клас:
    ```bash
    java CompletableFutureExamples
    ```

## Очікуваний результат
При запуску програми ви побачите наступне:

```output
Початок демонстрації методів CompletableFuture.
Демонстрація anyOf():
Перше успішно завершене завдання з результатом: Результат завдання 1

--- Розпочинаємо сценарій бронювання квитка ---
Перевірка наявності місць...
Пошук найкращої ціни...
Завершення бронювання...
Очікування завершення бронювання...
Наявність місць перевірено. Доступно: true
Найкраща ціна знайдена: 333.16
Бронювання успішно завершено. Ціна: 333.16
Результат бронювання: Бронювання успішно завершено. Ціна: 333.16
Демонстрація методів CompletableFuture завершена.
```
## Пояснення коду

*   **`demonstrateAnyOf()`:** Демонструє використання `CompletableFuture.anyOf()` для отримання результату першого успішного завдання. Створюються три асинхронні завдання, і `anyOf()` дозволяє отримати результат того, яке завершиться першим.
*   **`bookTicket()`:** Імітує процес бронювання квитка.
    *   `checkAvailability()`: Асинхронно перевіряє наявність місць.
    *   `findBestPrice()`: Використовується з `thenCompose()` та виконується лише після успішної перевірки наявності місць. Асинхронно шукає найкращу ціну.
    *   `completeBooking()`: Використовується з `thenCompose()` та виконується після знаходження ціни. Асинхронно завершує бронювання.
*   **`checkAvailability()`, `findBestPrice()`, `completeBooking()`:** Ці методи представляють окремі асинхронні операції, які повертають `CompletableFuture`.
*   **Використання `ExecutorService`:** Для виконання асинхронних завдань використовується пул потоків `executor`, що дозволяє виконувати завдання паралельно.

## Додатково

У коді також продемонстровано використання `thenCompose()` для створення залежних асинхронних операцій, де результат однієї операції є вхідним параметром для іншої. Хоча `thenCombine()` та `allOf()` не були використані в основному сценарії бронювання, їхні можливості описані вище, і їх можна інтегрувати для розширення функціоналу.