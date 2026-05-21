package model;

import composite.PanelGroup;
import composite.PanelLeaf;
import observer.LampObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Кнопка панели управления.
 * При изменении состояния уведомляет связанные лампы (паттерн Observer).
 *
 * Потокобезопасность обеспечивается через:
 * - ReentrantLock (мьютекс) для защиты состояния pressed (паттерн Mutex из курса)
 * - CopyOnWriteArrayList для списка наблюдателей (безопасная итерация при нотификации)
 * - volatile для видимости состояния между потоками (JMM)
 */
public class Button implements PanelComponent {

    private static final String PRESSED_SYMBOL = "o";
    private static final String RELEASED_SYMBOL = "O";

    // Мьютекс (ReentrantLock) — один поток в критической секции
    private final Lock stateLock = new ReentrantLock();

    // volatile: изменения видны всем потокам без полной блокировки
    private volatile boolean pressed;
    private final String name;

    // CopyOnWriteArrayList: безопасная итерация при нотификации из нескольких потоков
    private final CopyOnWriteArrayList<LampObserver> observers;

    public Button(final String buttonName) {
        this.name = buttonName;
        this.pressed = false;
        this.observers = new CopyOnWriteArrayList<>();
    }

    public void addObserver(final LampObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(final LampObserver observer) {
        observers.remove(observer);
    }

    /**
     * Нажатие кнопки. Защищено мьютексом:
     * только один поток может изменить состояние в момент времени.
     */
    public void press() {
        stateLock.lock();
        try {
            this.pressed = true;
        } finally {
            stateLock.unlock();
        }
        notifyObservers(true);
    }

    /**
     * Отпускание кнопки. Защищено мьютексом.
     */
    public void release() {
        stateLock.lock();
        try {
            this.pressed = false;
        } finally {
            stateLock.unlock();
        }
        notifyObservers(false);
    }

    public boolean isPressed() {
        return pressed; // volatile — безопасно без lock для чтения
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
        int i = 0;
        for (LampObserver o : observers) {
            if (i++ > 0) sb.append(", ");
            sb.append(o.getBindingDescription());
        }
        return sb.toString();
    }

    /**
     * Нотификация наблюдателей вне блокировки,
     * чтобы не держать мьютекс во время потенциально долгих операций.
     */
    private void notifyObservers(final boolean buttonPressed) {
        for (LampObserver observer : observers) {
            observer.onButtonStateChanged(buttonPressed);
        }
    }

    @Override
    public String render() {
        return pressed ? PRESSED_SYMBOL : RELEASED_SYMBOL;
    }

    @Override
    public void addToGroup(
            final PanelGroup buttonGroup,
            final PanelGroup lampGroup,
            final int x,
            final int y) {
        buttonGroup.add(new PanelLeaf(this, x, y));
    }
}
