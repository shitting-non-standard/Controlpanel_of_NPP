package npp.command;

import npp.model.Button;

/**
 * Команда нажатия кнопки. Паттерн Command.
 */
public class PressButtonCommand implements Command {

    private final Button button;

    public PressButtonCommand(final Button button) {
        this.button = button;
    }

    @Override
    public void execute() {
        button.press();
    }

    @Override
    public void undo() {
        button.release();
    }

    @Override
    public String getDescription() {
        return "Нажать кнопку '" + button.getName() + "'";
    }
}
