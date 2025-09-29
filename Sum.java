import java.util.Scanner;
import java.util.Arrays;
public class Sum {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int[] ar = new int[5];
        for(int i = 0; i < 5; i++)
        {
            ar[i] = input.nextInt();
        }
        Arrays.sort(ar);

        System.out.println(ar[0] + ar[4]);
    }
}
