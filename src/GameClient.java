import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.in;

public class GameClient {
    static DataOutputStream dos = null;
    static DataInputStream dis = null;

    private BufferedReader in = null;
    private BufferedWriter out = null;
    private String name;
    private Socket socket = null;

    public GameClient(){
        try {
            socket = new Socket("localhost", 50001); // 누구한테 연결할거야??? -> 서버정보가 필요
            System.out.println("연결됨 ... 포트번호 : " + socket.getLocalPort());
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //서버와 스트림 연결
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //서버와 스트림 연결

            connectServer(socket);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
       GameClient client = new GameClient();
    }

    private void connectServer(Socket socket) throws IOException{
        try {
            System.out.print("사용자 아이디를 만들어주세요 : ");
            name = in.readLine();

            Thread sendThread = new SendThread(socket, name);

            sendThread.setDaemon(true);
            sendThread.start();

            while (true) {
                String inputMsg = in.readLine();
                if (inputMsg == null) {
                    break;
                }
                System.out.println("From: " + inputMsg);
            }

        } catch (IOException e) {
            System.out.println("[서버와 접속 끊김]");
            throw new RuntimeException(e);
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //System.out.println("[서버와 연결 종료]");
    }
    class SendThread extends Thread {
        private Socket socket = null;
        private String name;
        public SendThread(Socket socket, String name) throws IOException { //생성자
            this.socket = socket;
            this.name = name;
        }
    }
}