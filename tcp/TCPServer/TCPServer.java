import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPServer {

    private final static String fileToSend = "C:/TCPServer/dlwlrma.jfif";

    private String clientLine = "";
    private String capitalizedLine = "";

    // initialize socket and input output streams
    private ServerSocket welcomeSocket   = null;
    private Socket connectionSocket      = null;

    private BufferedReader inFromClient  = null;
    private DataOutputStream outToClient = null;

    private FileInputStream fis          = null;

    
    // constructor to put ip iddress and port
    public TCPServer (int port, String file) {

        try {
            // create welcome socket, at the port
            welcomeSocket = new ServerSocket(port);
            System.out.println("Socket initializing in server...");
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void waitForClient() {
        System.out.println("Wait on welcome socket for contact by client...");
        try {
            connectionSocket = welcomeSocket.accept();
            // create input stream, attached to socket
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            // create output stream, attached to socket
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            printInfo();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void capitalization() {

        try {
            // read in line from socket
            clientLine = inFromClient.readLine();
            capitalizedLine = clientLine.toUpperCase() + '\n';

            outToClient.writeBytes(capitalizedLine);
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void close() {
        // close the connection
        System.out.println("Closing...");
        try {
            welcomeSocket.close();
            connectionSocket.close();
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }

    public void printInfo() {
        System.out.println("Connected.");
        // print service port & client's address and port.
        System.out.printf("\tserver port: " + connectionSocket.getLocalPort() + "\n");
        System.out.printf("\tclient addr: " + connectionSocket.getInetAddress() + "\n");
        System.out.printf("\tclient port: " + connectionSocket.getPort() + "\n");
    }


    public void sendFile(String file) {

        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];

            while (fis.read(buffer) > 0) {
                outToClient.write(buffer);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException i) {
            System.out.println(i);
        }
        
        try {
            fis.close();
            outToClient.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
        
    }

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        int port;
        String word;

        Utils utils = new Utils();
        utils.welcome();

        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();

            switch (word) {
                // 8002, dlwlrma.jfif
                case "s":
                    // System.out.print("port: ");
                    // port = keyboard.nextInt();

                    // System.out.print("file name: ");
                    // word = keyboard.next();

                    TCPServer server = new TCPServer(8002, "dlwlrma.jfif");
                    server.waitForClient();
                    server.capitalization();
                    server.close();
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
