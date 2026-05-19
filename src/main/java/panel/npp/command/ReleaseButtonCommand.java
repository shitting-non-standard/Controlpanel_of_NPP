package panel.npp.command;

import panel.npp.model.Button;

/**
 * Команда отпускания кнопки панели управления.
 * Реализует паттерн Command.
 * Не является Spring-бином — создаётся при каждом действии пользователя.
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
