package panel.npp.container;

import panel.npp.model.PanelComponent;

/**
 * Контейнер панели управления размером NxM ячеек.
 * Не является Spring-бином — создаётся через RandomPanelBuilder как структура данных.
 */
public class ControlPanel {

    private final Cell[][] cells;
    private final int width;
    private final int height;

    public ControlPanel(final int panelWidth, final int panelHeight) {
        this.width = panelWidth;
        this.height = panelHeight;
        this.cells = new Cell[panelHeight][panelWidth];
        initCells();
    }

    private void initCells() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell(x, y);
            }
        }
    }

    public void placeComponent(final int x, final int y, final PanelComponent component) {
        validateCoords(x, y);
        cells[y][x].setComponent(component);
    }

    public Cell getCell(final int x, final int y) {
        validateCoords(x, y);
        return cells[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("Панель управления АЭС (")
                .append(width).append("x").append(height)
                .append("):\n");
        sb.append("   ");
        for (int x = 0; x < width; x++) {
            sb.append(String.format(" %3d ", x));
        }
        sb.append("\n");
        for (int y = 0; y < height; y++) {
            sb.append(y).append("  ");
            for (int x = 0; x < width; x++) {
                sb.append(String.format("%-5s", cells[y][x].render()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void validateCoords(final int x, final int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                    "Некорректные координаты: (" + x + ", " + y + ")"
            );
        }
    }
}
