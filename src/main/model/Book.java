package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a book having a title, author and genre(s)
public class Book implements Writable {
    private String title;
    private String author;
    private List<String> genres;

    // EFFECTS: Book has given title and author and genre(s) is an empty List of String
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        genres = new ArrayList<>();
    }

    // EFFECTS: returns title
    public String getTitle() {
        return title;
    }

    // REQUIRES: author name is in form "Last&First" or ""
    // EFFECTS: returns author
    public String getAuthor() {
        return author;
    }

    // EFFECTS: returns genre(s)
    public List<String> getGenres() {
        return genres;
    }

    // MODIFIES: this
    // EFFECTS: String s is added to the genre list if it isn't already in genre list, and then returns true
    // returns false otherwise
    public boolean addGenre(String s) {
        if ((getGenres().isEmpty()) || !(getGenres().contains(s))) {
            EventLog.getInstance().logEvent(new Event("Added genre " + s + " to '" + title + "' By " + author));
            genres.add(s);

            return true;
        }
        return false;
    }

    @Override
    //EFFECTS: returns this JSON object for fields in Book
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        String genreList = "";

        json.put("Title", title);
        json.put("Author", author);

        for (String g: genres) {
            genreList = genreList.concat("<" + g + ">");
        }

        json.put("Genres", genreList);
        return json;
    }
}
