package LexicalAnalyser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Analyser {
		
	String inputLine;
	String token;
	String tokenValue;
	ArrayList<String> tokeStrings;
	int state,lineNo;
	ArrayList<String> keywords,indentifiers,operators,symbols,numbers;
	BufferedReader bReader;
	ArrayList<String> listOfKeywords = new ArrayList<>();
	ArrayList<String> globallistOfOperators = new ArrayList<>();
	ArrayList<String> listOfArithmeticOperators = new ArrayList<>();
	ArrayList<String> listOfRelationalOperators = new ArrayList<>();
	ArrayList<String> listOfLogicalOperators = new ArrayList<>();
	ArrayList<String> listOfBitwiseOperators = new ArrayList<>();
	ArrayList<String> listOfAssignmentOperators = new ArrayList<>();
	ArrayList<String> listOfParentheses = new ArrayList<>();
	//-----------------------------------------------------
	ArrayList<String> arithmeticOperators = new ArrayList<>();
	ArrayList<String> relationalOperators = new ArrayList<>();
	ArrayList<String> logicalOperators = new ArrayList<>();
	ArrayList<String> bitwiseOperators = new ArrayList<>();
	ArrayList<String> assignmentOperators = new ArrayList<>();
	ArrayList<String> parentheses = new ArrayList<>();
	
	public Analyser(){
		InitializeKeywords();
		InitGlobalListOfOperators();
		InitlistOfAssignmentOperators();
		InitArithmeticOperators();
		InitlistOfBitwiseOperators();
		InitlistOfLogicalOperators();
		InitRelationalOperators();
		InitParentheses();
	}
	
	void inputFunction(){
			
		try {
			bReader = new BufferedReader(new FileReader("E:\\D drive desktop\\vit\\New folder\\Compiler\\src\\inputFile"));
				
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found");			
			}
			
			
	}
		
	void mainAnalyser() throws IOException{
		int i;
		lineNo = 0;
		while((inputLine = bReader.readLine()) != null){
				//Call each tokenizer function on the inptLine
				//split string across tokens
			renewLists();
			lineNo++;
			i = 0;
			tokenValue = "";
			state = 0;
			while (i < inputLine.length()) {
					/*To Do
					 1. Number, Float, Exponential
					 2. Directives
					 3. Headers.
					 */
					
					
				String currentChar = ""+inputLine.charAt(i);
				switch (state) {
				
					case 0:						
						if(Character.isLetter(inputLine.charAt(i))){
							state = 1;
							tokenValue = tokenValue + currentChar;
							//System.out.println("true");
						}						
						else if(globallistOfOperators.contains(currentChar)){
							state = 4;
							tokenValue = "" + currentChar;
						}
						else if(listOfParentheses.contains(currentChar)){
							addToken("Parentheses", currentChar);
						}
						else if(Character.isDigit(inputLine.charAt(i))){
							state = 3;
							tokenValue = tokenValue + currentChar;	
						}
						break;
						// Case 1 Identifies keywords and non-Numerc-identifier
					case 1:
						if(Character.isLetter(inputLine.charAt(i))){
							state = 1;
							tokenValue = tokenValue + inputLine.charAt(i);
						}
						else if(checkIfDelimiter(i) ){
							if(listOfKeywords.contains(tokenValue)){
								addToken("Keywords",tokenValue);								
							}
							else {
								addToken("Identifier",tokenValue);
							}
						}
						else if(listOfParentheses.contains(currentChar)){
							if(listOfKeywords.contains(tokenValue)){
								addToken("Keywords",tokenValue);
								
							}
							else {
								addToken("Identifier",tokenValue);
							}
							parentheses.add(currentChar);
						}
						else if(globallistOfOperators.contains(currentChar)){
							addToken("Identifier",tokenValue);
							tokenValue = ""+inputLine.charAt(i);
							state=4;							
						}
						else {
							state = 2;
							tokenValue = tokenValue + inputLine.charAt(i);
						}
						break;
					//Case 2 identifies Identifiers with nos and special characters.	
					case 2:
						if(Character.isJavaIdentifierPart(inputLine.charAt(i))){
							state = 2;
							tokenValue = tokenValue + inputLine.charAt(i);
						}
						else if(checkIfDelimiter(i)){
							addToken("Identifier",tokenValue);
						}
						else if(listOfParentheses.contains(currentChar)){
							if(listOfKeywords.contains(tokenValue)){
								addToken("Keywords",tokenValue);							
							}
							else {
								addToken("Identifier",tokenValue);
							}
							parentheses.add(currentChar);
						}
						else if(globallistOfOperators.contains(currentChar)){
							addToken("Identifier",tokenValue);
							tokenValue = ""+inputLine.charAt(i);
							state=4;							
						}
						
						break;
					case 3:
						if(Character.isDigit(inputLine.charAt(i)) || inputLine.charAt(i) == '.'){
							state = 3;
							tokenValue = tokenValue + inputLine.charAt(i);
						
						}
						
						if(checkIfDelimiter(i))
							addToken("Number", tokenValue);
						break;
					//Case 4 identifies single char & double char Operators
					case 4:
						if(!checkIfDelimiter(i)){
							tokenValue = tokenValue + inputLine.charAt(i);
							//System.out.println("Two char operator : "+tokenValue);
						}
						
						addToken("Operator", tokenValue);
					default:
						break;
					}
					i++;
				}
				printToken();
			}
		}
		
		boolean checkIfDelimiter(int i){
			if(inputLine.charAt(i) == ' ' || inputLine.charAt(i) == '\t' || inputLine.charAt(i) == '\n' || inputLine.charAt(i) == ';'
					|| inputLine.charAt(i) == ',')
				return true;
			else {
				return false;
			}
		}
		
		void renewLists(){
			keywords = new ArrayList<String>();
			indentifiers = new ArrayList<String>();
			operators = new ArrayList<String>();
			symbols = new ArrayList<String>();
			numbers = new ArrayList<String>();
			arithmeticOperators = new ArrayList<>();
			relationalOperators = new ArrayList<>();
			logicalOperators = new ArrayList<>();
			bitwiseOperators = new ArrayList<>();
			assignmentOperators = new ArrayList<>();
			parentheses = new ArrayList<>();
		}
		
		void addToken(String token, String tokenValue){
			/*switch (token) {
			case "Keywords":
				keywords.add(tokenValue);
				break;
			case "Identifier":
				if(!indentifiers.contains(tokenValue)){
					indentifiers.add(tokenValue);
				}				
				break;
			case "Number":
				numbers.add(tokenValue);
				break;
			case "Operator":
				
				if(listOfArithmeticOperators.contains(tokenValue)){
					arithmeticOperators.add(tokenValue);
				}
				
				else if(listOfAssignmentOperators.contains(tokenValue)){
					assignmentOperators.add(tokenValue);
				}
				
				else if(listOfBitwiseOperators.contains(tokenValue)){
					bitwiseOperators.add(tokenValue);
				}
				
				else if(listOfLogicalOperators.contains(tokenValue)){
					logicalOperators.add(tokenValue);
				}
				
				else if(listOfRelationalOperators.contains(tokenValue)){
					relationalOperators.add(tokenValue);
				}
				operators.add(tokenValue);
				break;
			case "Parentheses":
				parentheses.add(tokenValue);
			default:
				break;
			}
			*/
			System.out.println(token + ": " + tokenValue);
			this.tokenValue = "";
			this.state = 0;
		}
		
		void printToken(){
			System.out.println();
			//System.out.println("----------------Line number "+lineNo+" ----------------");
			/*
			if(!keywords.isEmpty()){
				System.out.println("Keywords: " + keywords);
			}
			if(!indentifiers.isEmpty()){
				System.out.println("Identifiers: " + indentifiers);
			}
			if(!numbers.isEmpty()){
				System.out.println("Numbers: " + numbers);
			}
			if(!parentheses.isEmpty()){
				System.out.println("Parentheses: "+parentheses);
			}					
			if (!arithmeticOperators.isEmpty()) {
				System.out.println("Arithmetic Operators : "+arithmeticOperators);
			}
			if (!assignmentOperators.isEmpty()) {
				System.out.println("Assignment Operators : "+assignmentOperators);
			}
			if (!bitwiseOperators.isEmpty()) {
				System.out.println("Bitwise Operators : "+bitwiseOperators);
			}
			if (!logicalOperators.isEmpty()) {
				System.out.println("Logical Operators : "+logicalOperators);
			}
			if (!relationalOperators.isEmpty()) {
				System.out.println("Relational Operators : "+relationalOperators);
			}*/
		//	System.out.println(lineNo + ": " + "keywords: " + keywords);
			
		}
		
		void InitializeKeywords(){
			 listOfKeywords.add("auto");
			 listOfKeywords.add("break");
			 listOfKeywords.add("case");
			 listOfKeywords.add("char");
			 listOfKeywords.add("const");
			 listOfKeywords.add("continue");
			 listOfKeywords.add("default");
			 listOfKeywords.add("do");
			 listOfKeywords.add("double");
			 listOfKeywords.add("else");
			 listOfKeywords.add("enum");
			 listOfKeywords.add("extern");
			 listOfKeywords.add("float");
			 listOfKeywords.add("for");
			 listOfKeywords.add("goto");
			 listOfKeywords.add("if");
			 listOfKeywords.add("int");
			 listOfKeywords.add("long");
			 listOfKeywords.add("return");
			 listOfKeywords.add("short");
			 listOfKeywords.add("sizeof");
			 listOfKeywords.add("static");
			 listOfKeywords.add("struct");
			 listOfKeywords.add("switch");
			 listOfKeywords.add("typedef");
			 listOfKeywords.add("void");
			 listOfKeywords.add("while");
		}
		
		void InitArithmeticOperators(){
			listOfArithmeticOperators.add("+");
			listOfArithmeticOperators.add("-");
			listOfArithmeticOperators.add("*");
			listOfArithmeticOperators.add("/");
			listOfArithmeticOperators.add("%");
			listOfArithmeticOperators.add("++");
			listOfArithmeticOperators.add("--");
			
		}
		
		void InitRelationalOperators(){
			listOfRelationalOperators.add("==");
			listOfRelationalOperators.add("!=");
			listOfRelationalOperators.add(">");
			listOfRelationalOperators.add("<");
			listOfRelationalOperators.add(">=");
			listOfRelationalOperators.add("<=");
		}
		
		void InitlistOfLogicalOperators(){
			listOfLogicalOperators.add("&&");
			listOfLogicalOperators.add("||");
			listOfLogicalOperators.add("!");
		}
		
		void InitlistOfBitwiseOperators(){
			listOfBitwiseOperators.add("&");
			listOfBitwiseOperators.add("|");
			listOfBitwiseOperators.add("^");
			listOfBitwiseOperators.add("~");
			listOfBitwiseOperators.add("<<");
			listOfBitwiseOperators.add(">>");
			
		}
		
		void InitlistOfAssignmentOperators(){
			listOfAssignmentOperators.add("=");
			listOfAssignmentOperators.add("+=");
			listOfAssignmentOperators.add("-=");
			listOfAssignmentOperators.add("*=");
			listOfAssignmentOperators.add("/=");
			listOfAssignmentOperators.add("%=");
			listOfAssignmentOperators.add("<<=");
			listOfAssignmentOperators.add(">>=");
			listOfAssignmentOperators.add("&=");
			listOfAssignmentOperators.add("^=");
			listOfAssignmentOperators.add("|=");
			
		}
		
		void InitGlobalListOfOperators(){
			globallistOfOperators.add("=");
			globallistOfOperators.add("+");
			globallistOfOperators.add("-");
			globallistOfOperators.add("*");
			globallistOfOperators.add("/");
			globallistOfOperators.add("%");
			globallistOfOperators.add("<");
			globallistOfOperators.add(">");
			globallistOfOperators.add("&");
			globallistOfOperators.add("^");
			globallistOfOperators.add("|");
			globallistOfOperators.add("~");
			globallistOfOperators.add("!");
			
		}
		
		void InitParentheses(){
			listOfParentheses.add("(");
			listOfParentheses.add(")");
			listOfParentheses.add("{");
			listOfParentheses.add("}");
			listOfParentheses.add("[");
			listOfParentheses.add("]");
		}
		
		String removeMultipleWhitespaces(String inputlineWithSpaces){
			String str = inputlineWithSpaces;
		    StringTokenizer token = new StringTokenizer(str, " ");
	        StringBuffer inputlineWithoutSpaces = new StringBuffer();
	        while(token.hasMoreElements()){
		       	inputlineWithoutSpaces.append(token.nextElement()).append(" ");
		     }
			 return inputlineWithoutSpaces.toString();
		 }
		
		public static void main(String[] args) throws IOException {
			Analyser analyser = new Analyser();
			analyser.inputFunction();
			analyser.mainAnalyser();
		}
}


