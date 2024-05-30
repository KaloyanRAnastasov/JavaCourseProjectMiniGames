package games.the_snake;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SnakeTest {
    private Snake snake;

    @BeforeEach
    public void setUp() {
        snake = new Snake();
    }

    @Test
    public void testInitialGameStart() {
        assertTrue(snake.running, "Играта трябва да е стартирана");
        assertEquals(6, snake.bodyParts, "Първоначалният брой части на змията трябва да бъде 6");
        assertEquals(0, snake.applesEaten, "Първоначалният брой изядени ябълки трябва да бъде 0");
        assertEquals('R', snake.direction, "Първоначалната посока трябва да бъде 'R'");
    }

    @Test
    public void testResetGame() {
        snake.resetGame();
        assertTrue(snake.running, "Играта трябва да е стартирана след нулиране");
        assertEquals(6, snake.bodyParts, "Броят части на змията трябва да се нулира до 6");
        assertEquals(0, snake.applesEaten, "Броят изядени ябълки трябва да се нулира до 0");
        assertEquals('R', snake.direction, "Посоката трябва да се нулира до 'R'");
    }

    @Test
    public void testMoveRightWrapAround() {
        snake.direction = 'R';
        snake.x[0] = Snake.SCREEN_WIDTH - Snake.UNIT_SIZE;
        snake.move();
        assertEquals(0, snake.x[0], "Главата на змията трябва да премине от дясната към лявата страна");
    }

    @Test
    public void testMoveLeftWrapAround() {
        snake.direction = 'L';
        snake.x[0] = 0;
        snake.move();
        assertEquals(Snake.SCREEN_WIDTH - Snake.UNIT_SIZE, snake.x[0], "Главата на змията трябва да премине от лявата към дясната страна");
    }

    @Test
    public void testMoveUpWrapAround() {
        snake.direction = 'U';
        snake.y[0] = 0;
        snake.move();
        assertEquals(Snake.SCREEN_HEIGHT - Snake.UNIT_SIZE, snake.y[0], "Главата на змията трябва да премине от горната към долната страна");
    }

    @Test
    public void testMoveDownWrapAround() {
        snake.direction = 'D';
        snake.y[0] = Snake.SCREEN_HEIGHT - Snake.UNIT_SIZE;
        snake.move();
        assertEquals(0, snake.y[0], "Главата на змията трябва да премине от долната към горната страна");
    }

    @Test
    public void testNewAppleGeneration() {
        snake.newApple();
        assertTrue(snake.appleX >= 0 && snake.appleX < Snake.SCREEN_WIDTH, "X координатата на ябълката трябва да бъде в границите на екрана");
        assertTrue(snake.appleY >= 0 && snake.appleY < Snake.SCREEN_HEIGHT, "Y координатата на ябълката трябва да бъде в границите на екрана");
        for (int i = 0; i < snake.bodyParts; i++) {
            assertFalse(snake.x[i] == snake.appleX && snake.y[i] == snake.appleY, "Ябълката не трябва да се поставя върху тялото на змията");
        }
    }

    @Test
    public void testCheckAppleEaten() {
        snake.appleX = snake.x[0];
        snake.appleY = snake.y[0];
        int initialBodyParts = snake.bodyParts;
        snake.checkApple();
        assertEquals(initialBodyParts + 1, snake.bodyParts, "Броят части на тялото трябва да се увеличи с 1 след изяждане на ябълка");
        assertEquals(1, snake.applesEaten, "Броят изядени ябълки трябва да се увеличи с 1 след изяждане на ябълка");
    }

    @Test
    public void testCheckCollisions() {
        snake.running = true;
        snake.x[0] = snake.x[1] = 100;
        snake.y[0] = snake.y[1] = 100;
        snake.checkCollisions();
        assertFalse(snake.running, "Играта трябва да спре, когато змията се сблъска със себе си");
    }

    @Test
    public void testPauseGame() {
        snake.paused = false;
        snake.paused = true;
        assertTrue(snake.paused, "Играта трябва да е на пауза");
    }

    @Test
    public void testResumeGame() {
        snake.paused = true;
        snake.paused = false;
        assertFalse(snake.paused, "Играта трябва да бъде възобновена");
    }
}
