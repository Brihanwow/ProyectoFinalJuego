package dao;

import bean.Jugador;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;

public class JugadoresDao {
    private SessionFactory sessionFactory = null;

    public JugadoresDao() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");        	
        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
        sb.applySettings(cfg.getProperties());
        StandardServiceRegistry standardServiceRegistry = sb.build();           	
        sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
    }
    
    public List<Jugador> obtenerJugadores() {
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Jugador> jugadores = session.createQuery("from jugadores").list();
        return jugadores;
    }
    
    public void registrarJugador(Jugador jugador){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(jugador);
        session.getTransaction().commit();
    }
    
     public Jugador obtenerJugador(int id){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        Jugador jugador = null;
        jugador = (Jugador) session.createQuery("from jugadores j where j.id = :id")
                .setParameter("id", id)
                .list().get(0);
        session.getTransaction().commit();
        return jugador;
    }
     
     public void actualizarJugador(Jugador jugador){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(jugador);
        session.getTransaction().commit();
    }
     
     public void eliminarJugador(int id){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        Jugador jugador = null;
        jugador = (Jugador) session.createQuery("from jugadores j where j.id = :id")
                .setParameter("id", id)
                .list().get(0);
        int id_usuario = jugador.getId_usuario();
        session.delete(jugador);
        session.getTransaction().commit();
        new UsuariosDao().eliminarUsuario(id_usuario);
    }
}