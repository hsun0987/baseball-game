import java.util.Random;
import java.util.Scanner;

public class game {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //랜덤만드는거 - 세자리/네자리 몇인이 할건지

        System.out.println("몇 자리 수로 게임을 할건가요? (3/4) >> ");
        String input = sc.nextLine();


        if(input == "3"){
            int rand = (int)(Math.random() * 3) + 1;
        }
        else if (input == "4") {
            int rand = (int)(Math.random() * 4) + 1;
        }


    }
}
