package panel.npp.factory;

import org.springframework.stereotype.Component;
import panel.npp.model.Button;
import panel.npp.model.PanelComponent;

/**
 * Фабрика для создания кнопок панели управления.
 * Реализует паттерн Factory Method.
 * Является Spring-бином (@Component) — фабрики могут быть бинами.
 */
@Component
public class ButtonFactory implements ComponentFactory {

    @Override
    public PanelComponent create(final String name) {
        return createButton(name);
    }

    public Button createButton(final String name) {
        return new Button(name);
    }
}
