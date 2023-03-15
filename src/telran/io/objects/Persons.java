package telran.io.objects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Persons implements Serializable, Iterable<Person> {

	private static final long serialVersionUID = 1L;

	static String filePath = "persons.data";
	List<Person> persons = new ArrayList<>();

	void addPerson(Person person) {
		persons.add(person);
	}

	@Override
	public Iterator<Person> iterator() {
		return persons.iterator();
	}

	public void save() {
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filePath))) {
//			stream.writeObject(this);
			writeObject(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeObject(ObjectOutputStream stream) throws Exception {
		stream.defaultWriteObject();
	}
	
	static public Persons restore() {
		Persons res = null;
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filePath))) {
			res = (Persons)stream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			res = new Persons();
		}
		return res;
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		
	}

}
