import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class Main {

    Socket socket;


    //클라이언트 동작 메서드
    public void startClient(String IP, int port) {

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP, port);
                    System.out.println("chat start");
                    receive();

                } catch (Exception e) {
                    stopClient();
                    e.printStackTrace();
                }
            }
        };

        Thread thread1 = new Thread(thread);
        thread1.start();

    }


    //클라이언트 종료 메서드
    public void stopClient() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //서버로 부터 메세지를 전달받는 메서드
    public void receive() {
        while (true) {
            try {
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[512];
                int length = in.read(buffer);
                if (length == -1) throw new IOException();
                String message = new String(buffer, 0, length, "UTF-8");
                System.out.printf("%s", message);

            } catch (Exception e) {
                stopClient();
                e.printStackTrace();

            }
        }

    }


    //전송메서드
    public void send(String message) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    OutputStream out = socket.getOutputStream();
                    byte[] buffer;
                    buffer = message.getBytes("UTF-8");
                    out.write(buffer);
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                    stopClient();

                }

            }
        };

    }

    public static void main(String[] args) {

        Main me = new Main();
        me.startClient("127.0.0.1",9870);


    }
}
