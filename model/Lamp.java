package model;

import composite.PanelGroup;
import composite.PanelLeaf;
import observer.LampObserver;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Лампа цветовой индикации панели управления.
 * Реализует LampObserver для отслеживания состояния кнопок (паттерн Observer).
 *
 * Потокобезопасность обеспечивается через:
 * - AtomicInteger для счётчика нажатых кнопок (атомарные операции без блокировок)
 *   (паттерн Atomic из курса: java.util.concurrent.atomic)
 */
public class Lamp implements PanelComponent, LampObserver {

    private static final String INACTIVE_SYMBOL = "Л";
    private static final String ACTIVE_PREFIX = "Л_";

    // AtomicInteger — неблокирующий счётчик, потокобезопасный через CAS-операции
    private final AtomicInteger pressedButtonsCount;
    private final Color indicationColor;
    private final String name;

    public Lamp(final String lampName, final Color color) {
        this.name = lampName;
        this.indicationColor = color;
        this.pressedButtonsCount = new AtomicInteger(0);
    }

    public String getName() {
        return name;
    }

    public Color getIndicationColor() {
        return indicationColor;
    }

    public boolean isActive() {
        return pressedButtonsCount.get() > 0;
    }

    /**
     * Вызывается из потока кнопки. AtomicInteger обеспечивает
     * потокобезопасное изменение без явной синхронизации.
     */
    @Override
    public void onButtonStateChanged(final boolean buttonPressed) {
        if (buttonPressed) {
            pressedButtonsCount.incrementAndGet();
        } else {
            // updateAndGet гарантирует атомарность проверки и обновления
            pressedButtonsCount.updateAndGet(v -> Math.max(0, v - 1));
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
    public void addToGroup(
            final PanelGroup buttonGroup,
            final PanelGroup lampGroup,
            final int x,
            final int y) {
        lampGroup.add(new PanelLeaf(this, x, y));
    }
}
