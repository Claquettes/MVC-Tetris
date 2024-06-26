package views;

import javax.imageio.ImageIO;
import javax.management.monitor.GaugeMonitor;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.TileObserver;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import models.Piece;

import controllers.GameController;
import models.Grid;

/**
 * Class to create the main board of the game
 * 
 * This class is used to create the main board of the game. It contains the title, score, best score, level, next piece, hold piece, and game over label. It also contains methods to start the game, update the game, reset the game, draw the grid, clear the display grid, and draw the piece preview.
 * 
 * @version 1.0
 * @since 2024-04-14
 * @see GameController
 * @see Grid
 */
public class MainBoard extends JPanel implements Observer {

    private static GameController gameController;
    private static Grid currentGrid;
    private static JLabel Title;
    private static JLabel LabelScore;
    private static JLabel Score;
    private static JLabel LabelBestScore;
    private static JLabel BestScore;
    private static JLabel LabelLevel;
    private static JLabel Level;
    private static JLabel NextPiece;
    private static JLabel HoldPiece;
    private static JLabel GameOver;
    private static Font font;
    private static Font titleFont;

    private Image backgroundImage;

    /**
     * Constructor for the MainBoard class
     * 
     * @see GameController
     * @see Grid
     */
    public MainBoard(float volume) {
        setLayout(null);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/Font/telelower.ttf"));
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/Font/Tetris.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            backgroundImage = ImageIO.read(new File("assets/Mockup/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Title = new JLabel("Tetris");
        Title.setForeground(new Color(255, 235, 59));
        Title.setFont(titleFont.deriveFont(50f));
        Title.setBounds(150, 15, 500, 50);
        add(Title);

        LabelScore = new JLabel("Score: ");
        LabelScore.setForeground(new Color(244, 67, 54));
        LabelScore.setFont(font.deriveFont(25f));
        LabelScore.setBounds(430, 100, 500, 25);
        add(LabelScore);

        Score = new JLabel("0");
        Score.setForeground(new Color(244, 67, 54));
        Score.setFont(font.deriveFont(25f));
        Score.setBounds(430, 125, 500, 25);
        add(Score);

        LabelBestScore = new JLabel("Best Score: ");
        LabelBestScore.setForeground(new Color(76, 175, 80));
        LabelBestScore.setFont(font.deriveFont(25f));
        LabelBestScore.setBounds(430, 175, 500, 25);
        add(LabelBestScore);
        
        BestScore = new JLabel("0");
        BestScore.setForeground(new Color(76, 175, 80));
        BestScore.setFont(font.deriveFont(25f));
        BestScore.setBounds(430, 200, 500, 25);
        add(BestScore);

        LabelLevel = new JLabel("Level: ");
        LabelLevel.setForeground(new Color(255, 152, 0));
        LabelLevel.setFont(font.deriveFont(25f));
        LabelLevel.setBounds(430, 600, 500, 25);
        add(LabelLevel);

        Level = new JLabel("1");
        Level.setForeground(new Color(255, 152, 0));
        Level.setFont(font.deriveFont(25f));
        Level.setBounds(510, 600, 500, 25);
        add(Level);

        NextPiece = new JLabel("Next Piece");
        NextPiece.setForeground(new Color(244, 67, 54));
        NextPiece.setFont(font.deriveFont(25f));
        NextPiece.setBounds(430, 270, 500, 25);
        add(NextPiece);

        HoldPiece = new JLabel("Hold Piece");
        HoldPiece.setForeground(new Color(244, 67, 54));
        HoldPiece.setFont(font.deriveFont(25f));
        HoldPiece.setBounds(430, 450, 500, 25);
        add(HoldPiece);

        GameOver = new JLabel("Game Over");
        GameOver.setForeground(new Color(244, 67, 54));
        GameOver.setFont(font.deriveFont(60f));
        GameOver.setBounds(100, 200, 500, 70);
        add(GameOver);
        GameOver.setVisible(false);

        currentGrid = new Grid(volume);
        currentGrid.addObserver(this);
        gameController = new GameController(currentGrid, this);
    
        BestScore.setText(""+currentGrid.returnBestScore());
        //we reset the next piece and hold piece
        currentGrid.returnNextPiece();
        currentGrid.returnHoldPiece();
        this.start();
    }

    /**
     * Method to paint the components of the board
     * 
     * @param g the graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, -10, 0, this);
        drawGrid(currentGrid.returnGrid(), g);
        drawPiecePreview(currentGrid.returnNextPiece(), g, 455, 300, 25);
        drawPiecePreview(currentGrid.returnHoldPiece(), g, 455, 480, 25);
    }

    /**
     * Method to start the game
     * 
     */
    public void start() {
        currentGrid.start();
    }

    /**
     * Method to quit or restart the game when it is over
     * 
     */
    public void update(Observable o, Object arg) {
        if (arg == null && !GameOver.isVisible()) {
            repaint();

            Score.setText("" + currentGrid.returnScore());
            Level.setText("" + currentGrid.getLevel());
        } else {
            if (arg == "Game Over") {
                System.out.println("Game Over");
                if (!GameOver.isVisible())
                    clearDisplayGrid();
                GameOver.setVisible(true);

                // Affiche une boîte de dialogue
                int response = JOptionPane.showOptionDialog(null, "Game Over. Que voulez-vous faire ?", "Game Over",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new String[] { "Rejouer", "Quitter" }, "Rejouer");

                if (response == 0) {
                    // Si l'utilisateur choisit "Rejouer", réinitialisez le jeu
                    System.out.println("Rejouer");
                    resetGame();
                } else {
                    // Si l'utilisateur choisit "Quitter", arrêtez le jeu
                    System.out.println("Quitter");
                    System.exit(0);
                }
            } else {
                System.err.println("error obervation arg not recognized");
            }
        }
    }

    /**
     * Method to reset the game
     * 
     */
    public void resetGame() {
        GameOver.setVisible(false);
        currentGrid.reset();
        Score.setText("0");
        BestScore.setText(""+currentGrid.returnBestScore());
    }

    /**
     * Method to draw the grid of the game with colors and shadows
     * 
     * @param grid the grid of the game
     * @param g the graphics object
     */
    public void drawGrid(int[][] grid, Graphics g) {
        // clear le pannel
        g.setColor(new Color(47, 39, 41));
        g.fillRect(10, 140, 400, 800);
    
        // affiche des carré de couleur rouge où grid contient 1
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    Color baseColor = TetrisColor.getColorByValue(grid[i][j]).getColor();
                    g.setColor(baseColor);
                    g.fillRect(10 + i * 40, 140 + j * 40, 40, 40); // décalé de 10px à gauche et 30px vers le haut
    
                    // Draw shadows
                    Color shadowColor = baseColor.darker().darker(); // Make the shadow darker
                    g.setColor(shadowColor);
                    g.fillRect(10 + i * 40, 140 + j * 40, 5, 40); // Shadow on the left side
                    g.fillRect(10 + i * 40, 140 + j * 40 + 35, 40, 5); // Shadow on the bottom side
    
                    // Draw highlights
                    Color highlightColor = baseColor.brighter().brighter(); // Make the highlight brighter
                    g.setColor(highlightColor);
                    g.fillRect(10 + i * 40, 140 + j * 40, 40, 5); // Highlight on the top side
                    g.fillRect(10 + i * 40 + 35, 140 + j * 40, 5, 40); // Highlight on the right side
                }
            }
        }
    }

    /**
     * Method to clear the grid of the game
     * 
     */
    public void clearDisplayGrid() {
        Graphics g = this.getGraphics();
        g.setColor(new Color(47, 39, 41));
        g.fillRect(10, 140, 400, 800);
    }

    /**
     * Method to draw the preview of the piece falling
     * 
     * @param p the piece to draw
     * @param g the graphics object
     * @param x the x position
     * @param y the y position
     * @param blocSize the size of the bloc
     */
    public void drawPiecePreview(Piece p, Graphics g, int x, int y, int blocSize) {
        if (p != null) {
            int[][] shape = p.getShape();
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        g.setColor(TetrisColor.getColorByValue(8).getColor());
                        g.fillRect(x + i * blocSize, y + j * blocSize, blocSize, blocSize);
                    }
                }
            }
        }
    }

}
