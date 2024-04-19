package io.github.cuisse.sandbox;

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

    void removeSand(int x, int y) {
        if (container.inside(x, y)) {
            var sand = container.delete(x, y);
            if (sand != null) {
                if (last == null || sand.after(last)) {
                    last = sand;
                }
                sand.wakeup(this);
                count -= 1;
                updated += 1;
                setPixel(x, y, Constants.BACKGROUND_COLOR);
                if (sand == head) {
                    head = sand.next;
                } else if (sand == tail) {
                    tail = sand.prev;
                } else {
                    if (sand.prev != null) sand.prev.next = sand.next;
                    if (sand.next != null) sand.next.prev = sand.prev;
                }
            }
        }
    }

    Sand createSand(int x, int y, int c) {
        if (container.inside(x, y) == false || container.get(x, y) != null) {
            return null;
        }
        Sand sand = new Sand(x, y, c, this);
        count += 1;
        updated += 1;
        if (updated == 1) {
            last = sand;
        }
        container.set(x, y, sand);
        if (head == null) {
            head = sand;
            tail = head;
        } else {
            Sand current = sand;
            if (head.next == null) {
                head.next    = current;
                current.prev = head;
                tail         = current;
            } else {
                current.prev = tail;
                tail.next    = current;
                tail         = current;
            }
        }
        return sand;
    }

    void update() {
        if (last != null) {
            updated = 0;
            Sand sand = tail;
            while (sand != null) {
                sand.update(this);
                if (sand == last) {
                    break;
                }
                sand = sand.prev;
            }
            if (updated == 0) {
                last = null;
            }
            image.updatePixels();
            container.paint();
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
            sand.update(this);
            sand = sand.prev;
        }
        image.updatePixels();
        container.paint();
    }
    
}
