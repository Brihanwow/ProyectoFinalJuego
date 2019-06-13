<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de jugadores</title>
    </head>
    <body>
        <jsp:include page="plantilla.jsp" />
        <br>
        <button type="button" class="btn" style="margin-left: 10px;">
            <a href="registrarJugador.jsp">Registrar Jugador (max 4)</a>
        </button>
        <div class="main">
            <table border="1" class="table">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Color</th>
                        <th>Segundos para jugar</th>
                        <th colspan="2">Opciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="jugador" >
                        <tr>
                            <td>${jugador.usuario.nombre}</td>
                            <td><span style="font-weight: bold; color:${jugador.color}">${jugador.color}</span></td>
                            <td>${jugador.segundos}</td>
                            <td><a href="Controller?accion=editarJugador&id=${jugador.id}">Editar</a></td>
                            <td><a href="Controller?accion=eliminarJugador&id=${jugador.id}">Eliminar</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
<script type="text/javascript">
    $(document).ready(function () {
        let elements = $("#menus").children();
        for(let i=0; i < elements.length; i++){
            $(elements[i]).removeClass("active");
        }
        $("#m_jugadores").addClass("active");
    });
</script>