package command;

import model.Button;

/**
 * Команда нажатия кнопки панели управления.
 * Реализует паттерн Command.
 */
public class PressButtonCommand implements Command {

    private final Button button;

    public PressButtonCommand(final Button targetButton) {
        this.button = targetButton;
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