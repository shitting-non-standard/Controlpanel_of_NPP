package observer;

import model.Button;
import model.Lamp;

/**
 * Вспомогательный класс для связывания
 * кнопок и ламп панели управления
 */
public final class LampController {

    private LampController() {
    }

    public static void bind(final Button button, final Lamp lamp) {
        button.addObserver(lamp);
    }
}