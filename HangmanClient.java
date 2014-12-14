import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
   
   @author Athanasios Babasidis
   @version 4.3

 */

public class HangmanClient extends JFrame {

   //private JTextArea jtaRecieved = new JTextArea("Messages",10,10);
   private JTextField jtaGuess = new JTextField("X");
   
   //ChatServer cs = new ChatServer();
   
   private static String serverIP;
   private Socket connect;
   private String letter;
   private PrintWriter socket_out;
   
   public static void main(String[] args){
      args = new String[]{"localhost"};
      if (args.length!=1){
         System.out.println("Please add a hostname");
         System.exit(1);
      } 
      else{
         serverIP=args[0];
         new HangmanClient(serverIP);
      }
   }
   
   public HangmanClient(String name) {
      
      //set defaults of game window
      setSize(800,500);    
      setLocationRelativeTo(null);
      System.out.println("Setting sized client...");
      
      //Start Chat Client
      //ChatClient cc = new ChatClient(name);
      new StartChatClient(name, this).start();
      try{ 
         connect = new Socket(serverIP, 15678);
         socket_out = new PrintWriter(new OutputStreamWriter((connect.getOutputStream())));
      } 
      catch(IOException e){
         e.printStackTrace();
      } //end try & catch
         
      //START GUI
      System.out.println("Launching Hangman client...");
      //add(cc,BorderLayout.EAST);
      
      System.out.println("Added cc");
      
   
      //Panel including text area and button to guess a letter
      JPanel gPanel = new JPanel();
      JButton sendLetter = new JButton("Send Guess!");
      gPanel.add(jtaGuess);
      gPanel.add(sendLetter);
      add(gPanel,BorderLayout.CENTER);
      
      System.out.println("Added gPanel");
      
      //set defaults
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
      
      System.out.println("Should be visible..");
   
      //create pop up window to choose difficulty / choose a word
      final JFrame popup = new JFrame();      
      popup.setSize(350,300);
      popup.setLayout(new GridLayout(8,1));
      JButton easy = new JButton("Easy");
      JButton med = new JButton("Medium");
      JButton hard = new JButton("Hard");
      final JTextField tf = new JTextField();
      JButton submit = new JButton("Submit Word");
      
      System.out.println("Before action listeners");
      
      //easy action listener
      easy.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent ae){
                  try{                     
                     System.out.println("Loading " + ae.getActionCommand() + " array list of words... ");
                     //String choice = "easy";
                     socket_out.println("EASY");
                     socket_out.flush();
                     popup.dispose();
                     //setChoice(choice);
                     System.out.println("Client " + connect);
                  } 
                  catch(Exception e){
                     e.printStackTrace();
                  }//end try & catch
               
               }
            }
         );
         
      //medium action listener
      med.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent ae){
                  try{
                     System.out.println("Loading " + ae.getActionCommand() + " array list of words... ");
                     //String choice = "easy";
                     socket_out.println("MED");
                     socket_out.flush();
                     popup.dispose();
                     //setChoice(choice);
                     System.out.println("Client " + connect);
                  } 
                  catch(Exception e){
                     e.printStackTrace();
                  }//end try & catch
               }
            }
         );
         
      //hard actionlistener
      hard.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent ae){
                  try{
                     System.out.println("Loading " + ae.getActionCommand() + " array list of words... ");
                     //String choice = "easy";
                     socket_out.println("HARD");
                     socket_out.flush();
                     popup.dispose();
                     //setChoice(choice);
                     System.out.println("Client " + connect);
                  } 
                  catch(Exception e){
                     e.printStackTrace();
                  }//end try & catch
               }
            }
         );
         
      //submit action listener
      //send data from JTextField to server (custom word)
      submit.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent ae){
                  socket_out.println("SUBMIT");
                  String word = tf.getText();
                  socket_out.println(word);
                  socket_out.flush();
                  popup.dispose();
               }
            }
         );   
         
      JLabel label = new JLabel("                         Welcome to Hangman!");
      JLabel label2 = new JLabel(" Please choose game difficulty or enter your own word! ");
      JLabel label3 = new JLabel("                   OR enter a custom word to add!");
      
      //add pop up to GUI
      popup.add(label);
      popup.add(label2);
      popup.add(easy);
      popup.add(med);
      popup.add(hard);
      popup.add(label3);
      popup.add(tf);
      popup.add(submit);
      
      System.out.println("Added popups");
      
      //set pop up defaults
      popup.setVisible(true);
      popup.setLocationRelativeTo(null);
      popup.setAlwaysOnTop(true);
      System.out.println("Hangman Client Loaded.");
      
   //       try{
   //          socket_out.close();
   //             connect.close();
   //       }//end try//end try
   //       catch(Exception e){
   //          e.printStackTrace();
   //       }

   }//end client constructor
   
   
   /**
      Method to send letter to server  
      **********************************
      PUT THIS IN ACTION LISTENER FOR THE
      JTEXTAREA & JTEXTFIELD
      **********************************
   
   private void sendLetter(String letter) {
      try{
         System.out.println("Letter: " + letter);
         socket_out.println(letter);
         socket_out.flush();
      } catch(Exception e){
         e.printStackTrace();
      }//end try & catch
   }//end sendLetter
   */

   public class StartChatClient extends Thread {
      String name;
      JFrame frame;
      public StartChatClient(String name, JFrame frame) {
         this.name = name;
         this.frame = frame;
      }
      
      public void run() {         
         frame.add(new ChatClient(name),BorderLayout.EAST);
      }
   }

}