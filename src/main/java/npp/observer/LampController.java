package npp.observer;

import npp.model.Button;
import npp.model.Lamp;

/**
 * Вспомогательный класс для связывания кнопок и ламп.
 */
public final class LampController {

    private LampController() {
    }

    public static void bind(final Button button, final Lamp lamp) {
        button.addObserver(lamp);
    }
}
