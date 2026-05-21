package builder;

import container.ControlPanel;
import factory.ButtonFactory;
import factory.LampFactory;
import model.Button;
import model.Color;
import model.Lamp;
import observer.LampController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Строитель панели управления со случайным размещением компонентов.
 * Реализует паттерн Builder.
 */
public class RandomPanelBuilder implements PanelBuilder {

    private static final int MIN_SIZE = 2;
    private static final double BUTTON_PROBABILITY = 0.3;
    private static final double LAMP_PROBABILITY = 0.4;

    private int panelWidth;
    private int panelHeight;
    private ControlPanel panel;
    private final List<Button> buttons;
    private final List<Lamp> lamps;
    private final Random random;
    private final Color[] colors;
    private int buttonCounter;
    private int lampCounter;

    public RandomPanelBuilder() {
        this(System.currentTimeMillis());
    }

    public RandomPanelBuilder(final long seed) {
        this.random = new Random(seed);
        this.buttons = new ArrayList<>();
        this.lamps = new ArrayList<>();
        this.colors = Color.values();
        this.buttonCounter = 1;
        this.lampCounter = 1;
    }

    @Override
    public PanelBuilder setSize(final int width, final int height) {
        if (width < MIN_SIZE || height < MIN_SIZE) {
            throw new IllegalArgumentException(
                    "Минимальный размер панели: "
                            + MIN_SIZE + "x" + MIN_SIZE
            );
        }
        this.panelWidth = width;
        this.panelHeight = height;
        this.panel = new ControlPanel(width, height);
        return this;
    }

    @Override
    public PanelBuilder placeComponents() {
        buttons.clear();
        lamps.clear();
        buttonCounter = 1;
        lampCounter = 1;

        ButtonFactory buttonFactory = new ButtonFactory();

        for (int y = 0; y < panelHeight; y++) {
            for (int x = 0; x < panelWidth; x++) {
                double roll = random.nextDouble();
                if (roll < BUTTON_PROBABILITY) {
                    placeButton(x, y, buttonFactory);
                } else if (roll < BUTTON_PROBABILITY + LAMP_PROBABILITY) {
                    placeLamp(x, y);
                }
            }
        }

        ensureAtLeastOneButton(buttonFactory);
        ensureAtLeastOneLamp();

        return this;
    }

    @Override
    public PanelBuilder configureBindings() {
        for (Button button : buttons) {
            int bindCount = 1 + random.nextInt(
                    Math.max(1, lamps.size())
            );
            bindCount = Math.min(bindCount, lamps.size());

            List<Lamp> shuffled = new ArrayList<>(lamps);
            shuffleList(shuffled);

            for (int i = 0; i < bindCount; i++) {
                LampController.bind(button, shuffled.get(i));
            }
        }
        return this;
    }

    @Override
    public ControlPanel build() {
        return panel;
    }

    public List<Button> getButtons() {
        return new ArrayList<>(buttons);
    }

    public List<Lamp> getLamps() {
        return new ArrayList<>(lamps);
    }

    private void placeButton(
            final int x,
            final int y,
            final ButtonFactory factory) {
        Button btn = factory.createButton("Btn" + buttonCounter++);
        panel.placeComponent(x, y, btn);
        buttons.add(btn);
    }

    private void placeLamp(final int x, final int y) {
        Color color = colors[random.nextInt(colors.length)];
        LampFactory lampFactory = new LampFactory(color);
        Lamp lamp = lampFactory.createLamp("Lamp" + lampCounter++);
        lamps.add(lamp);
    }

    private void ensureAtLeastOneButton(final ButtonFactory factory) {
        if (buttons.isEmpty()) {
            placeButton(0, 0, factory);
        }
    }

    private void ensureAtLeastOneLamp() {
        if (lamps.isEmpty()) {
            placeLamp(panelWidth - 1, panelHeight - 1);
        }
    }

    private void shuffleList(final List<Lamp> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Lamp temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
}