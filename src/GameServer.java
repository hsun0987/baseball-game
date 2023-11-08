import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

public class GameServer {
    private Socket socket;
    //클라이언트의 정보를 담는 리스트
    public static ArrayList<ClientThread> clients = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(50001);
            System.out.println("대기 중 ...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // 연결이 수립되면 socket 객체 변환
                System.out.println("연결됨 : " + clientSocket.getLocalAddress());

                ClientThread clientThread = new ClientThread(clientSocket);
                clients.add(clientThread);
                clientThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class ClientThread extends Thread {
    // 자릿수 입력 받기
    // 자릿수로 랜덤 문제 출제
    private Socket clientSocket; // 클라이언트 소켓
    private BufferedReader in;
    private BufferedWriter out;
    //private ObjectInputStream ois; // 객체를 읽어오기 위한 ObjectInputStream
    //private ObjectOutputStream oos; // 객체를 전송하기 위한 ObjectOutputStream

    public Vector userName = new Vector(); // 사용자 이름을 저장하는 Vector

    public ClientThread(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.setDaemon(true); // 데몬 스레드로 설정
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void gameStart(){
        // 게임 시작
        if(GameServer.clients.size() >= 2){     // 클라이언트가 2명 이상일 때
            try {
                for (int i = 3; i > 0; i--){
                    System.out.println("[" + i + "초 후에 게임을 시작합니다 . . . ]" );
                    Thread.sleep(1000);
                }
            }catch (InterruptedException i){
            }
        }
    }

    public void run(){
        try{
            while (true){
                String name = in.readLine();
                System.out.println("[" + name + "]" + "님이 입장하셨습니다." );
                userName.add(name);

                Iterator<String> it = userName.iterator();
                while (it.hasNext()){

                }

                gameStart();
                // 게임 코드

                System.out.println("[" + name + "] 님이 퇴장하였습니다.");

                in.close();
                // ois.close();
                out.close();
                //  oos.close();

                removeClient(this);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 클라이언트가 퇴장한 경우 클라이언트를 리스트에서 제거
    public static void removeClient(ClientThread client) {
        GameServer.clients.remove(client);
    }

    // 클라이언트에게 메시지를 전송하는 메서드
    public void sendMessage(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

}




