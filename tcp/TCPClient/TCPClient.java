import java.awt.desktop.SystemEventListener;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {

    private static String fileToSeek  = null;
    private static String fileToSend = null;
    private static String extension = null;
    private static int totalChunks;

    // initialize socket and input output streams
    private Socket clientSocket             = null;

    private DataOutputStream outToServer    = null;
    private BufferedReader inFromServer     = null;

    private InputStream is                  = null;
    private FileInputStream fis = null;
    private FileOutputStream fos            = null;
    private BufferedOutputStream bos        = null;

    // constructor to put ip iddress and port
    public TCPClient (String address, int port, String localAddr, int localPort, String fileToSeek) {

        this.fileToSeek = fileToSeek;
        extension = fileToSeek.split("\\.(?=[^\\.]+$)")[1];
        // create client socket, connect to server
        try {
            InetAddress addr = InetAddress.getByName(address);
            clientSocket = new Socket(addr, port, addr, localPort);

            // takes input stream from terminal
            //inFromUser = new BufferedReader(new InputStreamReader(System.in));
            // sends output stream attached to the socket
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
            totalChunks = Integer.parseInt(inFromServer.readLine());
        }
        catch (IOException i) {
            System.out.println(i);
        }

        byte[] aByte = new byte[1];
        byte[] buffer = new byte[10000];
        int bytesRead;

        if (is != null) {

            try {
                int chunk = 1;
                fos = new FileOutputStream("./client8002/chunk" + chunk + "." + extension);
                bos = new BufferedOutputStream(fos);

                while ((chunk < totalChunks) && ((bytesRead = is.read(buffer, 0, buffer.length)) > 0)) {
                    bos.write(buffer, 0, bytesRead);
                    bos.flush();
                    System.out.printf("chunk %2d is received! in %5d bytes:)\n", chunk, bytesRead);

                    if (chunk == totalChunks -1) break;

                    chunk++;
                    fos = new FileOutputStream("./client8002/chunk" + chunk + "." + extension);
                    bos = new BufferedOutputStream(fos);
                }

                System.out.println("'" + fileToSeek + "' Saved in client's directory:)");
                bos.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }

    public void mergeFile() {

        System.out.println("Starting merge in 5 seconds...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        File ofile = new File("./client8002/" + fileToSeek);

        byte[] buffer;
        int bytesRead = 0;

        List<File> list = new ArrayList<File>();
        for (int i = 1; i < totalChunks; i++) {
            list.add(new File("./client8002/chunk" + i + "." + extension));
        }

        try {
            fos = new FileOutputStream(ofile, true);
            for (File f : list) {
                fis = new FileInputStream(f);
                buffer = new byte[(int)f.length()];
                bytesRead = fis.read(buffer, 0, (int)f.length());
                assert(bytesRead == buffer.length);
                assert(bytesRead == (int)f.length());
                fos.write(buffer);
                fos.flush();
                buffer = null;
                fis.close();
                fis = null;
            }
            fos.close();
            fos = null;

        } catch (IOException i) {
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

    // 8002, youandme.jpg
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("invalid command-line arguments: " + args.length);
            System.exit(-1);
        }

        int localPort = 0;
        try {
            localPort = Integer.parseInt(args[0]);
            fileToSeek = args[1];
        } catch (NumberFormatException e) {
            System.out.println(e);
            System.exit(-1);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        Scanner keyboard = new Scanner(System.in);

        Utils utils = new Utils();
        utils.welcome();

        String word;
        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();
            switch (word) {
                case "c":
                    TCPClient client = new TCPClient("127.0.0.1", 8001, "127.0.0.1", localPort,  fileToSeek);
                    client.saveFile();
                    client.mergeFile();
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
