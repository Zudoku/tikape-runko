package tsoha.ystavapalvelu.models.admin;

public class Yllapitaja {
    private int id;
    private String kayttajanimi;
    private String salasana;

    public Yllapitaja(int id, String kayttajanimi, String salasana) {
        this.id = id;
        this.kayttajanimi = kayttajanimi;
        this.salasana = salasana;
    }

    public int getId() {
        return id;
    }

    public String getKayttajanimi() {
        return kayttajanimi;
    }

    public String getSalasana() {
        return salasana;
    }
}
