import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static ExecutorService threadPool; //쓰레드풀 생성
    public static Vector<Client> clients = new Vector<>();//현재 서버에 연결된 클라이언트들을 담는 Vector
    ServerSocket serverSocket;

    public void startServer(String IP, int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(IP, port));
            Date date_now = new Date(System.currentTimeMillis());
            System.out.println("[Server On]" +IP+":"+port+":"+date_now);
        } catch (Exception e) {
            e.printStackTrace();
            if (!serverSocket.isClosed()) {
                stopServer();
                return;
            }

        }
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        clients.add(new Client(socket)); //연결된 클라이언트 소켓을 clients 에 추가
                        System.out.println("[client accepted]" + socket.getRemoteSocketAddress() + ":"
                                + Thread.currentThread().getName());

                    } catch (Exception e) {
                        if (!serverSocket.isClosed()) {
                            stopServer();
                            break;
                        }
                    }

                }
            }

        };
        threadPool = Executors.newCachedThreadPool();
        threadPool.submit(thread);
    }

    public void stopServer() {
        //현재 작동중인 모든 소켓들을 받기
        Iterator<Client> iterator = clients.iterator();
        try{

        while(iterator.hasNext()){
            Client client = iterator.next();
            client.socket.close();
            iterator.remove();

        }
        if(serverSocket !=null && !serverSocket.isClosed()){
            serverSocket.close();
        }
        if(threadPool !=null && !threadPool.isShutdown()){
            threadPool.shutdown();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //main 시작점
    public static void main(String[] args) {
        Main me = new Main();
        me.startServer("127.0.0.1",9870);

    }
}