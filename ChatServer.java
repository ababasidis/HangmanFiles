import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/** 
   ChatServer is a server to be used with ChatClient.java. This file sets up a server for a basic
   multithreaded chat. Used with ISTE 121 Final Project.
   
   @author Jacob Annal
   @author Athanasios Babasidis
   @author Arielle Hirsch
   @version 3.3

 */


public class ChatServer extends JPanel {
   
   Vector<ObjectOutputStream>connectedClients = new Vector<ObjectOutputStream>();

   
   public static void main(String [] args){
     ChatServer cs = new ChatServer();
   }//end main
   
   public ChatServer() {
      try{
         ServerSocket server = new ServerSocket(16789);
         while(true) {
            try{
               Socket connection = server.accept();
               ObjectOutputStream out;
               out = new ObjectOutputStream(connection.getOutputStream());
               InnerThread t = new InnerThread(connection, out);
               t.start();
               connectedClients.add(out);
            }catch(EOFException eof){
               eof.printStackTrace();
            }
         }//end while
      }catch(IOException ioe){
         ioe.printStackTrace();
      }//end catch
   }//end constructor  
   
   public class InnerThread extends Thread {
      Socket cs;
      ObjectOutputStream out;
      ObjectInputStream in;

      public InnerThread(Socket cs, ObjectOutputStream out) {
         this.out = out;
         this.cs = cs;
      }
      
      /*
         RUN THREAD
      */      
      public void run() {
         try {
            in = new ObjectInputStream(cs.getInputStream());

            String message = "Now connected";
            try{
               System.out.println("Server Message: " + message);
               out.writeObject("SERVER: " + message);
               out.flush();
            }catch(IOException ioe) {
               ioe.printStackTrace();
            }
            do{
               //begin conversation
               try{
                  message = (String) in.readObject();
                  for (ObjectOutputStream o : connectedClients) {
                     if (o != out) {
                        o.writeObject(message);
                        o.flush();
                     }   
                     //figure out a way to properly remove the disconnected client in the case where they close abnormally instead of typing CLIENT-END                
                  }
               }catch(ClassNotFoundException cnf){
                  cnf.printStackTrace();
               }
            }while(!message.equals("CLIENT - END"));
            
            out.close();
            in.close();
            connectedClients.remove(out);

         }
         catch(IOException ioe) {
            ioe.printStackTrace();
         }
      }
   }
}//end class ChatServer