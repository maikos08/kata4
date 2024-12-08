package software.ulpgc.kata4.arquitecture.io;

import software.ulpgc.kata4.arquitecture.model.Game;

public interface GameWriter extends AutoCloseable {
    void write(Game game);
}
