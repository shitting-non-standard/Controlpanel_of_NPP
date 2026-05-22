package panel.npp.factory;

import panel.npp.model.PanelComponent;

/**
 * Абстрактная фабрика компонентов панели управления.
 * Реализует паттерн Factory Method.
 */
public interface ComponentFactory {

    PanelComponent create(String name);
}
