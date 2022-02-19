package model;

import model.Book;
import model.Event;
import model.EventLog;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

// Represents the catalogue of all books in the library
public class Catalogue implements Writable {
    private String name;
    private ArrayList<Book> catalogue;
    //protected EventLog el;
    // NOTE: I realized LinkedList took up more memory and is less scalable
    // -->I'm trying to store large entries...is there anyway that is more efficient?

    // EFFECTS: constructs Catalogue with no name and an empty list of books
    public Catalogue() {
        this.name = "";
        catalogue = new ArrayList<>();
    }

    // EFFECTS: constructs Catalogue with name and an empty list of books
    public Catalogue(String name) {
        this.name = name;
        catalogue = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // EFFECTS: returns true if the catalogue is empty,
    // and false otherwise
    public boolean isEmpty() {
        if (catalogue.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: returns the number of books in the library catalogue
    public int length() {
        return catalogue.size();
    }

    // EFFECTS: returns an unmodifiable list of Books in this catalogue
    public List<Book> getBooksInCatalogue() {
        return Collections.unmodifiableList(catalogue);
    }

    // MODIFIES: this
    // EFFECTS: Book b is added to the catalogue if a book with the same title and author
    // isn't already in the catalogue and then returns true,
    // otherwise returns false
    public boolean addBook(Book b) {
        String title = b.getTitle();
        String author = b.getAuthor();

        for (Book book: catalogue) {
            if ((title.equals(book.getTitle())) && (author.equals(book.getAuthor()))) {
                return false;
            }
        }

        catalogue.add(b);

        EventLog.getInstance().logEvent(
                new Event(b.getTitle() + " by " + b.getAuthor() + " was added to the catalogue " + name));

        return true;
    }

    //EFFECTS: Returns both the title and author in format "Title by AuthorName" of each book in the Catalogue
    public List<String> viewCatalogue() {
        List<String> wholeCatalogue = new ArrayList<>();

        //System.out.println("Title and Author of books in the Catalogue");

        for (Book book: catalogue) {
            String bookListing = book.getTitle() + " by " + book.getAuthor();
            wholeCatalogue.add(bookListing);
        }

        EventLog.getInstance().logEvent(
                new Event("Viewed catalogue " + name));

        return wholeCatalogue;
    }

    //EFFECTS: Returns a List of Books in Catalogue that contains String s in their titles
    public List<Book> searchCatalogue(String s) {
        List<Book> searchBooks = new ArrayList<>();

        for (Book book: catalogue) {
            if (book.getTitle().contains(s)) {
                searchBooks.add(book);
            }
        }

        EventLog.getInstance().logEvent(
                new Event("Searched the catalogue " + name + " for book with keyword: " + s));

        //may need to require !isEmpty()
        return searchBooks;
    }

    // REQUIRES: catalogue.size() > 0
    // EFFECTS: Returns Book in Catalogue with same title and author if it exists in Catalogue,
    // otherwise, return null
    public Book getBook(String title, String author) {
        //requires???? or needs to potentially check if catalogue is empty
        for (Book book: catalogue) {
            if ((title.equals(book.getTitle())) && (author.equals(book.getAuthor()))) {
                return book;
            }
        }
        return null; //I may need to deal with this null and how it affects REQUIRES
    }

    // EFFECTS: Using the returned Book in Catalogue with same title and author,
    //the List of genres is added to the book unless the Book wasn't in the catalogue
    public void addBookGenre(String title, String author, List<String> genre) {
        Book b = getBook(title, author);
        //I may need to deal with the possible null and how it affects REQUIRES
        //might also change parameters to just be (be getBook(String t, String a)) but that
        // might be complicating things unnecessarily

        if (b != null) {
            for (String s : genre) {
                b.addGenre(s);
            }
        }
    }

    @Override
    //EFFECTS: returns this JSON object for fields in Book
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("Books", booksToJson());
        return json;
    }

    // EFFECTS: returns books in the catalogue as a JSON array
    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Book t : catalogue) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

    /*public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(EventLog.getInstance());
        }
    }*/
}



