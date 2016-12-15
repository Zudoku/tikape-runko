package tsoha.ystavapalvelu.models.user;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HakutarkoitusDao {

    private Database database;

    public HakutarkoitusDao(Database database) {
        this.database = database;
    }

    public List<Hakutarkoitus> getKaikkiHakuValinnat(){
        List<Hakutarkoitus> hakutarkoitukset = new ArrayList<>();
        hakutarkoitukset.add(new Hakutarkoitus("Urheilukaveri",1, false));
        hakutarkoitukset.add(new Hakutarkoitus("Onlinekaveri",2, false));
        hakutarkoitukset.add(new Hakutarkoitus("Syvät keskustelut",3, false));
        hakutarkoitukset.add(new Hakutarkoitus("Väittely",4, false));
        hakutarkoitukset.add(new Hakutarkoitus("Baariseura",5, false));
        hakutarkoitukset.add(new Hakutarkoitus("Hengailu",6, false));
        return hakutarkoitukset;
    }

    public List<Hakutarkoitus> getAloitusValinnat(){
        List<Hakutarkoitus> hakutarkoitukset = new ArrayList<>();
        hakutarkoitukset.forEach(hakutarkoitus -> hakutarkoitus.setChecked(true));
        return hakutarkoitukset;
    }

    public List<Hakutarkoitus> getHakuValinnatKayttajalle(int asiakas_id) throws SQLException {
        List<Hakutarkoitus> tulos = getKaikkiHakuValinnat();

        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT hakutarkoitus_id FROM Asiakashakutarkoitus " +
                "WHERE asiakas_id=?");

        statement.setInt(1, asiakas_id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            int foundId = resultSet.getInt("hakutarkoitus_id");
            tulos.forEach(hakutarkoitus -> {
                if(foundId == hakutarkoitus.getId()){
                    hakutarkoitus.setChecked(true);
                }
            });
        }
        statement.close();
        connection.close();



        return tulos;
    }

    public void poistaHakuValinnatKayttajalle(int asiakas_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Asiakashakutarkoitus WHERE asiakas_id=?");

        statement.setInt(1, asiakas_id);
        statement.execute();
        statement.close();
        connection.close();
    }

    public void lisaaHakuValinnatKayttajalle(List<Hakutarkoitus> hakutarkoitukset, int asiakas_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id,asiakas_id)" +
                " VALUES (?,?)");
        for (Hakutarkoitus hakutarkoitus : hakutarkoitukset) {
            statement.setInt(1, hakutarkoitus.getId());
            statement.setInt(2, asiakas_id);
            statement.execute();
        }

        statement.close();
        connection.close();
    }
}
