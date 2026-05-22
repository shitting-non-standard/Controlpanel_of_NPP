package npp.factory;

import npp.model.Button;
import npp.model.PanelComponent;
import org.springframework.stereotype.Component;

/**
 * Фабрика для создания кнопок панели управления.
 * Паттерн Factory Method.
 *
 * Spring Bean — синглтон. Фабрика является значимым/разделяемым объектом,
 * поэтому её целесообразно делать Spring Bean и внедрять через DI.
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
