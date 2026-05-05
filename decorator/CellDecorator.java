package decorator;

import composite.PanelGroup;
import model.PanelComponent;

/**
 * Абстрактный декоратор компонента ячейки
 * панели управления.
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

    @Override
    public void addToGroup(
            final PanelGroup buttonGroup,
            final PanelGroup lampGroup,
            final int x,
            final int y) {
        wrapped.addToGroup(buttonGroup, lampGroup, x, y);
    }
}