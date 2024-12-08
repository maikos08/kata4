package software.ulpgc.kata4.arquitecture.io;

import software.ulpgc.kata4.arquitecture.model.Game;

public class CsvGameDeserializer implements GameDeserializer {

    public static final int NA_Value = -1;

    public CsvGameDeserializer() {
    }

    @Override
    public Game deserialize(String text) {
        return deserialize(text.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
    }

    private Game deserialize(String[] split) {
        return new Game(
                toInt(split[0]),
                split[1],
                gamePlatform(normalize(split[2])),
                toInt(split[3]),
                gameGenre(normalize(split[4])),
                split[5],
                salesToInt(split[6]),
                salesToInt(split[7]),
                salesToInt(split[8]),
                salesToInt(split[9]),
                salesToInt(split[10])
        );
    }

    private int salesToInt(String s) {
        return (int) (Double.parseDouble(s) * Math.pow(10, 6));
    }

    private Game.Genre gameGenre(String normalize) {
        return Game.Genre.valueOf(normalize);
    }

    private Game.Platform gamePlatform(String s) {
        return Game.Platform.valueOf(s);
    }

    private String normalize(String s) {
        if (s.contains("-")) s = s.replace("-", "");
        if (Character.isDigit(s.charAt(0))) s = "_" + s;
        return s;
    }

    private int toInt(String s) {
        return (s.equals("N/A")) ?
                NA_Value:
                Integer.parseInt(s);
    }
}

