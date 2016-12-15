package tsoha.ystavapalvelu.database.dao;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.message.Viesti;
import tsoha.ystavapalvelu.models.page.EsittelySivu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViestiDao implements Dao<Viesti, Integer>{

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    private Viesti collect(ResultSet results) throws SQLException {
        int id = results.getInt("id");
        int lahettaja = results.getInt("lahettaja");
        int vastaanttaja = results.getInt("vastaanottaja");
        Timestamp lahetetty = results.getTimestamp("lahetetty");
        String sisalto = results.getString("sisalto");

        return new Viesti(id, lahettaja, vastaanttaja, lahetetty, sisalto);
    }



    public String haeVastaanottajanNimi(Viesti viesti) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT kayttajanimi FROM Asiakas WHERE id=?");

        statement.setInt(1, viesti.getVastaanottaja());

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

    public String haeLahettajanNimi(Viesti viesti) throws SQLException {

        if(viesti.getLahettaja() == -1) {
            return "Anonyymi";
        }

        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT kayttajanimi FROM Asiakas WHERE id=?");

        statement.setInt(1, viesti.getLahettaja());

        ResultSet result = statement.executeQuery();
        String name = "Anonyymi";
        if(result.next()) {
            name = result.getString("kayttajanimi");
        }
        result.close();
        statement.close();
        connection.close();

        return name;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Viesti WHERE id=?");

        statement.setInt(1, key);

        ResultSet result = statement.executeQuery();


        if(result.next()) {
            Viesti loydos = collect(result);
            statement.close();
            result.close();
            connection.close();
            return loydos;
        }
        connection.close();
        return null;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        List<Viesti> viestit = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet result = statement.executeQuery();

        while(result.next()){
            viestit.add(collect(result));
        }
        statement.close();
        result.close();
        connection.close();


        return viestit;
    }

    public List<Viesti> findAllLahetetty(int id) throws SQLException {
        List<Viesti> viestit = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Viesti WHERE lahettaja=?");
        statement.setInt(1, id);

        ResultSet result = statement.executeQuery();

        while(result.next()){
            Viesti viesti = collect(result);
            viesti.setVastaanottajaString(haeVastaanottajanNimi(viesti));
            viestit.add(viesti);
        }
        statement.close();
        result.close();
        connection.close();


        return viestit;
    }

    public List<Viesti> findAllVastaanotettu(int id) throws SQLException {
        List<Viesti> viestit = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Viesti WHERE vastaanottaja=?");
        statement.setInt(1, id);

        ResultSet result = statement.executeQuery();

        while(result.next()){
            Viesti viesti = collect(result);
            viesti.setLahettajaString(haeLahettajanNimi(viesti));
            viestit.add(viesti);
        }
        statement.close();
        result.close();
        connection.close();


        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Viesti WHERE id=?");

        statement.setInt(1, key);

        statement.execute();

        statement.close();
        connection.close();
    }

    public void deleteMessagesRelatedTo(Integer asiakas_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Viesti WHERE lahettaja = ? OR vastaanottaja = ?");

        statement.setInt(1, asiakas_id);
        statement.setInt(2, asiakas_id);

        statement.execute();

        statement.close();
        connection.close();
    }

    public Viesti update(Viesti viesti) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement statement = connection.prepareStatement("UPDATE Viesti SET " +
                "sisalto=? WHERE id=?");


        statement.setString(1, viesti.getSisalto());
        statement.setInt(2, viesti.getId());

        statement.executeUpdate();
        statement.close();
        connection.close();

        return findOne(viesti.getId());
    }

    public void add(Viesti viesti) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Viesti " +
                "(lahettaja, vastaanottaja, lahetetty, sisalto) VALUES (?,?,?,?)");

        statement.setInt(1, viesti.getLahettaja());
        statement.setInt(2, viesti.getVastaanottaja());
        statement.setTimestamp(3, viesti.getLahetetty());
        statement.setString(4, viesti.getSisalto());

        statement.execute();
        statement.close();
        connection.close();

    }
}
