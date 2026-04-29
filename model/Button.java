package model;

import observer.LampObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Кнопка панели управления
 * (паттерн Observer).
 */
public class Button implements PanelComponent {

    private static final String PRESSED_SYMBOL = "o";
    private static final String RELEASED_SYMBOL = "O";

    private boolean pressed;
    private final String name;
    private final List<LampObserver> observers;

    public Button(final String buttonName) {
        this.name = buttonName;
        this.pressed = false;
        this.observers = new ArrayList<>();
    }

    public void addObserver(final LampObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(final LampObserver observer) {
        observers.remove(observer);
    }

    /**
     * Нажимает кнопку и уведомляет наблюдателей.
     */
    public void press() {
        this.pressed = true;
        notifyObservers();
    }

    /**
     * Отпускает кнопку и уведомляет наблюдателей.
     */
    public void release() {
        this.pressed = false;
        notifyObservers();
    }

    public boolean isPressed() {
        return pressed;
    }

    public String getName() {
        return name;
    }

    public List<LampObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    private void notifyObservers() {
        for (LampObserver observer : observers) {
            observer.onButtonStateChanged(pressed);
        }
    }

    @Override
    public String render() {
        return pressed ? PRESSED_SYMBOL : RELEASED_SYMBOL;
    }
}