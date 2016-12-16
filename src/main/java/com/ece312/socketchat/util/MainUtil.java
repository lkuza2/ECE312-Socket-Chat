package com.ece312.socketchat.util;

import com.ece312.socketchat.server.MainServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by kuzalj on 12/15/2016.
 */
public class MainUtil {

    private static MainUtil instance;
    private String localUsername;
    private int port;
    private HashMap<Integer, String> connectedUsers = new HashMap<>();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelGroup broadcast;

    private MainUtil() {

    }

    public static MainUtil getInstance() {
        if (instance == null)
            instance = new MainUtil();
        return instance;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Java Socket Chat Server v1.00");
        System.out.println("Please Enter a Port Number: ");
        setPort(scanner.nextInt());
        System.out.println("Please Enter a Username: ");
        setLocalUsername(scanner.next());

        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine(); //you get the IP as a String

            System.out.println("Starting Server on local address: " + InetAddress.getLocalHost().getHostAddress() +
                    " Global Address: " + ip + " and port: " + getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new MainServer(getPort())).start();

        System.out.println("Ready to handle commands.");
        printCursor();
        handleCommands(scanner);
    }

    public void handleCommands(Scanner scanner) {
        String command;
        while (!(command = scanner.nextLine()).equals("escape")) {
            switch (command.trim()) {
                case "exit":
                    exit();
                    break;
                case "":
                    break;
                default:
                    sendData("<" + getLocalUsername() + ">" + command, null);
                    printCursor();
                    break;
            }
        }
    }

    public void printCursor() {
        System.out.print("<" + getLocalUsername() + ">");
    }

    public void sendData(String data, Channel exclude) {
        for (Channel channel : getBroadcast()) {
            if ((exclude != null && channel != exclude) || exclude == null) {
                final ByteBuf message = channel.alloc().buffer(1024);// (2)
                message.writeCharSequence(data, Charset.defaultCharset());
                channel.writeAndFlush(message).awaitUninterruptibly().isSuccess();
            }
        }
    }

    public void exit() {
        sendData("exit", null);
        getBossGroup().shutdownGracefully();
        getWorkerGroup().shutdownGracefully();
        System.exit(0);
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public ChannelGroup getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(ChannelGroup broadcast) {
        this.broadcast = broadcast;
    }
}
