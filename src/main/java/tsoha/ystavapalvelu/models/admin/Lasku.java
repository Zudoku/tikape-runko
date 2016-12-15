package tsoha.ystavapalvelu.models.admin;

import java.sql.Timestamp;

public class Lasku {
    private int id;
    private int asiakas_id;
    private int yllapitaja_id;
    private Timestamp laskutusaika;
    private String yllapitajaString;

    public Lasku(int id, int asiakas_id, int yllapitaja_id, Timestamp laskutusaika) {
        this.id = id;
        this.asiakas_id = asiakas_id;
        this.yllapitaja_id = yllapitaja_id;
        this.laskutusaika = laskutusaika;
        this.yllapitajaString = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsiakas_id() {
        return asiakas_id;
    }

    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }

    public int getYllapitaja_id() {
        return yllapitaja_id;
    }

    public void setYllapitaja_id(int yllapitaja_id) {
        this.yllapitaja_id = yllapitaja_id;
    }

    public Timestamp getLaskutusaika() {
        return laskutusaika;
    }

    public void setLaskutusaika(Timestamp laskutusaika) {
        this.laskutusaika = laskutusaika;
    }

    public String getYllapitajaString() {
        return yllapitajaString;
    }

    public void setYllapitajaString(String yllapitajaString) {
        this.yllapitajaString = yllapitajaString;
    }
}
