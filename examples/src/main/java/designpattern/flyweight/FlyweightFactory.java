package designpattern.flyweight;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

class FlyweightFactory {
	Map<String, Flyweight> flyweights = new HashMap<>();

	Flyweight getFlyweight(String path) throws URISyntaxException, IOException {
		if (flyweights.containsKey(path))
			return flyweights.get(path);

		// get resource file uri
		File file = new File(getClass().getClassLoader()
				.getResource(path).toURI());

		// load image
		Flyweight fw = new Flyweight(ImageIO.read(file));
		flyweights.put(path, fw);
		return fw;
	}
}
