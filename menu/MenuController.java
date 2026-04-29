package menu;

import builder.PanelBuilder;
import builder.RandomPanelBuilder;
import command.CommandHistory;
import command.PressButtonCommand;
import command.ReleaseButtonCommand;
import composite.PanelGroup;
import composite.PanelLeaf;
import container.Cell;
import container.ControlPanel;
import model.Button;
import model.Lamp;

import java.util.List;
import java.util.Scanner;

/**
 * Контроллер интерактивного меню панели управления.
 */
public class MenuController {

    private static final int MIN_PANEL_SIZE = 2;
    private static final int MAX_PANEL_SIZE = 10;
    private static final int DEFAULT_SIZE = 4;

    private ControlPanel panel;
    private List<Button> buttons;
    private List<Lamp> lamps;
    private final CommandHistory commandHistory;
    private final Scanner scanner;

    public MenuController() {
        this.commandHistory = new CommandHistory();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcome();
        generateDefaultPanel();
        runMainLoop();
    }

    private void printWelcome() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Панель управления АЭС              ║");
        System.out.println("║   Controlpanel_of_NPP                ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void runMainLoop() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Выберите пункт: ");
            running = handleMainChoice(choice);
        }
        System.out.println(
                "Завершение работы системы. До свидания!"
        );
        scanner.close();
    }

    private boolean handleMainChoice(final int choice) {
        switch (choice) {
            case 1:
                System.out.println(panel.render());
                break;
            case 2:
                generatePanelWithParams();
                break;
            case 3:
                pressButtonMenu();
                break;
            case 4:
                releaseButtonMenu();
                break;
            case 5:
                commandHistory.undoLast();
                System.out.println(panel.render());
                break;
            case 6:
                showStatus();
                break;
            case 7:
                showCompositeStructure();
                break;
            case 0:
                return false;
            default:
                System.out.println(
                        "Неверный выбор. Попробуйте снова."
                );
        }
        return true;
    }

    private void printMainMenu() {
        System.out.println(
                "\n┌─────────────────────────────────────┐"
        );
        System.out.println(
                "│          Главное меню               │"
        );
        System.out.println(
                "├─────────────────────────────────────┤"
        );
        System.out.println(
                "│  1. Отобразить панель               │"
        );
        System.out.println(
                "│  2. Создать новую панель            │"
        );
        System.out.println(
                "│  3. Нажать кнопку                   │"
        );
        System.out.println(
                "│  4. Отпустить кнопку                │"
        );
        System.out.println(
                "│  5. Отменить последнее действие     │"
        );
        System.out.println(
                "│  6. Показать статус панели          │"
        );
        System.out.println(
                "│  7. Показать структуру (Composite)  │"
        );
        System.out.println(
                "│  0. Выход                           │"
        );
        System.out.println(
                "└─────────────────────────────────────┘"
        );
    }

    private void generateDefaultPanel() {
        System.out.println(
                "Генерация панели по умолчанию ("
                        + DEFAULT_SIZE + "x" + DEFAULT_SIZE + ")..."
        );
        buildPanel(DEFAULT_SIZE, DEFAULT_SIZE,
                System.currentTimeMillis());
        System.out.println(panel.render());
    }

    private void generatePanelWithParams() {
        System.out.println(
                "\n--- Создание новой панели управления ---"
        );
        int width = readIntInRange(
                "Ширина (" + MIN_PANEL_SIZE
                        + "-" + MAX_PANEL_SIZE + "): ",
                MIN_PANEL_SIZE,
                MAX_PANEL_SIZE
        );
        int height = readIntInRange(
                "Высота (" + MIN_PANEL_SIZE
                        + "-" + MAX_PANEL_SIZE + "): ",
                MIN_PANEL_SIZE,
                MAX_PANEL_SIZE
        );
        System.out.print(
                "Использовать фиксированный сид? (д/н): "
        );
        String answer = scanner.nextLine().trim().toLowerCase();
        long seed = System.currentTimeMillis();
        if (answer.equals("д")) {
            seed = readLong("Введите сид: ");
        }
        buildPanel(width, height, seed);
        System.out.println(
                "Панель " + width + "x" + height
                        + " успешно создана!"
        );
        System.out.println(panel.render());
    }

    private void buildPanel(
            final int width,
            final int height,
            final long seed) {
        RandomPanelBuilder builder = new RandomPanelBuilder(seed);
        PanelBuilder panelBuilder = builder
                .setSize(width, height)
                .placeComponents()
                .configureBindings();
        panel = panelBuilder.build();
        buttons = builder.getButtons();
        lamps = builder.getLamps();
    }

    private void pressButtonMenu() {
        if (buttons.isEmpty()) {
            System.out.println("На панели нет кнопок.");
            return;
        }
        System.out.println("\n--- Нажатие кнопки ---");
        printButtonList();
        int index = readIntInRange(
                "Номер кнопки (1-" + buttons.size() + "): ",
                1,
                buttons.size()
        );
        Button button = buttons.get(index - 1);
        if (button.isPressed()) {
            System.out.println(
                    "Кнопка '" + button.getName()
                            + "' уже нажата."
            );
            return;
        }
        commandHistory.executeCommand(
                new PressButtonCommand(button)
        );
        System.out.println(panel.render());
    }

    private void releaseButtonMenu() {
        if (buttons.isEmpty()) {
            System.out.println("На панели нет кнопок.");
            return;
        }
        System.out.println("\n--- Отпускание кнопки ---");
        printButtonList();
        int index = readIntInRange(
                "Номер кнопки (1-" + buttons.size() + "): ",
                1,
                buttons.size()
        );
        Button button = buttons.get(index - 1);
        if (!button.isPressed()) {
            System.out.println(
                    "Кнопка '" + button.getName()
                            + "' уже отпущена."
            );
            return;
        }
        commandHistory.executeCommand(
                new ReleaseButtonCommand(button)
        );
        System.out.println(panel.render());
    }

    private void showStatus() {
        System.out.println(
                "\n=== Статус панели управления АЭС ==="
        );
        System.out.println("Кнопки:");
        for (Button btn : buttons) {
            String state = btn.isPressed()
                    ? "[o] нажата  "
                    : "[O] отпущена";
            System.out.println(
                    "  " + btn.getName() + " — " + state
                            + " | ламп: "
                            + btn.getObservers().size()
            );
        }
        System.out.println("Лампы:");
        for (Lamp lamp : lamps) {
            String state = lamp.isActive()
                    ? "активна  [" + lamp.getIndicationColor() + "]"
                    : "неактивна";
            System.out.println(
                    "  " + lamp.getName() + " — " + state
            );
        }
        System.out.println("=====================================");
    }

    private void showCompositeStructure() {
        System.out.println(
                "\n--- Структура панели (Composite) ---"
        );
        PanelGroup root = new PanelGroup("Панель управления АЭС");
        PanelGroup buttonGroup = new PanelGroup("Кнопки");
        PanelGroup lampGroup = new PanelGroup("Лампы");

        for (int y = 0; y < panel.getHeight(); y++) {
            for (int x = 0; x < panel.getWidth(); x++) {
                Cell cell = panel.getCell(x, y);
                if (!cell.isEmpty()) {
                    buttonGroup.add(
                            new PanelLeaf(cell.getComponent(), x, y)
                    );
                }
            }
        }

        for (Lamp lamp : lamps) {
            lampGroup.add(new PanelLeaf(lamp, 0, 0));
        }

        root.add(buttonGroup);
        root.add(lampGroup);
        System.out.println(root.getDescription());
    }

    private void printButtonList() {
        System.out.println("Кнопки на панели:");
        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);
            String state = btn.isPressed()
                    ? "[o] нажата  "
                    : "[O] отпущена";
            System.out.println(
                    "  " + (i + 1) + ". "
                            + btn.getName() + " — " + state
                            + " | ламп: "
                            + btn.getObservers().size()
            );
        }
    }

    private int readInt(final String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(
                        scanner.nextLine().trim()
                );
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    private long readLong(final String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Long.parseLong(
                        scanner.nextLine().trim()
                );
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    private int readIntInRange(
            final String prompt,
            final int min,
            final int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(
                    "Введите число от " + min + " до " + max + "."
            );
        }
    }
}