package test;

import model.Book;
import model.Catalogue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    private Catalogue c;

    @BeforeEach
    public void runBefore() {
        c = new Catalogue(("Catalogue"));
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCatalogue() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCatalogue.json");
            writer.open();
            writer.write(c);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCatalogue.json");
            c = reader.read();
            assertEquals("Catalogue", c.getName());
            assertEquals(0, c.length());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCatalogue() {
        try {
            Book b1 = new Book("ORV", "Shing&Song");
            b1.addGenre("Epic");
            b1.addGenre("I Wanna Cry");
            c.addBook(b1);
            c.addBook(new Book("LOL", "Last&First"));

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCatalogue.json");
            writer.open();
            writer.write(c);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCatalogue.json");
            c = reader.read();
            assertEquals("Catalogue", c.getName());
            List<Book> books = c.getBooksInCatalogue();
            assertEquals(2, c.length());

            List<String> genres= b1.getGenres();
            checkBook("ORV", "Shing&Song", genres, books.get(0));
            checkBook("LOL","Last&First", new ArrayList<String>(), books.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
