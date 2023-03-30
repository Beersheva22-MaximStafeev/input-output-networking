package telran.view;

//import java.io.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class StandardInputOutput implements InputOutput {
	Scanner scanner = new Scanner(System.in);

	@Override
	public String readString(String prompt) {
		writeLine(prompt);
		return scanner.nextLine();
	}

	@Override
	public void writeString(Object obj) {
		System.out.print(obj.toString());

	}
	
//	public static void main(String[] args) {
//		StandartInputOutput std = new StandartInputOutput();
////		double d = std.readNumber("Enter duoble", "Error entering double"); 
////		std.writeString("readed " + d);
//
////		 LocalDate dat = std.readDateNew("Enter date", "Error entering date", "yyyy-MM-dd", LocalDate.parse("2022-01-01"), LocalDate.parse("2023-01-01")); 
////		std.writeString("readed " + dat);
//		
//		String emailRegex = "[a-zA-Z\\d]+(\\\\.[a-zA-Z\\\\d]+)+@[a-zA-Z\\d]+(\\.[a-zA-Z\\d]+)+";
//		String mail = std.readStringPredicate("Enter email", "Wrong email", el -> el.matches(emailRegex));
//		std.writeLine("readed: " + mail);
//	}

}