package tsoha.ystavapalvelu.controller;

import spark.Request;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.message.ViestiDao;
import tsoha.ystavapalvelu.models.user.Asiakas;
import tsoha.ystavapalvelu.models.user.AsiakasDao;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AsiakasController {

    private AsiakasDao asiakasDao;
    private Database database;
    private ViestiDao viestiDao;

    public AsiakasController(Database database) {
        this.database = database;
        this.asiakasDao = new AsiakasDao(database);
        this.viestiDao = new ViestiDao(database);
        this.init();
    }

    private void init() {
        get("/register", (req, res) -> {
            HashMap map = new HashMap<>();
            boolean badCreds = "1".equals(req.queryParams("badcreds"));
            map.put("badcreds", badCreds);
            if(req.session().attribute("errors") != null) {
                map.put("errors", req.session().attribute("errors"));
            } else {
                map.put("errors", new ArrayList<String>());
            }

            if(req.session().attribute("validatedinput") != null) {
                map.put("validatedinput", req.session().attribute("validatedinput"));
            } else {
                map.put("validatedinput", new Asiakas(-1, "", "", null, 1, null, ""));
            }

            req.session().removeAttribute("errors");
            req.session().removeAttribute("validatedinput");

            return new ModelAndView(map, "register");
        }, new ThymeleafTemplateEngine());

        post("/register", (req, res) -> {
            List<String> errors = new ArrayList<String>();
            Asiakas input = new Asiakas(-1, "", "", null, 1, null, "");

            //Validoi input


            String kayttajanimi = req.queryParams("kayttajanimi");
            String salasana = req.queryParams("salasana");
            String osoite = req.queryParams("osoite");
            String sukupuoliRaw = req.queryParams("sukupuoli");
            String syntymaaikaRaw = req.queryParams("syntymaaika");

            validoiKayttajanimi(kayttajanimi, errors, input);
            validoiSalasana(salasana, errors, input);
            validoiOsoite(osoite, errors, input);
            validoiSukupuoli(sukupuoliRaw, errors, input);
            validoiSyntymapaiva(syntymaaikaRaw, errors, input);

            if(!errors.isEmpty()) {
                req.session().attribute("errors", errors);
                req.session().attribute("validatedinput", input);
                res.redirect("/register?badcreds=1", 302);
                return "OK";
            }

            try {
                asiakasDao.add(input);
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
                res.redirect("/?norights=1", 302);
            }
            boolean updated = "1".equals(req.queryParams("updated"));
            map.put("updated", updated);

            map.put("kayttaja", sessioAsiakas.getKayttajanimi());
            map.put("kayttajatiedot", sessioAsiakas);
            map.put("kayttajaid", sessioAsiakas.getId());

            if(req.session().attribute("errors") != null) {
                map.put("errors", req.session().attribute("errors"));
            } else {
                map.put("errors", new ArrayList<String>());
            }

            req.session().removeAttribute("errors");

            return new ModelAndView(map, "omattiedot");
        }, new ThymeleafTemplateEngine());

        post("/profile/:id/update", (req, res) -> {
            List<String> errors = new ArrayList<String>();
            int urlID = Integer.parseInt(req.params("id"));

            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            Asiakas input = new Asiakas(sessioAsiakas.getId(), sessioAsiakas.getKayttajanimi(), sessioAsiakas.getSalasana(),
                    sessioAsiakas.getSyntymaaika(), sessioAsiakas.getSukupuoli(), sessioAsiakas.getLiittynyt(), sessioAsiakas.getOsoite());
            if(sessioAsiakas == null) {
                res.redirect("/login", 302);
            } else if(sessioAsiakas.getId() != urlID) {
                res.redirect("/?norights=1", 302);
            }
            validoiOsoite(req.queryParams("osoite"), errors, input);

            if(req.queryParams("salasana") != null  && !req.queryParams("salasana").isEmpty()) {
                validoiSalasana(req.queryParams("salasana"), errors, input);

            }
            validoiSukupuoli(req.queryParams("sukupuoli"), errors, input);
            validoiSyntymapaiva(req.queryParams("syntymaaika"), errors, input);

            if(!errors.isEmpty()) {
                req.session().attribute("errors", errors);
                res.redirect("/profile/" + urlID, 302);
                return "OK";
            }
            Asiakas uusiAsiakas = asiakasDao.update(input);
            req.session().attribute("asiakas", uusiAsiakas);

            res.redirect("/profile/" + urlID + "?updated=1", 302);
            return "OK";
        });
        get("/profile/:id/delete", (req, res) -> {
            HashMap map = new HashMap<>();

            int urlID = Integer.parseInt(req.params("id"));
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/login", 302);

            } else if(sessioAsiakas.getId() != urlID) {
                res.redirect("/?norights=1", 302);
            }

            viestiDao.deleteMessagesRelatedTo(urlID);
            asiakasDao.delete(urlID);
            req.session(true);
            req.session().removeAttribute("asiakas");
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

    private void validoiKayttajanimi(String input, List<String> errors, Asiakas userdata){
        if(input == null || input.isEmpty()){
            errors.add("Käyttäjänimi ei voi olla tyhjä!");
            return;
        }
        if(input.length() > 20) {
            errors.add("Käyttäjänimi ei voi olla yli 20 pituinen!");
            return;
        }
        userdata.setKayttajanimi(input);
    }

    private void validoiSalasana(String input, List<String> errors, Asiakas userdata) {
        if(input == null || input.isEmpty()){
            errors.add("Salasana ei voi olla tyhjä!");
            return;
        }
        if(input.length() > 100) {
            errors.add("Salasana ei voi olla yli 100 pituinen!");
            return;
        }
        userdata.setSalasana(input);
    }

    private void validoiOsoite(String input, List<String> errors, Asiakas userdata) {
        if(input == null || input.isEmpty()){
            errors.add("Osoite ei voi olla tyhjä!");
            return;
        }
        if(input.length() > 100) {
            errors.add("Osoite ei voi olla yli 130 pituinen!");
            return;
        }
        userdata.setOsoite(input);
    }
    private void validoiSyntymapaiva(String input, List<String> errors, Asiakas userdata) {
        if(input == null || input.isEmpty()){
            errors.add("Syntymäpäivä ei voi olla tyhjä!");
            return;
        }
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
            date = format.parse(input);
            if(!input.equals(format.format(date))) {
                date = null;
            }
        } catch(ParseException e) {

        }
        if(date == null) {
            errors.add("Antamasi syntymäpäivä ei ole oikeassa muodossa (dd.MM.YYYY)");
            return;
        } else {
            userdata.setSyntymaaika(date);
        }
    }

    private void validoiSukupuoli(String input, List<String> errors, Asiakas userdata) {
        if(input == null || input.isEmpty()){
            errors.add("Sukupuoli ei voi olla tyhjä!");
            return;
        }
        if(input.equals("1") || input.equals("2") || input.equals("3")) {
            errors.add("Antamasi sukupuoli on virheellinen!");
            return;
        }
        userdata.setSukupuoli(Integer.parseInt(input));
    }



}
