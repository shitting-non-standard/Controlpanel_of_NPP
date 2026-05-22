package npp.builder;

import npp.container.ControlPanel;

/**
 * Интерфейс строителя панели управления.
 * Паттерн Builder.
 */
public interface PanelBuilder {
    PanelBuilder setSize(int width, int height);
    PanelBuilder placeComponents();
    PanelBuilder configureBindings();
    ControlPanel build();
}
