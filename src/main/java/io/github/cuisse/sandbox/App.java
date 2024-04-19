package io.github.cuisse.sandbox;

import java.util.UUID;
import java.awt.event.KeyEvent;

import processing.core.PApplet;

public class App extends PApplet {

    Sandbox sandbox;

    public static void main(String[] args) {
        PApplet.main("io.github.cuisse.sandbox.App");
    }

    @Override
    public void settings() {
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void setup() {
        colorMode(Constants.COLOR_MODE, Constants.MAX_COLOR);
        pixelDensity(Constants.PIXEL_DENSITY);
        frameRate(Constants.FRAME_RATE);
        sandbox = new Sandbox(new RectangleContainer(width, height), this, width, height);
    }

    @Override
    public void keyPressed() {
        if (keyCode == KeyEvent.VK_S) {
            sandbox.image.save(UUID.randomUUID() + "_drawing.png");
        } else if (keyCode == KeyEvent.VK_P) {
            int mx = mouseX;
            int my = mouseY;
            if (sandbox.container.inside(mx, my)) {
                Sand sand = sandbox.container.get(mx, my);
                if (sand != null) {
                    sandbox.sand_color = sand.color;
                }
            }
        }
    }

    @Override
    public void draw() {
        image(sandbox.image, 0, 0);
        try {
            sandbox.update();
            sandbox.update();
        } catch (Throwable error) {
            error.printStackTrace();
        }
        if (keyPressed) {
            if (keyCode == UP) {
                sandbox.sand_color = color(random(1, 255), random(1, 255), random(1, 255));
            } else {
                for (int i = -Constants.BRUSH_SIZE; i < Constants.BRUSH_SIZE; i++) {
                    for (int j = -Constants.BRUSH_SIZE; j < Constants.BRUSH_SIZE; j++) {
                        if (i * i + j * j > Constants.BRUSH_SIZE * Constants.BRUSH_SIZE) {
                            continue;
                        }
                        if (random(1) < Constants.BRUSH_RANDOMNESS) {
                            continue;
                        }
                        if (keyCode == LEFT) {
                            sandbox.createSand(mouseX + i, mouseY + j, sandbox.sand_color);
                        } else if (keyCode == RIGHT) {
                            sandbox.removeSand(mouseX + i, mouseY + j);
                        }
                    }
                }
            }
        }

        fill(color(100, 255, 50));
        text("FPS: " + frameRate + ", GRAINS: " + sandbox.count + ", UPDATED: " + sandbox.updated, 0, 0, 100, 100);
    }

}
