---
title: Proxy, Adapter and Flyweight
permalink: /09ln-proxy-adapter-flyweight/
---

# Design Patterns, pt. 3

This week we'll focus on three structural patterns that are frequent in application development: _Proxy_, _Adapter_ and _Flyweight_.


# Proxy

Consider the following example.
You design a campus app that provides information such as timetables, room plans, cafeteria meal plan, etc.
The class responsible for retrieving the meal plan might look like this:

```java
class Meal {
	String name;
	List<String> notes;
}
```
```java
class MensaService {
	interface OpenMensaApi {
		@GET("canteens/229/days/{date}/meals")
		Call<List<Meal>> listMeals(@Path("date") String date);
	}

	OpenMensaApi api;

	MensaService() {
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://openmensa.org/api/v2/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();

		api = retrofit.create(OpenMensaApi.class);
	}

	List<Meal> getMeals(String date) throws IOException {
		Call<List<Meal>> call = api.listMeals(date);
		Response<List<Meal>> resp = call.execute();
		return resp.body();
	}
}
```

Later in your app, you might use this class as follows:

```java
class SomeApp {
	public static void main(String... args) {
		MensaService ms = new MensaService();

		List<Meal> meals = ms.getMeals("20170612");
	}
}
```

You test your product and observe that students keep looking at the app every 5 minutes in the morning.
Clearly, every request to get the meals of a certain date will result in a subsequent (network) call to the OpenMensa API.
This is unfortunate: first, the remote server may become unreachable if wifi drops, or slow during "rush hour"; second (and more importantly), the information is quite static -- it usually doesn't change!

This is where the _proxy_ pattern comes in.
We create a subclass that satisfies the same interface as the base class, but adds caching functionality:

```java
class SomeApp {
	public static void main(String... args) {
		// anonymous derived class for brevity
		MensaService proxy = new MensaService() {
			Map<String, List<Meal>> cache = new HashMap<>();
			List<Meal> getMeals(String date) throws IOException {
				if (cache.containsKey(date))
					return cache.get(date);

				List<Meal> meals = super.getMeals(date);
				cache.put(date, meals);
				return meals;
			}
		};

		List<Meal> meals = proxy.getMeals("20170612");
	}
}
```

This way, the request for the meal plan of a certain date will only be executed once; for subsequent calls, the proxy returns the cached responses.
The fact that the proxy has the same interface allows the client to dynamically select to use the proxy or not.

> Note: Subclassing is one option; more frequently, both the "real" service and the proxy would implement the same interface, and the proxy would maintain a reference to the real service.


## Structure

<div class="imgcols">
<img alt="dp-proxy" src="/assets/dp-proxy.svg">
<img alt="dp-proxy-process" src="/assets/dp-proxy_001.svg">
</div>

Proxies come in different flavors:

- **Remote** proxy (aka. _Ambassador_): Provides local proxy to remote object (different process or physical location)
- **Virtual** proxy: Creates expensive objects on demand; not to be confused with singleton (unique instance)
- **Protection** Proxy: controls access to the original object, e.g. read-only access that simulates write.

> Note: A **smart reference** is a proxy, too: it behaves just like the underlying object, but manages the state of the instance.


## Examples

- Caching for network requests
- Log output routing
- Lazy initialization of expensive objects
- Related: security facade; behaves like proxy, but hides error handling or authentication


## Proxy, Decorator and Composite

Proxy, [Decorator](/03ln-inheritance/) and [Composite](/07ln-iterator-composite-observer/) pattern have a similar structure using recursive composition.
However, the point of the 
- _decorator_ is to _add functionality without subclassing_: one enclosed instance plus extra logic;
- _composite_ is to model a recursive structure, such as user interface widgets: arbitrary number of enclosed instances, logic typically restricted to traversing the structure or specific to leaf classes;
- _proxy_ is to mimic the original object (!) while adding access control or caching.

> Note: In edge cases, a proxy actually behaves like a decorator (or vice versa).
> However, decorators can typically be stacked, often in arbitrary order;
> Proxy hierarchies are typically very flat: either there is a proxy, or there is none.


---

# Adapter

Let's stick with the example above, where you implemented a `MensaService` class that allows you to get the list of meals via `.getMeals(date)`.
Now you meet with your friend who is writing the front-end part of the app.
Well, they were expecting you to provide the meals in form of an `Iterable`:

```java
interface MealProvider extends Iterable<Meal> {
	void setDate(String date);
	// Iterator<Meal> iterator();  <-- from Iterable!
}
```

Doh.
This is quite a different interface, but there is no way that either of you changes their code -- think of all the refactoring of the unit tests etc.!

This is where the _adapter_ pattern comes in.
Just like you use adapters for tools if they don't fit, you can create an adapter that fits both ends:

```java
class MealAdapter extends MensaService implements MealProvider {
	private String date;
	
	@Override
	public void setDate(String date) { this.date = date; }
	
	@Override
	public Iterator<Meal> iterator() {
		try {
			// optionally: use today if date == null?
			return super.getMeals(date).iterator();
		} catch (IOException e) {
			return null;
		}
	}
}
```

Voila, this is your _class adapter_:

```java
MealProvider mp = new MealAdapter();
mp.setDate("20171206");
for (Meal m : mp)
	System.out.println(m);
```

Alternatively, you could write an _object adapter_, that implements the target interface, but maintains a reference to an instance of the class to be adapted (see structure below).


## Structure

_Class_ adapter:

![dp-adapter-class](/assets/dp-adapter.svg)

_Object_ adapter:

![dp-adapter-object](/assets/dp-adapter_001.svg)


> Note: The Adapter is not to be confused with the [Facade](https://en.wikipedia.org/wiki/Facade_pattern), in which a whole subsystem is abstracted into a single class.
> An example for a Facade would be to couple the classes `Engine`, `Transmission` and `Starter` into the class `Auto`, which adds the logic on how to start, drive and stop.


## Examples

- `ArrayAdapter` in Android to render data arrays in views
- Wrappers for third-party libraries

---


# Flyweight

Consider 


![dp-flyweight](/assets/dp-flyweight.svg)

> Note: In practice, the `operation()` often hands a data handle (reference) to the caller, instead of actually performing an operation with the provided extrinsic state.

## Examples

- Glyph (letter) rendering for text fields; intrinsic state: true-type fonts (often several MB), extrinsic state: position on screen, scale (size).
- Browser rendering the same media multiple times; intrinsic state: actual media (image, video, audio), extrinsic state: location on screen
- Android `RecyclerView`; intrinsic state: inflated layout of `RecycleView`, extrinsic state: actual contents to be displayed (often nested with further Flyweight)
- Video games rendering/tiling engines; intrinsic state: actual texture or tile, extrinsic state: 3D location and orientation

<p style="text-align: right">&#8718;</p>