
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.StringReader;

public class Client{
  private String host;
  private int port;

  public static void main(String[] args) throws UnknownHostException, IOException {
    new Client("127.0.0.1",12345).run();
  }

  public Client(String host, int port){
    this.host = host;
    this.port = port;
  }

  public void run() throws UnknownHostException, IOException {
    Socket client = new Socket(host, port);
    System.out.println("client succesfully connected to the server...");

    PrintStream output = new PrintStream(client.getOutputStream());

    Scanner sc = new Scanner(System.in);
    System.out.println("Your Nickname: ");
    String nickname = sc.nextLine();

    new Thread(new ReceiveMessageHandler(client.getInputStream())).start();

    System.out.println("Messages: \n");

    while(sc.hasNextLine()){
      output.println(sc.nextLine());
    }

    output.close();
    sc.close();
    client.close();
  }

}

class ReceiveMessageHandler implements Runnable{
  private InputStream server;
  public ReceiveMessageHandler(InputStream server){
    this.server = server;
  }
  public void run() {
    Scanner s = new Scanner(server);
    String tmp = "";
    while(s.hasNextLine()){
      tmp = s.nextLine();
      if(tmp.charAt(0) == '['){
        tmp = tmp.substring(1, tmp.length() - 1);
        System.out.println(
          "\n Users List: "+
          new ArrayList<String>(Arrays.asList(tmp.split(",")))+"\n"
        );

      }else{
        try {
          System.out.println("\n"+getTagValue(tmp));

        }catch(Exception ignore){}
      }
    }
    s.close();
  }
  

  public static String getTagValue(String xml){
    return  xml.split(">")[2].split("<")[0] + xml.split("<span>")[1].split("</span>")[0];
  }

}