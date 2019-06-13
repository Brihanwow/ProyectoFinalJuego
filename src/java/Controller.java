import bean.Jugador;
import bean.Usuario;
import com.google.gson.Gson;
import dao.JugadoresDao;
import dao.ResultadosDao;
import dao.UsuariosDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import negocio.Nodo;
import negocio.Arista;

@WebServlet(urlPatterns = {"/Controller"})
public class Controller extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        switch (accion) {
        case "initial": 
            response.setContentType("text/html;charset=UTF-8");
            this.inicializarMatriz(request, response);
            break;
           
        case "jugada" :
            response.setContentType("text/html;charset=UTF-8");
            this.realizarJugada(request, response);
            break;
            
        case "jugadores":
            request.setAttribute("list", new JugadoresDao().obtenerJugadores());
            request.getRequestDispatcher("jugadores.jsp").forward(request, response);
            break;
            
        case "saveJugador":
            Usuario usuario = new Usuario();
            usuario.setRol("jugador");
            usuario.setNombre(request.getParameter("nombre"));
            usuario.setPassword(request.getParameter("password"));
            int id = new UsuariosDao().registrarUsuario(usuario);
            if(id == 0){
                request.setAttribute("msj", "La cantidad máxima de jugadores no puede ser mayor a 4");
                request.getRequestDispatcher("registrarJugador.jsp").forward(request, response);
            }
            else{
                Jugador jugador = new Jugador();
                jugador.setId_usuario(id);
                jugador.setColor(request.getParameter("color"));
                jugador.setSegundos(Integer.parseInt(request.getParameter("segundos")));
                new JugadoresDao().registrarJugador(jugador);
                request.setAttribute("msj", "ok");
                request.getRequestDispatcher("registrarJugador.jsp").forward(request, response);
            }
            break;
        
        case "editarJugador":
            request.setAttribute("jugador", new JugadoresDao().obtenerJugador(Integer.parseInt(request.getParameter("id"))));
            request.getRequestDispatcher("editarJugador.jsp").forward(request, response);
            break;
            
        case "updateJugador":
            Usuario userUpadte = new UsuariosDao().obtenerUsuario(Integer.parseInt(request.getParameter("id_usuario")));
            userUpadte.setNombre(request.getParameter("nombre"));
            userUpadte.setPassword(request.getParameter("password"));
            new UsuariosDao().actualizarUsuario(userUpadte);
            
            Jugador jugadorUpadte = new JugadoresDao().obtenerJugador(Integer.parseInt(request.getParameter("id")));
            jugadorUpadte.setColor(request.getParameter("color"));
            jugadorUpadte.setSegundos(Integer.parseInt(request.getParameter("segundos")));
            new JugadoresDao().actualizarJugador(jugadorUpadte);

            response.sendRedirect("Controller?accion=jugadores");
            break;
        
        case "eliminarJugador":
            new JugadoresDao().eliminarJugador(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect("Controller?accion=jugadores");
            break;
           
        case "obtenerJugadores":
            List<HashMap> list = new ArrayList<HashMap>();
            for(Jugador j : new JugadoresDao().obtenerJugadores()){
                HashMap<String,String> map = new LinkedHashMap<String,String>();
                map.put("id", String.valueOf(j.getId()));
                map.put("nombre", j.getUsuario().getNombre());
                map.put("color", j.getColor());
                list.add(map);
            }
            String json = new Gson().toJson(list);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            break;
            
        case "insertarGanador":
            new ResultadosDao().registrarResultado(Integer.parseInt(request.getParameter("ganador")));
            PrintWriter outR = response.getWriter();
            outR.print("ok");
            outR.flush();
            break;
            
        case "resultados":
            request.setAttribute("list", new ResultadosDao().obtenerResultados());
            request.getRequestDispatcher("resultados.jsp").forward(request, response);
            break;
            
        default:
            break;
        }
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void inicializarMatriz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("matriz");
        }
        
        Nodo [][] matriz = new Nodo[6][7];
        for(int f=0;f<matriz.length;f++) {
            for(int c=0;c<matriz[f].length;c++) {
                matriz[f][c]= new Nodo("");
            }
        }
        session.setAttribute("matriz",matriz);
        
        PrintWriter out = response.getWriter();
        out.print(1);
    }
    
    private void realizarJugada(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Nodo [][] matriz = (Nodo[][]) session.getAttribute("matriz");
        
        int f = Integer.parseInt(request.getParameter("fila"));
        int c = Integer.parseInt(request.getParameter("columna"));
        Nodo nodo = new Nodo(request.getParameter("jugadorActual"));
        
        if(session.getAttribute("ultimoNodo") != null){
            nodo.addArista(new Arista(nodo, (Nodo) session.getAttribute("ultimoNodo"), 1));
        }
        
        matriz[f][c]= nodo;
        session.setAttribute("matriz",matriz);
        session.setAttribute("ultimoNodo", nodo);
        
        String ganador = this.buscarGanador(matriz);
        if(!ganador.isEmpty()){
            session.removeAttribute("ultimoNodo");
            session.removeAttribute("matriz");
            Nodo [][] matrizP = new Nodo[6][7];
            for(int f_=0;f_<matrizP.length;f_++) {
                for(int c_=0;c_<matrizP[f_].length;c_++) {
                    matrizP[f_][c_]= new Nodo("");
                }
            }
            session.setAttribute("matriz",matrizP);
        }
        
        PrintWriter out = response.getWriter();
        out.print(ganador);
    }
    
    private String buscarGanador(Nodo[][] matriz) {
        //Buscamos en horizontal
        for (int f = 0; f < matriz.length; f++) {
            int[] jugadores = new int[]{ 0,0,0,0 }; 
            for (int c = 0; c < matriz[f].length; c++) {
                if (matriz[f][c].getJugador().isEmpty()) {
                    jugadores[0] = 0;
                    jugadores[1] = 0;
                    
                    
                    jugadores[2] = 0;
                    jugadores[3] = 0;
                } 
                else if (!(matriz[f][c].getJugador().isEmpty())) {
                    int posJugador = Integer.parseInt(matriz[f][c].getJugador()) - 1;
                    jugadores[posJugador] = jugadores[posJugador] + 1;
                    if(jugadores[posJugador] == 4) {
                        return matriz[f][c].getJugador();
                    }
                }
            }
        }

        //Buscamos en vertical de abajo a arriba
        for (int c = 0; c < matriz[0].length; c++) {
            int[] jugadores = new int[]{ 0,0,0,0 }; 
            for (int f = matriz.length - 1; f >= 0; f--) {	//De abajo a arriba para poder cortar.
                if (matriz[f][c].getJugador().isEmpty()) {
                    break;
                }
                else if (!(matriz[f][c].getJugador().isEmpty())) {
                    int posJugador = Integer.parseInt(matriz[f][c].getJugador()) - 1;
                    jugadores[posJugador] = jugadores[posJugador] + 1;
                    if(jugadores[posJugador] == 4){
                        return matriz[f][c].getJugador();
                    }
                }
            }
        }

        //Buscamos en diagonal de izquierda a derecha
        for (int i = -(matriz[0].length + 4); i < matriz[0].length; i++) {
            int[] jugadores = new int[]{ 0,0,0,0 }; 
            for (int f = 0; f < matriz.length; f++) {
                int c = i + f;
                if ((c < 0) || (c >= matriz[0].length)) {
                    continue;
                }
                if (matriz[f][c].getJugador().isEmpty()) {
                    jugadores[0] = 0;
                    jugadores[1] = 0;
                    jugadores[2] = 0;
                    jugadores[3] = 0;
                } else if (!(matriz[f][c].getJugador().isEmpty())) {
                    int posJugador = Integer.parseInt(matriz[f][c].getJugador()) - 1;
                    jugadores[posJugador] = jugadores[posJugador] + 1;
                    if(jugadores[posJugador] == 4){
                        return matriz[f][c].getJugador();
                    }
                }
            }
        }

        //Buscamos en diagonal de derecha a izquierda
        for (int i = 0; i < matriz[0].length + 4; i++) {
           int[] jugadores = new int[]{ 0,0,0,0 }; 
            for (int f = 0; f < matriz.length; f++) {
                int c = i - f;
                if ((c < 0) || (c >= matriz[0].length)) {
                    continue;
                }
                if (matriz[f][c].getJugador().isEmpty()) {
                    jugadores[0] = 0;
                    jugadores[1] = 0;
                    jugadores[2] = 0;
                    jugadores[3] = 0;
                } else if (!(matriz[f][c].getJugador().isEmpty())) {
                    int posJugador = Integer.parseInt(matriz[f][c].getJugador()) - 1;
                    jugadores[posJugador] = jugadores[posJugador] + 1;
                    if(jugadores[posJugador] == 4){
                        return matriz[f][c].getJugador();
                    }
                }
            }
        }
        return "";
    }
}