package npp.observer;

/**
 * Интерфейс наблюдателя для ламп панели управления.
 * Паттерн Observer.
 */
public interface LampObserver {
    void onButtonStateChanged(boolean buttonPressed);
    String getBindingDescription();
}
