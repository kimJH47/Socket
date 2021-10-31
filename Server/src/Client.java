import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {


    Socket socket;
    Room room;
    String ID = ""; // 직렬화로 추가하기
    String name = new String("Unknown");

    public Client(Socket socket) {
        this.socket = socket;


    }

    public void enterRoom(Room room) {
        //방입장
        this.room = room;
        //receive();

    }

    public void exitRoom() {
        //자신이 가지오있는 room 객체의 clients 리스트중 본인 삭제
        room.clients.remove(this);

    }

    public void wait_queue() {
        //미구현
        while (room == null) {
            System.out.println();
        }
    }

    public void receive() {
        //클라이언트로 메세지를 받는 메서드
        //수정필요
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
                        for (Client client : room.clients) {
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

                } catch (Exception e) {
                    try {
                        System.out.println("[message send error]"
                                + socket.getRemoteSocketAddress() + ":" + Thread.currentThread().getName());
                        room.clients.remove(Client.this);
                    } catch (Exception e2) {
                        e2.printStackTrace();

                    }

                }
            }
        };
        Main.threadPool.submit(thread);

    }


}
