import com.sun.source.tree.Scope;

import java.awt.font.OpenType;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client { //chatServer 가 통신일 하기위해 Client 클래스를 가지고있음
    //한명의 클라이언트와 통신을 가능하게 해주는 클래스

    Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
        receive();
    }

    public void receive() {
        //클라이언트로 메세지를 받는 메서드
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        InputStream in = socket.getInputStream();
                        byte[] buffer = new byte[512];
                        int length = in.read(buffer);
                        while (length == -1) throw new IOException();
                        System.out.println("[message received]" + socket.getRemoteSocketAddress() + ":"
                                + Thread.currentThread().getName()
                        );
                        String message = new String(buffer, 0, length, "UTF-8");
                        for (Client client : Main.clients) {
                            client.send(message);
                        }
                    }
                } catch (Exception e) {
                    try {
                        System.out.println("[message receiving error]"
                                + socket.getRemoteSocketAddress() + ":" + Thread.currentThread().getName());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        };
        Main.threadPool.submit(thread);

    }

    public void send(String message) {
        //클라이언트에게 메세지를 전송하는 메서드
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                OutputStream out = socket.getOutputStream();
                byte[] buffer = message.getBytes("UTF-8");
                out.write(buffer);
                out.flush();
                    System.out.println("[message send Success]"
                            + socket.getRemoteSocketAddress() + ":" + Thread.currentThread().getName());
                } catch (Exception e) {
                    try {
                        System.out.println("[message send error]"
                                + socket.getRemoteSocketAddress() + ":" + Thread.currentThread().getName());
                       Main.clients.remove(Client.this);
                    } catch (Exception e2) {
                        e2.printStackTrace();

                    }

                }
            }
        };
        Main.threadPool.submit(thread);

    }


}
