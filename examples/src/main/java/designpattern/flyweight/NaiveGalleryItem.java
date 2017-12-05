package designpattern.flyweight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class NaiveGalleryItem {
	final Image image;
	final String caption;

	NaiveGalleryItem(String caption, String path) throws IOException, URISyntaxException {
		this.caption = caption;

		// get resource file uri
		File file = new File(getClass().getClassLoader()
				.getResource(path).toURI());

		// load image
		this.image = ImageIO.read(file);
	}
}
