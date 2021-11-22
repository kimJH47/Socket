import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {


    Socket socket;
    private Room room;
    private String ID = ""; // 직렬화로 추가하기
    private String name = new String("");
    private boolean joinFlag = false; // 대기열입장여부
    private Protocol protocol;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        receive();


    }

    public void enterRoom(Room room) {
        //방입장
        this.room = room;

    }

    public void exitRoom() {
        //자신이 가지오있는 room 객체의 clients 리스트중 본인 삭제
        room.clients.remove(this);

    }

    public void wait_queue() {
        //미구현
        while (room == null) {
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
                        try {
                            protocol = (Protocol) in.readObject();
                            System.out.println("[message received]" + socket.getRemoteSocketAddress() + ":"
                                    + Thread.currentThread().getName() + ":" + protocol);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            continue;
                        } catch (SocketException e) {
                            break;
                        }
                        if (protocol instanceof ChatData) {

                        } else if (protocol instanceof LoginData) {
                            analysisLoginData((LoginData) protocol);

                        } else if (protocol instanceof SignUpData) {

                        } else if (protocol instanceof JoinData) {

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

    public void analysisLoginData(LoginData data) {
        String ID = data.getID();
        String passWord = data.getPassWord();
        try {
            if (Main.DAO.signIn(ID, passWord)) {
                out.writeObject(new JoinData(true, JoinData.LOGIN_ACCESS));
            } else {
                out.writeObject(new JoinData(true, JoinData.LOGIN_FAILED));
            }
            out.flush();//?


        } catch (Exception e) {
            try{
                out.writeObject(new JoinData(true, JoinData.LOGIN_FAILED));
                e.printStackTrace();
            }catch (Exception e1){
                   e1.printStackTrace();
            }
        }

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

    public String getName() {
        return name;
    }
}
