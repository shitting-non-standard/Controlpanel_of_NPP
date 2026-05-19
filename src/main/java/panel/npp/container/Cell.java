package panel.npp.container;

import panel.npp.decorator.BorderCellDecorator;
import panel.npp.model.PanelComponent;

/**
 * Ячейка контейнера панели управления.
 * Может содержать один компонент или быть пустой.
 * Не является Spring-бином — создаётся в ControlPanel как структура данных.
 */
public class Cell {

    private static final String EMPTY_SYMBOL = "[ ]";

    private PanelComponent component;
    private final int coordX;
    private final int coordY;

    public Cell(final int x, final int y) {
        this.coordX = x;
        this.coordY = y;
        this.component = null;
    }

    public void setComponent(final PanelComponent panelComponent) {
        this.component = panelComponent;
    }

    public PanelComponent getComponent() {
        return component;
    }

    public boolean isEmpty() {
        return component == null;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public String render() {
        if (isEmpty()) {
            return EMPTY_SYMBOL;
        }
        return new BorderCellDecorator(component).render();
    }
}
