package npp.model;

import npp.composite.PanelGroup;

/**
 * Базовый интерфейс для всех компонентов панели управления.
 */
public interface PanelComponent {
    String render();

    void addToGroup(PanelGroup buttonGroup, PanelGroup lampGroup, int x, int y);
}
