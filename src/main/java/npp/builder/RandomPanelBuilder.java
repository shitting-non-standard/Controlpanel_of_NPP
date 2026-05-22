package npp.builder;

import npp.container.ControlPanel;
import npp.factory.ButtonFactory;
import npp.factory.LampFactory;
import npp.model.Button;
import npp.model.Color;
import npp.model.Lamp;
import npp.observer.LampController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Строитель панели управления со случайным размещением компонентов.
 * Паттерн Builder.
 *
 * Spring Bean — прототип. Каждый раз при создании новой панели
 * нужен свой экземпляр строителя с независимым состоянием (counters, seed).
 * Фабрики (ButtonFactory, LampFactory) внедряются через DI.
 */
@Component
@Scope("prototype")
public class RandomPanelBuilder implements PanelBuilder {

    private static final int MIN_SIZE = 2;
    private static final double BUTTON_PROBABILITY = 0.3;
    private static final double LAMP_PROBABILITY = 0.4;

    private final ButtonFactory buttonFactory;
    private final LampFactory lampFactory;

    private int panelWidth;
    private int panelHeight;
    private ControlPanel panel;
    private final List<Button> buttons = new ArrayList<>();
    private final List<Lamp> lamps = new ArrayList<>();
    private Random random = new Random();
    private final Color[] colors = Color.values();
    private int buttonCounter = 1;
    private int lampCounter = 1;

    /**
     * DI через конструктор — фабрики являются синглтонами-Spring Bean.
     */
    @Autowired
    public RandomPanelBuilder(final ButtonFactory buttonFactory, final LampFactory lampFactory) {
        this.buttonFactory = buttonFactory;
        this.lampFactory = lampFactory;
    }

    public void setSeed(final long seed) {
        this.random = new Random(seed);
    }

    @Override
    public PanelBuilder setSize(final int width, final int height) {
        if (width < MIN_SIZE || height < MIN_SIZE) {
            throw new IllegalArgumentException(
                    "Минимальный размер панели: " + MIN_SIZE + "x" + MIN_SIZE);
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

        for (int y = 0; y < panelHeight; y++) {
            for (int x = 0; x < panelWidth; x++) {
                double roll = random.nextDouble();
                if (roll < BUTTON_PROBABILITY) {
                    placeButton(x, y);
                } else if (roll < BUTTON_PROBABILITY + LAMP_PROBABILITY) {
                    placeLamp(x, y);
                }
            }
        }

        ensureAtLeastOneButton();
        ensureAtLeastOneLamp();
        return this;
    }

    @Override
    public PanelBuilder configureBindings() {
        for (Button button : buttons) {
            int bindCount = 1 + random.nextInt(Math.max(1, lamps.size()));
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

    private void placeButton(final int x, final int y) {
        Button btn = buttonFactory.createButton("Btn" + buttonCounter++);
        panel.placeComponent(x, y, btn);
        buttons.add(btn);
    }

    private void placeLamp(final int x, final int y) {
        Color color = colors[random.nextInt(colors.length)];
        Lamp lamp = lampFactory.createLamp("Lamp" + lampCounter++, color);
        panel.placeComponent(x, y, lamp);
        lamps.add(lamp);
    }

    private void ensureAtLeastOneButton() {
        if (buttons.isEmpty()) {
            placeButton(0, 0);
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
