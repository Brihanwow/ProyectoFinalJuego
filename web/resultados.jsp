<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Historial de resultados</title>
    </head>
    <body>
        <jsp:include page="plantilla.jsp" />
        <div class="main">
            <table border="1" class="table">
                <thead>
                    <tr>
                        <th>Partida</th>
                        <th>Jugador</th>
                        <th>Posicion</th>
                        <th>Tiempo invertido</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="resultado" >
                        <tr>
                            <td>${resultado.partida}</td>
                            <td>${resultado.jugador}</td>
                            <td>${resultado.posicion}</td>
                            <td>${resultado.tiempo_invertido}</td>
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
        $("#m_resultados").addClass("active");
    });
</script>