<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registar Jugador</title>
    </head>
    <body>
        <jsp:include page="plantilla.jsp" />
        <div class="main">
            <form action="Controller?accion=saveJugador" method="post" style="width: 30%; margin: 0 auto;">
                <div class="form-group">
                    <label for="nombre">Nombre</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="color">Color</label>
                    <input type="color" class="form-control" id="color" name="color" required>
                </div>
                <div class="form-group">
                    <label for="segundos">Segundos para jugar</label>
                    <input type="number" required class="form-control" id="segundos" name="segundos" min="0" max="3600" value="60">
                </div>
                <button type="submit" class="btn btn-default">Registrar</button>
            </form>
            <c:out value="${msj}"></c:out>
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