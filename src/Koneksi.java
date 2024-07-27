import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    private Connection connection;

    public Koneksi() {
        try {
            String user = "root";
            String pw = "";
            String db = "jdbc:mysql://127.0.0.1:3306/dbMhs";
            connection = DriverManager.getConnection(db, user, pw);
            System.out.println("Koneksi berhasil");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
