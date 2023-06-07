package com.premiumminds.internship.snail;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by aamado on 05-05-2023.
 */
class SnailShellPattern implements ISnailShellPattern {

    /**
     * Method to get snailshell pattern
     *
     * @param matrix matrix of numbers to go through
     * @return order array of values thar represent a snail shell pattern
     */
    private final ExecutorService executor;

    private int x, y, left_limit, right_limit, up_limit, down_limit;
    private final Queue<String> DirectionsList;


    public SnailShellPattern(){
        /**
         * Contructor
         * */
        this.executor = Executors.newSingleThreadExecutor();
        this.DirectionsList = new LinkedList<>(Arrays.asList("R", "D", "L", "U"));
    }


    public boolean checkBoundaries(int possibleX, int possibleY) {
        /**
         * @param possibleX position of the next X
         * @param possibleY position of the next Y
         * Checks if the position will be in a valid position within our bondaires
         * @return True if valid movement, False if not
         */
        return left_limit <= possibleX && possibleX <= right_limit && up_limit <= possibleY && possibleY <= down_limit;
    }

    public void changeDirection(){
        /**
         * Changes directions by putting the Head of the queue in the back and changes the boundaries of the opposite
         * corner. The pattern is always Right, Down, Left, Up
         */
        DirectionsList.add(DirectionsList.poll());
        if(DirectionsList.peek().equals("L"))right_limit--;
        if(DirectionsList.peek().equals("R"))left_limit++;
        if(DirectionsList.peek().equals("D"))up_limit++;
        if(DirectionsList.peek().equals("U"))down_limit--;
    }

    public void run() {
        /**
         * Funcion that checks the direction which we are moving and moves according to it, whenever we change
         * directions we always know the next movement as well
         */
        String direction = DirectionsList.peek();
        if (direction.equals("R")) {
            if (checkBoundaries(x + 1, y)) {
                x++;
            } else {
                changeDirection();
                y++;
            }
        } else if (direction.equals("D")) {
            if (checkBoundaries(x, y + 1)) {
                y++;
            } else {
                changeDirection();
                x--;
            }
        } else if (direction.equals("L")) {
            if (checkBoundaries(x - 1, y)) {
                x--;
            } else {
                changeDirection();
                y--;
            }
        } else if (direction.equals("U")) {
            if (checkBoundaries(x, y - 1)) {
                y--;
            } else {
                changeDirection();
                x++;
            }
        }
    }

    public int[] snailPath(int[][] matrix){

        int maxsteps = matrix.length*matrix.length;
        int[] path = new int[maxsteps];
        path[0] = matrix[0][0];
        /*Setting the boundaries of the matrix*/
        up_limit = 0;
        right_limit = matrix.length-1;
        down_limit = matrix.length-1;
        left_limit = 0;

        x = 0;
        y = 0;

        int step = 1;
        while(step<maxsteps){
            run();
            path[step] = matrix[y][x];
            step++;
        }
        return path;
    }

    public Future<int[]> getSnailShell(final int[][] matrix) {
        Future<int[]> result;

        /*If matrix isn't a square one we return an empty array*/
        for (int[] ints : matrix) {
            for (int j = 0; j < ints.length; j++) {
                if (ints.length != matrix[j].length) return executor.submit(() -> new int[0]);
            }
        }

        result = executor.submit(() -> snailPath(matrix));
        return result;
    }
}
