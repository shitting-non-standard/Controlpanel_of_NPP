package model;

/**
 * Перечисление доступных цветов индикации ламп
 */
public enum Color {
    RED("R"),
    GREEN("G"),
    BLUE("B"),
    YELLOW("Y"),
    WHITE("W");

    private final String code;

    Color(final String colorCode) {
        this.code = colorCode;
    }

    public String getCode() {
        return code;
    }
}