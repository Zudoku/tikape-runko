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
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/register", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "register");
        }, new ThymeleafTemplateEngine());
        
        get("/login", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());
        
        get("/sendmessage/1", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "lahetaviesti");
        }, new ThymeleafTemplateEngine());
        
        get("/mymessages", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "omatviestit");
        }, new ThymeleafTemplateEngine());
        
        get("/myprofile", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "omattiedot");
        }, new ThymeleafTemplateEngine());
        
        get("/mypages", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "omatesittelysivut");
        }, new ThymeleafTemplateEngine());
        
        get("/sharedpages", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "omattiedot");
        }, new ThymeleafTemplateEngine());
        
        get("/newpage", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "esittelysivujaetut");
        }, new ThymeleafTemplateEngine());
        
        get("/page/1", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "esittelysivukatselma");
        }, new ThymeleafTemplateEngine());
        
        get("/page/edit/1", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "esittelysivumuokkaus");
        }, new ThymeleafTemplateEngine());
        
        get("/pagelist", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "esittelysivulistaus");
        }, new ThymeleafTemplateEngine());
        
        
    }
}
