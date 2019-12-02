import java.awt.desktop.SystemEventListener;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPClient {

    private static String fileToSeek  = null;
    private static String fileToSend = null;

    // initialize socket and input output streams
    private Socket clientSocket             = null;
    private BufferedReader inFromUser       = null;
    private DataOutputStream outToServer    = null;

    private BufferedReader inFromServer     = null;

    private FileOutputStream fos            = null;
    private InputStream is                  = null;
    private BufferedInputStream bis = null;
    private BufferedOutputStream bos        = null;
    private ByteArrayOutputStream baos = null;

    // constructor to put ip iddress and port
    public TCPClient (String address, int port, String localAddr, int localPort, String file) {

        fileToSeek = file;
        // create client socket, connect to server
        try {
            InetAddress addr = InetAddress.getByName(address);
            clientSocket = new Socket(addr, port, addr, localPort);

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

    public void printInfo() {
        System.out.println("Connected.");
        System.out.printf("\tserver addr: " + clientSocket.getInetAddress() + "\n");
        System.out.printf("\tserver port: " + clientSocket.getPort() + "\n");
        System.out.printf("\tclient port: " + clientSocket.getLocalPort() + "\n");
    }

    public void saveFile() {

        try {
            outToServer.writeBytes( fileToSeek + '\n');
        }
        catch (IOException i) {
            System.out.println(i);
        }


        byte[] aByte = new byte[1];
        byte[] buffer = new byte[10000];
        int bytesRead;

        bis = new BufferedInputStream(is);

        if (is != null) {

            try {
                fos = new FileOutputStream("./client8002/" + fileToSeek);
                bos = new BufferedOutputStream(fos);

                int chunk = 1;
                while ((bytesRead = is.read(buffer, 0, buffer.length)) > 0) {
                    bos.write(buffer, 0, bytesRead);
                    bos.flush();
                    System.out.printf("chunk %2d is received! in %5d bytes:)\n", chunk, bytesRead);
                    chunk++;
                }

                System.out.println("'" + fileToSeek + "' Saved in client's directory:)");
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

    // 800X, dlwlrma.jfif
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        int localPort = 8002;//Integer.parseInt(args[1]);
        String file = "youandme.jpg";//= args[2];

        Utils utils = new Utils();
        utils.welcome();

        String word;
        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();
            switch (word) {
                case "c":
                    TCPClient client = new TCPClient("127.0.0.1", 8001, "127.0.0.1", localPort,  file);
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
