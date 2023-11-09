import org.w3c.dom.ls.LSOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class GameServer {

    static ServerSocket serverSocket = null;
    static ArrayList<Socket> socketList = new ArrayList<>();
    static Vector userName = new Vector();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(50001);
            while (true) {

                System.out.println("대기중...");
                Socket socket = serverSocket.accept();// 연결이 수립되면 socket 객체가 반환

                socketList.add(socket);

                new Thread(() -> {
                    communicateWithClient(socket);
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void communicateWithClient(Socket socket){
        DataInputStream dis = null;
        DataOutputStream dos = null;
        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
        String id = "";
        String message = "";
        int count = 1;

        System.out.println("[서버] " + isa.getHostName() + "의 연결 요청을 수락함");
        try {
            dis = new DataInputStream(socket.getInputStream());
            id = dis.readUTF();
            message = "";
            System.out.println(id + "님이 입장하셨습니다." );
            userName.add(id);

            if(userName.size() == 2){
                gameStart(socket);
            }
            //데이터 받기
            while (true) {
                for (Socket clientSocket : socketList) {
                    if (clientSocket.equals(socket)) continue;
                    dos = new DataOutputStream(clientSocket.getOutputStream());

                    message = dis.readUTF();
                    dos.writeUTF(id + " : "+ message);
                    dos.flush();
                }
                message = dis.readUTF();
                System.out.println(id + " : "+ message);
            }

        } catch (Exception e) {
            try {
                System.out.println(id + " 님이 나가셨습니다.");
                socketList.remove(socket);
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 게임 시작
    public static void gameStart(Socket socket){
        try {
            DataOutputStream dos = null;
            dos = new DataOutputStream(socket.getOutputStream());

            for (Socket clientSocket : socketList) {
                dos = new DataOutputStream(clientSocket.getOutputStream());
                for (int i = 3; i > 0; i--) {
                    dos.writeUTF("[" + i + "초 후에 게임을 시작합니다 . . . ]");
                    Thread.sleep(1000);
                }
                gameRun(socket);
            }
            dos.flush();


        }catch (InterruptedException | IOException ie){
            ie.printStackTrace();
        }
    }

    public static void gameRun(Socket socket){
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = null;
            String message = "";

            Scanner sc = new Scanner(System.in);
            Random random = new Random();

            for (Socket clientSocket : socketList) {
                dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF("===========야구 게임 시작===========");
                dos.writeUTF("맞추실 숫자 자릿수를 정해주세요 (3 or 4): ");
            }

            int numCount = Integer.parseInt(dis.readUTF());

            int n1[] = new int[numCount];
            int n2[] = new int[numCount];

            boolean bCheck = false;
            int index = 0;

            while (true) {
                for (int i = 0; i < n1.length; i++) {
                    index = (int) (Math.random() * 10);
                    n1[i] = index;
                }
                if (numCount == 3 ) {
                    if (!(n1[0] == n1[1] || n1[1] == n1[2] || n1[0] == n1[2])) {
                        break;
                    }
                } else if ( numCount ==4 ){
                    if (!(n1[0] == n1[1] || n1[1] == n1[2] || n1[0] == n1[2])) {
                        if (!(n1[3] == n1[1] || n1[3] == n1[2] || n1[3] == n1[0])) {
                            break;
                        }
                    }
                } else {
                    dos.writeUTF("3이나 4자리만 가능함 ㅡㅡ");
                    break;
                }
            }
            dos.writeUTF("플레이 할 이닝 수를 정해주세요");
            int inning = Integer.parseInt(dis.readUTF());
            dos.writeUTF("===========Play Ball!!===========");

            for (int i = 1 ; i <= inning ; i++ ) {
                dos.writeUTF(i + " Inning >> ");


                int nn = Integer.parseInt(dis.readUTF());
                if (numCount == 3) {
                    n2[0] = nn / 100;
                    n2[1] = (nn % 100) / 10;
                    n2[2] = nn % 10;
                }
                if (numCount == 4) {
                    n2[0] = nn / 1000;
                    n2[1] = (nn % 1000) / 100;
                    n2[2] = (nn % 100) / 10;
                    n2[3] = nn % 10;
                }
                int strike = 0;
                int ball = 0;
                for(int j=0; j < n1.length; j++){
                    for(int k=0; k < n2.length ; k++) {
                        if (n1[j] == n2[k])
                            if (j == k) {
                                strike++;
                            } else {
                                ball++;
                            }
                    }
                }
                dos.writeUTF( strike + "S " + ball + "B");
                if ( strike == numCount) {
                    dos.writeUTF("You Win!");
                    dos.writeUTF("축하합니다! 정답을 맞추셨어요.");
                    break;
                }
            }
            dos.writeUTF("정답 : ");
            for (int m : n1)
                dos.writeUTF("" + m);
            //dos.writeUTF("\n");

            dos.writeUTF("============게임 종료============");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
   /* String setTurn(){
        int count = Server.socketList.size();

    }*/
}