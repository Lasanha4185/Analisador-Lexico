package lexical;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import util.TokenType;
import java.util.HashMap;
import java.util.Map;


public class Scanner {
	
	private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("int",   TokenType.RW_INT);
        reservedWords.put("float", TokenType.RW_FLOAT);
        reservedWords.put("print", TokenType.RW_PRINT);
        reservedWords.put("if",    TokenType.RW_IF);
        reservedWords.put("else",  TokenType.RW_ELSE);
    }
    
	private int state;
	private char[] sourceCode;
	private int pos;
	
	private int line = 1;
    private int column = 1;
	
	public Scanner(String filename) {
		try {
			String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			sourceCode = content.toCharArray();
			pos = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Token nextToken() {
		char currentChar;
		String content = "";
		state = 0;
		
		while (true) {
			if(isEoF()) {
				if(!content.isEmpty()) {
					switch(state){
					case 1: 
						if(reservedWords.containsKey(content)) {
							return new Token(reservedWords.get(content), content);
						} else {
							return new Token(TokenType.IDENTIFIER, content);
						}
					case 2: 
					case 5:
						return new Token(TokenType.NUMBER, content);
					case 3: 
						char firstChar = content.charAt(0);
						if(firstChar == '=') {
							return new Token(TokenType.ASSIGNMENT, content);
						} else {
							return new Token(TokenType.REL_OPERATOR, content);
						}
					case 4:
						throw new RuntimeException("Lexical Error (line " + line + ", col " + column + "): Incomplete number at end of file: '" + content + "'");
					case 8:
					case 9:
						throw new RuntimeException("Lexical Error (line " + line + ", col " + column + "): Unterminated multi-line comment at end of file.");
					}
					
				}
				return null;
			}
			currentChar = nextChar();
			
			switch(state) {
			case 0:
				if(isLetter(currentChar)) {
					content+=currentChar;
					state = 1;
				} else if(isDigit(currentChar)) {
					content += currentChar;
					state = 2;
				} else if(currentChar == '.') {
					content += currentChar;
					state = 4;
				} else if(isWhitespace(currentChar)) {
					state = 0;
				} else if(isRelOperator(currentChar)) {
					content += currentChar;
					state = 3;
				} else if(currentChar == '#') {
					state = 6;
				} else if(currentChar == '/') {
					state = 7;
				} else if(isMathOperator(currentChar)) {
					content += currentChar;
					return new Token(TokenType.MATH_OPERATOR, content);
				} else if(currentChar == '(') {
					content += currentChar;
					return new Token(TokenType.LEFT_PAREN, content);
				} else if(currentChar == ')') {
					content += currentChar;
					return new Token(TokenType.RIGHT_PAREN, content);
				} else {
					throw new RuntimeException("Lexical Error (line " + line + ", col " + column + "): Unrecognized symbol '" + currentChar + "'");
				}
				break;
			case 1:
				if(isLetter(currentChar) || isDigit(currentChar)) {
					content+=currentChar;
				} else {
					back();
					if(reservedWords.containsKey(content)) {
						return new Token(reservedWords.get(content), content);
					} else {
						return new Token(TokenType.IDENTIFIER, content);
					}
				}
				break;
			case 2:
				if(isDigit(currentChar)) {
					content += currentChar;
					state = 2;
				} else if(currentChar == '.') {
					content += currentChar;
					state = 4;
				} else {
					back();
					return new Token(TokenType.NUMBER, content);
				}
				break;
			case 3:
				char firstChar = content.charAt(0);
				if(currentChar == '=') {
					content += currentChar;
				} else {
					back();
				}
				if(firstChar == '<' || firstChar == '>') {
					return new Token(TokenType.REL_OPERATOR, content);
				} else if(firstChar == '=') {
					return new Token(TokenType.ASSIGNMENT, content);
				} else {
					throw new RuntimeException("Lexical Error (line " + line + ", col " + column + "): Malformed operator '" + content + "'");
				}
			case 4:
				if(isDigit(currentChar)) {
					content += currentChar;
					state = 5;
				} else {
					throw new RuntimeException("Lexical Error (line " + line + ", col " + column + "): Malformed number '" + content + "'. Dot must be followed by a digit.");
				}
				break;
			case 5:
				if (isDigit(currentChar)) {
	                content += currentChar; 
	            } else {
	                back();
	                return new Token(TokenType.NUMBER, content);
	            }
	            break;
			case 6:
				if(currentChar == '\n' || currentChar == '\r') {
					state = 0;
				}
				break;
			case 7:
				if(currentChar == '*') {
					state = 8;
				} else {
					back();
	                return new Token(TokenType.MATH_OPERATOR, "/");
				}
				break;
			case 8:
				if(currentChar == '*') {
					state = 9;
				}
				break;
			case 9:
				if(currentChar == '/') {
					state = 0;
				} else if(currentChar != '*') {
					state = 8;
				}
				break;
			}
		}
	}
	
	private boolean isLetter(char c) {
		return (c>='a' && c <= 'z') || (c>='A' && c <= 'Z');		
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isMathOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	
	private boolean isRelOperator(char c) {
		return c == '>' || c == '<' || c == '=' || c == '!';
	}
	
	private boolean isWhitespace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
	private char nextChar() {
		if(isEoF()) {
			return '\0';
		}
		char currentChar = sourceCode[pos++];
		if(currentChar == '\n') {
			line++;
			column = 1;
		} else {
			column++;
		}
		return currentChar;
	}
	
	private void back() {
		pos--;
	}
	
	private boolean isEoF() {
		return pos >= sourceCode.length;
	}

	

}
