package threads;

import java.util.LinkedList;
import java.util.List;

class Buffer<T> {
	List<T> buffer = new LinkedList<>();
	final int max = 10;

	synchronized void put(T obj) throws InterruptedException {
		// wait until buffer not full
		while (buffer.size() == 10)
			wait();

		buffer.add(obj);

		// wake up other threads waiting for buffer to change
		notifyAll();
	}

	synchronized T get() throws InterruptedException {
		// wait until there's something in the buffer
		while (buffer.size() == 0)
			wait();

		T obj = buffer.remove(0);

		// wake up other threads waiting for buffer to change
		notifyAll();
		return obj;
	}
}
