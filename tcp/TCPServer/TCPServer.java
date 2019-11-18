import java.awt.desktop.SystemEventListener;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPServer {

    private static String fileToSend = null;
    private static String fileToSeek = null;

    // initialize socket and input output streams
    private ServerSocket welcomeSocket   = null;
    private Socket connectionSocket      = null;

    private BufferedReader inFromClient  = null;
    private BufferedOutputStream outToClient = null;
    private DataOutputStream outToClientName = null;

    private FileInputStream fis          = null;
    private BufferedInputStream bis              = null;

    
    // constructor to put ip iddress and port
    public TCPServer (int port, String file) {

        fileToSend = file;
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
            outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());

            printInfo();
        }
        catch (IOException i) {
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


    public void sendFile() {

        try {
            fileToSeek = inFromClient.readLine();
        }
        catch (IOException i) {
            System.out.println(i);
        }

        try {
            if (!fileToSeek.equals(fileToSend)) {
                System.out.println("Can't find file name " + fileToSeek);
                outToClientName.writeBytes(fileToSend + '\n');
                close();
                //System.exit(-1);
            }
        }
        catch (IOException i) {
            System.out.println(i);
        }

        File myFile = new File( "./" + fileToSend );
        byte[] buffer = new byte[(int)myFile.length()];

        try {
            fis = new FileInputStream(myFile);
//            while (fis.read(buffer) > 0) {
//                outToClient.write(buffer);
//            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }


        bis = new BufferedInputStream(fis);
        try {
            bis.read(buffer, 0, buffer.length);
            outToClient.write(buffer, 0, buffer.length);
            outToClient.flush();
            System.out.println("'dlwlrma.jfif' is Sent!:)");
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

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        int port;// = Integer.parseInt(args[0]);
        String word;

        Utils utils = new Utils();
        utils.welcome();

        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();
            // port, args[1]
            switch (word) {
                case "s":
                    TCPServer server = new TCPServer(8002, "dlwlrma.jfif");
                    server.waitForClient();
                    server.sendFile();
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
