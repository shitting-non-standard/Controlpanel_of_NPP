package builder;

import container.ControlPanel;

/**
 * Интерфейс строителя панели управления.
 * Реализует паттерн Builder.
 */
public interface PanelBuilder {

    PanelBuilder setSize(int width, int height);

    PanelBuilder placeComponents();

    PanelBuilder configureBindings();

    ControlPanel build();
}