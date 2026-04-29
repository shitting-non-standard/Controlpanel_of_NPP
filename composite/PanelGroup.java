package composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Составной узел — группа элементов панели управления.
 * Реализует паттерн Composite (составной узел).
 */
public class PanelGroup implements PanelElement {

    private final String groupName;
    private final List<PanelElement> children;

    public PanelGroup(final String name) {
        this.groupName = name;
        this.children = new ArrayList<>();
    }

    public void add(final PanelElement element) {
        children.add(element);
    }

    public void remove(final PanelElement element) {
        children.remove(element);
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Группа [").append(groupName).append("]:\n");
        for (PanelElement child : children) {
            sb.append("  ").append(child.getDescription()).append("\n");
        }
        return sb.toString();
    }
}