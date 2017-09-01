

import javax.swing.*;
import java.awt.*;

/**
 * Created by SPAS on 25/08/2017.
 */
public class TetrisFrame extends JFrame{

    private TetrisGrid tGrid;
    private ScoreLabel sPanel;

    public void setUp() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setSize(720, 950);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelContainer = new JPanel();
        add(panelContainer);
//        panelContainer.setBackground(Color.black);
        panelContainer.setLayout(new BorderLayout(0,0));

        sPanel = new ScoreLabel();
//        sPanel.setPreferredSize(new Dimension(720, 15));
        tGrid = new TetrisGrid(sPanel);

        tGrid.setBackground(Color.black);
        panelContainer.add(sPanel,BorderLayout.SOUTH);
        panelContainer.add(tGrid,BorderLayout.CENTER);
        panelContainer.add(new JPanel(),BorderLayout.EAST);
        panelContainer.add(new JPanel(),BorderLayout.WEST);
        this.setVisible(true);
        tGrid.startGame();
    }
}
