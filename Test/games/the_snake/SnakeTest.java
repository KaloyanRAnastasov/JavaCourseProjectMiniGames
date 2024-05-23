package games.the_snake;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;

import static org.junit.Assert.*;

public class SnakeTest {
    private Snake snake;

    @Before
    public void setUp() {
        snake = new Snake();
    }

    @Test
    public void startGame() {
        snake.startGame();
        assertTrue(snake.running);
        assertEquals(6, snake.bodyParts);
        assertEquals(0, snake.applesEaten);
        assertEquals('R', snake.direction);
        assertNotNull(snake.timer);
        assertTrue(snake.timer.isRunning());
    }

    @Test
    public void resetGame() {
        snake.resetGame();
        assertTrue(snake.running);
        assertFalse(snake.paused);
        assertEquals(6, snake.bodyParts);
        assertEquals(0, snake.applesEaten);
        assertEquals('R', snake.direction);
        assertTrue(snake.timer.isRunning());
    }

    @Test
    public void newApple() {
        snake.newApple();
        assertTrue(snake.appleX >= 0 && snake.appleX < Snake.SCREEN_WIDTH);
        assertTrue(snake.appleY >= 0 && snake.appleY < Snake.SCREEN_HEIGHT);
    }

    @Test
    public void move() {
        snake.startGame();
        int initialX = snake.x[0];
        int initialY = snake.y[0];
        snake.move();
        assertEquals(initialX + Snake.UNIT_SIZE, snake.x[0]);
        assertEquals(initialY, snake.y[0]);
    }

    @Test
    public void checkApple() {
        snake.startGame();
        snake.appleX = snake.x[0];
        snake.appleY = snake.y[0];
        int initialBodyParts = snake.bodyParts;
        int initialApplesEaten = snake.applesEaten;
        snake.checkApple();
        assertEquals(initialBodyParts + 1, snake.bodyParts);
        assertEquals(initialApplesEaten + 1, snake.applesEaten);
    }

    @Test
    public void checkCollisions() {
        snake.startGame();
        snake.x[0] = snake.x[1];  // Simulate collision with itself
        snake.checkCollisions();
        assertFalse(snake.running);
    }

    @Test
    public void actionPerformed() {
        snake.startGame();
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
        snake.actionPerformed(e);
        // Ensure game logic is processed and repaint is called. No direct assertion.
    }
}
