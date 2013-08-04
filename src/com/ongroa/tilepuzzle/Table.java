package com.ongroa.tilepuzzle;

import java.util.Random;

public class Table {

    private int nofMoves = 0;
    private int SIZE = 0;
    private int MAX_SIZE = 10;
    private int MIN_SIZE = 1;
    private String[][] table = new String[MAX_SIZE][MAX_SIZE];

    /**
     * @return
     */
    public int getSize() { return SIZE; }

    /**
     * @return
     */
    public int getMaxSize() { return MAX_SIZE; }

    /**
     * @return
     */
    public int getNofMoves() { return nofMoves; }

    /**
     * 
     */
    public void resetNofMoves() { nofMoves = 0; }

    /**
     * @return
     */
    public String[][] getTable() { return table; }

    /**
     * @param table
     */
    private void resetTable(String[][] table) {
        int x = 1;
        for (int i = 0; i < SIZE; i++) 
            for (int j = 0; j < SIZE; j++) {
                table[i][j] = "" + x;
                x++;
            }
        table[SIZE - 1][SIZE - 1] = " ";
    }

    /**
     * @param size
     */
    public void setSize(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE) return;
        SIZE = size;
        resetTable(table);
    }

    /**
     * @return
     */
    public int getHoleIdx() { return getTileIdx(" "); }

    /**
     * @param x
     * @return
     */
    public int getTileIdx(String x) {
        for (int i = 0; i < SIZE; i++) 
            for (int j = 0; j < SIZE; j++)
                if (table[i][j] == x) 
                    return i * SIZE + j;
        return -1;
    }

    /**
     * @param coord
     */
    public void swap(int coord) {
        if (isLegalMove(coord) == false) return;
        int holeIdx = getHoleIdx();
        table[holeIdx / SIZE][holeIdx % SIZE] = 
            table[coord / SIZE][coord % SIZE];
        table[coord / SIZE][coord % SIZE] = " ";
        nofMoves++;
    }

    /**
     * 
     */
    public void shuffle() {
        int coord;
        int n = 1000000;
        final Random myRandom = new Random();
        for (int i = 0; i < n; i++) {
            coord = (int)(myRandom.nextInt(SIZE * SIZE));
            if (isLegalMove(coord)) swap(coord);
        }
    }

    /**
     * @param coord
     * @return
     */
    private boolean isLegalMove(int coord) {
        if (coord > SIZE * SIZE - 1) return false;
        int holeIdx = getHoleIdx();
        if (holeIdx == -1) return false;
        if (Math.abs(holeIdx - coord) == SIZE) return true;
        if (holeIdx % SIZE == 0 && coord - holeIdx == 1) return true;
        if (holeIdx % SIZE == SIZE - 1 && coord - holeIdx == -1) return true;
        if (holeIdx % SIZE > 0 && holeIdx % SIZE < SIZE - 1 && 
                Math.abs(holeIdx - coord) == 1) return true;
        return false;
    }

    /**
     * @return
     */
    public boolean isDone() {
        String[][] newTable = new String[SIZE][SIZE];
        resetTable(newTable);
        for (int i = 0; i < SIZE; i++) 
            for (int j = 0; j < SIZE; j++)
                if (! table[i][j].equals(newTable[i][j])) return false;
        return true;
    }

}
