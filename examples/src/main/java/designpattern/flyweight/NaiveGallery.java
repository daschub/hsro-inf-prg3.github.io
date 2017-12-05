package designpattern.flyweight;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

class NaiveGallery {
	public static void main(String[] args) throws IOException, URISyntaxException {
		List<NaiveGalleryItem> items = new LinkedList<>();

		String[] paths = {"tiebomber.bmp", "tiefighter.bmp", "tieinterceptor.bmp"};

		long now = System.currentTimeMillis();
		System.out.println(String.format("Zeit: %.3fs", (System.currentTimeMillis() - now) / 1000.));

		for (int i = 0; i < 1000; i++) {
			items.add(new NaiveGalleryItem("Item " + i, paths[i % paths.length]));
		}

		System.out.println(String.format("Zeit: %.3fs", (System.currentTimeMillis() - now) / 1000.));
	}
}
