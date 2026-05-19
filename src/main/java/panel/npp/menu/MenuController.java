package panel.npp.menu;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import panel.npp.builder.PanelBuilder;
import panel.npp.builder.RandomPanelBuilder;
import panel.npp.command.CommandHistory;
import panel.npp.command.PressButtonCommand;
import panel.npp.command.ReleaseButtonCommand;
import panel.npp.composite.PanelGroup;
import panel.npp.container.Cell;
import panel.npp.container.ControlPanel;
import panel.npp.factory.ButtonFactory;
import panel.npp.factory.LampFactory;
import panel.npp.model.Button;
import panel.npp.model.Lamp;
import panel.npp.observer.LampController;

import java.util.List;
import java.util.Scanner;

/**
 * Контроллер интерактивного меню панели управления АЭС.
 * Является Spring-бином (@Component) и точкой запуска приложения (ApplicationRunner).
 * Scanner не является бином — создаётся вручную как утилитарный объект ввода.
 */
@Component
public class MenuController implements ApplicationRunner {

    private static final int MIN_PANEL_SIZE = 2;
    private static final int MAX_PANEL_SIZE = 10;
    private static final int DEFAULT_SIZE = 4;

    private ControlPanel panel;
    private List<Button> buttons;
    private List<Lamp> lamps;

    private final CommandHistory commandHistory;
    private final ButtonFactory buttonFactory;
    private final LampFactory lampFactory;
    private final LampController lampController;

    public MenuController(final CommandHistory commandHistory,
                          final ButtonFactory buttonFactory,
                          final LampFactory lampFactory,
                          final LampController lampController) {
        this.commandHistory = commandHistory;
        this.buttonFactory = buttonFactory;
        this.lampFactory = lampFactory;
        this.lampController = lampController;
    }

    @Override
    public void run(final ApplicationArguments args) {
        // Scanner не является бином — это простой утилитарный объект
        Scanner scanner = new Scanner(System.in);
        try {
            start(scanner);
        } finally {
            scanner.close();
        }
    }

    private void start(final Scanner scanner) {
        printWelcome();
        generateDefaultPanel();
        runMainLoop(scanner);
    }

    private void printWelcome() {
        System.out.println("+--------------------------------------+");
        System.out.println("|   Панель управления АЭС              |");
        System.out.println("|   Controlpanel_of_NPP                |");
        System.out.println("+--------------------------------------+");
    }

    private void runMainLoop(final Scanner scanner) {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt(scanner, "Выберите пункт: ");
            running = handleMainChoice(scanner, choice);
        }
        System.out.println("Завершение работы системы. До свидания!");
    }

    private boolean handleMainChoice(final Scanner scanner, final int choice) {
        switch (choice) {
            case 1 -> System.out.println(panel.render());
            case 2 -> generatePanelWithParams(scanner);
            case 3 -> pressButtonMenu(scanner);
            case 4 -> releaseButtonMenu(scanner);
            case 5 -> {
                commandHistory.undoLast();
                System.out.println(panel.render());
            }
            case 6 -> showStatus();
            case 7 -> showCompositeStructure();
            case 8 -> showBindings();
            case 0 -> { return false; }
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
        return true;
    }

    private void printMainMenu() {
        System.out.println("\n+--------------------------------------+");
        System.out.println("|           Главное меню               |");
        System.out.println("+--------------------------------------+");
        System.out.println("|  1. Отобразить панель                |");
        System.out.println("|  2. Создать новую панель             |");
        System.out.println("|  3. Нажать кнопку                    |");
        System.out.println("|  4. Отпустить кнопку                 |");
        System.out.println("|  5. Отменить последнее действие      |");
        System.out.println("|  6. Показать статус панели           |");
        System.out.println("|  7. Показать структуру               |");
        System.out.println("|  8. Показать связи кнопок и ламп     |");
        System.out.println("|  0. Выход                            |");
        System.out.println("+--------------------------------------+");
    }

    private void generateDefaultPanel() {
        System.out.println("Генерация панели по умолчанию ("
                + DEFAULT_SIZE + "x" + DEFAULT_SIZE + ")...");
        buildPanel(DEFAULT_SIZE, DEFAULT_SIZE, System.currentTimeMillis());
        System.out.println(panel.render());
    }

    private void generatePanelWithParams(final Scanner scanner) {
        System.out.println("\n--- Создание новой панели управления ---");
        int width = readIntInRange(scanner,
                "Ширина (" + MIN_PANEL_SIZE + "-" + MAX_PANEL_SIZE + "): ",
                MIN_PANEL_SIZE, MAX_PANEL_SIZE);
        int height = readIntInRange(scanner,
                "Высота (" + MIN_PANEL_SIZE + "-" + MAX_PANEL_SIZE + "): ",
                MIN_PANEL_SIZE, MAX_PANEL_SIZE);
        System.out.print("Использовать фиксированный сид? (д/н): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        long seed = System.currentTimeMillis();
        if (answer.equals("д")) {
            seed = readLong(scanner, "Введите сид: ");
        }
        buildPanel(width, height, seed);
        System.out.println("Панель " + width + "x" + height + " успешно создана!");
        System.out.println(panel.render());
    }

    private void buildPanel(final int width, final int height, final long seed) {
        // RandomPanelBuilder не является бином — создаётся для каждой генерации
        RandomPanelBuilder builder = new RandomPanelBuilder(
                seed, buttonFactory, lampFactory, lampController
        );
        PanelBuilder panelBuilder = builder
                .setSize(width, height)
                .placeComponents()
                .configureBindings();
        panel = panelBuilder.build();
        buttons = builder.getButtons();
        lamps = builder.getLamps();
    }

    private void pressButtonMenu(final Scanner scanner) {
        if (buttons.isEmpty()) {
            System.out.println("На панели нет кнопок.");
            return;
        }
        System.out.println("\n--- Нажатие кнопки ---");
        printButtonList();
        int index = readIntInRange(scanner,
                "Номер кнопки (1-" + buttons.size() + "): ", 1, buttons.size());
        Button button = buttons.get(index - 1);
        if (button.isPressed()) {
            System.out.println("Кнопка '" + button.getName() + "' уже нажата.");
            return;
        }
        commandHistory.executeCommand(new PressButtonCommand(button));
        System.out.println(panel.render());
    }

    private void releaseButtonMenu(final Scanner scanner) {
        if (buttons.isEmpty()) {
            System.out.println("На панели нет кнопок.");
            return;
        }
        System.out.println("\n--- Отпускание кнопки ---");
        printButtonList();
        int index = readIntInRange(scanner,
                "Номер кнопки (1-" + buttons.size() + "): ", 1, buttons.size());
        Button button = buttons.get(index - 1);
        if (!button.isPressed()) {
            System.out.println("Кнопка '" + button.getName() + "' уже отпущена.");
            return;
        }
        commandHistory.executeCommand(new ReleaseButtonCommand(button));
        System.out.println(panel.render());
    }

    private void showStatus() {
        System.out.println("\n=== Статус панели управления АЭС ===");
        System.out.println("Кнопки:");
        for (Button btn : buttons) {
            String state = btn.isPressed() ? "[o] нажата  " : "[O] отпущена";
            System.out.println("  " + btn.getName() + " — " + state
                    + " | ламп: " + btn.getObservers().size());
        }
        System.out.println("Лампы:");
        for (Lamp lamp : lamps) {
            String state = lamp.isActive()
                    ? "активна  [" + lamp.getIndicationColor() + "]"
                    : "неактивна";
            System.out.println("  " + lamp.getName() + " — " + state);
        }
        System.out.println("=====================================");
    }

    private void showCompositeStructure() {
        System.out.println("\n--- Структура панели (Composite) ---");
        PanelGroup root = new PanelGroup("Панель управления АЭС");
        PanelGroup buttonGroup = new PanelGroup("Кнопки");
        PanelGroup lampGroup = new PanelGroup("Лампы");

        for (int y = 0; y < panel.getHeight(); y++) {
            for (int x = 0; x < panel.getWidth(); x++) {
                Cell cell = panel.getCell(x, y);
                if (!cell.isEmpty()) {
                    cell.getComponent().addToGroup(buttonGroup, lampGroup, x, y);
                }
            }
        }

        root.add(buttonGroup);
        root.add(lampGroup);
        System.out.println(root.getDescription());
    }

    private void showBindings() {
        System.out.println("\n--- Связи кнопок и ламп ---");
        for (Button button : buttons) {
            System.out.println(button.getName() + " -> " + button.getBindingsDescription());
        }
        System.out.println("---------------------------");
    }

    private void printButtonList() {
        System.out.println("Кнопки на панели:");
        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);
            String state = btn.isPressed() ? "[o] нажата  " : "[O] отпущена";
            System.out.println("  " + (i + 1) + ". " + btn.getName()
                    + " — " + state + " | ламп: " + btn.getObservers().size());
        }
    }

    private int readInt(final Scanner scanner, final String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    private long readLong(final Scanner scanner, final String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    private int readIntInRange(final Scanner scanner, final String prompt,
                               final int min, final int max) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Введите число от " + min + " до " + max + ".");
        }
    }
}
