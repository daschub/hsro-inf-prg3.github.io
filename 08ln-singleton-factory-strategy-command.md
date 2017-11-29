---
title: Singleton, Factory, Strategy and Command
permalink: /08ln-singleton-factory-strategy-command/
---

# Design Patterns, pt. 2

This week we'll talk about two structural patterns (_Singleton_ and _Factory_) as well as two behavioral patterns (_Strategy_ and _Command_).

# Singleton

The task at hand is pretty simple: How can you ensure that a certain object is unique among your application?

In Java, there are a number of ways to realize that.
First, lets start with the obvious: we need to control who is allowed to create instances.
The safest thing to do is to make the constructor _private_.
If we can only create instances _from within the class_, we can allocate a static attribute at startup.

```java
class Singleton {
	public static final Singleton instance = new Singleton();

	private Singleton() {

	}
}
```
```java
Singleton.instance.doSomething();
```

This works if the constructor is trivial, and no further setup of `instance` is required.
But what if you need to do some extra work for `instance` to be ready-to-use?
The answer is: use a _static initializer block_.

And one more thing: the public visibility does not allow to guard the instance, e.g. from simultaneous access from multiple threads.
To fix this, use a getter method.

```java
class Singleton {
	private final static Singleton instance;

	static {
		instance = new Singleton();

		// do more work...
	}

	private Singleton() {

	}

	public static Singleton getInstance() {
		// guard if necessary...
		return instance;
	}
}
```
```java
Singleton.getInstance().doSomething();
```

The drawback of this solution is that the singleton is now instantiated at startup, and regardless if it is actually used.
To fix this, use a lazy initializer:

```java
class Singleton {
	private static Singleton instance;  // note: no final!

	public static Singleton getInstance() {
		if (instance == null) {
			instance = new Singleton();

			// do more stuff...
		}

		return instance;
	}

	private Singleton() {

	}
}
```
```java
Singleton.getInstance().doSomething();
```

All of these methods can be fooled using reflection.
If you truly want to disable further instantiation, use an `enum`, as described in [Bloch's Effective Java](https://www.amazon.de/Effective-Java-Edition-Joshua-Bloch/dp/0321356683).

```java
enum Singleton {
	INSTANCE;

	// specify more attributes, e.g. a counter
	int counter = 0;

	public int getCount() {
		return counter++;
	}
}
```
```java
System.out.println(Singleton.INSTANCE.getCount());  // "0"
System.out.println(Singleton.INSTANCE.getCount());  // "1"
System.out.println(Singleton.INSTANCE.getCount());  // "2"
```

Why woud you do this? ¯\\_(ツ)_/¯

No seriously.
It has to do with serialization: attributes of enums are not serialized (which can be a good or bad thing, it depends).
Most developers opt for the _lazy initialization_ method, since it makes the initializaion more controllable.


## Structure and Participants

![dp-singleton](/assets/dp-singleton.svg)

- `Singleton`
	+ typically responsible for managing its unique instance
	+ provides operation to obtain unique instance (in Java: `static` method)


## Discussion

Singletons are standard practice to avoid resource conflicts or overallocation.
However, they are at the same time (strongly) disencouraged if working in a multi-threaded (parallel) environment: while the actual resource conflict can be (usually) solved with locking, the process itself may dramatically reduce the benefit of parallel processing.

For advanced developers: Favor [_dependency injection_](https://en.wikipedia.org/wiki/Dependency_injection) over singletons.


## Examples

- Logging facilities
- Event busses and dispatch queues
- Device handles (there is only 1 physical device, e.g. `System.out`)
- Service objects (eg. API wrappers, ...)


# Factory

A factory provides instances that fulfill a certain interface.

## A Basic Example

Let's consider a basic example: a package with public interfaces but package-private classes.

```java
package mypackage;

public interface Klass {
	void method();
}
```
```java
package mypackage;

class KlassImpl implements Klass {
	public void method() {
		System.out.println("Hello World!");
	}
}
```

So from outside the package, you can't instanciate `KlassImpl`:

```java
package someApp;
class MyApp {
	public static void main(String... args) {
		mypackage.Klass k = new mypackage.KlassImpl();  // not visible!
	}
}
```

This is where you need a factory method, often attached to an abstract class or as a default method to an interface.

```java
package mypackage;

public interface Klass {
	void method();
	default Klass create() {
		return new KlassImpl();  // inside package: visible!
	}
}
```
```java
mypackage.Klass k = mypackage.Klass.create();
```

As you can see, the _user_ of the package relies on the interface, and has no idea on which class was actually instantiated.


## A More Sophisticated Example

Recall last week's [composite pattern](/07ln-iterator-composite-observer/) which can be found for example in the implementation of JSON or an XML tree.
In principal, both can store key-value relations, potentially nested:

```json
{
	"key": "value",
	"nested": {
		"key": "value"
	}
}
```
```xml
<element>
	<key>value</key>
	<element>
		<key>value</key>
	</element>
</element>
```

```java
interface Component {
	String toString();
}
interface Composite extends Component {
	void add(Component c);
}
interface Leaf extends Component {
}
```

With realizations for Json (`JsonComponent`, `JsonComposite`, `JsonLeaf`) and XML (`XmlComponent`, ...) that model similar structure, but different `toString()` serialization.

Without a factory, you would have to manually construct the composite:

```java
JsonComposite root = new JsonComposite("root");
root.add(new JsonLeaf("key", "value"));
Composite nested = new JsonComposite("nested");
nested.add(new JsonLeaf("key", "value"));
root.add(nested);
System.out.println(root);  // "root": {"key": "value", "nested": {"key": "value"}}
```

And similarly for `XmlComposite`.
If you abstract the instance creation into a factory, you could generalize the code significantly:

```java
interface CompositeFactory {
	Composite createComposite(String name);
	Leaf createLeaf(String name, String value);
}
```
```java
class JsonFactory implements CompositeFactory {
	@Override
	public Composite createComposite(String name) {
		return new JsonComposite(name);
	}

	@Override
	public Leaf createLeaf(String name, String value) {
		return new JsonLeaf(name, value);
	}
}
```

```java
CompositeFactory f = new JsonFactory();
// CompositeFactory f = new XmlFactory();

Composite root = f.createComposite("root");
root.add(f.createLeaf("key", "value"));

Composite nested = f.createComposite("nested");
nested.add(f.createLeaf("key", "value"));

root.add(nested);

System.out.println(root);
```

As you can see, you only need to replace the factory that produces the concrete clases; the construction logic remains the same.


## Structure and Participants

![dp-factory](/assets/dp-abstract-factory.svg)

- `AbstractFactory`
	+ declares interface for operations that create the abstract products
- `ConcreteFactory`
	+ _implements_ the operations and procudes concrete products
- `AbstractProduct`
	+ declares interface for operations
- `ConcreteProduct`
	+ _implements_ the operations
- `Client`
	+ uses only interfaces declared by `AbstractFactory` and `AbstractProduct`


## Discussion

The factory pattern is omnipresent; sometimes it is realized as a single _factory method_, sometimes as a larger factory serving different objects.

The most common use is when developing against interfaces where the implementing classes are package-private.
The package would then expose a _factory_ that allows to generate instances that implement the public interfaces -- with internals hidden from the client.


## Examples

Typically objects that are either complicated to instantiate or which should not be exposed outside of a package.

- Iterators (probably the most frequently used factory)
- Objects that have complex intantiation protocols
- Logging instances
- API wrappers


# Strategy

## Structure and Participants

![dp-strategy](/assets/dp-strategy.svg)


## Discussion

The strategy pattern if often "in disguise."
For example, the `Stream.filter(Predicate<T> p)`, `Interable.iterator()` or `Collection.sort(Comparator<T> c)` are all flavors of the strategy pattern: they allow to do the same thing in different ways.

You can easily spot potential refactoring areas if you have code such as

```java
if (isJson())
	return parseJson(data);
else if (isXML())
	return parseXML(data);
else
	return data.raw;
```

with the `parse*()` functions being members.
Refactor to the factory pattern by isolating them and use them via a common interface.


## Examples

- `Comparator` interface, to customize sorting
- Encryption and authentication protocols
- Media encoders (mp3, mp4, aac, etc.)
- Serializers ("save as..." feature)


# Command

## Structure and Participants

![dp-command](/assets/dp-command.svg)

- `Command`
	+ declares an interface for executing an operation
- `ConcreteCommand`
	+ _implements_ the operation
	+ uses the receiver as needed
- `Client` (application)
	+ creates `ConcreteCommand` and hands receiver
- `Invoker`
	+ actually calls `.execute()`
- `Receiver`
	+ the object used by the strategy


## Discussion

The command pattern is more frequent than you might initially think.
Think of it this way: whenever you allow the user to sequentially apply certain commands to your data/state, you may want to be able to undo those at some point.
Building up a stack of actions automatically leads to adopting the command pattern.


## Examples

- Editors that support undo or macros
- Databases with transaction/rollback support
- Filesystems with journalling
- Version control (eg. git)
- Realizations of automatons


<p style="text-align: right">&#8718;</p>
