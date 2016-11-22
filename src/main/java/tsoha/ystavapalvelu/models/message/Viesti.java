package tsoha.ystavapalvelu.models.message;

public class Viesti {
    private Integer lahettaja;
    private Integer asiakas;
    private long lahetetty;
    private String sisalto;

    public Viesti(Integer lahettaja, Integer asiakas, long lahetetty, String sisalto) {
        this.lahettaja = lahettaja;
        this.asiakas = asiakas;
        this.lahetetty = lahetetty;
        this.sisalto = sisalto;
    }

    public Integer getLahettaja() {
        return lahettaja;
    }

    public void setLahettaja(Integer lahettaja) {
        this.lahettaja = lahettaja;
    }

    public Integer getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(Integer asiakas) {
        this.asiakas = asiakas;
    }

    public long getLahetetty() {
        return lahetetty;
    }

    public void setLahetetty(long lahetetty) {
        this.lahetetty = lahetetty;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }
}
