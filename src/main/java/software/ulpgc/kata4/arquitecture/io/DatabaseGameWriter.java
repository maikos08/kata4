package software.ulpgc.kata4.arquitecture.io;

import software.ulpgc.kata4.arquitecture.model.Game;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NVARCHAR;

public class DatabaseGameWriter implements GameWriter
{
    private final Connection connection;
    private final PreparedStatement insertGamesPreparedStatement;

    public static DatabaseGameWriter open(File file) throws SQLException {
        return new DatabaseGameWriter("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public DatabaseGameWriter(String connection) throws SQLException {
        this(DriverManager.getConnection(connection));
    }

    public DatabaseGameWriter(Connection connection) throws SQLException {
        this.connection = connection;
        setAutoCommmitFalse();
        createTables();
        this.insertGamesPreparedStatement = createPreparedStatement();
    }

    private final static String InsertGameStatement = """
        INSERT INTO games (ranking,name,platform,releaseDate,genre,publisher,naSales,euSales,jpSales,otherSales,globalSales)
        VALUES (?,?,?,?,?,?,?,?,?,?,?)
        """;
    private PreparedStatement createPreparedStatement() throws SQLException {
        return connection.prepareStatement(InsertGameStatement);
    }

    private final static String DropTableStatement = """
            DROP TABLE IF EXISTS games
            """;
    private final static String CreateGamesTableStatement = """
        CREATE TABLE games (
            ranking INTEGER PRIMARY KEY,
            name TEXT ALTERNATIVE KEY,
            platform TEXT NOT NULL,
            releaseDate INTEGER,
            genre TEXT,
            publisher TEXT,
            naSales INTEGER,
            euSales INTEGER,
            jpSales INTEGER,
            otherSales INTEGER,
            globalSales INTEGER)
        """;
    private void createTables() throws SQLException {
        connection.createStatement().execute(DropTableStatement);
        connection.createStatement().execute(CreateGamesTableStatement);
    }

    private void setAutoCommmitFalse() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void write(Game game) {
        try {
            insertGameStatement(game).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement insertGameStatement(Game game) throws SQLException {
        insertGamesPreparedStatement.clearParameters();
        parametersOf(game).forEach(this::define);
        return insertGamesPreparedStatement;
    }

    private void define(Parameter p) {
        try {
            if (p.value == null)
                insertGamesPreparedStatement.setNull(p.index, p.type);
            else
                insertGamesPreparedStatement.setObject(p.index, p.value, p.type);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Parameter> parametersOf(Game game) {
        return List.of(
                new Parameter(1, game.ranking(), INTEGER),
                new Parameter(2, game.name(), NVARCHAR),
                new Parameter(3, game.platform(), NVARCHAR),
                new Parameter(4, game.releaseDate() != -1 ? game.releaseDate(): null, INTEGER),
                new Parameter(5, game.genre(), NVARCHAR),
                new Parameter(6, game.publisher(), NVARCHAR),
                new Parameter(7, game.naSales(), INTEGER),
                new Parameter(8, game.euSales(), INTEGER),
                new Parameter(9, game.jpSales(), INTEGER),
                new Parameter(10, game.otherSales(), INTEGER),
                new Parameter(11, game.globalSales(), INTEGER)
        );
    }

    @Override
    public void close() throws Exception {
        connection.commit();
        connection.close();
    }

    private record Parameter(int index, Object value, int type) {
    }
}
