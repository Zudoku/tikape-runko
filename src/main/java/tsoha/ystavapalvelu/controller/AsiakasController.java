package tsoha.ystavapalvelu.controller;

import spark.Request;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.user.Asiakas;
import tsoha.ystavapalvelu.models.user.AsiakasDao;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AsiakasController {

    private AsiakasDao asiakasDao;
    private Database database;

    public AsiakasController(Database database) {
        this.database = database;
        this.asiakasDao = new AsiakasDao(database);
        this.init();
    }

    private void init() {
        get("/register", (req, res) -> {
            HashMap map = new HashMap<>();
            boolean badCreds = "1".equals(req.queryParams("badcreds"));
            map.put("badcreds", badCreds);



            return new ModelAndView(map, "register");
        }, new ThymeleafTemplateEngine());

        post("/register", (req, res) -> {
            String kayttajanimi = req.queryParams("kayttajanimi");
            String salasana = req.queryParams("salasana");
            String osoite = req.queryParams("osoite");
            Integer sukupuoli = Integer.parseInt(req.queryParams("sukupuoli"));

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");

            Date syntymaaika = format.parse(req.queryParams("syntymaaika"));
            Date liittynyt = new Date();

            Asiakas uusiAsiakas = new Asiakas(-1, kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite);
            try {
                asiakasDao.add(uusiAsiakas);
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
                res.redirect("/register?badcreds=1", 302);
                return "OK";
            }

            req.session(true).attribute("asiakas", asiakasDao.getAsiakasFromCredentials(kayttajanimi, salasana));
            res.redirect("/", 302);
            return "OK";

        });

        get("/login", (req, res) -> {
            HashMap map = new HashMap<>();
            boolean badCreds = "1".equals(req.queryParams("badcreds"));
            map.put("badcreds", badCreds);
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        post("/login", (req, res) -> {
            String kayttajanimi = req.queryParams("kayttajanimi");
            String salasana = req.queryParams("salasana");
            Asiakas loydettyAsiakas = asiakasDao.getAsiakasFromCredentials(kayttajanimi, salasana);
            if(loydettyAsiakas != null){
                req.session(true).attribute("asiakas", loydettyAsiakas);
                res.redirect("/", 302);
            } else {

                res.redirect("/login?badcreds=1", 302);
            }

            return "OK";

        });

        get("/profile/:id", (req, res) -> {
            HashMap map = new HashMap<>();

            int urlID = Integer.parseInt(req.params("id"));
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/login", 302);

            } else if(sessioAsiakas.getId() != urlID) {
                res.redirect("/", 302);
            }
            boolean updated = "1".equals(req.queryParams("updated"));
            map.put("updated", updated);

            return new ModelAndView(map, "omattiedot");
        }, new ThymeleafTemplateEngine());

        post("/profile/:id/update", (req, res) -> {
            HashMap map = new HashMap<>();

            int urlID = Integer.parseInt(req.params("id"));
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/login", 302);

            } else if(sessioAsiakas.getId() != urlID) {
                res.redirect("/", 302);
            }
            sessioAsiakas.setOsoite(req.queryParams("osoite"));
            sessioAsiakas.setSalasana(req.queryParams("salasana"));
            sessioAsiakas.setSukupuoli(Integer.parseInt(req.queryParams("sukupuoli")));

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");

            Date syntymaaika = format.parse(req.queryParams("syntymaaika"));

            sessioAsiakas.setSyntymaaika(syntymaaika);


            Asiakas uusiAsiakas = asiakasDao.update(sessioAsiakas);
            req.session().attribute("asiakas", uusiAsiakas);

            res.redirect("/profile/" + urlID + "?updated=1", 302);
            return "OK";
        });
        post("/profile/:id/delete", (req, res) -> {
            HashMap map = new HashMap<>();

            int urlID = Integer.parseInt(req.params("id"));
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/login", 302);

            } else if(sessioAsiakas.getId() != urlID) {
                res.redirect("/", 302);
            }


            asiakasDao.delete(urlID);
            req.session(true);
            res.redirect("/", 302);

            return "OK";
        });

        get("/logout", (req, res) -> {
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas != null) {
                req.session().removeAttribute("asiakas");
            }
            res.redirect("/", 302);

            return "OK";
        });


    }

}
