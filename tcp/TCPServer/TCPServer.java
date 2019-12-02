import java.awt.desktop.SystemEventListener;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPServer {

    private static String fileToSend = null;
    private static String fileToSeek = null;
    private static String extension = null;
    private static int totalChunks;

    // initialize socket and input output streams
    private ServerSocket welcomeSocket       = null;
    private Socket connectionSocket          = null;

    private BufferedReader inFromClient      = null;
    private BufferedOutputStream outToClient = null;
    private DataOutputStream outToClientName = null;

    private FileOutputStream fos             = null;
    private FileInputStream fis              = null;
    private BufferedOutputStream bos         = null;
    private BufferedInputStream bis          = null;

    
    // constructor to put ip iddress and port
    public TCPServer(int port, String fileToSend) {

        this.fileToSend = fileToSend;
        extension = fileToSend.split("\\.(?=[^\\.]+$)")[1];
        try {
            // create welcome socket, at the port
            welcomeSocket = new ServerSocket(port);
            System.out.println("Socket initializing in server...");
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public int seederMakeChunk() {
        File myFile = new File("./" + fileToSend);
        int totalBytes = (int)myFile.length();
        System.out.println("file byte(s): " + totalBytes);

        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        byte[] buffer = new byte[10000];
        int byteRead;
        int chunk = 1;

        try {
            while ((byteRead = bis.read(buffer, 0, buffer.length)) > 0) {
                fos = new FileOutputStream("./seeder8001/chunk" + chunk + "." + extension);
                bos = new BufferedOutputStream(fos);
                bos.write(buffer, 0, byteRead);
                bos.flush();
                System.out.printf("chunk %2d is chopped! in %5d bytes:)\n", chunk, byteRead);
                chunk++;
            }
        } catch (IOException i) {
            System.out.println(i);
        }

        return chunk;
    }

    public void waitForClient() {
        System.out.println("Wait on welcome socket for contact by client...");
        try {
            connectionSocket = welcomeSocket.accept();
            // create input stream, attached to socket
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            // create output stream, attached to socket
            outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            outToClientName = new DataOutputStream(connectionSocket.getOutputStream());

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

        if (!fileToSeek.equals(fileToSend)) {
            System.out.println("Can't find file name " + fileToSeek);
            //outToClientName.writeBytes(fileToSend + '\n');
            //close();
            //System.exit(-1);
        }

        totalChunks = seederMakeChunk();
        try {
            outToClientName.writeBytes(Integer.toString(totalChunks) + '\n');
        } catch (IOException i) {
            System.out.println(i);
        }

        File myFile = new File( "./" + fileToSend );
        byte[] buffer = new byte[10000];//[(int)myFile.length()];


        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
//            while (fis.read(buffer) > 0) {
//                outToClient.write(buffer);
//            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }

        try {
            int total = 0;
            int current = 0;
            int chunk = 1;
            String extension = fileToSend.split("\\.(?=[^\\.]+$)")[1];

            fis = new FileInputStream("./seeder8001/chunk" + chunk + "." + extension);
            bis = new BufferedInputStream(fis);
            while ((chunk < totalChunks) && ((current = bis.read(buffer, 0, buffer.length)) > 0)) {
                outToClient.write(buffer, 0, current);
                outToClient.flush();
                System.out.printf("chunk %2d is sent! in %5d bytes:)\n", chunk, current);
                total += current;

                if (chunk == totalChunks -1) break;

                chunk++;
                fis = new FileInputStream("./seeder8001/chunk" + chunk + "." + extension);
                bis = new BufferedInputStream(fis);
                Thread.sleep(1000);
            }

            System.out.println("'" + fileToSend + "' is sent! in total " + total + " bytes:)");
        }
        catch (IOException i) {
            System.out.println(i);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
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

    // (filename.)
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        fileToSend = "youandme.jpg"; //= args[1];
        String word;

        Utils utils = new Utils();
        utils.welcome();

        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();

            switch (word) {
                case "s":
                    TCPServer server = new TCPServer(8001, fileToSend);
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
