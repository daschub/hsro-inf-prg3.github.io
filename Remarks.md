# Überarbeiteter Inhalt zum WS 2017

## Toolbox
- IntelliJ
- Gradle build system
- Git

## Generics
- Wiedereinstieg aus dem 2. Semester: generische Klassen und Methoden
- Bounds und Wildcards
- Generics und Vererbung

## Klassen, Vererbung und erweiterte Sprachfeatures
Nochmal mit Wolfgang/Prg2 abzustimmen.
- Klassen, Interfaces, abstrakte Klassen (Beispiel state machine)
- Interfaces und Factories
- Innere Klassen: Sichtbarkeiten (private, public, package, static)
- FunctionalInterfaces und Lambda
- `default`-Methoden
- Reflection und Annotationen

## Design Patterns
- Iterator
- Factory
- Flyweight
- Composite
- Strategy
- Observer
- Singleton
- Proxy und Adapter
- Visitor

_Weitere optionale Patterns:_

- Data Transfer Objets
- Repository
- Model View Controller/Model View ViewModel

## Fortgeschrittene Aspekte der Softwarearchitektur

_Sollten schon gemacht werden, da sie im weiteren Studium als gegeben betrachtet werden, vielleicht aber keine extra Übung sondern nur Artikel/Buch?_

- Enums
- Exceptions: Wann behandle ich was

_Soll das raus? Vielleicht eine Artikelsammlung bzw. Buchkapitel zum selbststudium? Oder durch eine gute Übung?_
- Komponenten und Schnittstellen
- Daten vs. Entitätstypen
- Enge und lose Kopplung
- Android GUI
- Dependency Injection

_Einführung durch Artikelsammlung in die einzelnen Themen und Anwendung mit Übung z.B. in ersten 2-3 Mini-PStAs Komponenten bauen zum Abrufen u. Konvertieren von Daten mit Hilfe von Resteasy, dann Komponenten in .jar-Dateien packen und in Android App dann mit Gradle einbinden? Dadurch würden sich auf jeden Fall Komponenten, Interfaces, Factory, DTOs, Repositories, ggf. Proxy-Pattern, Entitätstypen und Exceptions ergeben?_

## Nebenläufigkeit
- Threads und Runnable
- Synchronisation mit `join`, `synchronized`, `wait` und `notify`
- `Future` und `CompletableFuture`: Promises and Promise Chaining


## Funktionale Programmierung (Januarwochen)
- Immutability
- Einfache Beispiele (z.B. Mergesort mit immutable list)
- Java Streams: `forEach`, `filter`, `map`, `reduce`; `Optional`
- Set Comprehension mit `Stream.generate()`?
- Lazy Evaluation und `.parallel()`
- Wichtige `FunctionalInterface`s: `Predicate`, `Function`, `BinaryOperator`, ...
- Lambda und Methodenreferenzen (insb. Konstruktor)
- Erweiterte Funktionen: `collect`, `groupingBy`
- Übung evtl. mit Hibernate um Streams auf einer Datenbank anzuwenden? Ggf. mit Herr Höfing bzw. Herr Krüger absprechen ob die Studenten noch JDBC machen in den Übungen?
