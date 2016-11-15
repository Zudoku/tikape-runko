package tsoha.ystavapalvelu;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tsoha.ystavapalvelu.database.Database;

public class Main {

    public static void main(String[] args) throws Exception {
        // Asetetaan public kansio kaikkien nähtäville
        Spark.staticFileLocation("/public");
        
        String jdbcOsoite = "";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 
        
        Database database = new Database(jdbcOsoite);
        database.init();

        // jos heroku antaa PORT-ympäristömuuttujan, asetetaan portti 
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
