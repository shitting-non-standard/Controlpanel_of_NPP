package npp.composite;

import npp.model.PanelComponent;

/**
 * Лист составной структуры — отдельный компонент панели управления.
 * Паттерн Composite (листовой узел).
 */
public class PanelLeaf implements PanelElement {

    private final PanelComponent component;
    private final int coordX;
    private final int coordY;

    public PanelLeaf(final PanelComponent component, final int x, final int y) {
        this.component = component;
        this.coordX = x;
        this.coordY = y;
    }

    @Override
    public String getDescription() {
        return "Компонент на (" + coordX + "," + coordY + "): " + component.render();
    }
}
