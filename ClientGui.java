
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

public class  ClientGui() extends Thread {
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
    this.port = 12345;
    this.name = "nickname";

    String fontFamily = "Arial, sans-serif";
    Font font = new Font(fontFamily, Font.PLAIN, 15);

    final JFrame frame = new JFrame("Chat");
    frame.getContentPane().setLayout(null);
    frame.setSize(800, 800);
    frame.setResizeable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }
}