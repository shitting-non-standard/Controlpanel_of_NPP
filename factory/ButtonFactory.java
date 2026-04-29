package factory;

import model.Button;
import model.PanelComponent;

/**
 * Фабрика для создания кнопок панели управления
 * Реализует паттерн Factory Method.
 */
public class ButtonFactory implements ComponentFactory {

    @Override
    public PanelComponent create(final String name) {
        return createButton(name);
    }

    public Button createButton(final String name) {
        return new Button(name);
    }
}