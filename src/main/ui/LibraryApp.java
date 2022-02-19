package ui;

import model.Book;
import model.Catalogue;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.Objects;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.IOException;

//Represents the console-based user interface for the library application
public class LibraryApp {
    //maybe assign each catalogue a destination??? with its own getter?
    private static final String JSON_STORE_LIB = "./data/Library.json";
    private static final String JSON_STORE_FAV = "./data/Favorites.json";
    private Catalogue lib;
    private Catalogue fav;
    private Catalogue selected;

    //All the ui and persistence Java objects
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //EFFECTS: runs the library application
    public LibraryApp() {
        runLibrary();
    }

    //MODIFIES: this
    //EFFECTS: processes the user input
    private void runLibrary() {
        boolean activeUse = true;
        String command = null;

        initialize();

        this.selected = selectCatalogue();

        while (activeUse) {
            displayMenu();
            command = input.next();
            //command = command.toLowerCase(); //unnecessary

            if (command.equals("q")) {
                //saveCatalogues(lib);
                //saveCatalogues(fav);
                saveCatalogues();
                activeUse = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nThank you for using this application, bye bye.");

        input.close(); //closes Scanner once done
    }

    //MODIFIES: this !!!!!!!
    //EFFECTS: processes the user command and calls appropriate methods
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddBookToLibraryCatalogue();
        } else if (command.equals("g")) {
            doAddGenreToBook();
        } else if (command.equals("v")) {
            doViewCatalogue();
        } else if (command.equals("s")) {
            doSearchCatalogue();
        } else if (command.equals("c")) {
            this.selected = selectCatalogue();
        } else {
            System.out.println("Selection is not valid...");
        }

        //create new method that displays another menu and processes command, if selected = lib
        //then player can either add a book to favorites or go back to main menu
    }


    // MODIFIES: this
    // EFFECTS: initializes the catalogues and variables and objects
    private void initialize() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");//NOTE: I'm still a little shaky on what I'm retrieving from Scanner

        System.out.println("Loading saved catalogues....");
/*      //not working currently for some reason (Reader causing issues with path)
        try { loadCatalogues();
        } catch (NullPointerException | FileNotFoundException e) {
            System.out.println("It seems you have no saved library file yet.");
            lib = new Catalogue("Library");}
        try {loadCatalogues(fav);
        } catch (NullPointerException | FileNotFoundException e) {
            System.out.println("It seems you have no saved Favorites file yet.");
            fav = new Catalogue("Favorites"); }*/
        try {
            loadCatalogues();
        } catch (NullPointerException | FileNotFoundException e) {
            System.out.println("It seems you have no saved library or favorites file yet.");
            lib = new Catalogue("Library");
            fav = new Catalogue("Favorites");
        }

        //add pre-loaded example books into the library catalogue
        for (int i = 0; i < 10; i++) {
            lib.addBook(new Book(Integer.toString(i),"Last&First"));
        }

        System.out.println("\n~Welcome to the Library App~");
    }

    // EFFECTS: displays menu of options for user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add new title");
        System.out.println("\tg -> Add Genre(s) to titles");
        System.out.println("\tv -> View Catalogue");
        System.out.println("\ts -> Search title");
        System.out.println("\tc -> Change selected catalogue");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: saves library and favorites catalogue separate files
    private void saveCatalogues() {

        jsonWriter = new JsonWriter(JSON_STORE_LIB);

        try {
            jsonWriter.open();
            jsonWriter.write(lib);
            jsonWriter.close();
            System.out.println("Saved " + lib.getName() + " to " + JSON_STORE_LIB);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_LIB);
        }

        jsonWriter = new JsonWriter(JSON_STORE_FAV);

        try {
            jsonWriter.open();
            jsonWriter.write(fav);
            jsonWriter.close();
            System.out.println("Saved " + fav.getName() + " to " + JSON_STORE_FAV);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_FAV);
        }
    }

    // REQUIRES: catalogue's name != ""
    // EFFECTS: saves given catalogue to a file of their name
    private void saveCatalogues(Catalogue c) {
        final String JSON_STORE = "./data/" + c.getName() + ".json";
        jsonWriter = new JsonWriter(JSON_STORE);

        try {
            jsonWriter.open();
            jsonWriter.write(c);
            jsonWriter.close();
            System.out.println("Saved " + c.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads library and favorites catalogues from files
    private void loadCatalogues() throws FileNotFoundException {

        jsonReader = new JsonReader(JSON_STORE_LIB);
        try {
            lib = jsonReader.read();
            System.out.println("Loaded " + lib.getName() + " from " + JSON_STORE_LIB);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_LIB);
        }

        jsonReader = new JsonReader(JSON_STORE_FAV);
        try {
            fav = jsonReader.read();
            System.out.println("Loaded " + fav.getName() + " from " + JSON_STORE_FAV);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_FAV);
        }
    }

    // REQUIRES: catalogue's name != ""
    // MODIFIES: this
    // EFFECTS: loads given catalogue from their files with their name
    private void loadCatalogues(Catalogue c) throws FileNotFoundException {
        final String JSON_STORE = "./data/" + c.getName() + ".json";
        jsonReader = new JsonReader(JSON_STORE);

        try {
            c = jsonReader.read();
            System.out.println("Loaded " + c.getName() + " from " + JSON_STORE);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts the addition of a title to the catalogue
    private void doAddBookToLibraryCatalogue() {
        //Catalogue selected = selectCatalogue();
        System.out.println("Enter the title: ");
        String title = input.next();
        System.out.println("Enter author (LastName&FirstName): ");
        String author = input.next();
        //these need to be tested to ensure right info is contained
        //may need to create while loop to force user

        Book book = new Book(title, author);

        if (selected.addBook(book)) {
            System.out.println("Successfully added " + title + " by " + author + ".\n");
            runAddBookFromLibraryToFavorites(book);
            //perhaps just do a if statement, when select = lib run addBook
            //when select = fav, automatically add fav to lib as well or this goes into code where
            //fav can only add books from lib to fav.
        } else {
            System.out.println("Unsuccessful, book already exists in catalogue...\n");
        }
    //could potentially print out something here
    }

    // MODIFIES: this (and Book within this, if it exists)
    // EFFECTS: conducts the addition of a list of genres to a specified Book in Library
    // if it exists in the catalogue
    private void doAddGenreToBook() {
        //Catalogue selected = selectCatalogue();
        System.out.println("Enter the title: ");
        String title = input.next();
        System.out.println("Enter author (LastName&FirstName): ");
        String author = input.next();
        System.out.println("Enter genres separated by a comma without spaces: ");
        //these need to be tested to ensure right info is contained
        //may need to create while loop to force user

        List<String> genres = new ArrayList<>();

        String genreList = input.next(); //results in entry into loop
        System.out.println("genreList"); //TEST WHAT THIS PRINTS OUT

        while (genreList.contains(",")) { //builds the list of genres inputted
            int index = genreList.indexOf(",");
            String genreName = genreList.substring(index + 1);
            genreList = genreList.substring(0,index);

            genres.add(genreName);
        }
        genres.add(genreList); //adds final genre inputted

        if (selected.getBook(title, author) != null) {
            selected.addBookGenre(title, author, genres);
            System.out.println("Successfully added " + genreList + " to " + title + " by " + author + ".\n");
        } else {
            System.out.println("Unsuccessful, book does not exist in this catalogue...\n");
        }
        //could potentially print out something here
    }

    // EFFECTS: prints out the whole list of Books in Catalogue
    private void doViewCatalogue() {
        //Catalogue selected = selectCatalogue();

        List<String> wholeCatalogue = selected.viewCatalogue();

        if (wholeCatalogue.isEmpty()) {
            System.out.println("No titles to view in this catalogue...\n");
        } else {
            System.out.println("List of Titles in this catalogue:\n");
            for (String s: wholeCatalogue) {
                System.out.println("\n" + s);
            }
        }
        //could potentially print out something here
    }

    // EFFECTS: prints out the list of Books with titles containing the String of characters inputted by user
    private void doSearchCatalogue() {
        //Catalogue selected = selectCatalogue();

        System.out.print("Enter the search for title: ");
        String search = input.next();

        List<Book> searchResults = selected.searchCatalogue(search);

        if (searchResults.isEmpty()) {
            System.out.println("No titles with" + search + " found...\n");
        } else {
            System.out.println("Search Results:\n");
            for (Book book: searchResults) {
                System.out.println("\n" + book.getTitle() + " by " + book.getAuthor());
            }
        }
        //could potentially print out something here
    }

    // EFFECTS: prompts user to select Library or Favorites catalogue and returns it
    private Catalogue selectCatalogue() {
        String selection = "";  // forces entry into loop

        System.out.println("Select a catalogue:");

        while (!(selection.equals("l") || selection.equals("f"))) {
            System.out.println("l for Library");
            System.out.println("f for Favorites");
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("l")) {
            System.out.println("You have selected the Library Catalogue");
            return lib;
        } else {
            System.out.println("You have selected your Favorites Catalogue");
            return fav;
        }
    }

    //MODIFIES: Catalogue fav
    //EFFECTS: processes the user input and adds book to the Catalogue favorites from library when appropriate
    private void runAddBookFromLibraryToFavorites(Book b) {
        boolean active = true;
        String command = null;

        while (active && Objects.equals(selected, lib)) {
            System.out.println("\nWould you like to add " + b.getTitle() + " to your Favorites?");
            System.out.println("\ta -> Add title to Favorites");
            System.out.println("\tb -> No, go back");
            command = input.next();

            if (command.equals("b")) {
                active = false;
            } else if (command.equals("a")) {
                if (fav.addBook(b)) {
                    System.out.println("Successfully added " + b.getTitle() + " by " + b.getAuthor() + ".\n");
                } else {
                    System.out.println("Unsuccessful, book already exists in Favorites...\n");
                }
                active = false;
            } else {
                System.out.println("Selection is not valid...");
            }
        }
    }



}
