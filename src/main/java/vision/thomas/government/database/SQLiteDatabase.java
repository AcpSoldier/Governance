package vision.thomas.government.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase {

    private final File file;

    private Connection connection;

    public SQLiteDatabase(String path) {

        this.file = new File(path);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (open()) {
            System.out.println("Yaaaay!");
        }
    }

    public boolean open() {

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                open();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void close() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
