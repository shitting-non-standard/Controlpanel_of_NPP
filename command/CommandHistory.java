package command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * История выполненных команд панели управления с поддержкой отмены действий.
 * Часть паттерна Command.
 *
 * Потокобезопасность: ReentrantLock (мьютекс) защищает доступ к Deque,
 * который сам по себе не является потокобезопасным.
 */
public class CommandHistory {

    private final Deque<Command> history;
    // Мьютекс для защиты истории команд от конкурентного доступа
    private final Lock lock = new ReentrantLock();

    public CommandHistory() {
        this.history = new ArrayDeque<>();
    }

    public void executeCommand(final Command command) {
        command.execute();
        lock.lock();
        try {
            history.push(command);
        } finally {
            lock.unlock();
        }
        System.out.println("Выполнено: " + command.getDescription());
    }

    public void undoLast() {
        Command last;
        lock.lock();
        try {
            if (history.isEmpty()) {
                System.out.println("История команд пуста — нечего отменять.");
                return;
            }
            last = history.pop();
        } finally {
            lock.unlock();
        }
        last.undo();
        System.out.println("Отменено: " + last.getDescription());
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return history.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return history.size();
        } finally {
            lock.unlock();
        }
    }
}
