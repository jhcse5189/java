package com.assignment.bittorent;

public class Utils {

    public float version;

    public Utils() {
        version = (float) 0.1;
    }

    public void welcome() {
        System.out.printf(
                "Welcome to the BitTorrent Monitor.\n"
              + "version: %2.1f\n\n"
              + "Type 'h' for help.\n\n", version);
    }

    public void helpUsageClient() {
        System.out.printf(
                "List of all commands that my BitTorrent client supports.\n\n"
              + "init\tc -- initialize TCP client socket setting.\n"
              + "help\th -- Display this help block.\n"
              + "quit\tq -- Quit BitTorrent client.\n\n");
    }

    public void helpUsageServer() {
        System.out.printf(
                "List of all commands that my BitTorrent client supports.\n\n"
              + "init\ts -- initialize TCP server socket setting.\n"
              + "help\th -- Display this help block.\n"
              + "quit\tq -- Quit BitTorrent client.\n\n");
    }
}
