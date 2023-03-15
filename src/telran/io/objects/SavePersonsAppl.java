package telran.io.objects;

import java.io.*;

public class SavePersonsAppl {

	public static void main(String[] args) throws Exception {
		Person person = new Person(123, "Vasya"); 
		person.person = person;
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("persons.data"))) {
			output.writeObject(person);
		}
	}

}
