import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPClient {

    private final static String fileOutput = "C:/TCPClient/test.jfif";

    private String line = "";
    private String modifiedLine = "";

    // initialize socket and input output streams
    private Socket clientSocket             = null;
    private BufferedReader inFromUser       = null;
    private DataOutputStream outToServer    = null;

    //private BufferedReader inFromServer     = null;
    private BufferedReader inFromServer     = null;

    private FileOutputStream fos            = null;
    private ByteArrayOutputStream baos      = null;
    private InputStream is                  = null;
    private BufferedOutputStream bos        = null;

    // constructor to put ip iddress and port
    public TCPClient (String address, int port) {

        // create client socket, connect to server
        try {
            clientSocket = new Socket(address, port);

            // takes input stream from terminal
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            // sends output stream attached to the socket
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // create input stream attached to the socket
            //inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            is = clientSocket.getInputStream();

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

    public void saveFile() {

        byte[] aByte = new byte[1];
        int bytesRead;

        baos = new ByteArrayOutputStream();

        if (is != null) {

            try {
                fos = new FileOutputStream(fileOutput);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(aByte, 0, aByte.length);

                do {
                    baos.write(aByte);
                    bytesRead = is.read(aByte);
                } while (bytesRead != -1);

                bos.write(baos.toByteArray());
                bos.flush();
                System.out.println("Saved 'C:/TCPClient/test.jfif'!:)");
                bos.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }

//        try {
//            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            fos = new FileOutputStream("dlwlrma.jfif");
//            byte[] buffer = new byte[8192];
//
//            // file size in separate msg
//            int filesize = 10000;
//            int read = 0;
//            int totalRead = 0;
//            int remaining = filesize;
//
//            // while ((read = inFromServer.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//            //     System.out.println("debug...");
//            //     totalRead += read;
//            //     remaining -= read;
//            //     System.out.println("read " + totalRead + " bytes.");
//            //     fos.write(buffer, 0, read);
//            // }
//        }
//        catch (IOException i) {
//            System.out.println(i);
//        }
//    }

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

                    TCPClient client = new TCPClient("127.0.0.1", 8002);
//                    client.sendLine();
//                    client.recieveLine();
                    client.saveFile();
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
