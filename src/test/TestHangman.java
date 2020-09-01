package test;

import main.Hangman;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestHangman {
    static Random random;
    static Hangman hangman;
    int requestedLength;

    @BeforeAll
    public static void setupClass() {
        random = new Random();
        hangman = new Hangman();
        hangman.loadWords();
    }

    @BeforeEach
    public void setupTest() {
        requestedLength = random.nextInt(6) + 5;
    }

    @AfterEach
    public void tearDownTest() {
        random = null;
        requestedLength = 0;
        hangman = null;
    }

    /***
     * Tests whether hangman.countAlphabet
     * returns the correct number of times
     * the given alphabet appears in the given word
     */
    @Test
    void test_alphabetCountInWord() {
        String word = "pizza";
        char alphabet = 'a';
        int count = hangman.countAlphabet(word, alphabet);
        assertEquals(1, count);
    }

    @Test
    void test_lengthOfFetchedWordRandom() throws IOException {
        int requestedLength = random.nextInt(6) + 5;
        String word = hangman.fetchWord(requestedLength);
        assertEquals(word.length(), requestedLength);
    }

    @Test
    void test_uniquenessOfFetchedWord() throws IOException {
        int requestedLength = 0;
        Set<String> usedWordsSet = new HashSet<>();
        int round = 0;
        String word = null;
        while (round < 100) {
            requestedLength = random.nextInt(6) + 5;
            word = hangman.fetchWord(requestedLength);
            round++;
            assertTrue(usedWordsSet.add(word));
        }
    }

    @Test
    void test_fetchClueBeforeAnyGuess() {
        String clue = hangman.fetchClue("pizza");
        assertEquals("-----", clue);
    }

    @Test
    void test_fetchClueAfterCorrectGuess() {
        String clue = hangman.fetchClue("pizza");
        String newClue = hangman.fetchClue("pizza", clue, 'a');
        assertEquals("----a", newClue);
    }

    @Test
    void test_fetchClueAfterIncorrectGuess() {
        String clue = hangman.fetchClue("pizza");
        String newClue = hangman.fetchClue("pizza", clue, 'a');
        assertEquals("-----", newClue);
    }

    @Test
    void test_whenInvalidGuessThenFetchClueThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> hangman.fetchClue("pizza", "-----", '1')
                );
    }

    @Test
    void test_whenInvalidGuessThenFetchClueThrowsExceptionWithMessage() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> hangman.fetchClue("pizza", "-----", '1')
        );
        assertEquals("Invalid character", e.getMessage());
    }

    @Test
    void test_remainingTrialsBeforeAnyGuess() throws IOException {
        hangman.fetchWord(requestedLength);
        assertEquals(Hangman.MAX_TRIALS, hangman.remainingTrials);
    }

    @Test
    void test_remainingTrialsAfterOneGuess() throws IOException {
        hangman.fetchWord(requestedLength);
        hangman.fetchClue("pizza", "-----", 'a');
        assertEquals(Hangman.MAX_TRIALS - 1, hangman.remainingTrials);
    }
}
