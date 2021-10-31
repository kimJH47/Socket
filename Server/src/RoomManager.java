import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomManager {
    //main 서버에서 받은 메시지 정보를 각 room 에 해당하는 곳으로 전달하기

    Queue<Client> clients = new LinkedList<>();//대기열
    Set<Room> rooms = new HashSet<>();
    private int room_number = 0;
    static int users = 0;


    public RoomManager() {
        System.out.println("RoomManager 생성");

    }

    public void addClient(Client client) {

        this.clients.add(client);
        System.out.println("대기열 추가 :" + client.name + client.socket.getInetAddress());
        users++;
        if (users % 2 == 0)
            createRoom(clients);

    }

    public void createRoom(Queue<Client> clients) {
        //방만들기 수정필요
        System.out.println("Room 생성");
        rooms.add(new Room(room_number++, clients));
        clients.removeAll(clients);

    }
}
