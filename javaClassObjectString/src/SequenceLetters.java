import java.util.*;

public class SequenceLetters {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word1 = sc.next();
        String word2 = sc.next();

        System.out.println(sortWord(word1) + " " + sortWord(word2));
    }

    private static String sortWord(String word) {
        char[] chars = word.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
