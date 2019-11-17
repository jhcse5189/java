import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPClient {

    private String line = "";
    private String modifiedLine = "";

    // initialize socket and input output streams
    private Socket clientSocket             = null;
    private BufferedReader inFromUser       = null;
    private DataOutputStream outToServer    = null;

    //private BufferedReader inFromServer     = null;
    private BufferedReader inFromServer    = null;

    private FileOutputStream fos            = null;

    // constructor to put ip iddress and port
    public TCPClient (String address, int port, String destPath) {

        // create client socket, connect to server
        try {
            clientSocket = new Socket(address, port);

            // takes input stream from terminal
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            // sends output stream attached to the socket
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // create input stream attached to the socket
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            printInfo();

        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void sendLine() {

        try {
            line = inFromUser.readLine();

            // send line to server
            outToServer.writeBytes(line + '\n');
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void recieveLine() {
        // string to read message from server
        try {
            modifiedLine = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedLine);
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void close() {
        // close the connection
        System.out.println("Closing...");
        try {
            clientSocket.close();
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }

    public void saveFile(Socket clientSocket) {

        try {
            System.out.println("debug...");
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("debug...");
            fos = new FileOutputStream("dlwlrma.jfif");
            byte[] buffer = new byte[8192];
            System.out.println("debug...");

            // file size in separate msg
            int filesize = 10000;
            int read = 0;
            int totalRead = 0;
            int remaining = filesize;

            // while ((read = inFromServer.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            //     System.out.println("debug...");
            //     totalRead += read;
            //     remaining -= read;
            //     System.out.println("read " + totalRead + " bytes.");
            //     fos.write(buffer, 0, read);
            // }
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void printInfo() {
        System.out.println("Connected.");
        System.out.printf("\tserver addr: " + clientSocket.getInetAddress() + "\n");
        System.out.printf("\tserver port: " + clientSocket.getPort() + "\n");
        System.out.printf("\tclient port: " + clientSocket.getLocalPort() + "\n");
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
                // 127.0.0.1, 8002
                case "c":
                    // System.out.print("host: ");
                    // word = keyboard.next();

                    // System.out.print("port: ");
                    // port = keyboard.nextInt();

                    TCPClient client = new TCPClient("127.0.0.1", 8002, "C:/TCPClient/");
                    client.sendLine();
                    client.recieveLine();
                    client.close();
                    break;
                case "h":
                    utils.helpUsageClient();
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
