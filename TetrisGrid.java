

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by SPAS on 25/08/2017.
 */
public class TetrisGrid extends JPanel {

    private static final int gridH = 20;
    private static final int gridW = 12;
    private static boolean[][] grid;
//    private static boolean isPlaying;

    private static final int boxW = 57;
    private static final int boxH = 43;
    private static final int padding = 2;

    private MyWorker workerThread;

    private static int[] shapeXes;
    private static int[] shapeYs;

    private static Random numGen;
    private static int shapeIndex;

    private ScoreLabel sLabel;
    private MyArrowListener arrowListener;


    public TetrisGrid(ScoreLabel sLabel) {
        super();
        setSize(694, 874);
//        isPlaying = true;
        grid = new boolean[gridH][gridW];
        shapeXes = new int[4];
        shapeYs = new int[4];
        setFocusable(true);
        requestFocusInWindow();
        arrowListener = new MyArrowListener();
        addKeyListener(arrowListener);
        numGen = new Random();
        this.sLabel = sLabel;
    }

    public void startGame() {
        dropShape();
        workerThread = new MyWorker(this);
        workerThread.execute();
   /*     while (isPlaying) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            grid[3][6] = grid[7][5] = grid[0][0] = grid[8][5] = grid[8][6] = grid[19][11] = true;

            nextStep();
            repaint();
        }*/
    }

    public void endGame() {
        sLabel.gameOver();
        removeKeyListener(arrowListener);
        workerThread.cancel(true);
    }

    //make a new shape up top
    private void dropShape() {
        shapeIndex = numGen.nextInt(7) + 1;
        //TODO change later
//        shapeIndex = 6;
        switch (shapeIndex) {

            case 1:
                grid[0][4] = grid[0][5] = grid[1][5] = grid[1][6] = true;
                shapeXes[0] = 4;
                shapeYs[0] = 0;
                shapeXes[1] = 5;
                shapeYs[1] = 0;
                shapeXes[2] = 5;
                shapeYs[2] = 1;
                shapeXes[3] = 6;
                shapeYs[3] = 1;
                break;

            case 2:
                grid[1][5] = grid[1][6] = grid[0][6] = grid[0][7] = true;
                shapeXes[0] = 5;
                shapeYs[0] = 1;
                shapeXes[1] = 6;
                shapeYs[1] = 1;
                shapeXes[2] = 6;
                shapeYs[2] = 0;
                shapeXes[3] = 7;
                shapeYs[3] = 0;
                break;

            case 3:
                grid[0][4] = grid[0][5] = grid[0][6] = grid[0][7] = true;
                for (int i = 0; i < 4; i++) {
                    shapeXes[i] = i + 4;
                    shapeYs[i] = 0;
                }
                break;

            case 4:
                grid[1][5] = grid[0][5] = grid[0][6] = grid[0][7] = true;
                shapeXes[0] = 5;
                shapeYs[0] = 1;
                for (int i = 1; i < 4; i++) {
                    shapeXes[i] = i + 4;
                    shapeYs[i] = 0;
                }
                break;

            case 5:
                grid[0][4] = grid[0][5] = grid[0][6] = grid[1][6] = true;
                for (int i = 0; i < 3; i++) {
                    shapeXes[i] = i + 4;
                    shapeYs[i] = 0;
                }
                shapeXes[3] = 6;
                shapeYs[3] = 1;
                break;

            case 6:
                grid[0][4] = grid[0][5] = grid[0][6] = grid[1][5] = true;
                for (int i = 0; i < 3; i++) {
                    shapeXes[i] = i + 4;
                    shapeYs[i] = 0;
                }
                shapeXes[3] = 5;
                shapeYs[3] = 1;
                break;

            case 7:
                grid[0][5] = grid[0][6] = grid[1][5] = grid[1][6] = true;
                shapeXes[0] = 5;
                shapeYs[0] = 0;
                shapeXes[1] = 6;
                shapeYs[1] = 0;
                shapeXes[2] = 5;
                shapeYs[2] = 1;
                shapeXes[3] = 6;
                shapeYs[3] = 1;
                break;

            default:
                grid[0][4] = grid[0][5] = grid[0][6] = grid[0][7] = true;
                for (int i = 0; i < 4; i++) {
                    shapeXes[i] = i + 4;
                    shapeYs[i] = 0;
                }
                break;
        }
    }

    //could be improved to not move all the blocks every turn
    private void nextStep() {
        //TODO check if row is full
        /*for (int i = gridH - 2; i >= 0; i--) {
            for (int j = gridW - 1; j >= 0; j--) {
                if (grid[i][j] && !grid[i + 1][j]) {
                    grid[i][j] = false;
                    grid[i + 1][j] = true;
                    isMoving = true;
                }
            }
        }*/

        //updating the coordinates of the falling shape
//        if(isMoving){
        boolean isMoving = isValidMovement();
        if (isMoving) {
            for (int i = 0; i < 4; i++) {
                int x = shapeXes[i];
                int y = shapeYs[i];
                if (y < gridH - 1) {
                    grid[y + 1][x] = true;
                    shapeYs[i] += 1;
                }
            }
        }
        if (!isMoving) {
            for (int i = 0; i < 4; i++) {
                int x = shapeXes[i];
                int y = shapeYs[i];
                grid[y][x] = true;
            }
            checkRows();
            checkLose();
            dropShape();
        }
        repaint();
    }

    private boolean isValidMovement() {
        for (int i = 0; i < 4; i++) {
            int x = shapeXes[i];
            int y = shapeYs[i];
            grid[y][x] = false;

        }
        for (int i = 0; i < 4; i++) {
            int x = shapeXes[i];
            int y = shapeYs[i];
            if (y > gridH - 2) return false;
            if (grid[y + 1][x]) {
                return false;
            }
        }
        return true;
    }

    private void checkRows() {
        int fullBlocks = 0;
        for (int i = 0; i < gridH; i++) {
            for (int j = 0; j < gridW; j++) {
                if (grid[i][j]) {
                    fullBlocks++;
                } else break;
                if (fullBlocks == gridW) destroyRow(i);
            }
            fullBlocks = 0;
        }
//        repaint();
    }

    //everything above the row needs to come down
    private void destroyRow(int rowNumber) {
        sLabel.increaseScore();
        for (int i = rowNumber; i > 0; i--) {
            for (int j = gridW - 1; j >= 0; j--) {
                grid[i][j] = grid[i - 1][j];
            }
        }
    }

    //output game over message
    private void checkLose() {
        for (int j = 0; j < gridW; j++) {
            if (grid[0][j]) {
                System.err.println("GAME OVER");
                endGame();
                break;
            }
        }
    }

    /*protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.red);
        g.fillRect( 9 * boxW + 5, 7 * boxH , boxW - 5 , boxH);
        g.setColor(Color.green);
        g.fillRect( 10 * boxW + 5, 7 * boxH , boxW - 5, boxH);
        g.setColor(Color.blue);
        g.fillRect( 11 * boxW + 5, 7 * boxH , boxW - 5 , boxH );
        g.setColor(Color.red);
        g.fillRect( 0 * boxW + 5, 7 * boxH , boxW - 5, boxH);
    }*/

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < gridH; i++) {
            for (int j = 0; j < gridW; j++) {
                if (grid[i][j]) {
                    g.setColor(Color.red);
                    g.fillRect(j * boxW + padding, i * boxH + padding, boxW - padding, boxH - padding);
                }
            }
        }
    }

    private class MyArrowListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
//            System.out.println("event happening");
            int keyCode = e.getKeyCode();
            if (keyCode == e.VK_LEFT) {
                //check if there is space to the left to move in
                boolean isPossible = true;
                for (int i = 0; i < 4; i++) {
                    int x = shapeXes[i];
                    int y = shapeYs[i];
                    grid[y][x] = false;
                    if (shapeXes[i] <= 0) {
                        isPossible = false;
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    int x = shapeXes[i];
                    int y = shapeYs[i];
                    if (x == 0) {
                        isPossible = false;
                        break;
                    }
                    if (grid[y][x - 1]) {
                        isPossible = false;
                        break;
                    }
                }
                if (isPossible) {
                    for (int i = 0; i < 4; i++) {
                        int x = shapeXes[i];
                        int y = shapeYs[i];

                        grid[y][x - 1] = true;
                        shapeXes[i] -= 1;
                    }
                    nextStep();
                }
            }

            if (keyCode == e.VK_RIGHT) {
                //check if there is space to the right to move in
                boolean isPossible = true;
                for (int i = 0; i < 4; i++) {
                    int x = shapeXes[i];
                    int y = shapeYs[i];
                    grid[y][x] = false;
                    if (shapeXes[i] >= gridW - 1) {
                        isPossible = false;
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    int x = shapeXes[i];
                    int y = shapeYs[i];
                    if (x == gridW - 1) {
                        isPossible = false;
                        break;
                    }
                    if (grid[y][x + 1]) {
                        isPossible = false;
                        break;
                    }
                }
                if (isPossible) {
                    for (int i = 3; i >= 0; i--) {
                        int x = shapeXes[i];
                        int y = shapeYs[i];

//                        grid[y][x] = false;
                        grid[y][x + 1] = true;
                        shapeXes[i] += 1;
                    }
                    nextStep();
                }
            }

            if (keyCode == e.VK_DOWN) {
                //TODO test if program breaks from this
                nextStep();
            }

            if (keyCode == e.VK_UP) {
                rotate();
            }
        }
    }

    private void rotate() {
        int distance = 0;
        int leftSpace = 0;
        int leftSpace2 = 0;
        int rightSpace = 0;
        int rightSpace2 = 0;
        switch (shapeIndex) {

            case 1:
                distance = distanceDown(shapeYs[0], shapeXes[0]);
                if (distance == 0) {
                    upShift(2);
                } else if (distance == 1) {
                    upShift();
                }
                shapeIndex = 8;
                visibleFallingBlocks(false);

                shapeXes[0] += 1;
                shapeYs[1] += 1;
                shapeXes[2] -= 1;
                shapeXes[3] -= 2;
                shapeYs[3] += 1;

                visibleFallingBlocks(true);
                break;

            case 2:
                distance = distanceDown(shapeYs[3], shapeXes[3]);
                if (distance == 0) {
                    upShift(2);
                } else if (distance == 1) {
                    upShift();
                }
                shapeIndex = 9;
                visibleFallingBlocks(false);

                shapeXes[0] += 1;
                shapeYs[0] -= 1;
                shapeXes[2] += 1;
                shapeYs[2] += 1;
                shapeYs[3] += 2;

                visibleFallingBlocks(true);
                break;

            case 3:
                distance = distanceDown(shapeYs[2], shapeXes[2]);
                if (distance == 2) {
                    upShift();
                } else if (distance == 1) {
                    upShift(2);
                } else if (distance == 0) {
                    upShift(3);
                }
                shapeIndex = 10;
                visibleFallingBlocks(false);

                shapeXes[0] += 2;
                shapeYs[1] += 1;
                shapeXes[1] += 1;
                shapeYs[2] += 2;
                shapeYs[3] += 3;
                shapeXes[3] -= 1;

                visibleFallingBlocks(true);
                break;

            case 4:
                distance = distanceDown(shapeYs[2], shapeXes[2]);
                if(distance == 1) {
                    upShift();
                } else if (distance == 0) {
                    upShift(2);
                }
                shapeIndex = 11;
                visibleFallingBlocks(false);
                shapeYs[0] -= 1;
                shapeXes[1] += 1;
                shapeYs[2] += 1;
                shapeXes[3] -= 1;
                shapeYs[3] += 2;

                visibleFallingBlocks(true);
                break;

            case 5:
                distance = distanceDown(shapeYs[1], shapeXes[1]);
                if(distance == 0)upShift();
                if(shapeYs[0] > 0) {
                    shapeIndex = 14;
                    visibleFallingBlocks(false);
                    shapeYs[0] -= 1;
                    shapeXes[0] += 2;
                    shapeXes[1] += 1;
                    shapeYs[2] += 1;
                    shapeXes[3] -=1;
                    visibleFallingBlocks(true);
                }
                break;

            case 6:
                if (shapeYs[0] > 0) {
                    if(!grid[shapeYs[1] - 1][shapeXes[1]]) {
                        shapeIndex = 17;
                        visibleFallingBlocks(false);
                        shapeYs[2] -= 1;
                        shapeXes[2] -= 1;
                        visibleFallingBlocks(true);
                    }
                }
                break;
            case 8:
                visibleFallingBlocks(false);

                leftSpace = distanceLeft(shapeYs[2],shapeXes[2]);
                rightSpace = distanceRight(shapeYs[1], shapeXes[1]);
                leftSpace2 = distanceLeft(shapeYs[0],shapeXes[0]);
                rightSpace2 = distanceRight(shapeYs[3],shapeXes[3]);
                if(rightSpace == 0 && leftSpace > 0 && leftSpace2 > 1)sideShift(-1);
//                if(leftSpace == 0 && rightSpace > 1 && leftSpace2 >= 1)sideShift(1);
                //TODO can bug fix later
                if((rightSpace + leftSpace> 1 && leftSpace2 > 0) || (rightSpace + leftSpace == 1 && leftSpace2 > 1)
                        || (rightSpace + leftSpace > 0 && leftSpace2 == 1 && rightSpace2 > 1)) {
                    shapeIndex = 1;

                    shapeXes[0] -= 1;
                    shapeYs[1] -= 1;
                    shapeXes[2] += 1;
                    shapeXes[3] += 2;
                    shapeYs[3] -= 1;

                }
                visibleFallingBlocks(true);
                break;

            case 9:
                visibleFallingBlocks(false);

                leftSpace = distanceLeft(shapeYs[1],shapeXes[1]);
                rightSpace = distanceRight(shapeYs[2], shapeXes[2]);
                rightSpace2 = distanceRight(shapeYs[0],shapeXes[0]);
                leftSpace2 = distanceLeft(shapeYs[3],shapeXes[3]);
                if(leftSpace == 0 && rightSpace > 0 && rightSpace2 > 1)sideShift(1);
                if((rightSpace + leftSpace> 1 && rightSpace2 > 0) || (rightSpace + leftSpace == 1 && rightSpace2 > 1)
                        || (rightSpace + leftSpace > 0 && rightSpace2 == 1 && leftSpace2 > 1)) {
                    shapeIndex = 2;

                    shapeXes[0] -= 1;
                    shapeYs[0] += 1;
                    shapeXes[2] -= 1;
                    shapeYs[2] -= 1;
                    shapeYs[3] -= 2;

                }
                visibleFallingBlocks(true);
                break;

            case 10:
                leftSpace = distanceLeft(shapeYs[0], shapeXes[0]);
                rightSpace = distanceRight(shapeYs[0], shapeXes[0]);

                visibleFallingBlocks(false);

                if (leftSpace == 1 && rightSpace > 1) {
                    sideShift(1);
                } else if (leftSpace == 0 && rightSpace > 2) {
                    sideShift(2);
                } else if (rightSpace == 0 && leftSpace > 2) {
                    sideShift(-1);
                }

                if (leftSpace + rightSpace > 2) {
                    shapeIndex = 3;

                    shapeXes[0] -= 2;
                    shapeYs[1] -= 1;
                    shapeXes[1] -= 1;
                    shapeYs[2] -= 2;
                    shapeYs[3] -= 3;
                    shapeXes[3] += 1;
                }
                visibleFallingBlocks(true);
                break;

            case 11:
                leftSpace = distanceLeft(shapeYs[2], shapeXes[2]);
                rightSpace = distanceRight(shapeYs[2], shapeXes[2]);
                rightSpace2 = distanceRight(shapeYs[1], shapeXes[1]);
                visibleFallingBlocks(false);
                if(leftSpace < 2 && leftSpace + rightSpace >= 2 && rightSpace2 > 0) sideShift(2 - leftSpace);
                if(distanceLeft(shapeYs[2], shapeXes[2]) >= 2) {
                    shapeIndex = 12;
                    shapeXes[0] += 1;
                    shapeYs[1] += 1;
                    shapeXes[2] -= 1;
                    shapeYs[3] -= 1;
                    shapeXes[3] -= 2;
                }
                visibleFallingBlocks(true);
                break;

            case 12:
                visibleFallingBlocks(false);
                if (shapeYs[0] > 0) {
                    shapeIndex = 13;

                    shapeYs[0] += 1;
                    shapeXes[1] -= 1;
                    shapeYs[2] -= 1;
                    shapeYs[3] -= 2;
                    shapeXes[3] += 1;
                }
                visibleFallingBlocks(true);
                break;

            case 13:
                rightSpace = distanceRight(shapeYs[2], shapeXes[2]);
                leftSpace = distanceLeft(shapeYs[2], shapeXes[2]);
                leftSpace2 = distanceLeft(shapeYs[1], shapeXes[1]);
                visibleFallingBlocks(false);
                if(rightSpace < 2 && leftSpace + rightSpace >= 2 && leftSpace2 > 0) sideShift(rightSpace - 2);
                if(distanceRight(shapeYs[2], shapeXes[2]) >= 2) {
                    shapeIndex = 4;
                    shapeXes[0] -= 1;
                    shapeYs[1] -= 1;
                    shapeXes[2] += 1;
                    shapeYs[3] += 1;
                    shapeXes[3] += 2;
                }
                visibleFallingBlocks(true);
                break;

            case 14:
                leftSpace = distanceLeft(shapeYs[1], shapeXes[1]);
                rightSpace = distanceRight(shapeYs[2], shapeXes[2]);
                leftSpace2 = distanceLeft(shapeYs[3], shapeXes[3]);
                if(rightSpace == 0 && leftSpace > 1 && leftSpace2 > 0)sideShift(-1);

                if(distanceRight(shapeYs[2], shapeXes[2]) > 0) {
                    shapeIndex = 15;
                    visibleFallingBlocks(false);
                    shapeYs[0] += 2;
                    shapeXes[0] += 1;
                    shapeYs[1] += 1;
                    shapeXes[2] -= 1;
                    shapeYs[3] -= 1;
                    visibleFallingBlocks(true);
                }
                break;

            case 15:
                distance = distanceDown(shapeYs[1], shapeXes[1]);
                if(distance == 0)upShift();
                shapeIndex = 16;
                visibleFallingBlocks(false);
                shapeYs[0] += 1;
                shapeXes[0] -= 2;
                shapeXes[1] -= 1;
                shapeYs[2] -= 1;
                shapeXes[3] += 1;
                visibleFallingBlocks(true);
                break;

            case 16:
                rightSpace = distanceRight(shapeYs[1], shapeXes[1]);
                leftSpace = distanceLeft(shapeYs[2], shapeXes[2]);
                rightSpace2 = distanceRight(shapeYs[3], shapeXes[3]);
                if(leftSpace == 0 && rightSpace > 1 && rightSpace2 > 0)sideShift(1);

                //TODO minor bug with rotating to fill a spot in the last moment
                if(distanceLeft(shapeYs[2], shapeXes[2]) > 0) {
                    shapeIndex = 5;
                    visibleFallingBlocks(false);
                    shapeYs[0] -= 2;
                    shapeXes[0] -= 1;
                    shapeYs[1] -= 1;
                    shapeXes[2] += 1;
                    shapeYs[3] += 1;
                    visibleFallingBlocks(true);
                }
                break;

            case 17:
                rightSpace = distanceRight(shapeYs[1], shapeXes[1]);
                leftSpace = distanceLeft(shapeYs[0], shapeXes[0]);
                leftSpace2 = distanceLeft(shapeYs[2], shapeXes[2]);
                visibleFallingBlocks(false);
                if (rightSpace < 1 && leftSpace > 0 && leftSpace2 > 0) {
                    for (int i = 0; i < 4; i ++) {
                        shapeXes[i] -= 1;
                    }
                }
                if(distanceRight(shapeYs[1], shapeXes[1]) > 0) {
                    shapeIndex = 18;
                    shapeYs[3] -= 1;
                    shapeXes[3] += 1;

                }
                visibleFallingBlocks(true);
                break;

            case 18:
                if(distanceDown(shapeYs[1],shapeXes[1]) == 0)upShift();
                visibleFallingBlocks(false);
                shapeIndex = 19;
                shapeYs[0] += 1;
                shapeXes[0] += 1;
                visibleFallingBlocks(true);
                break;

            case 19:
                leftSpace = distanceLeft(shapeYs[1], shapeXes[1]);
                rightSpace = distanceRight(shapeYs[3], shapeXes[3]);
                rightSpace2 = distanceRight(shapeYs[0], shapeXes[0]);
                visibleFallingBlocks(false);
                if(leftSpace < 1 && rightSpace > 0 & rightSpace2 > 0) {
                    for (int i = 0; i < 4; i++) {
                        shapeXes[i] += 1;
                    }
                 }
                 if (distanceLeft(shapeYs[1], shapeXes[1]) > 0) {
                    shapeIndex = 6;
                    shapeYs[0] -= 1;
                    shapeXes[0] -= 1;
                    shapeYs[2] += 1;
                    shapeXes[2] += 1;
                    shapeYs[3] += 1;
                    shapeXes[3] -= 1;
                 }
                visibleFallingBlocks(true);
                break;

        }
        repaint();
    }

    //make the falling shape visible/invisible
    private void visibleFallingBlocks (boolean isVisible) {
        for (int i = 0; i < 4; i++) {
            grid[shapeYs[i]][shapeXes[i]] = isVisible;
        }
    }

    //returns number of empty spaces between this block and the bottom (or closest block down)
    private int distanceDown(int y, int x) {
        int count = 1;
        while (count < gridH - y && !grid[y + count][x]) {
            count++;
        }
        return count - 1;
    }

    private int distanceLeft(int y, int x) {
        int count = 1;
        while (x - count >= 0 && !grid[y][x - count]) {
            count++;
        }
        return count - 1;
    }

    private int distanceRight(int y, int x) {
        int count = 1;
        while (x + count < 12 && !grid[y][x + count]) {
            count++;
        }
        return count - 1;
    }

    //TODO can result in bugs that make blocks disappear
    //TODO can break the game with array index exception -1
    //moves the falling blocks up
    private void upShift(int times) {
        for (int i = 0; i < 4; i++) {
            grid[shapeYs[i]][shapeXes[i]] = false;
        }
        for (int i = 0; i < 4; i++) {
            shapeYs[i] -= times;
            grid[shapeYs[i]][shapeXes[i]] = true;
        }
    }
    private void sideShift(int times) {
        for (int i = 0; i < 4; i++) {
            grid[shapeYs[i]][shapeXes[i]] = false;
        }
        for (int i = 0; i < 4; i++) {
            shapeXes[i] += times;
            grid[shapeYs[i]][shapeXes[i]] = true;
        }
    }

    private void upShift() {
        upShift(1);
    }

    private class MyWorker extends SwingWorker {

        private TetrisGrid gridRef;


        private MyWorker(TetrisGrid gridRef) {
            this.gridRef = gridRef;
        }

        //should call next step every second
        @Override
        protected Object doInBackground() throws Exception {
            while (true) {
                Thread.sleep(1000);
                gridRef.nextStep();
            }
        }
    }
}
