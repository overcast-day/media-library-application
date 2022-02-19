package persistence;

import model.Catalogue;
import org.json.JSONObject;

import java.io.*;

//based on code from CPSC 210 - https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a writer that writes JSON representation of a catalogue to file
public class JsonWriter {
    private static final int INDENT = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of catalogue to file
    public void write(Catalogue c) {
        JSONObject json = c.toJson();
        saveToFile(json.toString(INDENT));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {

        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
