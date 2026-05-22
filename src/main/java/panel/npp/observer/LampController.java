package panel.npp.observer;

import org.springframework.stereotype.Component;
import panel.npp.model.Button;
import panel.npp.model.Lamp;

/**
 * Сервис для связывания кнопок и ламп панели управления.
 * Является Spring-бином (@Component) — сервис уровня приложения.
 */
@Component
public class LampController {

    public void bind(final Button button, final Lamp lamp) {
        button.addObserver(lamp);
    }
}
