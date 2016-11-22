package tsoha.ystavapalvelu.database;

import java.io.*;
import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {

            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        List<String> files = new ArrayList<>();
        files.add("sql/drop_tables.sql");
        files.add("sql/create_tables.sql");
        files.add("sql/add_test_data.sql");


        for(String file : files) {
            StringBuffer stringBuffer = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream(file); //Database.class.getResourceAsStream(file);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
                String s;
                while((s = reader.readLine()) != null) {
                    stringBuffer.append(s);
                }
                reader.close();
                String[] statements = stringBuffer.toString().split(";");
                for(int i = 0; i < statements.length; i++) {
                    lista.add(statements[i]);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return lista;
    }
}
