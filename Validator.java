public class Validator {

    public static boolean validate(java.util.List<Lexer.Token> tokens) {
        for (Lexer.Token t : tokens) {
            String tipo = t.type;
            String valor = t.value;
            int linea = t.line;
            int columna = t.column;

            if (tipo.equals("ERROR")) {
                System.out.printf("%nError léxico en línea %d, columna %d: '%s'%n", linea, columna, valor);
                printColored("ERROR", valor);
                return false;
            }

            printColored(tipo, valor);
        }
        System.out.println("\n\nEl archivo es válido.");
        return true;
    }

    // Método para imprimir colores en consola (ANSI)
    private static void printColored(String tipo, String valor) {
        String colorCode;
        switch (tipo) {
            case "KEYWORD": colorCode = "\u001B[35m"; break; // Magenta
            case "ID":      colorCode = "\u001B[34m"; break; // Azul
            case "NUMBER":  colorCode = "\u001B[33m"; break; // Amarillo
            case "STRING":  colorCode = "\u001B[32m"; break; // Verde
            case "COMMENT": colorCode = "\u001B[90m"; break; // Gris
            case "OP":      colorCode = "\u001B[36m"; break; // Cian
            case "SEP":     colorCode = "\u001B[31m"; break; // Rojo
            case "GROUP":   colorCode = "\u001B[37m"; break; // Blanco
            case "ERROR":   colorCode = "\u001B[41m"; break; // Fondo rojo
            default:        colorCode = "\u001B[0m";  break; // Reset
        }
        System.out.print(colorCode + valor + " " + "\u001B[0m");
    }

    // Ejemplo de uso con el lexer
    public static void main(String[] args) {
        String code = "var x := 10; if x > 5 then begin // comentario\nx := x + 1; end;";
        java.util.List<Lexer.Token> tokens = Lexer.tokenize(code);
        validate(tokens);
    }
}
