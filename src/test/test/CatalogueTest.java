package test;

import model.Book;
import model.Catalogue;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Unit tests for the Catalogue class
public class CatalogueTest {
    private static final int NUM_ITEMS = 10;
    public Catalogue catalogue;

    @BeforeEach
    public void runBefore() {
        catalogue = new Catalogue();
    }

    @Test
    public void testEmpty() {
        assertTrue(catalogue.isEmpty());
        assertEquals(0, catalogue.length());
    }

    @Test
    public void testAddBooktoEmpty() {
        assertTrue(catalogue.isEmpty());

        Book b = new Book("title", "Last&First");

        assertTrue(catalogue.addBook(b));
        assertEquals(1, catalogue.length());
        assertFalse(catalogue.isEmpty());
    }

    @Test
    public void testAddBookWithoutRepetition() {
        assertTrue(catalogue.isEmpty());

        for (int i = 0; i < NUM_ITEMS; i++) {
            assertTrue(catalogue.addBook(new Book(Integer.toString(i),"")));
        }

        assertEquals(NUM_ITEMS, catalogue.length());
        assertFalse(catalogue.isEmpty());
    }

    @Test
    public void testAddBookWithoutAuthorRepetition() {
        assertTrue(catalogue.isEmpty());

        for (int i = 0; i < NUM_ITEMS; i++) {
            assertTrue(catalogue.addBook(new Book(Integer.toString(i),"")));
        }

        for (int i = 0; i < NUM_ITEMS; i++) {
            assertTrue(catalogue.addBook(new Book(Integer.toString(i),"Last&Name")));
        }

        assertEquals(NUM_ITEMS*2, catalogue.length());
        assertFalse(catalogue.isEmpty());
    }

    @Test
    public void testAddBookWithoutTitleRepetition() {
        assertTrue(catalogue.isEmpty());

        for (int i = 0; i < NUM_ITEMS; i++) {
            assertTrue(catalogue.addBook(new Book(Integer.toString(i),"Last&Name")));
        }

        for (int i = NUM_ITEMS; i < NUM_ITEMS*2; i++) {
            assertTrue(catalogue.addBook(new Book(Integer.toString(i),"Last&Name")));
        }

        assertEquals(NUM_ITEMS*2, catalogue.length());
    }

    @Test
    public void testAddSameBook() {
        assertTrue(catalogue.isEmpty());

        assertTrue(catalogue.addBook(new Book("","")));
        assertTrue(catalogue.addBook(new Book("other","")));

        assertFalse(catalogue.addBook(new Book("","")));


        assertEquals(2, catalogue.length());
    }

    @Test
    public void testAddBookWithRepetition() {
        assertTrue(catalogue.isEmpty());

        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(Integer.toString(i), "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        for (int i = 0; i < NUM_ITEMS; i++) {
            assertFalse(catalogue.addBook(new Book(Integer.toString(i), "Last&First")));
        }

        assertEquals(NUM_ITEMS, catalogue.length());
    }

    @Test
    public void testAddBookWithSomeRepetition() {
        assertTrue(catalogue.isEmpty());

        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(Integer.toString(i), "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        for (int i = 5; i < NUM_ITEMS+5; i++) {
            Book b = new Book(Integer.toString(i), "Last&First");
            if(i < NUM_ITEMS) {
                assertFalse(catalogue.addBook(b));
            } else {
                assertTrue(catalogue.addBook(b));
            }
        }

        assertEquals(NUM_ITEMS+5, catalogue.length());
    }

    @Test
    public void testViewCatalogueEmpty() {
        assertTrue(catalogue.isEmpty());
        assertTrue(catalogue.viewCatalogue().isEmpty());
    }

    @Test
    public void testViewCatalogue() {
        assertTrue(catalogue.isEmpty());

        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(Integer.toString(i), "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        List<String> wholeCatalogue = catalogue.viewCatalogue();

        assertEquals(catalogue.length() ,wholeCatalogue .size());

        for (int i = 0; i < NUM_ITEMS; i++) {
            String s = i + " by " + "Last&First";
            assertEquals(wholeCatalogue.get(i), s);
        }
    }

    @Test
    public void testSearchEmpty() {
        assertTrue(catalogue.isEmpty());
        assertTrue(catalogue.searchCatalogue("x").isEmpty());
    }

    @Test
    public void testSearchNoneContains() {
        assertTrue(catalogue.isEmpty());
        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(i + "abc", "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        List<Book> searchCatalogue = catalogue.searchCatalogue("ad");

        assertEquals(0 ,searchCatalogue.size());
    }

    @Test
    public void testSearchAllContains() {
        assertTrue(catalogue.isEmpty());
        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(i + "abc", "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        List<Book> searchCatalogue = catalogue.searchCatalogue("ab");

        assertEquals(NUM_ITEMS ,searchCatalogue.size());
    }

    @Test
    public void testSearchSomeContains() {
        assertTrue(catalogue.isEmpty());
        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(i + "abc", "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        Book bk = new Book("zebra", "Last&First");

        assertTrue(catalogue.addBook(bk));

        for (int i = NUM_ITEMS; i < NUM_ITEMS+5; i++) {
            Book b = new Book(i + "abc", "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        List<Book> searchCatalogue = catalogue.searchCatalogue("zeb");

        assertEquals(1,searchCatalogue.size());
        assertEquals(bk,searchCatalogue.get(0));
    }

    @Test
    public void testGetBook() {
        assertTrue(catalogue.isEmpty());
        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(i + "abc", "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        Book bk = catalogue.getBook("0abc", "Last&First");
        Book b2 = catalogue.getBook("0abc", "");
        Book b3 = catalogue.getBook("", "Last&First");

        assertEquals("0abc",bk.getTitle());
        assertEquals("Last&First",bk.getAuthor());
        assertNull(b2);
        assertNull(b3);
    }

    @Test
    public void testAddGenres() {
        assertTrue(catalogue.isEmpty());
        for (int i = 0; i < NUM_ITEMS; i++) {
            Book b = new Book(i + "abc", "Last&First");
            assertTrue(catalogue.addBook(b));
        }

        Book bk = catalogue.getBook("0abc", "Last&First");
        Book b2 = catalogue.getBook("0abc", ""); //testing this null case is not necessary

        assertEquals(0,bk.getGenres().size());
        assertNull(b2);

        List<String> genres = new ArrayList<>();
        genres.add("Fantasy");
        genres.add("Romance");
        genres.add("Science Fiction");
        genres.add("Fantasy");

        catalogue.addBookGenre("0abc", "Last&First", genres);
        catalogue.addBookGenre("0abc", "", genres); //getBook returns null

        assertEquals(3,bk.getGenres().size());
        assertNull(b2);
    }

}
