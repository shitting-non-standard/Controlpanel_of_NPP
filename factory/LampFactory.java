package factory;

import model.Color;
import model.Lamp;
import model.PanelComponent;

/**
 * Фабрика для создания ламп цветовой индикации панели управления
 * Реализует паттерн Factory Method.
 */
public class LampFactory implements ComponentFactory {

    private final Color color;

    public LampFactory(final Color lampColor) {
        this.color = lampColor;
    }

    @Override
    public PanelComponent create(final String name) {
        return createLamp(name);
    }

    public Lamp createLamp(final String name) {
        return new Lamp(name, color);
    }
}