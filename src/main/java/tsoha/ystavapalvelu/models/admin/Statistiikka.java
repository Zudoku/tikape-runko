package tsoha.ystavapalvelu.models.admin;

public class Statistiikka {
    private int kayttajia;
    private int viesteja;
    private int sivuja;
    private int ystavapareja;

    public Statistiikka() {

    }

    public int getKayttajia() {
        return kayttajia;
    }

    public void setKayttajia(int kayttajia) {
        this.kayttajia = kayttajia;
    }

    public int getViesteja() {
        return viesteja;
    }

    public void setViesteja(int viesteja) {
        this.viesteja = viesteja;
    }

    public int getSivuja() {
        return sivuja;
    }

    public void setSivuja(int sivuja) {
        this.sivuja = sivuja;
    }


    public int getYstavapareja() {
        return ystavapareja;
    }

    public void setYstavapareja(int ystavapareja) {
        this.ystavapareja = ystavapareja;
    }
}
