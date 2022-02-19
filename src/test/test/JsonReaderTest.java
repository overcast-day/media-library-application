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

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Catalogue c = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCatalogue() {
        JsonReader reader = new JsonReader("./data/testWriterEmptyCatalogue.json");
        try {
            Catalogue c = reader.read();
            assertEquals("Catalogue", c.getName());
            assertEquals(0, c.length());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCatalogue() {
        JsonReader reader = new JsonReader("./data/testWriterGeneralCatalogue.json");
        try {
            Catalogue c = reader.read();
            assertEquals("Catalogue", c.getName());
            List<Book> books = c.getBooksInCatalogue();
            assertEquals(2, c.length());

            List<String> genres= new ArrayList<String>();
            genres.add("Epic");
            genres.add("I Wanna Cry");
            checkBook("ORV", "Shing&Song", genres, books.get(0));
            checkBook("LOL","Last&First", new ArrayList<String>(), books.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}

