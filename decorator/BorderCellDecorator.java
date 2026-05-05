package decorator;

import model.PanelComponent;

/**
 * Декоратор, добавляющий рамку вокруг
 * визуализации компонента ячейки.
 * Реализует паттерн Decorator.
 */
public class BorderCellDecorator extends CellDecorator {

    private static final String OPEN = "[";
    private static final String CLOSE = "]";

    public BorderCellDecorator(final PanelComponent component) {
        super(component);
    }

    @Override
    public String render() {
        return OPEN + getWrapped().render() + CLOSE;
    }
}