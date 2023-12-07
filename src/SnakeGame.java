import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;
    // Чем меньше значение DELAY, тем быстрее двигается змейка
    private static final int DELAY = 100;

    // Сколько нужно, чтоб пройти игру
    private static final int GAME_SIZE = 10;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private List<Point> snake;
    private Direction direction;
    private Point food;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = Direction.RIGHT;

        spawnFood();

        timer = new Timer(DELAY, this);

        getContentPane().add(new Board());

        addKeyListener(this);
        setFocusable(true);

        timer.start();
    }

    private void spawnFood() {
        int x = (int) (Math.random() * GRID_SIZE);
        int y = (int) (Math.random() * GRID_SIZE);
        food = new Point(x, y);

        // Make sure food does not spawn on the snake
        while (snake.contains(food)) {
            x = (int) (Math.random() * GRID_SIZE);
            y = (int) (Math.random() * GRID_SIZE);
            food.setLocation(x, y);
        }
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.y = (newHead.y - 1 + GRID_SIZE) % GRID_SIZE;
                break;
            case DOWN:
                newHead.y = (newHead.y + 1) % GRID_SIZE;
                break;
            case LEFT:
                newHead.x = (newHead.x - 1 + GRID_SIZE) % GRID_SIZE;
                break;
            case RIGHT:
                newHead.x = (newHead.x + 1) % GRID_SIZE;
                break;
        }

        if (newHead.equals(food)) {
            snake.add(0, food);
            spawnFood();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }

        // Check for collisions
        if (snake.size() > 1 && snake.subList(1, snake.size()).contains(newHead)) {
            gameOver();
        }

        if (snake.size() > GAME_SIZE){
            gameDone();
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void gameDone() {
        timer.stop();
        // Имя файла в текущей директории
        String fileName = "Пожелания.png";
        // Получаем текущую директорию
        String currentDirectory = System.getProperty("user.dir");
        // Создаем полный путь к файлу
        String filePath = currentDirectory + File.separator + fileName;
        JOptionPane.showMessageDialog(this, "Молодец! Прими наши пожелания!", "You Win", JOptionPane.INFORMATION_MESSAGE);
        FileOpener.openFile(filePath);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN) {
                    direction = Direction.UP;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP) {
                    direction = Direction.DOWN;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != Direction.RIGHT) {
                    direction = Direction.LEFT;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.LEFT) {
                    direction = Direction.RIGHT;
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private class Board extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        private void draw(Graphics g) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

            // Draw snake
            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}
