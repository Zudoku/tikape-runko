package tsoha.ystavapalvelu;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tsoha.ystavapalvelu.controller.AsiakasController;
import tsoha.ystavapalvelu.controller.EsittelySivuController;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.page.EsittelySivu;
import tsoha.ystavapalvelu.models.user.Asiakas;

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
            boolean kirjautunut = req.session().attribute("asiakas") != null;
            map.put("kirjautunut", !kirjautunut);
            if(kirjautunut) {
                map.put("kayttaja", ( (Asiakas)req.session().attribute("asiakas")).getKayttajanimi());
                map.put("kayttajaid", ( (Asiakas)req.session().attribute("asiakas")).getId());
            }

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        AsiakasController asiakasController = new AsiakasController(database);
        EsittelySivuController esittelySivuController = new EsittelySivuController(database);
        

        
        get("/sendmessage/1", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "lahetaviesti");
        }, new ThymeleafTemplateEngine());
        
        get("/mymessages", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "omatviestit");
        }, new ThymeleafTemplateEngine());

        

        
        get("/sharedpages", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "esittelysivujaetut");
        }, new ThymeleafTemplateEngine());
        
        get("/newpage", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "esittelysivuuusi");
        }, new ThymeleafTemplateEngine());

        

    }
}
