package bg.smg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PingPongGame extends JFrame {

    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int hilka_WIDTH = 20;
    private final int hilka_HEIGHT = 80;
    private final int BALL_SIZE = 20;

    private int hilka1Y = HEIGHT / 2 - hilka_HEIGHT / 2;
    private int hilka2Y = HEIGHT / 2 - hilka_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballSpeedX = 5;
    private int ballSpeedY = 2;

    public PingPongGame() {
        setTitle("Ping Pong Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }
        });
        timer.start();

        setFocusable(true);
        setVisible(true);
    }

    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP && hilka2Y > 0) {
            hilka2Y -= 20;
        } else if (keyCode == KeyEvent.VK_DOWN && hilka2Y < HEIGHT - hilka_HEIGHT) {
            hilka2Y += 20;
        }

        if (keyCode == KeyEvent.VK_W && hilka1Y > 0) {
            hilka1Y -= 20;
        } else if (keyCode == KeyEvent.VK_S && hilka1Y < HEIGHT - hilka_HEIGHT) {
            hilka1Y += 20;
        }
    }

    private void updateGame() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Ball and wall collisions
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballSpeedY = -ballSpeedY;
        }

        // Ball and hilkas collisions
        if (ballX <= hilka_WIDTH && ballY + BALL_SIZE >= hilka1Y && ballY <= hilka1Y + hilka_HEIGHT) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballX + BALL_SIZE >= WIDTH - hilka_WIDTH && ballY + BALL_SIZE >= hilka2Y && ballY <= hilka2Y + hilka_HEIGHT) {
            ballSpeedX = -ballSpeedX;
        }

        // Ball out of bounds
        if (ballX <= 0 || ballX + BALL_SIZE >= WIDTH) {
            ballX = WIDTH / 2 - BALL_SIZE / 2;
            ballY = HEIGHT / 2 - BALL_SIZE / 2;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.fillRect(hilka_WIDTH, hilka1Y, hilka_WIDTH, hilka_HEIGHT);
        g.fillRect(WIDTH - 2 * hilka_WIDTH, hilka2Y, hilka_WIDTH, hilka_HEIGHT);

        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
    }
}