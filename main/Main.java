package main;

import menu.MenuController;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        MenuController controller = new MenuController();
        controller.start();
    }
}