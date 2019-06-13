<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Proyecto final</title>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="css/estilos.css" />
    </head>
    <body>
        <jsp:include page="plantilla.jsp" />
        <div class="main">
            <p>Lista de jugadores</p>
            <div class="container">
                <div class="row">
                    <div class="col-sm-3">
                        <p>Jugador 1: <span id="jugador1" style="color:blue;">color</span> </p>
                        <p>Jugador 2: <span id="jugador2" style="color:yellow;">color</span> </p>
                        <p>Jugador 3: <span id="jugador3" style="color:green;">color</span> </p>
                        <p>Jugador 4: <span id="jugador4" style="color: red;">color</span> </p>
                    </div>
                    <div class="col-sm">
                        <p><b>Juego actual: </b><span id="juego_actual"></span></p>
                        <p>Turno de: <b id="turno_de"></b></p>
                        <p data-chronometer>00:00:00</p>
                        <button id="play" style="display: none;">play</button>
                        <button id="pause" style="display: none;">pause</button>
                        <button id="reset" style="display: none;">reset</button>
                    </div>
                </div>
                <div class="row">
                    <button type="button" disabled="disabled" id="btnEmparejar" class="btn btn-default">Emparejar</button>
                </div>
                <div class="row">
                    <br>
                </div>
                <div class="row">
                    <button type="button" disabled="disabled" id="btnIniciar" class="btn btn-primary">Iniciar juego</button>
                </div>
            </div>
            <br>
            
            <svg id="svg" width="600" height="650"></svg>
            <script src="js/funciones.js"></script>
        </div>
    </body>
</html>
<script type="text/javascript">
    $(document).ready(function () {
        let elements = $("#menus").children();
        for(let i=0; i < elements.length; i++){
            $(elements[i]).removeClass("active");
        }
        $("#m_inicio").addClass("active");
    });
</script>
