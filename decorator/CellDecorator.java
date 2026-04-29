package decorator;

import model.PanelComponent;

/**
 * Абстрактный декоратор компонента ячейки панели управления
 * Реализует паттерн Decorator.
 */
public abstract class CellDecorator implements PanelComponent {

    private final PanelComponent wrapped;

    protected CellDecorator(final PanelComponent component) {
        this.wrapped = component;
    }

    protected PanelComponent getWrapped() {
        return wrapped;
    }

    @Override
    public String render() {
        return wrapped.render();
    }
}