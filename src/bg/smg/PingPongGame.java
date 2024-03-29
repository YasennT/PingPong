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
    private final JLabel tochkiP1 = new JLabel("Player 1: 0", JLabel.CENTER);
    private final JLabel tochkiP2 = new JLabel("Player 2: 0", JLabel.CENTER);
    private String NameP1, NameP2;
    private String difficulty, table;

    // ПОЛЕ
    public PingPongGame() {
        getPlayerNames();

        setTitle("Ping Pong Game");
        setSize(WIDTH, HEIGHT+75);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikona();
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

    // ИКОНАТА НА ПРОЗОРЕЦА
    private void ikona()
    {
        ImageIcon img = new ImageIcon("resources/Ikonka.png");
        this.setIconImage(img.getImage());
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

        // КРАЙ НА ИГРАТА
        if(tochki1 == 10 || tochki2 == 10) {
            endGame();
        }

    }

    private void points() {
        tochkiP1.setText(NameP1 + ": " + tochki1);
        tochkiP2.setText(NameP2 + ": " + tochki2);
    }

    // КРАЙ НА ИГРАТА
    private void endGame() {
        String winner;
        if (tochki1 == 10) {
            winner = NameP1;
        } else {
            winner = NameP2;
        }

        Object[] options = {"Да", "Не"};
        int end = JOptionPane.showOptionDialog(this,
                winner + " спечели! Искате ли да играете пак?",
                "Край на играта",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (end == JOptionPane.YES_OPTION) {
            startNewGame();
        } else System.exit(0);
    }

    // НОВА ИГРА
    private void startNewGame() {
        tochki1 = 0;
        tochki2 = 0;
        points();

        ballX = WIDTH/2.0 - BALL_SIZE/2.0;
        ballY = HEIGHT/2.0 - BALL_SIZE/2.0;

        hilka1Y = HEIGHT/2 - HILKA_HEIGHT/2;
        hilka2Y = HEIGHT/2 - HILKA_HEIGHT/2;
    }

    // РЕЗУЛТАТИ
    private void ResultsPanel() {
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(0,2));

        scorePanel.add(tochkiP1);
        scorePanel.add(tochkiP2);
        tochkiP1.setFont(new Font("Arial", Font.PLAIN, 25));
        tochkiP2.setFont(new Font("Arial", Font.PLAIN, 25));

        add(scorePanel, BorderLayout.SOUTH);
    }

    // ПРОЗОРЕЦ С ИМЕНА, ТРУДНОСТ И МАСА
    private void getPlayerNames() {
        JTextField p1Field = new JTextField();
        JTextField p2Field = new JTextField();

        JComboBox<String> trudnost = new JComboBox<>(new String[]{"Easy", "Normal", "Hard"});
        JComboBox<String> kort = new JComboBox<>(new String[]{"Cyan", "Clay"});

        JPanel NamePanel = new JPanel(new GridLayout(4, 2));
        NamePanel.add(new JLabel("Player 1 Name:"));
        NamePanel.add(p1Field);
        NamePanel.add(new JLabel("Player 2 Name:"));
        NamePanel.add(p2Field);
        NamePanel.add(new JLabel("Difficulty:"));
        NamePanel.add(trudnost);
        NamePanel.add(new JLabel("Table:"));
        NamePanel.add(kort);

        int choiceDiff = JOptionPane.showConfirmDialog(null, NamePanel, "Enter Player Names", JOptionPane.OK_CANCEL_OPTION);
        if (JOptionPane.OK_OPTION == choiceDiff) {
            NameP1 = p1Field.getText();
            NameP2 = p2Field.getText();
            difficulty = (String) trudnost.getSelectedItem();
            table = (String) kort.getSelectedItem();
        }
        tochkiP1.setText(NameP1 + ": " + tochki1);
        tochkiP2.setText(NameP2 + ": " + tochki2);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // МАСА
        Color poleCyan = new Color(0,139,139);
        Color poleClay = new Color(177,117,30);
        switch (table) {
            case "Cyan" -> g.setColor(poleCyan);
            case "Clay" -> g.setColor(poleClay);
        }
        g.fillRect(0, 0, WIDTH, HEIGHT);

        switch (table) {
            case "Cyan" -> g.setColor(Color.WHITE);
            case "Clay" -> g.setColor(Color.LIGHT_GRAY);
        }
        g.drawLine(0, 315, WIDTH, 315);
        g.drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT);

        // ХИЛКИ
        Color hilka1= new Color(255,0,0);
        switch (table) {
            case "Cyan" -> g.setColor(hilka1);
            case "Clay" -> g.setColor(Color.BLACK);
        }
        g.fillRect(HILKA_WIDTH, hilka1Y+15, HILKA_WIDTH, HILKA_HEIGHT);

        Color hilka2 = new Color(0,0,190);
        g.setColor(hilka2);
        switch (table) {
            case "Cyan" -> g.setColor(hilka2);
            case "Clay" -> g.setColor(Color.BLACK);
        }
        g.fillRect(WIDTH - 2*HILKA_WIDTH, hilka2Y+15, HILKA_WIDTH, HILKA_HEIGHT);

        // ТОПЧЕ
        Color topka = new Color(255,165,0);
        switch (table) {
            case "Cyan" -> g.setColor(topka);
            case "Clay" -> g.setColor(Color.WHITE);
        }
        g.fillOval((int) ballX, (int) ballY, BALL_SIZE, BALL_SIZE);
    }

}