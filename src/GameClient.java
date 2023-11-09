import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    static DataOutputStream dos = null;
    static DataInputStream dis = null;
    public static void main(String[] args) {
        Socket socket = new Socket();

        Scanner sc = new Scanner(System.in);
        try {

            socket.connect(new InetSocketAddress("localhost", 50001));
            System.out.println("연결됨 ... 포트번호 : " + socket.getLocalPort());
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            System.out.print("사용자 아이디를 만들어주세요 : ");
            dos.writeUTF(sc.nextLine());

            new Thread(() -> {
                try {
                    while (true) {
                        String message = dis.readUTF();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            while (true) {
                // System.out.print(">>");
                dos.writeUTF(sc.nextLine());
                dos.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}