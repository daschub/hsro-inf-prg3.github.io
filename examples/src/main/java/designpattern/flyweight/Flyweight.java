package designpattern.flyweight;

import java.awt.*;

class Flyweight {
	// intrinsic state
	private final Image image;

	Flyweight(Image image) {
		this.image = image;
	}
}
