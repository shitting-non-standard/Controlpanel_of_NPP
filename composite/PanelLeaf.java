package composite;

import model.PanelComponent;

/**
 * Лист составной структуры — отдельный компонент
 * панели управления.
 * Реализует паттерн Composite (листовой узел).
 */
public class PanelLeaf implements PanelElement {

    private final PanelComponent component;
    private final int coordX;
    private final int coordY;

    public PanelLeaf(
            final PanelComponent panelComponent,
            final int x,
            final int y) {
        this.component = panelComponent;
        this.coordX = x;
        this.coordY = y;
    }

    @Override
    public String getDescription() {
        return "Компонент на (" + coordX + "," + coordY + "): "
                + component.render();
    }
}