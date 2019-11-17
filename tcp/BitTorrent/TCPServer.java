import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPServer {

    private String clientLine = "";
    private String capitalizedLine = "";

    // initialize socket and input output streams
    private ServerSocket welcomeSocket  = null;
    private Socket connectionSocket     = null;

    private BufferedReader inFromClient = null;
    private DataOutputStream outToClient = null;

    
    // constructor to put ip iddress and port
    public TCPServer (int port) {

        try {
            // create welcome socket, at the port
            welcomeSocket = new ServerSocket(port);
            System.out.println("Connected");

            //while (true) {

            // Wait, on welcome socket for contact by client
            connectionSocket = welcomeSocket.accept();
            // create input stream, attached to socket
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            // create output stream, attached to socket
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // read in line from socket
            clientLine = inFromClient.readLine();
            capitalizedLine = clientLine.toUpperCase() + '\n';

            outToClient.writeBytes(capitalizedLine);

            //}
        }
        catch (IOException i) {
            System.out.println(i);
        }

        // close the connection
        try {
            welcomeSocket.close();
            connectionSocket.close();
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        String word;

        Utils utils = new Utils();
        utils.welcome();

        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();

            switch (word) {
                case "s":
                    TCPServer server = new TCPServer(8002);
                    break;
                case "h":
                    utils.helpUsageServer();
                    break;
                case "q":
                    System.out.println("bye");
                    System.exit(0);
                default:
                    System.out.println("INVAILD INPUT");
                    break;
            }
        }
    }
}
