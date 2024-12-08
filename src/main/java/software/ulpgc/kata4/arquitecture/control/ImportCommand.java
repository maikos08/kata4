package software.ulpgc.kata4.arquitecture.control;

import software.ulpgc.kata4.arquitecture.io.*;
import software.ulpgc.kata4.arquitecture.model.Game;
import software.ulpgc.kata4.arquitecture.ui.ImportDialog;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ImportCommand implements Command{
    private final ImportDialog dialog;

    public ImportCommand(ImportDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void execute() {
        try (GameReader reader = new ZipFileGameReader(inputFile(), csvGameDeserializer());
             GameWriter writer = DatabaseGameWriter.open(outputFile())){
            doExecute(reader, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doExecute(GameReader reader, GameWriter writer) throws IOException {
        while (true) {
            Game game = reader.read();
            if (game == null) break;
            writer.write(game);
        }
    }

    private GameDeserializer csvGameDeserializer() {
        return new CsvGameDeserializer();
    }

    private File inputFile() {
        return dialog.get();
    }

    private File outputFile() {
        return new File("games-ranking.db");
    }
}
