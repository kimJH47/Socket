import java.io.ObjectInputStream;
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
    public static RoomManager roomManager = new RoomManager();
    ServerSocket serverSocket;

    public void startServer(String IP, int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(IP, port));
            Date date_now = new Date(System.currentTimeMillis());
            System.out.println("[Server On]" +IP+":"+port+":"+date_now);
            receiveClient();
        } catch (Exception e) {
            e.printStackTrace();
            if (!serverSocket.isClosed()) {
                stopServer();
                return;
            }
        }


    }

    public void receiveClient(){
        //클라이언트의 대기열요청을 받는 메서드
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("[client accepted]" + socket.getRemoteSocketAddress() + ":"
                                + Thread.currentThread().getName());
                        //클라이언트가 로비에서 대기열입장 요청시 소켓정보를 담고있는 클라이언트객체 생성 후 roomManager 객체에 전달
                        roomManager.addClient(new Client(socket));


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
        Iterator<Client> iterator = roomManager.clients.iterator();
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
        me.startServer("172.30.1.13",9001);

    }
}