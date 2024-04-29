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
Mouse Left Button = create sand particles 
Mouse Right Button = remove sand particles
Mouse Wheel = change the size of the brush
Mouse Center Click = pick up the current color at the mouse pointer  
N = change sand color to a random color
S = save current image as a png file
B = toggle paint mode 
C = clear the screen
R = toggle random color mode
I = toggle information 
U = undo the last action
T = toggle static mode
P = toggle plain color mode
L = open the color picker
G = toggle brush randomness 

```

## Example
![Gameplay](./assets/Gameplay.gif)
