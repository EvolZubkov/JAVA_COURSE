import java.util.Scanner;

public class ArrowScanner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int count = 0;
        for (int i = 0; i <= s.length() - 5; i++) {
            String sub = s.substring(i, i + 5);
            if (sub.equals(">>-->") || sub.equals("<--<<")) {
                count++;
            }
        }
        System.out.println(count);
    }
}
