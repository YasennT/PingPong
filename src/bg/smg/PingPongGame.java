package bg.smg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PingPongGame extends JFrame {

    private final int WIDTH = 900, HEIGHT = 600;
    private final int HILKA_WIDTH = 25, HILKA_HEIGHT = 120, BALL_SIZE = 30;

    private int hilka1Y = HEIGHT/2 - HILKA_HEIGHT/2, hilka2Y = HEIGHT/2 - HILKA_HEIGHT/2;
    private double ballX = WIDTH/2.0, ballY = HEIGHT/2.0;
    private double ballSpeedX = 10, ballSpeedY = 4;

    private int tochki1 = 0,tochki2 = 0;
    private final JLabel tochkiP1 = new JLabel("Player 1: 0");
    private final JLabel tochkiP2 = new JLabel("Player 2: 0");

    private String NameP1, NameP2;
    private String difficulty;

    // ПОЛЕ
    public PingPongGame() {
        getPlayerNames();

        setTitle("Ping Pong Game");
        setSize(WIDTH, HEIGHT+100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        switch (difficulty) {
            case "Easy" -> {
                ballSpeedX = 6.25;
                ballSpeedY = 2.25;
            }
            case "Normal" -> {
                ballSpeedX = 7.5;
                ballSpeedY = 3;
            }
            case "Hard" -> {
                ballSpeedX = 10;
                ballSpeedY = 4;
            }
        }
        ResultsPanel();
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        Timer timer = new Timer(10, e -> {
            updateGame();
            repaint();
        });
        timer.start();

        setVisible(true);
    }

    // МЕСТЕНЕ НА ХИЛКИТЕ
    private void handleKeyPress(KeyEvent e) {
        int buton = e.getKeyCode();

        if (buton == KeyEvent.VK_UP && hilka2Y>0) {
            hilka2Y -= 25;
        } else if (buton == KeyEvent.VK_DOWN && hilka2Y < HEIGHT - HILKA_HEIGHT) {
            hilka2Y += 25;
        }

        if (buton == KeyEvent.VK_W && hilka1Y>0) {
            hilka1Y -= 25;
        } else if (buton == KeyEvent.VK_S && hilka1Y < HEIGHT - HILKA_HEIGHT) {
            hilka1Y += 25;
        }
    }

    private void updateGame() {

        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // ТОПЧЕ В СТЕНА
        if (ballY<=0 || ballY >= HEIGHT - BALL_SIZE) {
            ballSpeedY = -ballSpeedY;
        }

        // ТОПЧЕ В ХИЛКА
        if (ballX <= HILKA_WIDTH && ballY + BALL_SIZE >= hilka1Y && ballY <= hilka1Y + HILKA_HEIGHT) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballX + BALL_SIZE >= WIDTH - HILKA_WIDTH && ballY + BALL_SIZE >= hilka2Y && ballY <= hilka2Y + HILKA_HEIGHT) {
            ballSpeedX = -ballSpeedX;
        }

        // АКО ИЗЛЕЗЕ
        if (ballX <= 0 || ballX + BALL_SIZE >= WIDTH) {
            if (ballX <= 0) {
                tochki2++;
            } else tochki1++;
            points();

            ballX = WIDTH/2.0 - BALL_SIZE/2.0;
            ballY = HEIGHT/2.0 - BALL_SIZE/2.0;
        }
    }

    private void points() {
        tochkiP1.setText(NameP1 + ": " + tochki1);
        tochkiP2.setText(NameP2 + ": " + tochki2);
    }

    // РЕЗУЛТАТИ
    private void ResultsPanel() {
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        scorePanel.add(tochkiP1);
        scorePanel.add(tochkiP2);

        add(scorePanel, BorderLayout.SOUTH);
    }

    // ПРОЗОРЕЦ С ИМЕНА И ТРУДНОСТ
    private void getPlayerNames() {
        JTextField p1Field = new JTextField();
        JTextField p2Field = new JTextField();

        JComboBox<String> trudnostBox = new JComboBox<>(new String[]{"Easy", "Normal", "Hard"});

        JPanel NamePanel = new JPanel(new GridLayout(3, 2));
        NamePanel.add(new JLabel("Player 1 Name:"));
        NamePanel.add(p1Field);
        NamePanel.add(new JLabel("Player 2 Name:"));
        NamePanel.add(p2Field);
        NamePanel.add(new JLabel("Difficulty:"));
        NamePanel.add(trudnostBox);

        int choice = JOptionPane.showConfirmDialog(null, NamePanel, "Enter Player Names", JOptionPane.OK_CANCEL_OPTION);
        if (JOptionPane.OK_OPTION == choice) {
            NameP1 = p1Field.getText();
            NameP2 = p2Field.getText();
            difficulty = (String) trudnostBox.getSelectedItem();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // ХИЛКИ
        g.setColor(Color.WHITE);
        g.fillRect(HILKA_WIDTH, hilka1Y, HILKA_WIDTH, HILKA_HEIGHT);

        g.fillRect(WIDTH - 2*HILKA_WIDTH, hilka2Y, HILKA_WIDTH, HILKA_HEIGHT);

        // ТОПЧЕ
        g.fillOval((int) ballX, (int) ballY, BALL_SIZE, BALL_SIZE);
    }

}