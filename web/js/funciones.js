//Constantes
const FILAS = 6;
const COLS = 7;
const SVG_NS = "http://www.w3.org/2000/svg";
const COLOR0 = "white";
var COLOR_HUMANO = "blue";
var jugadorActual = 1;
var jugada = 1;
var iteracion = 1;
var ganadores = [];
var dataIteracion = null;

//Variables Globales
var svg = null;
var turno = document.getElementById("jugador1").style.color;	//Cambiará de COLOR
var tablero; //array de colores (por columnas) de 7 arrays de 6 posiciones.
var vistaTablero; //array de circles (por columnas) de 7 arrays de 6 posiciones.
var puntos = [0, 0];
var contadorMovimientos = 0;
var rMargenY = 50;
var rAltura = 515;

$(document).ready(function () {
    inicializarMatriz();
    obtenerJugadores();
    $("#btnEmparejar").click(function(){
        //lo desordeno y pongo las dos posiciones vs las otra dos
        var data = JSON.parse(sessionStorage.getItem("jugadores"));
        data = data.sort(() => Math.random() - 0.5);
        var obj = {};
        obj["1"] = [data[0],data[1]];
        obj["2"] = [data[2],data[3]];
        sessionStorage.setItem("iteracion",JSON.stringify(obj));
        
        alert(`${data[0].nombre} vs ${data[1].nombre}\n${data[2].nombre} vs ${data[3].nombre}`);
        $("#btnEmparejar").attr("disabled","disabled");
        $("#btnIniciar").removeAttr("disabled");
        $("#juego_actual").html(`${data[0].nombre} vs ${data[1].nombre}`);
        $("#turno_de").html(data[0].nombre);
        $("#turno_de").css("color",data[0].color);
    })
    
    $("#btnIniciar").click(function(){
        $("#btnIniciar").attr("disabled","disabled");
        dataIteracion = JSON.parse(sessionStorage.getItem("iteracion"));
        COLOR_HUMANO = dataIteracion[iteracion][0]["color"];
        turno = dataIteracion[iteracion][0]["color"];
        jugadorActual = 1;
        jugada = 1;
        $("#svg").html("");
        preparar();
        $("#play").click();
    })
});

function inicializarMatriz() {
    $.ajax({
        type: "POST",
        url: "Controller",
        data: { accion : "initial" },
        success: function (data) {
            console.log('ok');
        }
    });
}

function enviarJugada(f = 0, c = 0){
    if(f === 0 && c === 0) return;
    $.ajax({
        type: "POST",
        url: "Controller",
        data: { 
            accion : "jugada",
            jugada : jugada,
            fila : f,
            columna : c,
            jugadorActual : jugadorActual
        },
        success: function (data) {
            if(data !== ""){
                $("#reset").click();
                var objIteracion = JSON.parse(sessionStorage.getItem("iteracion"));
                var ganador = objIteracion[iteracion][parseInt(data)-1];
                ganadores.push(ganador);
                alert(`El ganador es el jugador ${ganador["nombre"]}`);
                
                //insertar ganador en bd
                $.ajax({
                    type: "POST",
                    url: "Controller",
                    data: { accion : "insertarGanador", ganador : ganador["id"] },
                    success: function (r) {
                        console.log('ok');
                    }
                });
                
                iteracion++;
                $("#btnIniciar").removeAttr("disabled");
                if(ganadores.length === 2){
                    debugger;
                    iteracion = 1;
                    //cambiar obj iteracion
                    objIteracion = {};
                    objIteracion["1"] = [ganadores[0],ganadores[1]];
                    sessionStorage.setItem("iteracion",JSON.stringify(objIteracion));
                    alert("Los finalistas son: "+ganadores[0]["nombre"] + " vs "+ganadores[1]["nombre"]);
                    ganadores.length = 0;
                }
                try{
                    $("#juego_actual").html(`${objIteracion[iteracion][0].nombre} vs ${objIteracion[iteracion][1].nombre}`);
                    $("#turno_de").html(objIteracion[iteracion][0].nombre);
                    $("#turno_de").css("color",objIteracion[iteracion][0].color);
                }
                catch(error){
                    alert(`El gran ganador es el jugador ${ganador["nombre"]} Felicidades !`);
                    location.reload();
                }
            }
        }
    });
}

function obtenerJugadores() {
    $.ajax({
        type: "POST",
        url: "Controller",
        data: { accion : "obtenerJugadores" },
        success: function (data) {
            if(data == null || data.length < 4){
                return alert("Deben haber como minimo 4 jugadores");
            }
            sessionStorage.setItem("jugadores",JSON.stringify(data));
            for(var i=0;i<data.length;i++){
                $("#jugador"+(i+1)).html(data[i].nombre);
                $("#jugador"+(i+1)).css("color",data[i].color);
            }
            $("#btnEmparejar").removeAttr("disabled");
            cronometro();
        }
    });
}

function preparar() {
    svg = document.getElementById("svg");

    r = document.createElementNS(SVG_NS, "rect");
    r.setAttribute('x', 0);
    r.setAttribute('y', rMargenY);	//margen superior para las flechas
    r.setAttribute('rx', 20);
    r.setAttribute('ry', 20);
    r.setAttribute('width', 600);
    r.setAttribute('height', 515);
    svg.appendChild(r);

    //document.getElementById("btnSiguiente").addEventListener("click", iniciarPartida);

    iniciarPartida();
}

function iniciarPartida() {
    tablero = new Array(FILAS);
    vistaTablero = new Array(FILAS);
    for (let i = 0; i < FILAS; i++) {
        tablero[i] = new Array(COLS);
        vistaTablero[i] = new Array(COLS);
    }

    var cRadio = 40;
    for (var i = 0; i < FILAS; i++) {
        for (var j = 0; j < COLS; j++) {
            var cy = 5 + rMargenY + cRadio + (2 * cRadio + 5) * i;
            var cx = 5 + 0 + cRadio + (2 * cRadio + 5) * j;
            c = document.createElementNS(SVG_NS, "circle");
            //c.id = 
            c.setAttribute('cx', cx);
            c.setAttribute('cy', cy);
            c.setAttribute('r', 40);
            c.style.fill = COLOR0;
            svg.appendChild(c);
            vistaTablero[i][j] = c;
            tablero[i][j] = COLOR0;
        }
    }

    for (var i = 0; i < 7; i++) {
        f = document.createElementNS(SVG_NS, "polygon");
        var p1X = 15 + 85 * i;
        var p1Y = 15;
        var p2X = p1X + (86 - 15 * 2);
        var p2Y = p1Y;
        var p3X = p1X + (p2X - p1X) / 2;
        var p3Y = 40;
        f.setAttribute("points", p1X + "," + p1Y + " " + p2X + "," + p2Y + " " + p3X + "," + p3Y);
        f.setAttribute("data-col", i);
        svg.append(f);

        f.addEventListener("mouseover", cambiarColorFlecha);
        f.addEventListener("mouseout", borrarColorFlecha);
        f.addEventListener("click", clickColumna);
    }
}

function cambiarColorFlecha(evento) {
    var f = evento.target;
    f.style.fill = turno;
}

function borrarColorFlecha(evento) {
    var f = evento.target;
    f.style.fill = 'white';
}

function clickColumna(evento) {
    var col = evento.target.getAttribute("data-col");
    jugar(col, tablero);
}

function jugar(col, tablero) {
    if (!mover(col, tablero, turno)) {
        return;	
    }
    mostrar(tablero);
    enviarJugada(sessionStorage.getItem("fila"), sessionStorage.getItem("columna"));
    cambiarTurno();
}

function mover(col, tablero, color) {
    var fila = verPrimerHueco(col, tablero);
    if (fila == -1) return false; //Fila llena
    
    tablero[fila][col] = color;
    sessionStorage.setItem("fila",fila);
    sessionStorage.setItem("columna",col);
    
    return true;
}

function mostrar(tablero) {
    for (let i = 0; i < FILAS; i++) {
        for (let j = 0; j < COLS; j++)
            vistaTablero[i][j].style.fill = tablero[i][j];
    }
}

function verPrimerHueco(col, tablero) {
    var i = FILAS - 1;
    while (i >= 0) {
        if (tablero[i][col] == COLOR0)
            return i;
        i--;
    }
    //console.log("Columna Llena");
    return -1;
}

function cambiarTurno() {
    if (turno == COLOR_HUMANO) {
        if (jugadorActual == 2){
            jugada++;
            jugadorActual = 0;
        }
        jugadorActual = jugadorActual + 1;
        turno = dataIteracion[iteracion][jugadorActual -1]["color"];
        COLOR_HUMANO = turno;
        
        $("#turno_de").html(dataIteracion[iteracion][jugadorActual -1].nombre);
        $("#turno_de").css("color",dataIteracion[iteracion][jugadorActual -1].color);
        $("#reset").click();
        $("#play").click();
    }
}

function cronometro() {
    (() => {
        let hours = `00`,
                minutes = `00`,
                seconds = `00`,
                chronometerDisplay = document.querySelector(`[data-chronometer]`),
                chronometerCall

        function chronometer() {
            seconds++
            if (seconds < 10)
                seconds = `0` + seconds

            if (seconds > 59) {
                seconds = `00`
                minutes++

                if (minutes < 10)
                    minutes = `0` + minutes
            }

            if (minutes > 59) {
                minutes = `00`
                hours++

                if (hours < 10)
                    hours = `0` + hours
            }
            chronometerDisplay.textContent = `${hours}:${minutes}:${seconds}`
        }
        play.onclick = (event) => {
            chronometerCall = setInterval(chronometer, 1000)
            event.target.setAttribute(`disabled`, ``)
        }
        pause.onclick = () => {
            clearInterval(chronometerCall)
            play.removeAttribute(`disabled`)
        }
        reset.onclick = () => {
            clearInterval(chronometerCall)
            play.removeAttribute(`disabled`)
            chronometerDisplay.textContent = `00:00:00`

            hours = `00`,
                    minutes = `00`,
                    seconds = `00`
        }
    })()
}