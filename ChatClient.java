import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
   
   @author Athanasios Babasidis
   @version 4.1

 */

public class ChatClient extends JPanel {
   //Attributes
   private JTextArea chatWindow;
   private JTextField userText;
   private JButton send;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private String message = "";
   private String serverIP = "localhost";
   private Socket connection;
   
   public ChatClient(String name) {
      setLayout(new BorderLayout());
      userText = new JTextField(20);
      //userText.setEditable(false);
      send = new JButton();
      JLabel sender = new JLabel("Send");
      send.add(sender);
      
      add(send, BorderLayout.EAST);
      add(userText,BorderLayout.WEST);
      chatWindow = new JTextArea(25,20);
      add(new JScrollPane(chatWindow), BorderLayout.NORTH);
      System.out.println("Chat client being constructed...");
      setSize(300,150);
      //setVisible(true);
      
      System.out.println("Constructing Chat threads...");
      final ChatClientTh cct = new ChatClientTh(name);
      cct.start();
      
      send.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent event){
                  cct.sendMessage(userText.getText());
                  userText.setText("");
               }
            }
         );
   
   }
   
   /**
   public static void main(String [] args){
      ChatClientTh cct = new ChatClient("localhost").new ChatClientTh("localhost");
     cct.start();      
   }//end main
   */
   
   public static void main(String [] args) {
      new ChatClient("localhost");
   }
   
   class ChatClientTh extends Thread{
    //PrintWriter pw;
      public ChatClientTh(String host){
      // super("Client");
             System.out.println("Chat client thread created!");
             serverIP = host;
             System.out.println("SERVER IP: " + serverIP);
      //       userText = new JTextField(20);
      //       userText.setEditable(false);
      //       send = new JButton();
      //       JLabel sender = new JLabel("Send");
      //       send.add(sender);
      //       send.addActionListener(
      //             new ActionListener(){
      //                public void actionPerformed(ActionEvent event){
      //                   sendMessage(userText.getText());
      //                   userText.setText("");
      //                }
      //             }
      //          );
      //       add(send, BorderLayout.EAST);
      //       add(userText,BorderLayout.NORTH);
      //       chatWindow = new JTextArea();
      //       add(new JScrollPane(chatWindow), BorderLayout.CENTER);
      //       setSize(300,150);
      //       setVisible(true);
      //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }//end constructor 
   
      public void run(){
         try{
            connectToServer();
            setupStreams();
            whileChatting();
            System.out.println("Setting up chat streams...");
         }//end try
         catch(EOFException eofe){
            showMessage("\n  Client terminated connection");
         }//end eofe catch
         catch(ConnectException ce){
            showMessage("\nConnection refused");
         }//end ce catch
         catch(NullPointerException npe){
            showMessage("\nNo input");
         }//end npe catch
         catch(IOException ioe){
            showMessage("\nIO Exception");
         }//end ioe catch
         finally{
            closeChat();
         }//end finally
      }//end run()
   
   //connect to server
      private void connectToServer() throws IOException{
         showMessage("Attempting connection....\n");
         connection = new Socket(serverIP, 16789);
         showMessage("Connected to: " + connection.getInetAddress().getHostName());
      }//end connectToServer()
   
   //set up streams to send and receive messages
      private void setupStreams() throws IOException{
      // pw = new PrintWriter(connection.getOutputStream());
         output = new ObjectOutputStream(connection.getOutputStream());
         output.flush();
         input = new ObjectInputStream(connection.getInputStream());
         showMessage("\nClient now connected to Server\n");
      }//end setupStreams()
   
   //while chatting 
      private void whileChatting() throws IOException{
         ableToType(true);
         do{
            try{
               message = (String)input.readObject();
               showMessage("\n" + message);
            }//end try
            catch(ClassNotFoundException cnfe){
               showMessage("\nNo object type found");
            }//end catch
         }while(!message.equals("STOP"));
      }//end whileChatting()
   
   //close the streams and sockets
      private void closeChat(){
         showMessage("\nClosing Chat....");
         ableToType(false);
      //System.exit(0);
         try{
            output.close();
            input.close();
            connection.close();
         }//end try
         catch(IOException ioe){
            ioe.printStackTrace();
         }//end catch
         catch(NullPointerException npe){
            showMessage("");
         }//end npe catch
      }//end closeChat()
   
   //send messages to the server
      private void sendMessage(String message){
         try{
            System.out.println("Client Message: "+message);
         //pw.println(message);
         //pw.flush();
            output.writeObject("CLIENT: " + message);
            output.flush();
            showMessage("\nCLIENT: " + message);
         }//end try
         catch(IOException ioe){
            chatWindow.append("\nError sending message...");
         }//end catch
      }//end sendMessage()
   
   //update chatWindow
      private void showMessage(final String m){
         SwingUtilities.invokeLater(
               new Runnable(){
                  public void run(){
                     chatWindow.append(m);
                  }//end run
               }//end runnable
               );//invoke later
      }//end showMessage()
   
   //gives user permission to type chat into text box
      private void ableToType(final boolean tof){
         SwingUtilities.invokeLater(
               new Runnable(){
                  public void run(){
                     userText.setEditable(tof);
                  }//end run
               }//end runnable
               );//invoke later
      }//end ableToType()
   }//end ChatClientTh() Thread
}//end class ChatClient