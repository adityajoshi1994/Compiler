package Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GeneralizeedParser {
	String input;
	BufferedReader bReader;
	ArrayList<String> stack = new ArrayList<String>();
	FileInputStream file;
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	Map<String, Float> symbolTable = Collections.synchronizedMap(new HashMap<String, Float>());
	public ArrayList<String> LexCommunicator = new ArrayList<String>();
	
	public GeneralizeedParser() throws IOException {
		// TODO Auto-generated constructor stub
		stack.add("0");
		this.accessParseTableFile();
	}
	
	void accessParseTableFile() throws IOException{
		//hard code the parsing table for "if"
		file = new FileInputStream(new File("E:\\D drive desktop\\vit\\New folder\\Compiler\\src\\ParseTable2.xlsx"));
		workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        sheet = workbook.getSheetAt(0);

	}
	
	void openInputLexFile() throws IOException{
		bReader = new BufferedReader(new FileReader("E:\\D drive desktop\\vit\\New folder\\Compiler\\src\\outputFile"));
	}
	
	String accessParseTable(String cellId){
		CellReference cellReference = new CellReference(cellId);
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		Double double1;
		Integer i;
		
		try {
			switch (cell.getCellType()) 
	        {
	            case Cell.CELL_TYPE_NUMERIC:
	                double1 = cell.getNumericCellValue();
	                i = double1.intValue();
	                return i.toString();
	            case Cell.CELL_TYPE_STRING:
	                return cell.getStringCellValue();
	            default:
	            	return null;
	           
	        }
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	String getParserColumn(String symbol){
		switch(symbol){
			case "id":
				return "B";
			case "=":
				return "J";
			case "if":
				return "C";
			case "(":
				return "D";
			case ")":
				return "E";
			case "{":
				return "F";
			case "-":
				return "I";
			case "}":
				return "G";
			case "+":
				return "H";
			case ";":
				return "M";
			case "*":
				return "K";
			case "$":
				return "P";
			case "/":
				return "L";
			case "relop":
				return "N";
			case "num":
				return "O";
			case "S":
				return "Q";
			case "A":
				return "R";
			case "I":
				return "S";
			case "E":
				return "T";
			case "T":
				return "U";
			case "F":
				return "V";
			case "C":
				return "W";
			default:
				return null;
		}
	}
	
	/*void parseString() throws IOException{
		int i;
		String line = "";
		boolean read = true;
		stack.add("0");
		//stack.add("S");
		Integer currentState,tempCol;
		String state,symbol,referenceString;
		String operation;
		String[] production,temp;
		
		while (!stack.isEmpty()) {
			currentState = Integer.parseInt(stack.get(stack.size() - 1));
			if(read)
				line = bReader.readLine();
			symbol = getSymbol(line);
			
			currentState = currentState + 2;
			referenceString = getParserColumn(symbol) +  currentState.toString();
			currentState = currentState - 2;
			operation = accessParseTable(referenceString);			
			if(operation == null){
				System.out.println("String not accepted");
				break;
			}
				
			if(operation.charAt(0) == 'S'){
				//state = state in parseTable and Read the next character in string
				state = (String) operation.substring(1);
				read = true;
				//Push it on stack
				pushOnStack(symbol);
				pushOnStack(state);
			}
			
			else if(operation.charAt(0) == 'R') {
				//Pop of twice the number of character in parseTable[state][symbol]
				production = returnProductionRule(operation).split("->");
				temp = production[1].split(" ");
				popFromStack(2 * temp.length);
				//Substitute with symbol on the left of Production in parseTable[state][symbol]
				pushOnStack(production[0]);
				//Calculate the next State
				//state = getNextState();
				tempCol = Integer.parseInt(stack.get(stack.size() - 2)) + 2;
				referenceString = getParserColumn(stack.get(stack.size() - 1)) +  tempCol.toString();
				state = accessParseTable(referenceString);
				pushOnStack(state);
				read = false;
			}
			else if (operation.equals("accept")) {
				System.out.println("String accepted");
				break;
			}
			else {
				System.out.println("String not accepted");
				break;
			}
			System.out.println(stack);
		}
		
	}*/
	
	public int updateStack(){
		int i;
		String line = "";
		boolean read = true;
		//stack.add("0");
		//stack.add("S");
		Integer currentState,tempCol;
		String state,symbol,referenceString;
		String operation;
		String[] production,temp;
		currentState = Integer.parseInt(stack.get(stack.size() - 1));
		/*if(read)
			line = bReader.readLine();
		*/
		line = LexCommunicator.get(0);				
		symbol = getSymbol(line);
		
		currentState = currentState + 2;
		referenceString = getParserColumn(symbol) +  currentState.toString();
		currentState = currentState - 2;
		operation = accessParseTable(referenceString);
		
		if(operation == null){
			System.out.println("String not accepted");
			return 0;
		}
			
		if(operation.charAt(0) == 'S'){
			//state = state in parseTable and Read the next character in string
			state = (String) operation.substring(1);
			read = true;
			//Push it on stack
			pushOnStack(symbol);
			pushOnStack(state);
			LexCommunicator.remove(0);
		}
		
		else if(operation.charAt(0) == 'R') {
			//Pop of twice the number of character in parseTable[state][symbol]
			production = returnProductionRule(operation).split("->|~");
			temp = production[1].split(" ");
			popFromStack(2 * temp.length);
			//Substitute with symbol on the left of Production in parseTable[state][symbol]
			pushOnStack(production[0]);
			//Calculate the next State
			//state = getNextState();
			tempCol = Integer.parseInt(stack.get(stack.size() - 2)) + 2;
			referenceString = getParserColumn(stack.get(stack.size() - 1)) +  tempCol.toString();
			state = accessParseTable(referenceString);
			pushOnStack(state);
			read = false;
		}
		else if (operation.equals("accept")) {
			System.out.println("String accepted");
			return 0;
		}
		else {
			System.out.println(stack);
			System.out.println("String not accepted");
			return 0;
		}
		System.out.println(stack);
		return 1;
	
	}
	
	String getSymbol(String line){
		String symbol;
		if(line.split(":")[0].equals("Identifier"))
			symbol = "id";
		else if (line.split(":")[0].equals("Relational Operator")) {
			symbol = "relop";
		}
		else if (line.split(":")[0].equals("Integer")) {
			symbol = "num";
		}
		else if (line.equals(";") || line.equals("$")) {
			symbol = line;
		}
		else {
			symbol = line.split(":")[1];
		}
		return symbol;
	}
	
	static String returnProductionRule(String reduce){
		switch (reduce) {
		case "R1":
		reduce = "S->A S";
		break;
		case "R2":
		reduce = "S->I S";
		break;
		case "R3":
		reduce = "S->A";
		break;
		case "R4":
		reduce = "S->I";
		break;
		case "R5":
		reduce = "A->id = E ;";
		break;
		case "R6":
		reduce = "I->if ( C ) { S }";
		break;
		case "R7":
		reduce = "C->id relop id";
		break;
		case "R8":
		reduce = "E->E + T~+";
		break;
		case "R9":
		reduce = "E->E - T~-";
		break;
		case "R10":
		reduce = "E->T";
		break;
		case "R11":
		reduce = "T->T * F~*";
		break;
		case "R12":
		reduce = "T->T / F~/";
		break;
		case "R13":
		reduce = "T->F~assign";
		break;
		case "R14":
		reduce = "F->( E )";
		break;
		case "R15":
		reduce = "F->id";
		break;
		case "R16":
			reduce = "F->num~assign";
			break;
		default:
		break;
		}
		return reduce;
		}

	
	void pushOnStack(String s){
		stack.add(s);
	}
	
	void popFromStack(int count){
		for(int i = 0;i < count;i++){
			stack.remove(stack.size() - 1);
		}
	}
	
	/*public static void main(String[] args) throws IOException {
		GeneralizeedParser gp = new GeneralizeedParser();
		gp.openInputLexFile();
		gp.accessParseTableFile();
		gp.parseString();
	}*/
}
