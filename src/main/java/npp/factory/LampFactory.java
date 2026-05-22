package npp.factory;

import npp.model.Color;
import npp.model.Lamp;
import npp.model.PanelComponent;
import org.springframework.stereotype.Component;

/**
 * Фабрика для создания ламп цветовой индикации панели управления.
 * Паттерн Factory Method.
 *
 * Spring Bean — синглтон. Создаёт лампы со случайным цветом из Color.values().
 */
@Component
public class LampFactory implements ComponentFactory {

    @Override
    public PanelComponent create(final String name) {
        return createLamp(name, Color.RED);
    }

    public Lamp createLamp(final String name, final Color color) {
        return new Lamp(name, color);
    }
}
