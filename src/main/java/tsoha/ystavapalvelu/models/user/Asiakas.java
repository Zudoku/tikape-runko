package tsoha.ystavapalvelu.models.user;

import java.util.Date;

public class Asiakas {


    private int id;
    private String kayttajanimi;
    private String salasana;
    private Date syntymaaika;
    private int sukupuoli;
    private Date liittynyt;
    private String osoite;

    public Asiakas(int id, String kayttajanimi, String salasana, Date syntymaaika, int sukupuoli, Date liittynyt, String osoite) {
        this.id = id;
        this.kayttajanimi = kayttajanimi;
        this.salasana = salasana;
        this.syntymaaika = syntymaaika;
        this.sukupuoli = sukupuoli;
        this.liittynyt = liittynyt;
        this.osoite = osoite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKayttajanimi() {
        return kayttajanimi;
    }

    public void setKayttajanimi(String kayttajanimi) {
        this.kayttajanimi = kayttajanimi;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }

    public Date getSyntymaaika() {
        return syntymaaika;
    }

    public void setSyntymaaika(Date syntymaaika) {
        this.syntymaaika = syntymaaika;
    }

    public int getSukupuoli() {
        return sukupuoli;
    }

    public void setSukupuoli(int sukupuoli) {
        this.sukupuoli = sukupuoli;
    }

    public Date getLiittynyt() {
        return liittynyt;
    }

    public void setLiittynyt(Date liittynyt) {
        this.liittynyt = liittynyt;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    @Override
    public String toString() {
        return "Asiakas{" +
                "id=" + id +
                ", kayttajanimi='" + kayttajanimi + '\'' +
                ", salasana='" + salasana + '\'' +
                ", syntymaaika=" + syntymaaika +
                ", sukupuoli=" + sukupuoli +
                ", liittynyt=" + liittynyt +
                ", osoite='" + osoite + '\'' +
                '}';
    }
}
