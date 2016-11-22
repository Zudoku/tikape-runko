package tsoha.ystavapalvelu.models.user;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;

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

        statement = database.getConnection().prepareStatement("SELECT * FROM Asiakas WHERE id=?");
        statement.setInt(1,key);

        ResultSet result = statement.executeQuery();

        if(result.next()){
            return collect(result);
        }
        return null;
    }


    public Asiakas getAsiakasFromCredentials(String kayttajanimi, String salasana) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM Asiakas WHERE kayttajanimi=? AND salasana=?");

        statement.setString(1, kayttajanimi);
        statement.setString(2, salasana);

        ResultSet result = statement.executeQuery();

        if(result.next()){
            return collect(result);
        }
        return null;
    }

    public Asiakas update(Asiakas asiakas) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Asiakas SET " +
                "salasana=?, syntymaaika=?, sukupuoli=?, osoite=?");


        statement.setString(1, asiakas.getSalasana());
        statement.setDate(2, new java.sql.Date(asiakas.getSyntymaaika().getTime()));
        statement.setInt(3, asiakas.getSukupuoli());
        statement.setString(4, asiakas.getOsoite());

        statement.executeUpdate();

        return findOne(asiakas.getId());

    }

    @Override
    public List<Asiakas> findAll() throws SQLException {
        List<Asiakas> asiakkaat = new ArrayList<>();

        PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM Asiakas");

        ResultSet results = statement.executeQuery();

        while(results.next()){
            asiakkaat.add(collect(results));
        }

        return asiakkaat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement("DELETE FROM Asiakas WHERE id=?");

        statement.setInt(1, key);
        statement.execute();
    }

    public void add(Asiakas asiakas) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO Asiakas " +
                "(kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite) VALUES (?,?,?,?,?,?)");

        statement.setString(1, asiakas.getKayttajanimi());
        statement.setString(2, asiakas.getSalasana());
        statement.setDate(3, new java.sql.Date(asiakas.getSyntymaaika().getTime()));
        statement.setInt(4, asiakas.getSukupuoli());
        statement.setDate(5, new java.sql.Date(asiakas.getLiittynyt().getTime()));
        statement.setString(6, asiakas.getOsoite());

        statement.execute();

    }
}
