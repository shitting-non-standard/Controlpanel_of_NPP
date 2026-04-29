package model;

import observer.LampObserver;

/**
 * Лампа цветовой индикации панели управления
 * Реализует LampObserver для отслеживания
 * состояния кнопок (паттерн Observer).
 */
public class Lamp implements PanelComponent, LampObserver {

    private static final String INACTIVE_SYMBOL = "Л";
    private static final String ACTIVE_PREFIX = "Л_";

    private boolean active;
    private final Color indicationColor;
    private final String name;

    public Lamp(final String lampName, final Color color) {
        this.name = lampName;
        this.indicationColor = color;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public Color getIndicationColor() {
        return indicationColor;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void onButtonStateChanged(final boolean buttonPressed) {
        this.active = buttonPressed;
    }

    @Override
    public String render() {
        if (active) {
            return ACTIVE_PREFIX + indicationColor.getCode();
        }
        return INACTIVE_SYMBOL;
    }
}