package negocio;

import java.util.ArrayList;
import java.util.List;

public class Grafo {

    private List<Nodo> nodes;

    public void addNodo(Nodo node) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        nodes.add(node);
    }

    public List<Nodo> getNodos() {
        return nodes;
    }

    @Override
    public String toString() {
        return "Graph [nodes=" + nodes + "]";
    }
}