package tsoha.ystavapalvelu.models.message;

import tsoha.ystavapalvelu.database.Dao;
import tsoha.ystavapalvelu.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ViestiDao implements Dao<Viesti, Integer>{

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        return null;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

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
}
