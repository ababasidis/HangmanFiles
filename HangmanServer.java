import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.math.*;

/**
   Class HangmanServer is a server used to run a basic game of hangman
   @author Athanasios Babasidis
   @version 4.1

 **/



public class HangmanServer{

   //images -- THIS IS RECURSIVE
   //SHOULD USE A METHOD TO ADD THESE ALL AT ONCE
   private ImageIcon Image1 = new ImageIcon ("HangmanBG.jpg");//in array = 0
   private ImageIcon Image2 = new ImageIcon ("Hangman-1.jpg");//in array = 1
   private ImageIcon Image3 = new ImageIcon ("Hangman-2.jpg");//in array = 2
   private ImageIcon Image4 = new ImageIcon ("Hangman-3.jpg");//in array = 3
   private ImageIcon Image5 = new ImageIcon ("Hangman-4.jpg");//in array = 4 
   private ImageIcon Image6 = new ImageIcon ("Hangman-5.jpg");//in array = 5 
   private ImageIcon Image7 = new ImageIcon ("Hangman-6.jpg");//in array = 6
   private ImageIcon Image8 = new ImageIcon ("Hangman-7.jpg");//in array = 7
   private ImageIcon Image9 = new ImageIcon ("HangmanGO.jpg");//in array = 8
   private ImageIcon[] Images = new ImageIcon[10];
   static ArrayList<String> easy = new ArrayList<String>();
   static ArrayList<String> med  = new ArrayList<String>();
   static ArrayList<String> hard = new ArrayList<String>();
   private int numWrong = 0;
   private String choice = "";
   private String secretWord ="";
   private char[] letters;
   
   Vector<PrintWriter>connectedClients = new Vector<PrintWriter>();
   
   /**
      Constructor for class HangmanServer; This constructor accepts clients and runs the server thread.
   */
   public HangmanServer() {
      System.out.println("Running server...");
      //try to connect
      try{
         
         ServerSocket ss = new ServerSocket(15678);
         new InnerChat().start();
         char guess;
         while(true){
            Socket s = ss.accept();
            System.out.println("Server " + s);
            System.out.println("Client connecting to server...");
            ThreadServer ts = new ThreadServer(s);
            ts.start();
         }//end while
      } 
      catch(Exception e){
         e.printStackTrace();
      }//end try & catch
   }//end hangman server constructor
   
   //main method
   public static void main(String [] args) {
      
      
      easy.add("afraid");
      easy.add("dark");
      easy.add("devil");
      easy.add("evil");
      easy.add("owl");
      easy.add("treat");
      med.add("alarming");
      med.add("casket");
      med.add("scarecrow");
      med.add("hayride");
      med.add("vampire");
      hard.add("bloodcurling");
      hard.add("witchcraft");
      hard.add("apparition");
      hard.add("masquerade");
      hard.add("supernatural");
      
      //new ChatServer();
   
      new HangmanServer();
   }//end main
   
   //choose a random word  from list
   public void chooseWord(String _choice) {
      _choice = choice;
      if (_choice.equalsIgnoreCase("EASY")){
         //get a random word from easy list
         int i = (int)(Math.random()*easy.size());
         secretWord = easy.get(i);
         System.out.println("Secret word loaded: " + secretWord);
         parseWord(secretWord);
         
      }
      if (_choice.equalsIgnoreCase("MED")){
         
         int i = (int)(Math.random()*med.size());
         secretWord = med.get(i);
         System.out.println("Secret word loaded: " + secretWord);  
         parseWord(secretWord);
      }
      if (_choice.equalsIgnoreCase("HARD")){
      
         int i = (int)(Math.random()*hard.size());
         secretWord = hard.get(i);
         System.out.println("Secret word loaded: " + secretWord);
         parseWord(secretWord);
      }
   }//end chooseWord
   

   
   public void parseWord(String word) {
      letters =word.toCharArray();
      for(int i=0; i<letters.length; i++){
         System.out.println("Char array " + Arrays.asList(letters).indexOf(i) + " " + letters[i]);
      }
   }
   
   public void checkLetter(char guess) {
      for (char c:letters){
         if (c==guess){//need a printwriter or something
                     //reveal c in hidden word
         }
         else if (c!=guess){
            //need to check if after all of them there
            //is no match, not sure how to go about it, 
            //this will probably have to change a little
         }
      }
   }//end checkLetter
   
   public void wrong(){
   
      JFrame frame = new JFrame();
   
      //user has 7 tries
      if(numWrong == 1){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[1] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
      else if(numWrong == 2){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[2] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
      else if(numWrong == 3){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[3] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
      else if(numWrong == 4){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[4] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
      else if(numWrong == 5){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[5] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
      else if(numWrong == 6){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[6] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
      else if(numWrong == 7){
         JLabel wrongLetter = new JLabel();
         wrongLetter.setIcon( Images[8] );
         wrongLetter.setLayout( new BorderLayout() );
         frame.setContentPane( wrongLetter );
      }
         
   }//end wrong method
   
   class InnerChat extends Thread {
      public void run() {
         new ChatServer();
      }
   }

   //server thread
   class ThreadServer extends Thread{
      private PrintWriter out; 
      private BufferedReader in;
      
      //constructor
      public ThreadServer(Socket s){
         //try to set up streams
         try{
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            connectedClients.add(out);
            System.out.println("Player " + connectedClients.size() + " connected!");
         } 
         catch(Exception e){
            e.printStackTrace();
         }   
      }
      
      //run thread
      public void run() {
         System.out.println("Client thread running...");
         try{
            String server_in = "";
            System.out.println("Waiting for command...");
            
            //read  server_in from client
            while((server_in = in.readLine())!=null) {
               
               System.out.println("Reading from client... ");
               choice = server_in;
               
               //call chooseWord method to randomly pick a word for each if statement
               if (choice.equals("EASY")) {
                  chooseWord(choice);
                  System.out.println("Loading " + choice + " array list of words...");
               } 
               else if (choice.equals("MED")) {
                  chooseWord(choice);
                  System.out.println("Loading " + choice + " array list of words...");
               } 
               else if (choice.equals("HARD")) {
                  chooseWord(choice);
                  System.out.println("Loading " + choice + " array list of words...");
               }
               else if (choice.equalsIgnoreCase("SUBMIT")){
                  secretWord = in.readLine();
                  System.out.println("Secret word: "+secretWord);
                  parseWord(secretWord);
               }//end if 
            }//end whole
            System.out.println("Exiting loop " + server_in + "..."); 
         
            
         
         }catch(Exception e){
            e.printStackTrace();
         }//end try & catch
      }//end run()
      
   }//ends ThreadServer class
}//end class