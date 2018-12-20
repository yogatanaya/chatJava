
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientGui extends Thread{
  final JTextPane jtextFieldDiscussion = new JTextPane();
  final JTextPane jtextListUsers = new JTextPane();
  final JTextField jtextInputChat = new JTextField();
  private String oldMsg = "";
  private Thread read;
  private String serverName;
  private int PORT;
  private String name;
  BufferedReader input;
  PrintWriter output;
  Socket server;

  public ClientGui(){
    this.serverName = "localhost";
    this.PORT = 12345;
    this.name = "nickname";

    String fontFamily = "Arial, sans-serif";
    Font font = new Font(fontFamily, Font.PLAIN, 15);

    final JFrame frame = new JFrame("Chat");
    frame.getContentPane().setLayout(null);
    frame.setSize(700, 500);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // text discuession
    jtextFieldDiscussion.setBounds(25, 25, 490, 320);
    jtextFieldDiscussion.setFont(font);
    jtextFieldDiscussion.setMargin(new Insets(6,6,6,6));
    jtextFieldDiscussion.setEditable(false);
    JScrollPane jtextFieldDiscussionSP = new JScrollPane(jtextFieldDiscussion);
    jtextFieldDiscussionSP.setBounds(25,25,490,320);

    // list of users 
    jtextListUsers.setBounds(0, 350, 400, 50);
    jtextListUsers.setEditable(true);
    jtextListUsers.setFont(font);
    JScrollPane jsplituser = new JScrollPane(jtextListUsers);
    jsplituser.setBounds(520, 25, 156, 320);

    jtextListUsers.setContentType("text/html");
    jtextListUsers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // field to user input messages
    jtextInputChat.setBounds(0, 350, 400, 50);
    jtextInputChat.setFont(font);
    jtextInputChat.setMargin(new Insets(6, 6, 6, 6));
    final JScrollPane jtextInputChatSP = new JScrollPane(jtextInputChat);
    jtextInputChatSP.setBounds(25, 350, 650, 50);

    // button send
    final JButton jsbtn = new JButton("Send");
    jsbtn.setFont(font);
    jsbtn.setBounds(575, 410, 100, 35);

    // button disconnect
    final JButton jsDsc = new JButton("Disconnect");
    jsDsc.setFont(font);
    jsDsc.setBounds(25, 410, 130, 35);

    jtextInputChat.addKeyListener(new KeyAdapter(){
      // send message when hit enter
      public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
         // sendMessages();
        }

        // get last messages typed
        if(e.getKeyCode() == KeyEvent.VK_UP){
          String currentMsg = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMsg;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN){
          String currentMsg = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMsg;
        }

      }
    });

    // button send onClick
    jsbtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        //sendMessages();
      }
    });

    final JTextField tfName = new JTextField(this.name);
    final JTextField tfPort = new JTextField(this.PORT);
    final JTextField tfAddress = new JTextField(this.serverName);
    final JButton connectBtn = new JButton("Connect");

    connectBtn.setFont(font);
    tfAddress.setBounds(25, 380, 135, 40);
    tfName.setBounds(375, 380, 135, 40);
    tfPort.setBounds(200, 380, 135, 40);
    connectBtn.setBounds(575, 380, 100, 40);

    frame.add(connectBtn);
    frame.add(jtextFieldDiscussionSP);
    frame.add(jsplituser);
    frame.add(tfName);
    frame.add(tfPort);
    frame.add(tfAddress);
    frame.setVisible(true);

      //  info sur le Chat
     appendToPane(jtextFieldDiscussion, "<h4>Les commandes possibles dans le chat sont:</h4>"
     +"<ul>"
     +"<li><b>@nickname</b> pour envoyer un Message privé à l'utilisateur 'nickname'</li>"
     +"<li><b>#d3961b</b> pour changer la couleur de son pseudo au code hexadécimal indiquer</li>"
     +"<li><b>;)</b> quelques smileys sont implémentés</li>"
     +"<li><b>flèche du haut</b> pour reprendre le dernier message tapé</li>"
     +"</ul><br/>");


    // on Connect
    connectBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        try {
          name = tfName.getText();
          String port = tfPort.getText();
          serverName = tfAddress.getText();
          PORT = Integer.parseInt(port);

          appendToPane(jtextFieldDiscussion, "<span>Connecting to " + serverName + " on port " + PORT + "...</span>");
          server = new Socket(serverName, PORT);

          appendToPane(jtextFieldDiscussion, "<span>Connected to " +
            server.getRemoteSocketAddress()+"</span>");

          input = new BufferedReader(new InputStreamReader(server.getInputStream()));
          output = new PrintWriter(server.getOutputStream(), true);

          // send nickname to server
          output.println(name);
          
          // create new read Thread
          read = new Read();
          read.start();
          frame.remove(tfName);
          frame.remove(tfPort);
          frame.remove(tfAddress);
          frame.remove(connectBtn);
          frame.add(jsbtn);
          frame.add(jsDsc);
          frame.revalidate();
          frame.repaint();
          jtextFieldDiscussion.setBackground(Color.WHITE);
          jtextListUsers.setBackground(Color.WHITE);
        }catch(Exception ex){
          appendToPane(jtextFieldDiscussion, "<span>Could not connect to server</span>");
          JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
      }
    });

    
    // disconnect
    jsDsc.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent ae) {
        frame.add(tfName);
        frame.add(tfPort);
        frame.add(tfAddress);
        frame.add(connectBtn);
        frame.remove(jsbtn);
        frame.remove(jtextInputChatSP);
        frame.remove(jsDsc);
        frame.revalidate();
        frame.repaint();
        read.interrupt();
        jtextListUsers.setText(null);
        jtextFieldDiscussion.setBackground(Color.LIGHT_GRAY);
        jtextListUsers.setBackground(Color.LIGHT_GRAY);
        appendToPane(jtextFieldDiscussion, "<span>Connection closed.</span>");
        output.close();
      }
    });    
  }

  public class TextListener implements DocumentListener{
    
  }

  
  

}



