package factory;

import model.PanelComponent;

/**
 * Абстрактная фабрика компонентов панели управления
 * Реализует паттерн Factory Method.
 */
public interface ComponentFactory {

    PanelComponent create(String name);
}