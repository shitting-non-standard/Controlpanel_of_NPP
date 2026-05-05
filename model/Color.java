package model;

/**
 * Перечисление доступных цветов индикации ламп
 */
public enum Color {
    RED("кр"),
    GREEN("зл"),
    BLUE("сн"),
    YELLOW("жл"),
    WHITE("бл");

    private final String code;

    Color(final String colorCode) {
        this.code = colorCode;
    }

    public String getCode() {
        return code;
    }
}