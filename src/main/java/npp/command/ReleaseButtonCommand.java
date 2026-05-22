package npp.command;

import npp.model.Button;

/**
 * Команда отпускания кнопки. Паттерн Command.
 */
public class ReleaseButtonCommand implements Command {

    private final Button button;

    public ReleaseButtonCommand(final Button button) {
        this.button = button;
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
