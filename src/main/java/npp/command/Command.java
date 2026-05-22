package npp.command;

/**
 * Интерфейс команды панели управления.
 * Паттерн Command.
 */
public interface Command {
    void execute();
    void undo();
    String getDescription();
}
