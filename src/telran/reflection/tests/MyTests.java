package telran.reflection.tests;

public class MyTests {
	@SuppressWarnings("unused")
	private void test1() {
		int a = 1/0;
	}
	
	void abc() {
		System.out.println("ABC");
	}
	
	void testInner(String str) {
		System.out.println(str);
	}
	
	void test2() {
		
	}
	
	@SuppressWarnings("unused")
	private void test3() {
		throw new RuntimeException("bad test");
	}
}
