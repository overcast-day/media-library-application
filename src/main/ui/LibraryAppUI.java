package ui;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;

import model.Book;
import model.Catalogue;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.Objects;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.IOException;

//Represents the library application's main window frame
public class LibraryAppUI extends JFrame {
    //assign each catalogue a file path for data persistence
    private static final String JSON_STORE_LIB = "./data/Library.json";
    private static final String JSON_STORE_FAV = "./data/Favorites.json";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private Catalogue lib;
    private Catalogue fav;
    private Catalogue selected;

    //All the ui and persistence Java objects
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JDesktopPane desktop;
    private JInternalFrame startPanel;
    private JInternalFrame menuPanel;
    private ImageUI imagePanel;
    private OutputPanel viewPanel;

    //EFFECTS: constructs the welcome screen
    public LibraryAppUI() {
        super("Library Application");
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());

        setContentPane(desktop);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        startPanel = new JInternalFrame("~Welcome to the Library App~",false, false, false, false);
        startPanel.setLayout(new BorderLayout());
        startPanel.setPreferredSize(new Dimension(WIDTH / 2,HEIGHT / 3));
        startPanel.pack();
        startPanel.setVisible(true);
        desktop.add(startPanel, BorderLayout.CENTER);

        //add start button and welcome text
        startPanel.add(new JButton(new StartAction()));


        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //centreOnScreen(); helper method to implement
        setVisible(true); //makes the window visible
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            LogPrinter lp = new LogPrinter();
            lp.printLog(EventLog.getInstance());
            System.exit(0);
        }
    }

    //setter for selected
    public void setSelected(Catalogue c) {
        if (c.equals(lib) || c.equals(fav)) {
            selected = c;
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the catalogues and variables and objects from data stored, if it exists
    private void initialize() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");//NOTE: I'm still a little shaky on what I'm retrieving from Scanner

        System.out.println("Loading saved catalogues....");

        try {
            loadCatalogues();
            JOptionPane.showMessageDialog(null, "Sucessfully Loaded Previous Data",
                    "", JOptionPane.PLAIN_MESSAGE);
        } catch (NullPointerException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "It seems you have no saved library or favorites file yet.",
                    "", JOptionPane.INFORMATION_MESSAGE);
            lib = new Catalogue("Library");
            fav = new Catalogue("Favorites");
        }

        //add pre-loaded example books into the library catalogue
        for (int i = 0; i < 10; i++) {
            lib.addBook(new Book(Integer.toString(i),"Last&First"));
        }

        this.selected = lib; //for now setting it first to lib for ease of GUI
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where this menu will operate, and populates the window with panels to be use
    private void initializeGraphics() {
        menuPanel = new JInternalFrame("Menu", false, false, false, false);
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setLocation(0, HEIGHT / 15);

        addOptionBar();
        addMenuButtonPanel();

        menuPanel.pack();
        menuPanel.setVisible(true);
        desktop.add(menuPanel, BorderLayout.SOUTH);

        JInternalFrame imgPanel = new JInternalFrame("~~");
        imgPanel.setLayout(new BorderLayout());
        imgPanel.setPreferredSize(new Dimension(WIDTH / 2,HEIGHT / 2 + 50));
        imgPanel.setLocation(0, HEIGHT / 2 - 100);

        imagePanel = new ImageUI();
        imgPanel.add(imagePanel,BorderLayout.NORTH);

        imgPanel.pack();
        imgPanel.setVisible(true);
        desktop.add(imgPanel, BorderLayout.CENTER);

        JInternalFrame outputPanel = new JInternalFrame();
        desktop.add(outputPanel, BorderLayout.CENTER);

        viewPanel = new OutputPanel();
        desktop.add(viewPanel);
    }

    // MODIFIES: this
    //EFFECTS: Adds option bar at the top for menu items select catalogue, and save into
    private void addOptionBar() {
        JMenuBar optionBar = new JMenuBar();

        /*
        JMenu viewMenu = new JMenu("View");
        addMenuItem(viewMenu, new ViewCatalogueAction(lib, "Library"),
                KeyStroke.getKeyStroke("control <"));
        addMenuItem(viewMenu, new ViewCatalogueAction(fav, "Favorites"),
                KeyStroke.getKeyStroke("control >"));
        optionBar.add(viewMenu); */

        JMenu catalogueMenu = new JMenu("Select catalogue");
        addMenuItem(catalogueMenu, new SelectLibraryAction(), null);
        addMenuItem(catalogueMenu, new SelectFavoritesAction(), null);
        optionBar.add(catalogueMenu);

        JMenu saveMenu = new JMenu("Save Data");
        addMenuItem(saveMenu, new SaveDataAction(),
                KeyStroke.getKeyStroke("control S"));
        addMenuItem(saveMenu, new LoadDataAction(),
                KeyStroke.getKeyStroke("control L"));
        optionBar.add(saveMenu);

        JMenu exitMenu = new JMenu("Quit Application");
        addMenuItem(exitMenu, new QuitAppAction(), KeyStroke.getKeyStroke("control Q"));
        optionBar.add(exitMenu);

        setJMenuBar(optionBar);

        System.out.println(getJMenuBar());
    }

    // MODIFIES: this
    //EFFECTS: Adds an item with given specifications to given menu
        // theMenu---> menu to which new item is added to
        // action---> handler for new menu item
        // shortcut---> keystroke shortcut for this menu item
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke shortcut) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0)); //need to make it be lowercase
        menuItem.setAccelerator(shortcut);
        theMenu.add(menuItem);
    }

    // MODIFIES: this
    //EFFECTS: Adds JPanel of buttons to JFrame menuPanel
    //comment
    private void addMenuButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,2));

        JButton notAvailible = new JButton(new AddGenreAction()); //temporary
        notAvailible.setBackground(Color.RED);
        notAvailible.setOpaque(true);

        buttonPanel.add(new JButton(new AddTitleAction()));
        buttonPanel.add(notAvailible);
        //buttonPanel.add(new JButton(new AddGenreAction());
        buttonPanel.add(new JButton(new ViewCatalogueAction()));
        buttonPanel.add(new JButton(new SearchAction()));

        menuPanel.add(buttonPanel, BorderLayout.WEST);
    }

    // EFFECTS: saves library and favorites catalogue separate files
    private void saveCatalogues() throws FileNotFoundException {

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

        viewPanel.printCatalogue(wholeCatalogue);
    }

    // EFFECTS: prints out the whole list of Books in given Catalogue
    private void doViewCatalogue(Catalogue c) {
        //Catalogue selected = selectCatalogue();
        if (!c.equals(null)) {
            List<String> wholeCatalogue = c.viewCatalogue();
            viewPanel.printCatalogue(wholeCatalogue);
        }
    }

    // EFFECTS: prints out the list of Books with titles containing the String of characters inputted by user
    private void doSearchCatalogue(String search) {

        List<Book> searchBooks = selected.searchCatalogue(search);
        List<String> searchResults = new ArrayList<>();

        if (searchBooks.isEmpty()) {
            System.out.println("No titles with " + search + " found...\n");
        } else {
            for (Book book: searchBooks) {
                String bookListing = book.getTitle() + " by " + book.getAuthor();
                searchResults.add(bookListing);
            }
        }

        viewPanel.printCatalogue(searchResults);
        //could potentially print out something here
    }

    // Represents action to be taken when user clicks desktop
    // to switch focus. (Needed for key handling.)
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e)   {
            LibraryAppUI.this.requestFocusInWindow();
        }
    }

    // starts the application
    public static void main(String[] args) {
        new LibraryAppUI();
    }


    //MODIFIES: LibraryAppUI
    //Start button action is taken and user wants to start using the app
    private class StartAction extends AbstractAction {

        StartAction() {
            super("Start");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            startPanel.setVisible(false);
            boolean activeUse = true;
            String command = null;

            initializeGraphics();
            initialize();
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to be taken when user wants to add a new title to the catalogue
    private class AddTitleAction extends AbstractAction {
        AddTitleAction() {
            super("Add new title");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {

            JTextField title = new JTextField(5);
            JTextField author = new JTextField(5);

            JPanel addBookPanel = new JPanel();
            addBookPanel.add(new JLabel("Enter the title:"));
            addBookPanel.add(title);
            addBookPanel.add(Box.createHorizontalStrut(15)); // a line break between
            addBookPanel.add(new JLabel("Enter author (LastName&FirstName):"));
            addBookPanel.add(author);

            int answer = JOptionPane.showConfirmDialog(null, addBookPanel,
                    "Please enter book title and author", JOptionPane.OK_CANCEL_OPTION);

            if (answer == JOptionPane.OK_OPTION) {
                Book book = new Book(title.getText(), author.getText());

                if (selected.addBook(book)) {
                    JOptionPane.showMessageDialog(null,
                            "Successfully added " + title.getText() + " by " + author.getText(),
                            "Add Successful", JOptionPane.INFORMATION_MESSAGE);

                    new AddBookFromLibraryToFavorites(book);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "ERROR", "Add Unsuccessful, book already exists in catalogue...",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to be taken when user wants to adds a title from Library to Favorites
    private class AddBookFromLibraryToFavorites {

        private Book book;

        AddBookFromLibraryToFavorites(Book b) {
            setTitle("Add Favorites to titles");
            this.book = b;

            //AddBookToFavorites temporary location
            if (selected == lib) {
                int option = JOptionPane.showConfirmDialog(null,
                        "Would you like to add" + book.getTitle() + " to your Favorites?",
                        "Add title to your Favorites?", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    fav.addBook(book);
                    JOptionPane.showMessageDialog(null,
                            "Successfully added " + book.getTitle() + " by " + book.getAuthor(),
                            "Add Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                if (option == JOptionPane.NO_OPTION) {
                    remove(option);
                }
            }
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to be taken when user wants to add a new genres to the catalogue
    private class AddGenreAction extends AbstractAction {
        AddGenreAction() {
            super("Add Genre(s) to titles");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            doAddGenreToBook(); //stub
        }
    }

    //EFFECTS: Represents action to taken when user wants view all the titles in the selected catalogue
    private class ViewCatalogueAction extends AbstractAction {

        private Catalogue ct;

        ViewCatalogueAction() {
            super("View Catalogue");
        }

        ViewCatalogueAction(Catalogue c, String name) {
            super("View " + name + " Catalogue");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            doViewCatalogue();
        }
    }

    //EFFECTS: Represents action to taken when user wants search for titles in the selected catalogue
    private class SearchAction extends AbstractAction {
        SearchAction() {
            super("Search title");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {

            String search = JOptionPane.showInputDialog(null,
                    "What is the book's name?",
                    "Enter Book Title",
                    JOptionPane.QUESTION_MESSAGE);

            doSearchCatalogue(search);
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to taken when user wants select the library catalogue
    private class SelectLibraryAction extends AbstractAction {
        SelectLibraryAction()  {
            super("Library");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selected = lib;
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to taken when user wants select the fav catalogue
    private class SelectFavoritesAction extends AbstractAction {
        SelectFavoritesAction()  {
            super("Favorites");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selected = fav;
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to taken when user wants to save current all catalogues data in a file
    private class SaveDataAction extends AbstractAction {
        SaveDataAction()  {
            super("Save");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                saveCatalogues();
                JOptionPane.showMessageDialog(null,
                        "Saved to " + JSON_STORE_LIB + " and " + JSON_STORE_FAV,
                        "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "ERROR", "Save Unsuccessful", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //MODIFIES: LibraryAppUI
    //EFFECTS: Represents action to taken when user wants to load catalogues data in a file to app
    private class LoadDataAction extends AbstractAction {
        LoadDataAction()  {
            super("Load");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                loadCatalogues();
                JOptionPane.showMessageDialog(null,
                        "Loaded from" + JSON_STORE_LIB + " and " + JSON_STORE_FAV,
                        "Load Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "ERROR, unable to locate file", "Load Unsuccessful", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class QuitAppAction extends AbstractAction {
        QuitAppAction() {
            super("Quit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            LogPrinter lp = new LogPrinter();
            lp.printLog(EventLog.getInstance());
            System.exit(0);
        }
    }

}
