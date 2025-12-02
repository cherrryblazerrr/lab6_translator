import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Dictionary dict = new Dictionary();
        Translator translator = new Translator(dict);
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("\n  АНГЛІЙСЬКО - УКРАЇНСЬКИЙ ПЕРЕКЛАДАЧ\n");

        while (true) {
            System.out.print("Додати слово/фразу? (так/ні): ");
            if (!sc.nextLine().trim().matches("(?i)так|т|yes|y")) break;

            System.out.print("  англійською: ");
            String eng = sc.nextLine().trim();
            System.out.print("  українською: ");
            String ukr = sc.nextLine().trim();
            dict.add(eng, ukr);
        }

        System.out.println("\nПочнемо перекладати!\n");

        while (true) {
            System.out.print("→ ");
            String input = sc.nextLine().trim();
            if (input.isBlank()) continue;
            if (input.equalsIgnoreCase("вихід") || input.equalsIgnoreCase("exit")) {
                System.out.println("До зустрічі!");
                break;
            }
            System.out.println("  " + translator.translate(input) + "\n");
        }
    }
}