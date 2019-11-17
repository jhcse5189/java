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

    private BufferedReader inFromServer     = null;

    // constructor to put ip iddress and port
    public TCPClient (String address, int port) {

        // create client socket, connect to server
        try {
            clientSocket = new Socket(address, port);
            System.out.println("Connected");

            // takes input stream from terminal
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            // sends output stream attached to the socket
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            
            //while (!line.equals("Over")) {
            // create input stream attached to the socket
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            line = inFromUser.readLine();
            //}

            // send line to server
            outToServer.writeBytes(line + '\n');

        }
        catch (UnknownHostException u) {
            System.out.println(u);
        }
        catch (IOException i) {
            System.out.println(i);
        }

        // string to read message from server
        try {
            modifiedLine = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedLine);
        }
        catch (IOException i) {
            System.out.println(i);
        }


        // keep reading until "Over" is input
//        while (!line.equals("Over") && !line.isEmpty()) {
//
//            try {
//                line = input.readLine();
//                output.writeUTF(line);
//            }
//            catch(IOException i) {
//                System.out.println(i);
//            }
//        }



        // close the connection
        try {
//            input.close();
//            output.close();
            clientSocket.close();
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
                case "c":
                    TCPClient client = new TCPClient("127.0.0.1", 8002);
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
