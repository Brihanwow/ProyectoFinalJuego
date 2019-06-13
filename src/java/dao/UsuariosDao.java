package dao;

import bean.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;

public class UsuariosDao {

    private SessionFactory sessionFactory = null;

    public UsuariosDao() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");        	
        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
        sb.applySettings(cfg.getProperties());
        StandardServiceRegistry standardServiceRegistry = sb.build();           	
        sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
    }
 
    public int registrarUsuario(Usuario usuario){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        long cantidad =  
                (long)session.createQuery("select count(1) from usuarios where rol = :rol")
                .setParameter("rol", "jugador")
                .uniqueResult();
        if(cantidad >= 4) return 0;
        session.save(usuario);
        session.getTransaction().commit();
        return usuario.getId();
    }
    
    public void actualizarUsuario(Usuario usuario){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(usuario);
        session.getTransaction().commit();
    }
    
    public Usuario obtenerUsuario(int id){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        Usuario usuario = null;
        usuario = (Usuario) session.createQuery("from usuarios u where u.id = :id")
                .setParameter("id", id)
                .list().get(0);
        session.getTransaction().commit();
        return usuario;
    }
    
    public void eliminarUsuario(int id){
        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
         Usuario usuario = null;
        usuario = (Usuario) session.createQuery("from usuarios u where u.id = :id")
                .setParameter("id", id)
                .list().get(0);
        session.delete(usuario);
        session.getTransaction().commit();
    }
}
