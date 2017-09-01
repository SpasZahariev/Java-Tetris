
import javax.swing.*;
import java.awt.*;

public class ScoreLabel extends JLabel {

//    private static JTextField textField;
    private static int score = 0;

    public ScoreLabel() {
        super("Score: " + score, JLabel.CENTER);
        super.setFont(new Font("SansSerif",Font.BOLD,40));
//        textField = new JTextField("Score: " + score, JLabel.CENTER);
//        textField.setHorizontalAlignment(JLabel.CENTER);
//        setVisible(true);

    }
    public void increaseScore() {
        score += 1;
        super.setText("Score: " + score);
        //TODO see if I need the bottom line
//        textField.setText("Score: " + score);
//        repaint();
    }

    public void gameOver() {
        super.setText("GAME OVER");
//        repaint();
    }
}
