package npp.factory;

import npp.model.PanelComponent;

/**
 * Абстрактная фабрика компонентов панели управления.
 * Паттерн Factory Method.
 */
public interface ComponentFactory {
    PanelComponent create(String name);
}
