package tsoha.ystavapalvelu.models.user;

import java.util.Arrays;
import java.util.List;

public class Hakutarkoitus {

    public static List<String> laabelit = Arrays.asList("Urheilukaveri","Onlinekaveri","Syvät keskustelut","Väittely","Baariseura","Hengailu");

    private String label;
    private int id;
    private boolean checked;

    public Hakutarkoitus(String label, int id, boolean checked) {
        this.label = label;
        this.id = id;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
