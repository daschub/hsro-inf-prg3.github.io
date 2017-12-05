package designpattern.flyweight;

public class ExtrinsicState {
	final Flyweight fw;
	final String caption;

	ExtrinsicState(String caption, Flyweight fw) {
		this.caption = caption;
		this.fw = fw;
	}

}
