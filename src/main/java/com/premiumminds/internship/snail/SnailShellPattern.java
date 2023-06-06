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

    int x, y, left_limit, right_limit, up_limit, down_limit;
    Queue<String> DirectionsList;


    public SnailShellPattern(){
        this.executor = Executors.newSingleThreadExecutor();
        this.DirectionsList = new LinkedList<>(Arrays.asList("R", "D", "L", "U"));
    }


    public boolean checkBoundaries(int currX, int currY) {
        return left_limit <= currX && currX <= right_limit && up_limit <= currY && currY <= down_limit;
    }

    public void changeDirection(){
        DirectionsList.add(DirectionsList.poll());
        if(DirectionsList.peek().equals("L"))right_limit--;
        if(DirectionsList.peek().equals("R"))left_limit++;
        if(DirectionsList.peek().equals("D"))up_limit++;
        if(DirectionsList.peek().equals("U"))down_limit--;
    }

    public void run() {
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

    private void updateRightBoundaries(){
        right_limit--;
        down_limit--;
    }
    private void updateLowerBoundaries(){
        left_limit++;
        up_limit++;
    }

    public int[] snailPath(int[][] matrix){

        if (matrix.length != matrix[0].length){

        }

        int maxsteps = matrix.length*matrix.length;
        int[] path = new int[maxsteps];
        path[0] = matrix[0][0];
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
        result = executor.submit(() -> snailPath(matrix));
        return result;
    }
}
