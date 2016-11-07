package tsoha.ystavapalvelu;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tsoha.ystavapalvelu.database.Database;

public class Main {

    public static void main(String[] args) throws Exception {
        String jdbcOsoite = "";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 
        
        Database database = new Database(jdbcOsoite);
        database.init();

        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("toimiiko", database.testDatabaseConnection());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

    }
}
