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
                "valido1.txt", "var x: integer; begin x := 10; if x > 0 then x := x - 1; end.",
                "valido2.txt", "procedure saludar(); begin writeln('Hola'); end;",
                "valido3.txt", "{ Comentario } function suma(a,b:integer):integer; begin suma:=a+b; end;",
                "invalido1.txt", "var x := 10 @ 5;",
                "invalido2.txt", "write(\"Hola);",
                "invalido3.txt", "var 9var: integer;"
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
