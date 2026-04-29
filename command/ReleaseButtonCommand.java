package command;

import model.Button;

/**
 * Команда отпускания кнопки панели управления.
 * Реализует паттерн Command.
 */
public class ReleaseButtonCommand implements Command {

    private final Button button;

    public ReleaseButtonCommand(final Button targetButton) {
        this.button = targetButton;
    }

    @Override
    public void execute() {
        button.release();
    }

    @Override
    public void undo() {
        button.press();
    }

    @Override
    public String getDescription() {
        return "Отпустить кнопку '" + button.getName() + "'";
    }
}