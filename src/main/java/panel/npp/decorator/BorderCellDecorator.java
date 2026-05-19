package panel.npp.decorator;

import panel.npp.model.PanelComponent;

/**
 * Декоратор, добавляющий рамку вокруг визуализации компонента ячейки.
 * Реализует паттерн Decorator.
 * Не является Spring-бином — создаётся вручную в Cell.render().
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
