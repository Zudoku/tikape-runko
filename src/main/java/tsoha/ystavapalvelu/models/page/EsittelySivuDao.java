package tsoha.ystavapalvelu.models.page;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EsittelySivuDao implements Dao<EsittelySivu, Integer> {

    private Database database;

    public EsittelySivuDao(Database database) {
        this.database = database;
    }

    private EsittelySivu collect(ResultSet results) throws SQLException {

        Integer sivu_id = results.getInt("sivu_id");
        Integer omistaja_id = results.getInt("omistaja_id");
        String otsikko = results.getString("otsikko");
        String leipateksti = results.getString("leipateksti");
        Timestamp luotu = results.getTimestamp("luotu");
        Timestamp muokattu= results.getTimestamp("muokattu");
        boolean julkinen = results.getBoolean("julkinen");


        return new EsittelySivu(sivu_id, omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen);
    }

    @Override
    public EsittelySivu findOne(Integer key) throws SQLException {

        PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM Esittelysivu WHERE sivu_id=?");

        statement.setInt(1, key);

        ResultSet result = statement.executeQuery();

        if(result.next()) {
            return collect(result);
        }

        return null;
    }

    @Override
    public List<EsittelySivu> findAll() throws SQLException {
        List<EsittelySivu> esittelySivu = new ArrayList<>();
        PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM Esittelysivu WHERE " +
                "julkinen=true ORDER BY muokattu DESC");

        ResultSet result = statement.executeQuery();

        while(result.next()){
            esittelySivu.add(collect(result));
        }


        return esittelySivu;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement("DELETE FROM Esittelysivu WHERE sivu_id=?");
        statement.setInt(1, key);
        
        statement.execute();

    }

    public EsittelySivu update(EsittelySivu esittelySivu) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement("UPDATE EsittelySivu SET " +
                "otsikko=?, leipateksti=?, muokattu=?, julkinen=?");


        statement.setString(1, esittelySivu.getOtsikko());
        statement.setString(2, esittelySivu.getLeipateksti());
        statement.setDate(3, new java.sql.Date(esittelySivu.getMuokattu().getTime()));
        statement.setBoolean(4, esittelySivu.isJulkinen());


        statement.executeUpdate();

        return findOne(esittelySivu.getSivu_id());
    }
}
