package LexicalAnalyser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Analyser {
	
	boolean exp;
	String inputLine;
	String token;
	String tokenValue;
	ArrayList<String> tokeStrings;
	int state,lineNo;
	ArrayList<String> keywords,indentifiers,operators,symbols,numbers;
	BufferedReader bReader;
	PrintWriter writer;
	ArrayList<String> listOfKeywords = new ArrayList<>();
	ArrayList<String> globallistOfOperators = new ArrayList<>();
	ArrayList<String> listOfArithmeticOperators = new ArrayList<>();
	ArrayList<String> listOfRelationalOperators = new ArrayList<>();
	ArrayList<String> listOfLogicalOperators = new ArrayList<>();
	ArrayList<String> listOfBitwiseOperators = new ArrayList<>();
	ArrayList<String> listOfAssignmentOperators = new ArrayList<>();
	ArrayList<String> listOfParentheses = new ArrayList<>();
	
	ArrayList<String> listOfOpeningBrackets = new ArrayList<>();
	ArrayList<String> listOfClosingBrackets = new ArrayList<>();
	
	ArrayList<String> listOfPreprocessorDirectives = new ArrayList<>();
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
		InitPreprecessorDirectives();
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
	
	void openOutputFile(){
		try {
			writer = new PrintWriter("E:\\D drive desktop\\vit\\New folder\\Compiler\\src\\outputFile");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Output file not created");
		}
	}
		
	void mainAnalyzer() throws IOException{
		int i = 0;
		
		while ((inputLine = bReader.readLine()) != null) {
			removeMultipleWhitespaces(inputLine);
			i = 0;
			tokenValue = "";
			exp = false;
			while (i < inputLine.length()) {
				switch (state) {
				case 0:
					if(checkIfDelimiter(i)){
						tokenValue = "";
						state = 0;
						break;
					}											
					else if(Character.isLetter(inputLine.charAt(i)))
						state = 1;
					else if(inputLine.charAt(i) == '_')
						state = 2;
					
					else if (Character.isDigit(inputLine.charAt(i))) {
						state = 6;
					}
					
					else if (inputLine.charAt(i) == '/') {
						state = 11;
					}
					else if (globallistOfOperators.contains(Character.toString(inputLine.charAt(i)))) {
						state = 10;
						
					}
					
					else if (listOfOpeningBrackets.contains(Character.toString(inputLine.charAt(i)))) {
						tokenValue = tokenValue + inputLine.charAt(i);
						addToken("Opening Bracket", tokenValue);
						break;
					}
					
					else if (listOfClosingBrackets.contains(Character.toString(inputLine.charAt(i)))) {
						tokenValue = tokenValue + inputLine.charAt(i);
						addToken("Closing Bracket", tokenValue);
						break;
					}
					
					else if (inputLine.charAt(i) == '#') {
						state = 15;
						//break;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
				case 1:
					if(Character.isLetter(inputLine.charAt(i)))
						state = 1;
					else if (inputLine.charAt(i) == '.') {
						state = 16;
					}
					else if(Character.isDigit(inputLine.charAt(i)) || inputLine.charAt(i) == '_')
						state = 2;
					
					else {						
						if(listOfKeywords.contains(tokenValue))
							addToken("Keyword", tokenValue);
						else {
							addToken("Identifier", tokenValue);
						}
						//tokenValue = tokenValue + inputLine.charAt(i);						
						i--;
						//state = 0;
						break;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
					
				case 2:
					if(Character.isLetter(inputLine.charAt(i)) || Character.isDigit(inputLine.charAt(i))){
						state = 2;
					}
					
					else {
						addToken("Identifier", tokenValue);
						i--;
						break;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
					
				case 4:
					if(Character.isDigit(inputLine.charAt(i)))
						state = 4;
					else if(inputLine.charAt(i) == '.')
						state = 7;
					else if (inputLine.charAt(i) == 'E') {
						addToken("Number", tokenValue);
						addToken("Exponential", "E");
						break;
					}
					else {
						if(exp)
							addToken("Exponential", tokenValue);
						else {
							addToken("Integer", tokenValue);
						}
						
						i--;
						break;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
					
				case 5:
					if(inputLine.charAt(i) == '-')
						state = 9;
					else if (Character.isDigit(inputLine.charAt(i))) {
						state = 6;
					}
					else {
						state = 99;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
					
				case 6:
					if(Character.isDigit(inputLine.charAt(i)))
						state = 6;
					else if (inputLine.charAt(i) == '.') {
						state = 7;
					}
					else if (inputLine.charAt(i) == 'E') {
						addToken("Integer", tokenValue);
						addToken("Exponential", "E");
						break;
					}
					else {
						
							addToken("Integer", tokenValue);
							i--;
							break;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
				case 7:
					if(Character.isDigit(inputLine.charAt(i)))
						state = 8;
					else {
						state = 99;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
				
				case 8:
					if(Character.isDigit(inputLine.charAt(i)))
						state = 8;
					else if (inputLine.charAt(i) == 'E') {
						addToken("Float", tokenValue);
						addToken("Exponential", "E");
						break;
					}
					else {
						
							addToken("Float", tokenValue);						
							i--;
							break;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
					
				case 9:
					if(Character.isDigit(inputLine.charAt(i)))
						state = 4;
					else {
						state = 99;
					}
					tokenValue = tokenValue + inputLine.charAt(i);
					break;
					
				case 10:
					if(globallistOfOperators.contains(Character.toString(inputLine.charAt(i)))){
						tokenValue = tokenValue + inputLine.charAt(i);
						
						
					}
					else {
						i--;
					}
					
					if(listOfArithmeticOperators.contains(tokenValue)){
						addToken("Arithmetic Operator", tokenValue);
					}
					
					else if(listOfAssignmentOperators.contains(tokenValue)){
						addToken("Assignment Operator", tokenValue);
					}
					
					else if(listOfBitwiseOperators.contains(tokenValue)){
						addToken("Bitwise Operator", tokenValue);
					}
					
					else if(listOfLogicalOperators.contains(tokenValue)){
						addToken("Logical Operator", tokenValue);
					}
					
					else if(listOfRelationalOperators.contains(tokenValue)){
						addToken("Relational Operator", tokenValue);
					}
					else {
						addToken("Unknown Operator", tokenValue);
					}
					
					break;
					
				case 11:
					if(inputLine.charAt(i) == '*'){
						state = 12;
					}
					
					else if (inputLine.charAt(i) == '/') {
						state = 13;
					}
					
					else {
						addToken("Arithmetic Operator", tokenValue);
					}
					break;
				case 12:
					if(inputLine.charAt(i) == '*'){
						state = 14;
							
						}
					else {
						state = 12;
					}
						
					break;
					
				case 13:
					
					if(i == inputLine.length() - 1){

						state = 0;
					}
						
					else {
						state = 13;
					}
					break;
					
				case 14:
					if(inputLine.charAt(i) == '/')
						state = 0;
					else {
						state = 12;
					}
						
					break;
				case 15:
					if(checkIfDelimiter(i)){
						//System.out.println(tokenValue);
						if(listOfPreprocessorDirectives.contains(tokenValue)){
							addToken("Preprocessor Directive", tokenValue);
						}
					}
					else {
						tokenValue = tokenValue + inputLine.charAt(i);
					}
					
					break;
					
				case 16:
					if(inputLine.charAt(i) == 'h'){
						tokenValue = tokenValue + inputLine.charAt(i);
						addToken("Header File", tokenValue);
					}
					else {
						state = 99;
					}
					break;
				default:
					addToken("Unidentified token: ", tokenValue);
					break;
				}
				i++;				
			}
			//System.out.println();
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
			
			System.out.println(token + ":" + tokenValue);
			String string = token + ":" + tokenValue;
			writer.write(string);
			writer.write("\n");
			this.tokenValue = "";
			this.state = 0;
			this.exp = false;
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
			listOfArithmeticOperators.add("-=");
			listOfArithmeticOperators.add("+=");
			listOfArithmeticOperators.add("*=");
			listOfArithmeticOperators.add("/=");
			
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
			listOfOpeningBrackets.add("(");
			listOfClosingBrackets.add(")");
			listOfOpeningBrackets.add("{");
			listOfClosingBrackets.add("}");
			listOfOpeningBrackets.add("[");
			listOfClosingBrackets.add("]");
		}
		
		void InitPreprecessorDirectives(){
			listOfPreprocessorDirectives.add("#include");
			listOfPreprocessorDirectives.add("#define");
			listOfPreprocessorDirectives.add("#undef");
			listOfPreprocessorDirectives.add("#ifdef");
			listOfPreprocessorDirectives.add("#ifndef");
			listOfPreprocessorDirectives.add("#if");
			listOfPreprocessorDirectives.add("#else");
			listOfPreprocessorDirectives.add("#elif");
			listOfPreprocessorDirectives.add("#endif");
			listOfPreprocessorDirectives.add("#error");
			listOfPreprocessorDirectives.add("#pragma");
			
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
			analyser.openOutputFile();
			analyser.mainAnalyzer();
			analyser.writer.close();
			analyser.bReader.close();
		}
}


