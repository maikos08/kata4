package software.ulpgc.kata4.app;

import software.ulpgc.kata4.arquitecture.control.ImportCommand;
import software.ulpgc.kata4.arquitecture.ui.ImportDialog;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        new ImportCommand(importDialog()).execute();
    }

    private static ImportDialog importDialog() {
        return () -> new File("archive.zip");
    }
}
