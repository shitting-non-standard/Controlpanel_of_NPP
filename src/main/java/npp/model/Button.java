package npp.model;

import npp.composite.PanelGroup;
import npp.composite.PanelLeaf;
import npp.observer.LampObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Кнопка панели управления.
 * При изменении состояния уведомляет связанные лампы (паттерн Observer).
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

    public void press() {
        this.pressed = true;
        notifyObservers();
    }

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

    public String getBindingsDescription() {
        if (observers.isEmpty()) {
            return "нет связанных ламп";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < observers.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(observers.get(i).getBindingDescription());
        }
        return sb.toString();
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

    @Override
    public void addToGroup(final PanelGroup buttonGroup, final PanelGroup lampGroup,
                           final int x, final int y) {
        buttonGroup.add(new PanelLeaf(this, x, y));
    }
}
