package tsoha.ystavapalvelu.models.page;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EsittelySivu {

    private int sivu_id;
    private int omistaja_id;
    private String otsikko;
    private String leipateksti;
    private Timestamp luotu;
    private Timestamp muokattu;
    private boolean julkinen;

    private String omistajaString;

    public String getOmistajaString() {
        return omistajaString;
    }

    public void setOmistajaString(String omistajaString) {
        this.omistajaString = omistajaString;
    }

    public EsittelySivu(int sivu_id, int omistaja_id, String otsikko, String leipateksti, Timestamp luotu, Timestamp muokattu, boolean julkinen) {
        this.sivu_id = sivu_id;
        this.omistaja_id = omistaja_id;
        this.otsikko = otsikko;
        this.leipateksti = leipateksti;
        this.luotu = luotu;
        this.muokattu = muokattu;
        this.julkinen = julkinen;
    }

    public int getSivu_id() {
        return sivu_id;
    }

    public void setSivu_id(int sivu_id) {
        this.sivu_id = sivu_id;
    }

    public int getOmistaja_id() {
        return omistaja_id;
    }

    public void setOmistaja_id(int omistaja_id) {
        this.omistaja_id = omistaja_id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public String getLeipateksti() {
        return leipateksti;
    }

    public void setLeipateksti(String leipateksti) {
        this.leipateksti = leipateksti;
    }

    public Timestamp getLuotu() {
        return luotu;
    }

    public void setLuotu(Timestamp luotu) {
        this.luotu = luotu;
    }

    public Timestamp getMuokattu() {
        return muokattu;
    }

    public void setMuokattu(Timestamp muokattu) {
        this.muokattu = muokattu;
    }

    public boolean isJulkinen() {
        return julkinen;
    }

    public void setJulkinen(boolean julkinen) {
        this.julkinen = julkinen;
    }

    public String prettyPrint(Timestamp stamp) {
        return LocalDateTime.ofEpochSecond(stamp.getTime(), stamp.getNanos(), ZoneOffset.ofHours(1))
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "EsittelySivu{" +
                "sivu_id=" + sivu_id +
                ", omistaja_id=" + omistaja_id +
                ", otsikko='" + otsikko + '\'' +
                ", leipateksti='" + leipateksti + '\'' +
                ", luotu=" + luotu +
                ", muokattu=" + muokattu +
                ", julkinen=" + julkinen +
                '}';
    }
}
