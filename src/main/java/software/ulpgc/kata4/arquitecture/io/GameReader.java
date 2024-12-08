package software.ulpgc.kata4.arquitecture.io;

import software.ulpgc.kata4.arquitecture.model.Game;

import java.io.IOException;

public interface GameReader extends AutoCloseable {
    Game read() throws IOException;
}
