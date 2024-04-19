### Sandbox

Sand simulation using [Processing](https://processing.org/)

## Building
You can build up this project using maven.

- Requirements
    - Java 21
    - Maven 3.6.3

#### Maven
````shell
mvn clean package
````

## Running

#### using Java
````shell
java -jar sandbox-1.0.0.jar
````
#### using Maven
````shell
mvn exec:java -D"exec.mainClass"="io.github.cuisse.sandbox.App"
````

## Controls
```text
Mouse = use for drawing 
Left arrow = drop sand
Up arrow = change sand color
Left arrow = remove sand 
S = save current image 
P = select the current color at the mouse pointer  
```

## Example
![Gameplay](./assets/Gameplay.gif)