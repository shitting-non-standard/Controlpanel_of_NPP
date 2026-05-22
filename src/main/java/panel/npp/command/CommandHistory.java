package panel.npp.command;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * История выполненных команд панели управления с поддержкой отмены действий.
 * Часть паттерна Command.
 * Является Spring-бином (@Component) — синглтон-сервис уровня приложения.
 */
@Component
public class CommandHistory {

    private final Deque<Command> history;

    public CommandHistory() {
        this.history = new ArrayDeque<>();
    }

    public void executeCommand(final Command command) {
        command.execute();
        history.push(command);
        System.out.println("Выполнено: " + command.getDescription());
    }

    public void undoLast() {
        if (history.isEmpty()) {
            System.out.println("История команд пуста — нечего отменять.");
            return;
        }
        Command last = history.pop();
        last.undo();
        System.out.println("Отменено: " + last.getDescription());
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public int size() {
        return history.size();
    }
}
