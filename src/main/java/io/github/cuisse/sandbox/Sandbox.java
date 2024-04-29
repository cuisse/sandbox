package io.github.cuisse.sandbox;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.core.PImage;

class Sandbox {
    Container container;
    Sand      head;
    Sand      tail;
    Sand      last; // Last updatable sand grain
    int       count;
    PImage    image;
    int       updated;
    PApplet   applet;
    int       sand_color;
    boolean   randomColor;
    List<List<Sand>> history = new ArrayList<>();
    boolean plainColor = false;

    Sandbox(Container container, PApplet applet, int w, int h) {
        this.image = applet.createImage(w, h, PApplet.RGB);
        this.image.loadPixels();
        this.applet = applet;
        this.container = container;
        this.sand_color = applet.color(255, 255, 255);
    }

    void setPixel(int x, int y, int c) {
        image.pixels[x + y * image.width] = c;
    }

    int sandColor() {
        if (randomColor) {
            return applet.color(applet.random(1, 255), applet.random(1, 255), applet.random(1, 255));
        }
        return sand_color;
    }

    void sandColor(int color) {
        sand_color = color;
    }

    void addParticles(List<Sand> particles) {
        for (Sand sand : particles) {
            count += 1;
            updated += 1;
            if (updated == 1) {
                last = sand;
            }
            container.set(sand.x, sand.y, sand);
            if (head == null) {
                head = sand;
                tail = head;
            } else {
                Sand current = sand;
                if (head.next == null) {
                    head.next = current;
                    current.prev = head;
                    tail = current;
                } else {
                    current.prev = tail;
                    tail.next = current;
                    tail = current;
                }
            }
        }
        history.add(particles);
        if (history.size() > Constants.MAX_HISTORY) {
            history.remove(0);
        }
    }

    void undo() {
        if (history.size() > 0) {
            List<Sand> particles = history.remove(history.size() - 1);
            for (Sand particle : particles) {
                deleteParticle(particle);
            }
        }
    }

    void update() {
        if (last != null) {
            updated = 0;
            Sand sand = tail;
            while (sand != null) {
                sand.update();
                sand.paint();
                if (sand == last) {
                    break;
                }
                sand = sand.prev;
            }
            if (updated == 0) {
                last = null;
            }
            return;
        }

        if (updated == 0) {
            if (head != null) {
                return;
            }
        }
        updated = 0;
        Sand sand = tail;
        while (sand != null) {
            sand.update();
            sand.paint();
            sand = sand.prev;
        }
    }

    void paint() {
        image.updatePixels();   
        container.paint();
    }

    void deleteParticle(Sand sand) {
        if (container.delete(sand.x, sand.y) == sand) {
            sand.wakeup();
            count   -= 1;
            updated += 1;
            setPixel(sand.x, sand.y, Constants.BACKGROUND_COLOR);
            if (sand.prev != null) {
                sand.prev.next = sand.next;
            }
            if (sand.next != null) {
                sand.next.prev = sand.prev;
            }
            if (sand == head) {
                head = sand.next;
            }
            if (sand == tail) {
                tail = sand.prev;
            }
        }
    }


    void clear() {
        container.clear();
        head = null;
        tail = null;
        last = null;
        count = 0;
        updated = 0;
        image.loadPixels();
        for (int i = 0; i < image.pixels.length; i++) {
            image.pixels[i] = Constants.BACKGROUND_COLOR;
        }
        image.updatePixels();
    }
    
}
