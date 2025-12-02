import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Dictionary {
    private final Map<String, String> words = new HashMap<>();
    private final String filename = "dictionary.txt";

    public Dictionary() {
        load();
        if (words.isEmpty()) {
            add("hello", "привіт");
            add("world", "світ");
            add("i", "я");
        }
    }

    private void load() {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            createDefaultFile();
            return;
        }

        try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
            int count = 0;
            for (String line : lines.toList()) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String eng = parts[0].trim();
                    String ukr = parts[1].trim();
                    if (!eng.isEmpty()) {
                        words.put(eng.toLowerCase(), ukr);
                        count++;
                    }
                }
            }
            System.out.printf("Завантажено словник: %d записів%n", count);
        } catch (Exception e) {
            System.out.println("Не вдалося прочитати словник, використовується порожній.");
        }
    }

    private void createDefaultFile() {
        String defaultContent = """
            # Англійсько-український словник
            # Формат: english=українська
            
            hello=привіт
            hi=привіт
            thank you=дякую
            please=будь ласка
            yes=так
            no=ні
            i=я
            you=ти
            love=люблю
            world=світ
            
            """;

        try {
            Files.writeString(Paths.get(filename), defaultContent, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Створено новий dictionary.txt з базовими словами");
            load();
        } catch (IOException e) {
            System.err.println("Не вдалося створити файл словника");
        }
    }

    public void add(String english, String ukrainian) {
        if (english == null || ukrainian == null || english.isBlank()) return;
        String key = english.strip().toLowerCase();
        String value = ukrainian.strip();

        if (!words.containsKey(key) || !words.get(key).equals(value)) {
            words.put(key, value);
            appendToFile(key + "=" + value);
            System.out.println("Додано: " + english + " → " + value);
        }
    }

    private void appendToFile(String line) {
        try {
            Files.writeString(Paths.get(filename), line + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException ignored) {}
    }

    public String get(String key) {
        if (key == null) return null;
        return words.get(key.toLowerCase().replaceAll("[^a-z' ]", ""));
    }

    public Set<String> getAllKeys() {
        return words.keySet();
    }

}