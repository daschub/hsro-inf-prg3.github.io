package livecoding;

import javakara.JavaKaraProgram;

import java.io.IOException;

public class StrategyExample extends JavaKaraProgram {
	public static void main(String[] args) throws IOException, InterruptedException {
		StrategyExample program = new StrategyExample();
		program.run("src/main/resources/world2.world");

		int c;
		while ((c = System.in.read()) != -1) {
			// do something with Kara.
		}
	}
}
