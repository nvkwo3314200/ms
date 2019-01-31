package ms;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestSystem {
	public static void main(String[] args) throws FileNotFoundException {
		OutputStream os = new FileOutputStream("F:\\log.txt", true);
        PrintStream pw = new PrintStream(os);
		System.setOut(pw);
		System.out.println("adsfasdfa");
	}
}
