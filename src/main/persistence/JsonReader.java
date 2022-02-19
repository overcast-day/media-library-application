package persistence;

import model.Catalogue;
import model.Book;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

//based on code from CPSC 210 - https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads a catalogue from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads catalogue from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Catalogue read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCatalogue(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private Catalogue parseCatalogue(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Catalogue c = new Catalogue(name);
        addBooks(c, jsonObject);
        return c;
    }

    // MODIFIES: c
    // EFFECTS: parses Books from JSON object and adds them to the given catalogue
    private void addBooks(Catalogue c, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Books");
        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            addBook(c, nextBook);
        }
    }

    // MODIFIES: c
    // EFFECTS: parses Book from JSON object and adds it to workroom
    private void addBook(Catalogue c, JSONObject jsonObject) {
        String title = jsonObject.getString("Title");
        String author = jsonObject.getString("Author");
        String genreList = jsonObject.getString("Genres");
        Book bk = new Book(title, author);

        parseGenresToAddToBook(genreList, bk);
        c.addBook(bk);
    }

    // EFFECTS: parses string of all genres from JSON object and adds the list of genres to given book
    private void parseGenresToAddToBook(String genreList, Book b) {
        List<String> genres = b.getGenres();

        if (!genreList.equals("")) {
            while (genreList.indexOf(">") != genreList.lastIndexOf(">")) {
                int endIndex = genreList.indexOf(">");

                genres.add(genreList.substring(1, endIndex));
                genreList = genreList.substring(endIndex + 1);
            }

            //adds last genre
            genres.add(genreList.substring(1, genreList.indexOf(">")));
        }
    }

}
