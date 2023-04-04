package telran.sportsmen;

public class Runner implements Sportsman {
	
	int distance;
	
	@Override
	public void action() {
		System.out.printf("runs distance %s\n", distance != 0 ? distance + " km" : "");
	}
	
	public Runner() {
		
	}
	
	public Runner(String distance) {
		this.distance = Integer.parseInt(distance);
	}

}
