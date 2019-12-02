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