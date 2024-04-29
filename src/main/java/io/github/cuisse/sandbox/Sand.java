package io.github.cuisse.sandbox;

import processing.core.PApplet;

class Sand {
    int     x;
    int     y;
    int     idle;
    Sand    prev;
    Sand    next;
    int     color;
    Sandbox box;
    boolean movable;

    Sand(int x, int y, int c, Sandbox box, boolean movable) {
        this.x = x;
        this.y = y;
        this.box = box;
        this.color = box.plainColor ? c : randomColor(c);
        this.movable = movable;
        if (this.movable == false) {
            idle = Constants.PARTICLE_MAX_INACTIVITY_TIME;
        }
    }

    boolean after(Sand other) {
        if (other == null) return true;
        if (x > other.x)   return true;
        if (y > other.y)   return true;
        return false;
    }

    void wakeup() {
        if (idle >= Constants.PARTICLE_MAX_INACTIVITY_TIME) {
            idle = 0;
            if (box.container.inside(x    , y - 1)) wakeup(box.container.get(x    , y - 1));
            if (box.container.inside(x + 1, y - 1)) wakeup(box.container.get(x + 1, y - 1));
            if (box.container.inside(x - 1, y - 1)) wakeup(box.container.get(x - 1, y - 1));
        }
    }

    void wakeup(Sand sand) {
        if (sand != null) {
            sand.wakeup();
        }
    }

    void update() {
        if (movable == false || box.container.inside(x, y + 1) == false || idle >= Constants.PARTICLE_MAX_INACTIVITY_TIME) {
            return;
        }

        box.updated += 1;

        boolean ui = box.container.inside(    x, y + 1); // under
        boolean li = box.container.inside(x - 1, y + 1); // left
        boolean ri = box.container.inside(x + 1, y + 1); // right
        boolean pi = box.container.inside(    x, y - 1); // up
        boolean rt = box.container.inside(x + 1, y - 1); // right top
        boolean lt = box.container.inside(x - 1, y - 1); // left top

        Sand under = ui ? box.container.get(    x, y + 1) : null;
        Sand left  = li ? box.container.get(x - 1, y + 1) : null;
        Sand right = ri ? box.container.get(x + 1, y + 1) : null;
        Sand up    = pi ? box.container.get(    x, y - 1) : null;
        Sand rtop  = rt ? box.container.get(x + 1, y - 1) : null;
        Sand ltop  = lt ? box.container.get(x - 1, y - 1) : null;

        if (up   != null) up.idle   = 0;
        if (rtop != null) rtop.idle = 0;
        if (ltop != null) ltop.idle = 0;

        if (under == null && ui) {
            if (left  != null) left.idle  = 0;
            if (right != null) right.idle = 0;
            moveTo(x, y + 1);
        } else {
            if (right == null && ri) {
                if (under != null) under.idle = 0;
                if (left  != null) left.idle  = 0;
                moveTo(x + 1, y + 1);
            } else {
                if (left == null && li) {
                    if (under != null) under.idle = 0;
                    if (right != null) right.idle = 0;
                    moveTo(x - 1, y + 1);
                }
            }
        }

        idle += 1;
    }

    void paint() {
        box.setPixel(x, y, color);
    }

    void moveTo(int nx, int ny) {
        box.setPixel(x, y, Constants.BACKGROUND_COLOR);
        box.container.set(x, y, null);
        box.container.set(nx, ny, this);
        this.x = nx;
        this.y = ny;
        this.idle = 0;
    }

    int randomColor(int c) {
        int r = (int) box.applet.random(0, 6);
        if (r == 1)
            return box.applet.color(PApplet.min(255, (int) (box.applet.red(c) * 0.8)),
                    PApplet.min(255, (int) (box.applet.green(c) * 0.8)),
                    PApplet.min(255, (int) (box.applet.blue(c) * 0.8)));
        if (r == 2)
            return box.applet.color(PApplet.min(255, (int) (box.applet.red(c) * 0.9)),
                    PApplet.min(255, (int) (box.applet.green(c) * 0.9)),
                    PApplet.min(255, (int) (box.applet.blue(c) * 0.9)));
        if (r == 3)
            return box.applet.color(PApplet.min(255, (int) (box.applet.red(c) * 1.0)),
                    PApplet.min(255, (int) (box.applet.green(c) * 1.0)),
                    PApplet.min(255, (int) (box.applet.blue(c) * 1.0)));
        if (r == 4)
            return box.applet.color(PApplet.min(255, (int) (box.applet.red(c) * 1.1)),
                    PApplet.min(255, (int) (box.applet.green(c) * 1.1)),
                    PApplet.min(255, (int) (box.applet.blue(c) * 1.1)));
        if (r == 5)
            return box.applet.color(PApplet.min(255, (int) (box.applet.red(c) * 1.2)),
                    PApplet.min(255, (int) (box.applet.green(c) * 1.2)),
                    PApplet.min(255, (int) (box.applet.blue(c) * 1.2)));
        if (r == 6)
            return box.applet.color(PApplet.min(255, (int) (box.applet.red(c) * 1.3)),
                    PApplet.min(255, (int) (box.applet.green(c) * 1.3)),
                    PApplet.min(255, (int) (box.applet.blue(c) * 1.3)));
        return c;
    }

}