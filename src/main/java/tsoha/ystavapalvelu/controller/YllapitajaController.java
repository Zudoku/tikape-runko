package tsoha.ystavapalvelu.controller;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.admin.Yllapitaja;
import tsoha.ystavapalvelu.models.admin.YllapitajaDao;
import tsoha.ystavapalvelu.models.message.ViestiDao;
import tsoha.ystavapalvelu.models.user.Asiakas;
import tsoha.ystavapalvelu.models.user.AsiakasDao;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;

public class YllapitajaController {

    private Database database;
    private YllapitajaDao yllapitajaDao;
    private ViestiDao viestiDao;
    private AsiakasDao asiakasDao;

    public YllapitajaController(Database database) {
        this.database = database;
        this.yllapitajaDao = new YllapitajaDao(database);
        this.init();
    }

    private void init() {
        get("/loginadmin", (req, res) -> {
            HashMap map = new HashMap<>();
            boolean badCreds = "1".equals(req.queryParams("badcreds"));
            map.put("badcreds", badCreds);
            map.put("adminlogin", true);
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        post("/loginadmin", (req, res) -> {
            String kayttajanimi = req.queryParams("kayttajanimi");
            String salasana = req.queryParams("salasana");
            Yllapitaja yllapitaja = yllapitajaDao.getYllapitajaFromCredentials(kayttajanimi, salasana);
            if(yllapitaja != null){
                req.session(true).attribute("admin", yllapitaja);
                res.redirect("/statistiikka", 302);
            } else {

                res.redirect("/loginadmin?badcreds=1", 302);
            }

            return "OK";

        });

        get("/logoutadmin", (req, res) -> {
            Yllapitaja sessioYllapitaja = req.session().attribute("admin");
            if(sessioYllapitaja != null) {
                req.session().removeAttribute("admin");
            }
            res.redirect("/", 302);

            return "OK";
        });

        get("/laskut", (req, res) -> {
            HashMap map = new HashMap<>();

            Yllapitaja sessioYllapitaja = req.session().attribute("admin");
            if(sessioYllapitaja == null) {
                res.redirect("/loginadmin", 302);
            }


            return new ModelAndView(map, "laskut");
        }, new ThymeleafTemplateEngine());

        get("/statistiikka", (req, res) -> {
            HashMap map = new HashMap<>();

            Yllapitaja sessioYllapitaja = req.session().attribute("admin");
            if(sessioYllapitaja == null) {
                res.redirect("/loginadmin", 302);
            }
            map.put("stats", yllapitajaDao.getStatiikka());


            return new ModelAndView(map, "statistiikka");
        }, new ThymeleafTemplateEngine());
    }
}
