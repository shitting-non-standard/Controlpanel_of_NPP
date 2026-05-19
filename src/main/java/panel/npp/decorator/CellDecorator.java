package panel.npp.decorator;

import panel.npp.composite.PanelGroup;
import panel.npp.model.PanelComponent;

/**
 * Абстрактный декоратор компонента ячейки панели управления.
 * Реализует паттерн Decorator.
 * Не является Spring-бином — создаётся вручную в Cell.render().
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
    public void addToGroup(final PanelGroup buttonGroup, final PanelGroup lampGroup,
                           final int x, final int y) {
        wrapped.addToGroup(buttonGroup, lampGroup, x, y);
    }
}
