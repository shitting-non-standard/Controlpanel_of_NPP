package panel.npp.factory;

import org.springframework.stereotype.Component;
import panel.npp.model.Color;
import panel.npp.model.Lamp;
import panel.npp.model.PanelComponent;

/**
 * Фабрика для создания ламп цветовой индикации панели управления.
 * Реализует паттерн Factory Method.
 * Является Spring-бином (@Component) — фабрики могут быть бинами.
 * Цвет задаётся при вызове createLamp(), а не через конструктор бина.
 */
@Component
public class LampFactory implements ComponentFactory {

    @Override
    public PanelComponent create(final String name) {
        return createLamp(name, Color.WHITE);
    }

    public Lamp createLamp(final String name, final Color color) {
        return new Lamp(name, color);
    }
}
