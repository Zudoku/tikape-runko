package tsoha.ystavapalvelu.models.message;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Viesti {
    private Integer id;
    private Integer lahettaja;
    private Integer vastaanottaja;
    private Timestamp lahetetty;
    private String sisalto;
    private String lahettajaString;
    private String vastaanottajaString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLahettaja() {
        return lahettaja;
    }

    public void setLahettaja(Integer lahettaja) {
        this.lahettaja = lahettaja;
    }

    public Integer getVastaanottaja() {
        return vastaanottaja;
    }

    public void setVastaanottaja(Integer vastaanottaja) {
        this.vastaanottaja = vastaanottaja;
    }

    public Timestamp getLahetetty() {
        return lahetetty;
    }

    public void setLahetetty(Timestamp lahetetty) {
        this.lahetetty = lahetetty;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    public Viesti(Integer id, Integer lahettaja, Integer vastaanottaja, Timestamp lahetetty, String sisalto) {
        this.id = id;

        this.lahettaja = lahettaja;
        this.vastaanottaja = vastaanottaja;
        this.lahetetty = lahetetty;
        this.sisalto = sisalto;
    }

    public String prettyPrint(Timestamp stamp) {
        return stamp.toString();
    }

    public String getLahettajaString() {
        return lahettajaString;
    }

    public void setLahettajaString(String lahettajaString) {
        this.lahettajaString = lahettajaString;
    }

    public String getVastaanottajaString() {
        return vastaanottajaString;
    }

    public void setVastaanottajaString(String vastaanottajaString) {
        this.vastaanottajaString = vastaanottajaString;
    }


}
