package negocio;

import java.util.ArrayList;
import java.util.List;

public class Nodo {

    private String jugador;
    private List<Arista> aristas;

    public Nodo(String jugador) {
        this.jugador = jugador;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public List<Arista> getAristas() {
        return aristas;
    }

    public void addArista(Arista arista) {
        if (aristas == null) {
            aristas = new ArrayList<>();
        }
        aristas.add(arista);
    }

    @Override
    public String toString() {
        return "\n \tNode [jugador=" + jugador + ", aristas=" + aristas + "]";
    }
}
