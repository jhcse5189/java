import java.awt.desktop.SystemEventListener;
import java.util.Scanner;
import java.io.*;
import java.net.*;

class TCPThread implements Runnable {
    public void run() {
        System.out.println("Thread start");
    }
}

class Buffer {
    private int loc = 0;
    private double[] data;

    public Buffer(int size) {
        data = new double[size];
    }

    public int getSize() { return data.length; }

    public synchronized void add(double toAdd) throws InterruptedException {
        if (loc >= data.length) {
            System.out.println("Buffer is full.");
            wait();
        }
        System.out.println("Adding item " + toAdd);
        System.out.flush();
        data[loc++] = toAdd;
        notifyAll();
    }

    public synchronized double remove() throws InterruptedException {
        if (loc <= 0) {
            System.out.println("Buffer is empty.");
            wait();
        }
        double hold = data[--loc];
        data[loc] = 0.0;
        System.out.println("Removing item " + hold);
        System.out.flush();
        notifyAll();
        return hold;
    }

    public synchronized String toString() {
        String toReturn = "";
        for (int i = 0; i < data.length; i++) {
            toReturn += String.format("%2.2f", data[i]) + " ";
        }
        return toReturn;
    }
}

class Producer extends Thread {
    public Producer() {}

    private int pNum;
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void produce() throws InterruptedException {
        for (int i = 0; i < buffer.getSize(); i++) {
            buffer.add(Math.random() * 100);
        }
    }
    /*
    public void produce() {
        for (int i = 0; i < 100; i++) {
            prodConTest.NUMBER = Math.random()*100;
            System.out.println("Producer: " + prodConTest.NUMBER);
        }
    }
    */

    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    public void run() {
        produce();
    }
     */
}

class Consumer extends Thread {

    private int pNum;
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void consume() throws InterruptedException {
        for (int i = buffer.getSize(); i >= 0; i--) {
            buffer.remove();
        }
    }

    /*
    public Consumer() {}

    public void consume() {
        for (int i = 100; i >= 0; i--) {
            System.out.println("Consumer: " + prodConTest.NUMBER);
        }
    }
     */

    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
    public void run() {
        consume();
    }
     */
}

class prodConTest {
//    static double NUMBER;

    public static void main(String[] args) {

        Buffer buff = new Buffer(100);

        Producer producer = new Producer(buff);
        Consumer consumer = new Consumer(buff);
        producer.start();
        consumer.start();
    }
}

class Counter {
    private int counter;

    public Counter() {
        counter = 0;
    }

    public int value() {
        return counter;
    }

    /* critical region */
    public synchronized void increment() {
        int local;
        local = counter;
        local++;
        counter = local;
    }
}

class RaceCondtionTest extends Thread {
    private Counter countObject;

    public RaceCondtionTest(Counter ctr) {
        countObject = ctr;
    }

    public void run() {
        countObject.increment();
    }

    public static void main(String[] args) {
        int i;
        Counter masterCounter = new Counter();
        RaceCondtionTest[] threads = new RaceCondtionTest[5000];

        System.out.println("The counter is " + masterCounter.value());
        for (i = 0; i < threads.length; i++) {
            threads[i] = new RaceCondtionTest(masterCounter);
            threads[i].start();
        }

        // Wait for the threads to finish
        for (i = 0; i < threads.length; i++) {

            /* .join() to wait
             * add synchronized keyword
             */
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("The counter is " + masterCounter.value());
    }
}

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
    public TCPServer(int port, String file) {

        fileToSend = file;
        try {
            // create welcome socket, at the port
            welcomeSocket = new ServerSocket(port);
            System.out.println("Socket initializing in server...");
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void seederMakeChunk(String file) {

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
        byte[] buffer = new byte[10000];//[(int)myFile.length()];
        System.out.println("file byte(s): " + myFile.length());

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
            int total = 0;
            int current = 0;
            int chunk = 1;
            while ((current = bis.read(buffer, 0, buffer.length)) > 0) {
                //System.out.println(current);
                outToClient.write(buffer, 0, current);
                outToClient.flush();
                System.out.printf("chunk %2d is sent! in %5d bytes:)\n", chunk, current);
                chunk++;
                total += current;
                Thread.sleep(500);
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
        String file = "youandme.jpg"; //= args[1];
        String word;

        Utils utils = new Utils();
        utils.welcome();

        while (true) {
            System.out.print("BT> ");
            word = keyboard.next();

            switch (word) {
                case "s":
                    TCPServer server = new TCPServer(8001, file);
                    server.seederMakeChunk(file);
                    server.waitForClient();
                    server.sendFile();
                    server.close();
                    break;
                case "h":
                    utils.helpUsageServer();
                    break;
                case "t":
                    Thread t = new Thread(new TCPThread());
                    t.start();
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
