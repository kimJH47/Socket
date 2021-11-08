import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

public class Room {

    Set<Client> clients = new HashSet<>();
    private int num = 0; //인원수
    private int room_num = 0;

    public Room(int room_num,Queue<Client> clients) {
        //clients 들을 방에 추가. client 도 방의 정보를 알고있어야함.

        for (Iterator<Client> iterator = clients.iterator(); iterator.hasNext(); ) {
            Client client =iterator.next();
            this.clients.add(client);
            client.enterRoom(this);
            this.room_num = room_num;
            num++;

        }
    }
    public void sendClients(String message){

    }
    public void send(String msg){
        Iterator<Client> clientIterator = clients.iterator();

        while (clientIterator.hasNext()){
            clientIterator.next().send(msg);
        }
    }


}

