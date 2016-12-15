package tsoha.ystavapalvelu.models.admin;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.user.Asiakas;

import java.sql.*;
import java.util.List;

public class YllapitajaDao implements Dao<Yllapitaja, Integer> {

    private Database database;

    public YllapitajaDao(Database database) {
        this.database = database;
    }

    private Yllapitaja collect(ResultSet results) throws SQLException {

        Integer id = results.getInt("id");
        String kayttajanimi = results.getString("kayttajanimi");
        String salasana = results.getString("salasana");

        return new Yllapitaja(id, kayttajanimi, salasana);
    }

    public Yllapitaja getYllapitajaFromCredentials(String kayttajanimi, String salasana) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Yllapitaja WHERE kayttajanimi=? AND salasana=?");

        statement.setString(1, kayttajanimi);
        statement.setString(2, salasana);

        ResultSet result = statement.executeQuery();


        if(result.next()){
            Yllapitaja loydos = collect(result);
            statement.close();
            result.close();
            connection.close();

            return loydos;
        }
        connection.close();
        return null;
    }

    @Override
    public Yllapitaja findOne(Integer key) throws SQLException {
        return null;
    }

    @Override
    public List<Yllapitaja> findAll() throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }

    public Statistiikka getStatiikka() throws SQLException {

        Statistiikka tulos = new Statistiikka();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS kayttajia FROM Asiakas");

        ResultSet result = statement.executeQuery();

        if(result.next()){
            tulos.setKayttajia(result.getInt("kayttajia"));
        }
        statement.close();
        result.close();

        statement = connection.prepareStatement(
                "SELECT COUNT(*) AS viesteja FROM Viesti");
        result = statement.executeQuery();

        if(result.next()){
            tulos.setViesteja(result.getInt("viesteja"));
        }
        statement.close();
        result.close();

        statement = connection.prepareStatement("SELECT COUNT(*) AS sivuja FROM Esittelysivu");
        result = statement.executeQuery();

        if(result.next()){
            tulos.setSivuja(result.getInt("sivuja"));

        }
        statement.close();
        result.close();

        statement = connection.prepareStatement("SELECT COUNT(*) AS laskuja FROM Lasku");
        result = statement.executeQuery();

        if(result.next()){
            tulos.setLaskuja(result.getInt("laskuja"));

        }
        statement.close();
        result.close();

        statement = connection.prepareStatement("SELECT COUNT(*) AS pareja FROM Ystavapari");
        result = statement.executeQuery();

        if(result.next()){
            tulos.setYstavapareja(result.getInt("pareja") / 2);

        }
        statement.close();
        result.close();


        connection.close();
        return tulos;
    }
}
