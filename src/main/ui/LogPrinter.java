package ui;

import model.Event;
import model.EventLog;

//Represents a printer that prints the events in an Event log
public class LogPrinter {

    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString() + "\n\n");
        }
    }
}
