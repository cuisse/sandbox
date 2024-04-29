package io.github.cuisse.sandbox;

class RectangleContainer implements Container {

    Sand[][] grid;

    RectangleContainer(int w, int h) {
        grid = new Sand[w][h];
    }

    @Override
    public Sand get(int x, int y) {
        return grid[x][y];
    }

    @Override
    public Sand delete(int x, int y) {
        Sand sand = grid[x][y];
        if (sand != null) {
            grid[x][y] = null;
        }
        return sand;
    }

    @Override
    public void set(int x, int y, Sand sand) {
        grid[x][y] = sand;
    }

    @Override
    public boolean inside(int x, int y) {
        return (x >= 0 && x < grid.length   ) &&
               (y >= 0 && y < grid[0].length);
    }

    @Override
    public void paint() { }

    @Override
    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }
    }

}