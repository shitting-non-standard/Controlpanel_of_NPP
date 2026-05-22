package panel.npp.command;

/**
 * Интерфейс команды панели управления.
 * Реализует паттерн Command.
 */
public interface Command {

    void execute();

    void undo();

    String getDescription();
}
