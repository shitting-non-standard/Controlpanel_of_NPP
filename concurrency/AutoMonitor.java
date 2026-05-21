package concurrency;

import command.PressButtonCommand;
import command.ReleaseButtonCommand;
import container.ControlPanel;
import model.Button;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Автоматический монитор панели АЭС.
 *
 * Работает в отдельном потоке (Runnable), имитируя автоматическое
 * срабатывание кнопок аварийной сигнализации.
 *
 * Запускается только вручную через меню (пункт 9).
 * После каждого авто-действия выводит актуальное состояние панели.
 */
public class AutoMonitor implements Runnable {

    private static final int MIN_DELAY_MS = 2000;
    private static final int MAX_DELAY_MS = 5000;

    private final List<Button> buttons;
    private final PanelEventBus eventBus;
    private final ControlPanel panel;
    private final Random random = new Random();

    // AtomicBoolean — потокобезопасный флаг без volatile+synchronized
    private final AtomicBoolean active = new AtomicBoolean(true);

    public AutoMonitor(final List<Button> panelButtons,
                       final PanelEventBus bus,
                       final ControlPanel controlPanel) {
        this.buttons = panelButtons;
        this.eventBus = bus;
        this.panel = controlPanel;
    }

    public void deactivate() {
        active.set(false);
    }

    public boolean isActive() {
        return active.get();
    }

    /**
     * Цикл автоматического мониторинга:
     * случайно нажимает/отпускает кнопки, имитируя сигналы датчиков.
     * После каждого действия выводит текущее состояние панели.
     */
    @Override
    public void run() {
        System.out.println("[AutoMonitor] Запущен автоматический мониторинг.");
        while (active.get() && !Thread.currentThread().isInterrupted()) {
            try {
                int delayMs = MIN_DELAY_MS + random.nextInt(MAX_DELAY_MS - MIN_DELAY_MS);
                Thread.sleep(delayMs);

                if (!active.get() || buttons.isEmpty()) break;

                Button target = buttons.get(random.nextInt(buttons.size()));
                if (target.isPressed()) {
                    System.out.println("[AutoMonitor] Авто-отпуск: " + target.getName());
                    eventBus.publish(new ReleaseButtonCommand(target));
                } else {
                    System.out.println("[AutoMonitor] Авто-нажатие: " + target.getName());
                    eventBus.publish(new PressButtonCommand(target));
                }

                // Короткая пауза, чтобы EventBus успел исполнить команду перед рендером
                Thread.sleep(150);
                System.out.println(panel.render());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[AutoMonitor] Прерван.");
                break;
            }
        }
        System.out.println("[AutoMonitor] Автоматический мониторинг остановлен.");
    }
}
