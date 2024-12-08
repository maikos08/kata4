package software.ulpgc.kata4.arquitecture.io;

import software.ulpgc.kata4.arquitecture.model.Game;

import java.io.*;
import java.util.zip.ZipInputStream;

public class ZipFileGameReader implements GameReader
{
    private final BufferedReader reader;
    private final GameDeserializer deserializer;

    public ZipFileGameReader(File file, GameDeserializer deserializer) throws IOException {
        this.reader = readerOf(file);
        this.deserializer = deserializer;
        skipHeader();
    }

    private void skipHeader() throws IOException {
        reader.readLine();
    }

    private BufferedReader readerOf(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(zipInputStream(file)));
    }

    private ZipInputStream zipInputStream(File file) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        zipInputStream.getNextEntry();
        return zipInputStream;
    }

    @Override
    public Game read() throws IOException {
        return deserialize(reader.readLine());
    }

    private Game deserialize(String line) {
        return (line == null)?
                null:
                deserializer.deserialize(line);
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
