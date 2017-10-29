/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.juegowebsocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Cesar
 */
@ServerEndpoint(value="/ServerSocket")
public class ServerSocket {
    /**
    * Lista de usuarios conectados
    */
   public static final List<Session> CONECTADOS=new ArrayList<Session>();

   public ServerSocket() {
      System.out.println("Se instancia objeto");
   }

   @OnOpen
   public void handleOpen(Session session) throws IOException{
      for(Session sessionc:CONECTADOS){
        sessionc.getBasicRemote().sendText("NUEVOJUGADOR");
        session.getAsyncRemote().sendText("NUEVOJUGADOR");
      }
      CONECTADOS.add(session);
      System.out.println("Usuarion numero "+CONECTADOS.size());
      session.getBasicRemote().sendText("IDENTIFICACION;"+(CONECTADOS.size()-1));
      
   }
   
   /**
    * Recibe y envia mensajes a todos los jugadores
    * @param message
    * @throws IOException 
    */
   @OnMessage
   public void handledMessage(String message) throws IOException {
      System.out.println(message);
      String[]datos=message.split(";");
      int i=0;
      for(Session session:CONECTADOS){                
        System.out.println("IDCONECTADO= "+i);
        System.out.println("IDENVIADO= "+datos[0]);       
         if(i!=Integer.valueOf(datos[0])){
            session.getBasicRemote().sendText(message);
         }
         i++;
      }
   }

   @OnClose
   public void handleClose(Session session){
      CONECTADOS.remove(session);
      System.out.println("Se cierra la conexion con usuario X");
   }

   @OnError
   public void handleError(Throwable e){
      e.printStackTrace();
   }
}
