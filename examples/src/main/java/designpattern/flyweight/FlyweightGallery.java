package designpattern.flyweight;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class FlyweightGallery {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<ExtrinsicState> items = new LinkedList<>();

		FlyweightFactory fwf = new FlyweightFactory();

		String[] paths = {"tiebomber.bmp", "tiefighter.bmp", "tieinterceptor.bmp"};

		long now = System.currentTimeMillis();
		System.out.println(String.format("Zeit: %.3fs", (System.currentTimeMillis() - now) / 1000.));

		for (int i = 0; i < 1000; i++) {
			// retrieve flyweight from factory
			Flyweight fw = fwf.getFlyweight(paths[i % paths.length]);

			// here we create the extrinsic state with a link to its intrinsic state
			items.add(new ExtrinsicState("Item " + i, fw));
		}

		System.out.println(String.format("Zeit: %.3fs", (System.currentTimeMillis() - now) / 1000.));
	}
}
