package tsoha.ystavapalvelu.database.dao;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.page.EsittelySivu;
import tsoha.ystavapalvelu.models.user.Asiakas;

import java.sql.*;
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
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Esittelysivu WHERE sivu_id=?");

        statement.setInt(1, key);

        ResultSet result = statement.executeQuery();


        if(result.next()) {
            EsittelySivu loydos = collect(result);
            loydos.setOmistajaString(findOmistaja(loydos.getOmistaja_id()));
            statement.close();
            result.close();
            connection.close();
            return loydos;
        }
        connection.close();
        return null;
    }

    public String findOmistaja(int omistaja_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT kayttajanimi FROM Asiakas WHERE id=?");

        statement.setInt(1, omistaja_id);

        ResultSet result = statement.executeQuery();
        String name = "";
        if(result.next()) {
            name = result.getString("kayttajanimi");
        }
        result.close();
        statement.close();
        connection.close();

        return name;
    }

    @Override
    public List<EsittelySivu> findAll() throws SQLException {
        List<EsittelySivu> esittelySivu = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Esittelysivu WHERE " +
                "julkinen=true ORDER BY muokattu DESC");

        ResultSet result = statement.executeQuery();

        while(result.next()){
            EsittelySivu loydos = collect(result);
            loydos.setOmistajaString(findOmistaja(loydos.getOmistaja_id()));
            esittelySivu.add(loydos);
        }
        statement.close();
        result.close();
        connection.close();


        return esittelySivu;
    }

    public List<EsittelySivu> search(String haku, List<Integer> kiinnostavatIdt) throws SQLException {
        List<EsittelySivu> esittelySivu = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Esittelysivu WHERE " +
                        "julkinen=true AND " +
                        "otsikko LIKE ? " +
                        "ORDER BY muokattu DESC"
        );
        statement.setString(1, "%" + haku + "%");
        ResultSet result = statement.executeQuery();

        while(result.next()){
            EsittelySivu loydos = collect(result);
            loydos.setOmistajaString(findOmistaja(loydos.getOmistaja_id()));
            esittelySivu.add(loydos);
        }
        statement.close();
        result.close();
        connection.close();


        List<EsittelySivu> lopullisetSivut = new ArrayList<>();

        esittelySivu.forEach(sivu -> {
            if(kiinnostavatIdt.contains(sivu.getOmistaja_id())) {
                lopullisetSivut.add(sivu);
            }
        });

        return lopullisetSivut;
    }


    public List<EsittelySivu> findAllWithOmistajaId(int omistaja_id) throws SQLException {
        List<EsittelySivu> esittelySivu = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Esittelysivu WHERE " +
                "omistaja_id=? ORDER BY muokattu DESC");

        statement.setInt(1, omistaja_id);
        ResultSet result = statement.executeQuery();

        while(result.next()){
            esittelySivu.add(collect(result));
        }
        statement.close();
        result.close();
        connection.close();

        return esittelySivu;
    }

    public List<EsittelySivu> findAllSharedFor(int asiakas_id) throws SQLException {
        List<EsittelySivu> esittelySivu = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Esittelysivu WHERE " +
                "sivu_id IN (SELECT sivu_id FROM Esittelysivuasiakas WHERE asiakas_id=?) ORDER BY muokattu DESC");

        statement.setInt(1, asiakas_id);
        ResultSet result = statement.executeQuery();

        while(result.next()){
            EsittelySivu a = collect(result);
            a.setOmistajaString(findOmistaja(a.getOmistaja_id()));
            esittelySivu.add(a);
        }
        statement.close();
        result.close();
        connection.close();

        return esittelySivu;
    }

    public List<EsittelySivu> findAllShareable(int asiakas_id, int omistaja_id) throws SQLException {
        List<EsittelySivu> esittelySivu = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Esittelysivu WHERE " +
                "julkinen=false AND " +
                "omistaja_id=? AND " +
                "sivu_id NOT IN (SELECT sivu_id FROM Esittelysivuasiakas WHERE asiakas_id=?) ORDER BY muokattu DESC");

        statement.setInt(1, omistaja_id);
        statement.setInt(2, asiakas_id);
        ResultSet result = statement.executeQuery();

        while(result.next()){
            EsittelySivu a = collect(result);
            a.setOmistajaString(findOmistaja(a.getOmistaja_id()));
            esittelySivu.add(a);
        }
        statement.close();
        result.close();
        connection.close();

        return esittelySivu;
    }

    public void lisaaJako(int asiakas_id, int page_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Esittelysivuasiakas (sivu_id, asiakas_id) " +
                "VALUES (?,?)");

        statement.setInt(1, page_id);
        statement.setInt(2, asiakas_id);

        statement.execute();

        statement.close();
        connection.close();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Esittelysivu WHERE sivu_id=?");
        statement.setInt(1, key);

        statement.execute();
        statement.close();
        connection.close();

    }

    public EsittelySivu update(EsittelySivu esittelySivu) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement statement = connection.prepareStatement("UPDATE EsittelySivu SET " +
                "otsikko=?, leipateksti=?, muokattu=?, julkinen=? WHERE sivu_id=?");


        statement.setString(1, esittelySivu.getOtsikko());
        statement.setString(2, esittelySivu.getLeipateksti());
        statement.setDate(3, new java.sql.Date(esittelySivu.getMuokattu().getTime()));
        statement.setBoolean(4, esittelySivu.isJulkinen());

        statement.setInt(5, esittelySivu.getSivu_id());


        statement.executeUpdate();
        statement.close();
        connection.close();

        return findOne(esittelySivu.getSivu_id());
    }

    public void add(EsittelySivu esittelySivu) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Esittelysivu " +
                "(omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) VALUES (?,?,?,?,?,?)");

        statement.setInt(1, esittelySivu.getOmistaja_id());
        statement.setString(2, esittelySivu.getOtsikko());
        statement.setString(3, esittelySivu.getLeipateksti());
        statement.setTimestamp(4, esittelySivu.getLuotu());
        statement.setTimestamp(5, esittelySivu.getMuokattu());
        statement.setBoolean(6, esittelySivu.isJulkinen());

        statement.execute();
        statement.close();
        connection.close();

    }
}
