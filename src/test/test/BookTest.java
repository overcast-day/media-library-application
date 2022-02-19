package test;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Unit tests for the Book class
class BookTest {

    @Test
    public void testGetTitle() {
        Book book = new Book("","");
        Book book2 = new Book("title", "Last&First");

        assertEquals("", book.getTitle());
        assertEquals("title", book2.getTitle());
    }

    @Test
    public void testGetAuthor() {
        Book book = new Book("","");
        Book book2 = new Book("title", "Last&First");

        assertEquals("", book.getAuthor());
        assertEquals("Last&First", book2.getAuthor());
    }

    @Test
    public void testGetGenresEmpty() {
        Book book = new Book("","");
        Book book2 = new Book("title", "Last&First");
        List<String> set1 = book.getGenres();
        List<String> set2 = book2.getGenres();

        assertEquals(0, set1.size());
        assertEquals(0, set2.size());
    }

    @Test
    public void testGetGenres() {
        Book book = new Book("","");
        Book book2 = new Book("title", "Last&First");

        assertTrue(book.getGenres().isEmpty());
        assertTrue(book2.getGenres().isEmpty());

        assertTrue(book.addGenre("Fantasy"));
        assertTrue(book.addGenre("Science Fiction"));
        assertTrue(book2.addGenre("Science Fiction"));


        List<String> set1 = book.getGenres();
        List<String> set2 = book2.getGenres();

        assertEquals(2, set1.size());
        assertEquals(1, set2.size());
    }

    @Test
    public void testAddSameGenres() {
        Book book = new Book("","");
        Book book2 = new Book("title", "Last&First");

        assertTrue(book.getGenres().isEmpty());
        assertTrue(book2.getGenres().isEmpty());

        assertTrue(book.addGenre("Fantasy"));
        assertTrue(book2.addGenre("Science Fiction"));

        assertFalse(book.addGenre("Fantasy"));
        assertFalse(book2.addGenre("Science Fiction"));

        assertEquals(1, book.getGenres().size());
        assertEquals(1, book2.getGenres().size());
    }
}