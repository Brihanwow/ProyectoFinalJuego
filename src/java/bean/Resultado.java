package bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "resultados")
public class Resultado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_partida")
    private int id_partida;

    @Column(name = "id_jugador")
    private int id_jugador;

    @Column(name = "posicion")
    private int posicion;

    @Column(name = "tiempo_invertido")
    private int tiempo_invertido;
    
    @Transient
    private String partida;
    
    @Transient
    private String jugador;

    public int getId() {
        return id;
    }

    public int getId_partida() {
        return id_partida;
    }

    public void setId_partida(int id_partida) {
        this.id_partida = id_partida;
    }

    public int getId_jugador() {
        return id_jugador;
    }
    
    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getTiempo_invertido() {
        return tiempo_invertido;
    }

    public void setTiempo_invertido(int tiempo_invertido) {
        this.tiempo_invertido = tiempo_invertido;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }
}