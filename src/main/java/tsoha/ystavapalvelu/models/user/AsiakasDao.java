package tsoha.ystavapalvelu.models.user;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.admin.Lasku;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsiakasDao implements Dao<Asiakas, Integer> {

    private Database database;

    public AsiakasDao(Database database) {
        this.database = database;
    }

    private Asiakas collect(ResultSet results) throws SQLException {

        Integer id = results.getInt("id");
        String kayttajanimi = results.getString("kayttajanimi");
        String salasana = results.getString("salasana");
        Date syntymaaika = results.getDate("syntymaaika");
        int sukupuoli = results.getInt("sukupuoli");
        Date littynyt = results.getDate("liittynyt");
        String osoite  = results.getString("osoite");

        return new Asiakas(id, kayttajanimi, salasana, syntymaaika, sukupuoli, littynyt, osoite);
    }

    @Override
    public Asiakas findOne(Integer key) throws SQLException {
        PreparedStatement statement;
        Connection connection = database.getConnection();
        statement = connection.prepareStatement("SELECT * FROM Asiakas WHERE id=?");
        statement.setInt(1,key);

        ResultSet result = statement.executeQuery();

        if(result.next()){
            Asiakas loydos = collect(result);
            statement.close();
            result.close();
            connection.close();
            return loydos;
        }
        statement.close();
        connection.close();
        return null;
    }


    public Asiakas getAsiakasFromCredentials(String kayttajanimi, String salasana) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Asiakas WHERE kayttajanimi=? AND salasana=?");

        statement.setString(1, kayttajanimi);
        statement.setString(2, salasana);

        ResultSet result = statement.executeQuery();


        if(result.next()){
            Asiakas loydos = collect(result);
            statement.close();
            result.close();
            connection.close();

            return loydos;
        }
        connection.close();
        return null;
    }

    public Asiakas update(Asiakas asiakas) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE Asiakas SET " +
                "salasana=?, syntymaaika=?, sukupuoli=?, osoite=? WHERE id=?");


        statement.setString(1, asiakas.getSalasana());
        statement.setDate(2, new java.sql.Date(asiakas.getSyntymaaika().getTime()));
        statement.setInt(3, asiakas.getSukupuoli());
        statement.setString(4, asiakas.getOsoite());

        statement.setInt(5, asiakas.getId());

        statement.executeUpdate();

        statement.close();
        connection.close();

        return findOne(asiakas.getId());

    }

    @Override
    public List<Asiakas> findAll() throws SQLException {
        List<Asiakas> asiakkaat = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Asiakas " +
                        "LEFT JOIN Lasku ON Asiakas.id=Lasku.asiakas_id " +
                        "LEFT JOIN Yllapitaja ON Lasku.yllapitaja_id=Yllapitaja.id"
        );

        ResultSet results = statement.executeQuery();

        while(results.next()){
            Asiakas loytynyt = collect(results);
            String yllapitajaString = results.getString("Yllapitaja.kayttajanimi");
            if (yllapitajaString == null || yllapitajaString.isEmpty()) {
                Lasku lasku = new Lasku(results.getInt("Lasku.id"),
                        results.getInt("Lasku.asiakas_id"),
                        results.getInt("Yllapitaja.id"),
                        results.getTimestamp("Lasku.laskutusaika"));
                lasku.setYllapitajaString(yllapitajaString);
            }

            asiakkaat.add(loytynyt);
        }
        statement.close();
        results.close();
        connection.close();
        return asiakkaat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Asiakas WHERE id=?");

        statement.setInt(1, key);
        statement.execute();
        statement.close();
        connection.close();
    }

    public void add(Asiakas asiakas) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Asiakas " +
                "(kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite) VALUES (?,?,?,?,?,?)");

        statement.setString(1, asiakas.getKayttajanimi());
        statement.setString(2, asiakas.getSalasana());
        statement.setDate(3, new java.sql.Date(asiakas.getSyntymaaika().getTime()));
        statement.setInt(4, asiakas.getSukupuoli());
        statement.setDate(5, new java.sql.Date(asiakas.getLiittynyt().getTime()));
        statement.setString(6, asiakas.getOsoite());

        statement.execute();

        statement.close();
        connection.close();

    }

    public List<Integer> findInterestingUsers(int asiakas_id) throws SQLException {
        List<Integer> asiakkaat = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT DISTINCT Asiakas.id FROM Asiakas " +
                        "LEFT JOIN Asiakashakutarkoitus " +
                        "ON Asiakas.id = Asiakashakutarkoitus.asiakas_id " +
                        "WHERE Asiakas.id <> ? AND " +
                        "Asiakashakutarkoitus.hakutarkoitus_id IN " +
                        "(SELECT hakutarkoitus_id FROM Asiakashakutarkoitus WHERE asiakas_id = ? )"
        );

        statement.setInt(1, asiakas_id);
        statement.setInt(2, asiakas_id);

        ResultSet results = statement.executeQuery();

        while(results.next()){
            asiakkaat.add(results.getInt("id"));
        }
        statement.close();
        results.close();
        connection.close();
        return asiakkaat;
    }
}
