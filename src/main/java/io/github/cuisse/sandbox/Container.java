package io.github.cuisse.sandbox;

public interface Container {
    Sand    get(int x, int y);
    Sand    delete(int x, int y);
    void    set(int x, int y, Sand sand);
    boolean inside(int x, int y);
    void    paint();
    void    clear();
}
