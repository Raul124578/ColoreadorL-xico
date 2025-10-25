import java.util.*;
import java.util.regex.*;

public class Lexer {
    
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "if", "then", "else", "for", "while", "do", "begin", "end", "var", "procedure", "function",
        "return", "class", "true", "false", "nil", "and", "or", "not"
    ));

    private static final String[][] TOKEN_SPECS = {
        {"COMMENT", "\\{[^}]*\\}|\\(\\*[\\s\\S]*?\\*\\)|//.*"},
        {"STRING", "\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'"},
        {"NUMBER", "\\b\\d+\\.\\d+\\b|\\b\\d+\\b"},
        {"EQ", "==|="},
        {"NEQ", "<>|!="},
        {"GTE", ">="},
        {"LTE", "<="},
        {"GT", ">"},
        {"LT", "<"},
        {"ASSIGN", ":="},
        {"OP", "\\+|\\-|\\*|/|\\^"},
        {"LPAREN", "\\("},
        {"RPAREN", "\\)"},
        {"LBRACK", "\\["},
        {"RBRACK", "\\]"},
        {"LBRACE", "\\{"},
        {"RBRACE", "\\}"},
        {"SEMI", ";"},
        {"COMMA", ","},
        {"ID", "\\b[A-Za-z_][A-Za-z0-9_]*\\b"},
        {"NEWLINE", "\\n"},
        {"SKIP", "[ \\t\\r]+"},
        {"MISMATCH", "."}
    };

    private static final Pattern MASTER_PATTERN;

    static {
        StringBuilder patternBuilder = new StringBuilder();
        for (String[] spec : TOKEN_SPECS) {
            if (patternBuilder.length() > 0) patternBuilder.append("|");
            patternBuilder.append(String.format("(?<%s>%s)", spec[0], spec[1]));
        }
        MASTER_PATTERN = Pattern.compile(patternBuilder.toString(), Pattern.MULTILINE);
    }

    public static List<Token> tokenize(String code) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = MASTER_PATTERN.matcher(code);

        int lineNum = 1;
        int lineStart = 0;

        while (matcher.find()) {
            String kind = null;
            String value = matcher.group();

            // Identificar qué grupo fue el que hizo match
            for (String[] spec : TOKEN_SPECS) {
                if (matcher.group(spec[0]) != null) {
                    kind = spec[0];
                    break;
                }
            }

            int column = matcher.start() - lineStart + 1;

            if ("NEWLINE".equals(kind)) {
                lineStart = matcher.end();
                lineNum++;
            } else if ("SKIP".equals(kind)) {
                continue;
            } else if ("COMMENT".equals(kind)) {
                tokens.add(new Token("COMMENT", value, lineNum, column));
            } else if ("ID".equals(kind)) {
                if (KEYWORDS.contains(value.toLowerCase())) {
                    tokens.add(new Token("KEYWORD", value, lineNum, column));
                } else {
                    tokens.add(new Token("ID", value, lineNum, column));
                }
            } else if ("NUMBER".equals(kind)) {
                tokens.add(new Token("NUMBER", value, lineNum, column));
            } else if ("STRING".equals(kind)) {
                if (value.length() >= 2 && 
                   ((value.startsWith("\"") && value.endsWith("\"")) ||
                    (value.startsWith("'") && value.endsWith("'")))) {
                    tokens.add(new Token("STRING", value, lineNum, column));
                } else {
                    tokens.add(new Token("ERROR", value, lineNum, column));
                    return tokens;
                }
            } else if (Arrays.asList("LPAREN","RPAREN","LBRACK","RBRACK","LBRACE","RBRACE").contains(kind)) {
                tokens.add(new Token("GROUP", value, lineNum, column));
            } else if (Arrays.asList("EQ","NEQ","GTE","LTE","GT","LT","ASSIGN","OP").contains(kind)) {
                tokens.add(new Token("OP", value, lineNum, column));
            } else if (Arrays.asList("SEMI","COMMA").contains(kind)) {
                tokens.add(new Token("SEP", value, lineNum, column));
            } else if ("MISMATCH".equals(kind)) {
                tokens.add(new Token("ERROR", value, lineNum, column));
                return tokens;
            }
        }

        return tokens;
    }

    public static void main(String[] args) {
        String code = "var x := 10; if x > 5 then begin // comentario\nx := x + 1; end;";
        List<Token> tokens = tokenize(code);
        for (Token t : tokens) {
            System.out.println(t);
        }
    }

    static class Token {
        String type;
        String value;
        int line;
        int column;

        public Token(String type, String value, int line, int column) {
            this.type = type;
            this.value = value;
            this.line = line;
            this.column = column;
        }

        @Override
        public String toString() {
            return String.format("(%s, \"%s\", line=%d, col=%d)", type, value, line, column);
        }
    }
}
