public class ColorRules {

    // Colores ANSI para consola
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    private static final String BLUE = "\u001B[34m";
    private static final String ORANGE = "\u001B[33m"; // no hay naranja, se usa amarillo
    private static final String WHITE = "\u001B[37m";
    private static final String YELLOW = "\u001B[93m";
    private static final String GREEN = "\u001B[32m";
    private static final String PINK = "\u001B[35m";
    private static final String GRAY = "\u001B[90m";
    private static final String RED_BG = "\u001B[41m";

    public static void printColored(String tokenType, String value) {
        String color;

        switch (tokenType) {
            case "KEYWORD": color = BOLD + BLUE; break;
            case "NUMBER":  color = BOLD + ORANGE; break;
            case "GROUP":   color = WHITE; break;
            case "OP":      color = BOLD + YELLOW; break;
            case "STRING":  color = GREEN; break;
            case "ID":      color = BOLD + PINK; break;
            case "COMMENT": color = GRAY; break;
            case "ERROR":   color = BOLD + RED_BG + WHITE; break;
            default:        color = WHITE; break;
        }

        System.ou
