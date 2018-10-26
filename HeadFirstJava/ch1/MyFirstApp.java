public class MyFirstApp {

	public static void main (String[] args) {
		System.out.println("I Rule!");
		System.out.println("The World!");
	
		int[] numList = {2, 4, 6, 8};
		String num = "8";
		int z = Integer.parseInt(num);

		if (num.equals("8") & z == 8)
			System.out.println("z is " + z + ".");

		try {
			readTheFile("myFile.txt");
		} catch(FileNotFoundException ex) {
			System.out.println("File not found.");
		}
	}
}