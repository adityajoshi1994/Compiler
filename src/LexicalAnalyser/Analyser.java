package LexicalAnalyser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.sound.midi.Patch;

public class Analyser {
		String inputLine;
		String token;
		String tokenValue;
		ArrayList<String> tokeStrings;
		int state,lineNo;
		ArrayList<String> keywords,indentifiers,operators,symbols,numbers;
		BufferedReader bReader;
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
					
					
					switch (state) {
					
					case 0:
						if(Character.isLetter(inputLine.charAt(i))){
							state = 1;
							//System.out.println("true");
						}
						
						else if(Character.isDigit(inputLine.charAt(i))){
							state = 3;
							
						}
							
						tokenValue = tokenValue + inputLine.charAt(i);
						break;
						// Case 1 Identifies keywords and non-Numerc-identifier
					case 1:
						if(Character.isLetter(inputLine.charAt(i))){
							state = 1;
							tokenValue = tokenValue + inputLine.charAt(i);
						}
						
						else if(checkIfDelimiter(i)){
							if(tokenValue.equals("int") || tokenValue.equals("float") || tokenValue.equals("for") ||
									tokenValue.equals("while") || tokenValue.equals("char") || tokenValue.equals("do") || tokenValue.equals("void")
									||  tokenValue.equals("return") || tokenValue.equals("boolean") || tokenValue.equals("break") || tokenValue.equals("int") || tokenValue.equals("if")
									|| tokenValue.equals("else if")){
								addToken("Keywords",tokenValue);
								
							}
							else {
								addToken("Identifier",tokenValue);
							}
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
						
						break;
					case 3:
						if(Character.isDigit(inputLine.charAt(i)) || inputLine.charAt(i) == '.'){
							state = 3;
							tokenValue = tokenValue + inputLine.charAt(i);
						
						}
						
						if(checkIfDelimiter(i))
							addToken("Number", tokenValue);
						break;
					default:
						break;
					}
					i++;
				}
				printToken();
				
				/*
				Identifier();
				Number();
				Operator();
				KeytokenValues();
				Symbols();
				Delimitor();
				*/
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
		}
		
		void addToken(String token, String tokenValue){
			//System.out.println(lineNo + ": " + token + " " + tokenValue);
			//this.tokenValue = "";
			
			
		//	keywords = new ArrayList<String>();
			
			switch (token) {
			case "Keywords":
				keywords.add(tokenValue);
				break;
			case "Identifier":
				indentifiers.add(tokenValue);
				break;
			case "Number":
				numbers.add(tokenValue);
				break;
			default:
				break;
			}
			
			this.tokenValue = "";
			this.state = 0;
		}
		
		void printToken(){
			System.out.println(lineNo + ": " + "keywords: " + keywords);
			System.out.println(lineNo + ": " + "Identifiers: " + indentifiers);
			System.out.println(lineNo + ": " + "Numbers: " + numbers);
			System.out.println(lineNo + ": " + "Operatos: " + operators);
			System.out.println(lineNo + ": " + "Symbols: " + symbols);
		//	System.out.println(lineNo + ": " + "keywords: " + keywords);
			
		}
		
		void Identifier(){
			//Identify if any identifier present and print it on the output
			//Prepare a consolidated report of identifier
			//variable, function 
		}
		
		boolean Number(){
			//Identify if any number present and print it on the output
			//Prepare a consolidated report of number
			//Pattern pattern = Pattern.compile("-?\\d+");
			try {
				Integer.parseInt(token);
			} catch (NumberFormatException e) {
				// TODO: handle exception
				return false;
			}
			return true;
			
		}
		
		void Operator(){
			//Identify if any operator present and print it on the output
			//Prepare a consolidated report of operator
		}
		
		void Delimitor(){
			//Identify if any delimitor present and print it on the output
			//Prepare a consolidated report of delimitor
		}
		
		void Comments(){
			//Identify if any comments present and print it on the output
			//Prepare a consolidated report of comments
		}
		
		void KeytokenValues(){
			//Identify if any keytokenValues present and print it on the output
			//Prepare a consolidated report of keytokenValues
		}
		
		void Symbols(){
			//Identify if any identifier present and print it on the output
			//Prepare a consolidated report of identifier
		}
		
		public static void main(String[] args) throws IOException {
			Analyser analyser = new Analyser();
			analyser.inputFunction();
			analyser.mainAnalyser();
		}
}


