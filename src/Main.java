import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    private static final String TEST_DIR = "test";

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--init-tests")) {
            initTestFiles();
            return;
        }

        if (args.length == 0) {
            System.out.println("Uso: java Main <archivo> o java Main --init-tests");
            return;
        }

        String filePath = args[0];
        try {
            String code = Files.readString(Paths.get(filePath));
            List<Lexer.Token> tokens = Lexer.tokenize(code);
            Validator.validate(tokens);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static void initTestFiles() {
        try {
            Files.createDirectories(Paths.get(TEST_DIR));

            Map<String, String> archivos = Map.of(
                "valido1.txt", "public class Test { public static void main(String[] args) { int x = 10; if (x > 0) x--; } }",
                "valido2.txt", "interface Example { void method(); }",
                "valido3.txt", "/* Comment */ class Calc { int sum(int a, int b) { return a + b; } }",
                "invalido1.txt", "class Test { int x = 10 @ 5; }", // Invalid operator
                "invalido2.txt", "class Test { String s = \"unclosed; }", // Unclosed string
                "invalido3.txt", "class 9Test { int x; }" // Invalid class name
            );

            for (var entry : archivos.entrySet()) {
                Path filePath = Paths.get(TEST_DIR, entry.getKey());
                Files.writeString(filePath, entry.getValue());
            }

            System.out.println("Archivos de prueba creados en ./" + TEST_DIR);
        } catch (IOException e) {
            System.out.println("Error al crear archivos de prueba: " + e.getMessage());
        }
    }
}
