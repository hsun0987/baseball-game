import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class game {
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        System.out.println("===========야구 게임 시작===========");
        System.out.print("맞추실 숫자 자릿수를 정해주세요 (3 or 4): ");
        int numCount = sc.nextInt();

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
                System.out.println("3이나 4자리만 가능함 ㅡㅡ");
                break;
            }
        }
        System.out.print("플레이 할 이닝 수를 정해주세요 : ");
        int inning = sc.nextInt();
        System.out.println("===========Play Ball!!===========");

        for (int i = 1 ; i <= inning ; i++ ) {
            System.out.print(i + " Inning >> ");
            int nn = sc.nextInt();
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
            System.out.println( strike + "S " + ball + "B");
            if ( strike == numCount) {
                System.out.println("You Win!");
                System.out.println("축하합니다! 정답을 맞추셨어요.");
                break;
            }
        }
        System.out.print("정답 : ");
        for (int m : n1)
            System.out.print(m);
        System.out.print("\n");

        System.out.println("============게임 종료============");
    }
}
