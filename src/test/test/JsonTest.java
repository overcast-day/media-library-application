package test;

import model.Book;
import model.Catalogue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class JsonTest {
    protected void checkBook(String title, String author, List<String> genres, Book bk) {
        assertEquals(title, bk.getTitle());
        assertEquals(author, bk.getAuthor());

        assertEquals(genres.size(), bk.getGenres().size());

        for (int i = 0; i<bk.getGenres().size(); i++) {
            assertEquals(genres.get(i), bk.getGenres().get(i));
        }

    }
}
