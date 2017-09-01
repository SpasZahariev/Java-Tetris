

import javax.swing.*;

/**
 * Created by SPAS on 25/08/2017.
 */
public class BoardSetUp {

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        TetrisFrame tFrame = new TetrisFrame();
                        tFrame.setUp();
                    }
                }
        );
    }
}

