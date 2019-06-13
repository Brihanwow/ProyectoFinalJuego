package dao;

import bean.Partida;
import bean.Resultado;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.transform.Transformers;

public class ResultadosDao {
    private SessionFactory sessionFactory = null;

    public ResultadosDao() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");        	
        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
        sb.applySettings(cfg.getProperties());
        StandardServiceRegistry standardServiceRegistry = sb.build();           	
        sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
    }
    
     public void registrarResultado(int id){
        Session session = this.sessionFactory.getCurrentSession();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(new Date());
        
        session.beginTransaction();
        
        Partida partida = new Partida();
        partida.setNombre("Partida "+strDate);
        session.save(partida);
        
        Resultado resultado = new Resultado();
        resultado.setId_partida(partida.getId());
        resultado.setId_jugador(id);
        resultado.setPosicion(1);
        resultado.setTiempo_invertido(100);
        session.save(resultado);
        
        session.getTransaction().commit();
    }
     
    public List<Resultado> obtenerResultados() {
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        String sql = "select "
                + "r.id as id,"
                + "p.nombre as partida,"
                + "u.nombre as jugador,"
                + "r.posicion as posicion,"
                + "r.tiempo_invertido as tiempo_invertido "
                + "from resultados r, partidas p, jugadores j, usuarios u "
                + "where r.id_partida = p.id "
                + "and r.id_jugador = j.id "
                + "and j.id_usuario = u.id and u.rol = :rol";
        Query query = session.createQuery(sql).setParameter("rol", "jugador");
        query.setResultTransformer(Transformers.aliasToBean(Resultado.class));
        return (List<Resultado>) query.list();
    }
}