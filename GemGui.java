/*
* Tyler Esterly
* CS251
* This class creates the GUI for, and
* initializes a Bejewled-style Gem-Match game.
*It uses the GemManager class to manage
* the bookkeeping.*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GemGui {
    public GemManager manager;
    JLabel scoreLabel;
    JLabel timeLabel;
    JButton restartButton = new JButton("Restart Game");
    int rows;
    int columns;
    int score;
    int currentCombo = 0;
    int largestCombo = 0;
    int clickRow, clickColumn, releaseRow, releaseColumn;
    static int highestScore = 0;
    boolean inClick = false;
    boolean cascade = false;
    boolean swapped = false;
    int startAnimation;
    int currentTime = 120;
    Timer gameTimer;
    Timer timer = new Timer(800,new animateCascade());


    public class GamePanel extends JPanel {

        public int getRowHeight() {
            return getHeight() / rows;
        }

        public int getColumnWidth() {
            return getWidth() / columns;
        }

        public void paintComponent(Graphics g) {
            int rowHeight = getRowHeight();
            int columnWidth = getColumnWidth();
            int rowOffset = rowHeight / rows;
            int columnOffset = columnWidth / columns;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (manager.gameBoard[i][j] == null) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(manager.gameBoard[i][j].gemColor);
                    }
                    g.fillOval(j * columnWidth + columnOffset,
                            i * rowHeight + rowOffset,
                            columnWidth - 2 * columnOffset,
                            rowHeight - 2 * rowOffset);
                }
                if (inClick) {
                    int x = (clickColumn / columnWidth) * columnWidth;
                    int y = (clickRow / rowHeight) * rowHeight;
                    g.setColor(Color.yellow);
                    g.drawRect(x, y, columnWidth, rowHeight);
                }
            }
        }
    }

    public class animateCascade implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if (swapped){
                gameFrame.repaint();
                swapped = false;
            }
            else if (cascade == true) {
                manager.cascade();
                gameFrame.repaint();
                cascade = false;
            }
            else if (manager.checkMatch()) {
                    currentCombo++;
                    manager.clear();
                    gameFrame.repaint();
                    startAnimation++;
                    score += manager.gemRemoval*currentCombo;
                    scoreLabel.setText("Score: " + score + "   Combo: " + currentCombo);
                    cascade = true;
            }
            else{
                if (currentCombo > largestCombo){
                    largestCombo = currentCombo;
                }
                currentCombo = 0;
                timer.stop();
            }

        }
    }

    public class InfoPanel extends JPanel implements ActionListener{
        JPanel gridPanel;
        public InfoPanel(){
            gridPanel = new JPanel();
            scoreLabel = new JLabel();
            timeLabel = new JLabel();
            restartButton = new JButton("Restart Game");
            restartButton.addActionListener(this);
            timeLabel.setBorder(BorderFactory.createLineBorder((Color.black),1));
            scoreLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            scoreLabel.setText("Score: \n" + score);
            timeLabel.setText("Time: " + currentTime);
            gridPanel.setPreferredSize(new Dimension(800,50));
            gridPanel.setLayout(new GridLayout(1,3,10,100));
            gridPanel.add(scoreLabel);
            gridPanel.add(restartButton);
            gridPanel.add(timeLabel);

            gameFrame.getContentPane().add(gridPanel, BorderLayout.SOUTH);
        }

        public void actionPerformed(ActionEvent e) {
            gameFrame.invalidate();
            gameFrame.dispose();
            runGame();
            currentTime= 0;
            gameTimer.restart();
        }
    }



        private void swap(int row1, int column1, int row2, int column2){
            startAnimation = 1;
            timer.setInitialDelay(5);
            manager.swapGems(row1, column1, row2, column2);
            swapped = true;
            timer.start();
        }

        void doMouseClick(int y1, int x1, int y2, int x2){
            int row1 = y1/gamePanel.getRowHeight();
            int col1 = x1/gamePanel.getColumnWidth();
            int row2 = y2/gamePanel.getRowHeight();
            int col2 = x2/gamePanel.getColumnWidth();
            swap(row1,col1,row2,col2);
        }


    Action updateTimer = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            currentTime--;
            timeLabel.setText("Time: " + currentTime);
            if (currentTime == 100){
                manager.numGems++;
            }
            if (currentTime == 80){
                manager.numGems++;
            }
            if (currentTime == 70){
                manager.numGems++;
            }
            if (currentTime == 0){
                if (highestScore < score) {
                    JOptionPane.showMessageDialog(gameFrame.getContentPane(),
                            "New High Score! \n" + "Old High Score: " + highestScore +
                                    "\nNew High Score: " + score +
                                    "\nBiggest Combo: " + largestCombo, "Time Up!",
                            JOptionPane.INFORMATION_MESSAGE);
                    highestScore = score;
                }
                else{
                    JOptionPane.showMessageDialog(gameFrame.getContentPane(),
                                    "Your Score: " + score + "\nHigh Score: " + highestScore +
                                    "\nBiggest Combo: " + largestCombo, "Time Up!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                gameFrame.invalidate();
                gameFrame.dispose();
                runGame();
            }
        }
    };

    private JFrame gameFrame;
    private GamePanel gamePanel;

    public GemGui(){
        GemManager manager = new GemManager(9,8);
        this.manager = manager;
        this.rows= manager.rows;
        this.columns = manager.columns;
        gameFrame = new JFrame("Gem Match");
        gamePanel = new GamePanel();
        InfoPanel infoPanel = new InfoPanel();
        gameTimer = new Timer(1000,updateTimer);
        gamePanel.setPreferredSize(new Dimension(85*rows,85*columns));
        gamePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!inClick) {
                    clickColumn = e.getX();
                    clickRow = e.getY();
                    inClick = true;
                    gameFrame.repaint();
                    }
                else if (inClick){
                    releaseColumn = e.getX();
                    releaseRow = e.getY();
                    doMouseClick(clickRow,clickColumn,releaseRow,releaseColumn);
                    inClick = false;
                    }
                }
            });
        gameFrame.add(gamePanel, BorderLayout.CENTER);
        gameFrame.getContentPane().setBackground(Color.black);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameTimer.start();
        gameFrame.pack();
        gameFrame.setVisible(true);
    }


    public void runGame(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GemGui gui = new GemGui();
            }
        });
    }
    public static void main(String[] args){
        GemGui gui = new GemGui();
    }
}
