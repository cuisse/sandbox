package io.github.cuisse.sandbox;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import java.awt.event.KeyEvent;
import java.awt.Color;

import javax.swing.JColorChooser;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class App extends PApplet {

    Sandbox sandbox;
    int     brushSize = Constants.DEFAULT_BRUSH_SIZE;
    boolean paint;
    boolean movable = true;
    boolean showInformation = true;
    boolean useRandomness = true;

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
    public void mouseWheel(MouseEvent event) {
        brushSize += event.getCount();
        if (brushSize < 1) {
            brushSize = 1;
        }
        if (brushSize > 100) {
            brushSize = 100;
        }
    }

    @Override
    public void mouseClicked() {
        if (mouseButton == CENTER) {
            int mx = mouseX;
            int my = mouseY;
            if (sandbox.container.inside(mx, my)) {
                Sand sand = sandbox.container.get(mx, my);
                if (sand != null) {
                    sandbox.sandColor(sand.color);
                }
            }
        }
    }

    @Override
    public void keyPressed() {
        switch (keyCode) {
            case KeyEvent.VK_N:
                sandbox.sandColor(color(random(1, 255), random(1, 255), random(1, 255)));
                break;
            
            case KeyEvent.VK_S:
                sandbox.image.save("sand_drawing_" + UUID.randomUUID() + ".png");
                break;

            case KeyEvent.VK_T:
                movable = !movable;
                break;

            case KeyEvent.VK_P:
                sandbox.plainColor = !sandbox.plainColor;
                break;

            case KeyEvent.VK_L:
                var color = JColorChooser.showDialog(null, "Color Picker", new Color(sandbox.sand_color));
                if (color != null) {
                    sandbox.sandColor(color(color.getRed(), color.getGreen(), color.getBlue()));
                }
                break;

            case KeyEvent.VK_U:
                sandbox.undo();
                break;

            case KeyEvent.VK_G:
                useRandomness = !useRandomness;
                break;

            case KeyEvent.VK_I:
                showInformation = !showInformation;
                break;

            case KeyEvent.VK_B:
                paint = !paint;
                break;

            case KeyEvent.VK_C:
                sandbox.clear();
                break;
            
            case KeyEvent.VK_R:
                sandbox.randomColor = !sandbox.randomColor;
                break;
        }
    }

    @Override
    public void draw() {
        image(sandbox.image, 0, 0);
        try {
            sandbox.update();
            sandbox.update();
            sandbox.paint();
        } catch (Throwable error) {
            error.printStackTrace();
        }
        if (mousePressed) {
            if (mouseButton == LEFT || mouseButton == RIGHT) {
                List<Sand> particles = null;
                for (int i = -brushSize; i < brushSize; i++) {
                    for (int j = -brushSize; j < brushSize; j++) {
                        if (i * i + j * j > brushSize * brushSize) {
                            continue;
                        }
                        if (useRandomness) {
                            if (random(1) < Constants.BRUSH_RANDOMNESS) {
                                continue;
                            }
                        }
                        int mx = mouseX + i;
                        int my = mouseY + j;
                        if (sandbox.container.inside(mx, my) == false) {
                            continue;
                        }
                        if (paint) {
                            Sand sand = sandbox.container.get(mx, my);
                            if (sand != null) {
                                sand.color = sandbox.sand_color;
                                sand.paint();
                            }
                        } else {
                            if (mouseButton == LEFT) {
                                if (particles == null) {
                                    particles = new ArrayList<>();
                                }
                                if (sandbox.container.get(mx, my) == null) {
                                    particles.add(new Sand(mx, my, sandbox.sand_color, sandbox, movable));
                                }
                            } else if (mouseButton == RIGHT) {
                                Sand sand = sandbox.container.get(mx, my);
                                if (sand != null) {
                                    sandbox.deleteParticle(sand);
                                }
                            }
                        }
                    }
                }
                if (particles != null) {
                    sandbox.addParticles(particles);
                }
            }
        }
        fill(color(100, 255, 50));
        if (showInformation) {
            text("FPS: " + frameRate + ", GRAINS: " + sandbox.count + ", UPDATED: " + sandbox.updated + ", BRUSH: " + brushSize, 0, 0, 100, 100);
        }
    }

}
