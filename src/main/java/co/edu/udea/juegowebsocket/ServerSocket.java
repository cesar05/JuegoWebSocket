/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.juegowebsocket;

import com.google.gson.Gson;
import datos.Jugador;
import datos.PosicionDecode;
import datos.PosicionEncode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Cesar
 */
//@ServerEndpoint(value = "/ServerSocket", encoders = {PosicionEncode.class}, decoders = {PosicionDecode.class})
@ServerEndpoint(value = "/ServerSocket")
public class ServerSocket {

    /**
     * Lista de usuarios conectados
     */
    public static final List<Session> CONECTADOS = new ArrayList<Session>();

    public ServerSocket() {
        System.out.println("Se instancia objeto");
    }

    /**
     * Metodo que se ejecuta cuando un usuario se conecta
     *
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void establecerConexion(Session session) throws IOException {
        Gson g=new Gson();
        Jugador jugador;
        String json;        
        for (Session conectado : CONECTADOS) {
            jugador=new Jugador();
            jugador.setEstado("NuevoJugadorEnLinea");            
            json=g.toJson(jugador, Jugador.class);
            conectado.getBasicRemote().sendText(json);
            session.getAsyncRemote().sendText(json);
        }
        CONECTADOS.add(session);
        jugador=new Jugador();
        jugador.setEstado("NuevoJugador");
        jugador.setId(""+(CONECTADOS.size() - 1));
        json=g.toJson(jugador, Jugador.class);
        session.getBasicRemote().sendText(json);
    }

    /**
     * Recibe y envia mensajes a todos los jugadores
     *
     * @param datos
     * @throws IOException
     */
    @OnMessage
    public void enviarYRecibirMensajes(String datos) throws IOException, EncodeException {
        Gson gson=new Gson();
        Jugador jugador=gson.fromJson(datos, Jugador.class);
        int i=0;
        for (Session session : CONECTADOS) {
            //Valida para no enviar datos al mismo jugador
            if(i!=Integer.valueOf(jugador.getId())){
                session.getBasicRemote().sendObject(datos);
            }
            i++;
        }       
    }

    @OnClose
    public void handleClose(Session session) {
        CONECTADOS.remove(session);
        System.out.println("Se cierra la conexion con usuario X");
    }

    @OnError
    public void handleError(Throwable e) {
        e.printStackTrace();
    }
}
