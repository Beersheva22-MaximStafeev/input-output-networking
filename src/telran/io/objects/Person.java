package telran.io.objects;

import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;
	public String name;
	public Person person;
	
	public Person(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "id: " + id + " name: " + name;
	}

}
