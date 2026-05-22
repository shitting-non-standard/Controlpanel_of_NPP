package npp.model;

import npp.composite.PanelGroup;
import npp.composite.PanelLeaf;
import npp.observer.LampObserver;

/**
 * Лампа цветовой индикации панели управления.
 * Паттерн Observer — реализует LampObserver.
 */
public class Lamp implements PanelComponent, LampObserver {

    private static final String INACTIVE_SYMBOL = "Л";
    private static final String ACTIVE_PREFIX = "Л_";

    private int pressedButtonsCount;
    private final Color indicationColor;
    private final String name;

    public Lamp(final String lampName, final Color color) {
        this.name = lampName;
        this.indicationColor = color;
        this.pressedButtonsCount = 0;
    }

    public String getName() {
        return name;
    }

    public Color getIndicationColor() {
        return indicationColor;
    }

    public boolean isActive() {
        return pressedButtonsCount > 0;
    }

    @Override
    public void onButtonStateChanged(final boolean buttonPressed) {
        if (buttonPressed) {
            pressedButtonsCount++;
        } else {
            pressedButtonsCount = Math.max(0, pressedButtonsCount - 1);
        }
    }

    @Override
    public String getBindingDescription() {
        return name + " [цвет=" + indicationColor.getCode() + "]";
    }

    @Override
    public String render() {
        if (isActive()) {
            return ACTIVE_PREFIX + indicationColor.getCode();
        }
        return INACTIVE_SYMBOL;
    }

    @Override
    public void addToGroup(final PanelGroup buttonGroup, final PanelGroup lampGroup,
                           final int x, final int y) {
        lampGroup.add(new PanelLeaf(this, x, y));
    }
}
