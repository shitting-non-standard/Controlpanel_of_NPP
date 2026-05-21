package concurrency;

import command.Command;
import command.CommandHistory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Шина событий панели управления АЭС.
 *
 * Реализует паттерн Producer-Consumer из курса многопоточности:
 * - Продюсеры (MenuController, AutoMonitor) кладут команды в BlockingQueue
 * - Консьюмер (фоновый поток-диспетчер) извлекает и исполняет команды
 *
 * Использует Semaphore для ограничения числа ожидающих команд.
 * Архитектура соответствует примеру Server/Client из
 * concurrency9_lw3_example репозитория курса.
 */
public class PanelEventBus implements Runnable {

    private static final int MAX_PENDING = 50;

    // BlockingQueue — потокобезопасная очередь (Producer-Consumer)
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();

    // Семафор: ограничивает число команд в очереди
    private final Semaphore queueSlots = new Semaphore(MAX_PENDING, true);

    // volatile: флаг остановки виден всем потокам без синхронизации
    private volatile boolean running = true;

    private final CommandHistory history;

    public PanelEventBus(final CommandHistory commandHistory) {
        this.history = commandHistory;
    }

    /**
     * Отправить команду (вызывается продюсером).
     * Семафор блокирует отправителя, если очередь заполнена.
     */
    public void publish(final Command command) throws InterruptedException {
        queueSlots.acquire();
        commandQueue.put(command);
    }

    public void stop() {
        running = false;
    }

    /**
     * Цикл консьюмера: извлекает команды и исполняет через CommandHistory.
     */
    @Override
    public void run() {
        System.out.println("[EventBus] Диспетчер команд запущен.");
        while (running || !commandQueue.isEmpty()) {
            try {
                Command cmd = commandQueue.take();
                history.executeCommand(cmd);
                queueSlots.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[EventBus] Диспетчер прерван.");
                break;
            }
        }
        System.out.println("[EventBus] Диспетчер команд остановлен.");
    }
}
