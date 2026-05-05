package observer;

/**
 * Интерфейс наблюдателя для ламп панели управления.
 * Реализует паттерн Observer.
 */
public interface LampObserver {

    void onButtonStateChanged(boolean buttonPressed);

    String getBindingDescription();
}