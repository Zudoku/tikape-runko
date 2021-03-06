package tsoha.ystavapalvelu.controller;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.message.Viesti;
import tsoha.ystavapalvelu.database.dao.ViestiDao;
import tsoha.ystavapalvelu.models.user.Asiakas;
import tsoha.ystavapalvelu.database.dao.AsiakasDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class ViestiController {

    private Database database;
    private ViestiDao viestiDao;
    private AsiakasDao asiakasDao;

    public ViestiController(Database database) {
        this.database = database;
        this.viestiDao = new ViestiDao(database);
        this.asiakasDao = new AsiakasDao(database);
        this.init();
    }

    private void init() {
        get("/message/:targetAsiakasId/send", (req, res) -> {
            HashMap map = new HashMap<>();

            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            Integer targetAsiakasId = Integer.parseInt(req.params("targetAsiakasId"));
            map.put("targetAsiakas", asiakasDao.findOne(targetAsiakasId));

            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());
            map.put("tekeminen",  "Lähetä viesti henkilölle: ");
            map.put("sending", true);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            if(req.session().attribute("errors") != null) {
                map.put("errors", req.session().attribute("errors"));
            } else {
                map.put("errors", new ArrayList<String>());
            }

            if(req.session().attribute("validatedinput") != null) {
                map.put("validatedinput", req.session().attribute("validatedinput"));
            } else {
                map.put("validatedinput", new Viesti(-1, sessioAsiakas.getId(), -1, now, ""));
            }

            req.session().removeAttribute("validatedinput");
            req.session().removeAttribute("errors");

            return new ModelAndView(map, "lahetaviesti");
        }, new ThymeleafTemplateEngine());

        post("/message/:targetAsiakasId/sendf", (req, res) -> {
            List<String> errors = new ArrayList<String>();

            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            Integer targetAsiakasId = Integer.parseInt(req.params("targetAsiakasId"));
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Viesti input = new Viesti(-1, -1, targetAsiakasId, now, "");

            String sisalto = req.queryParams("sisalto");
            String anonyymi = req.queryParams("anonyymiys");

            validoiSisalto(sisalto, errors, input);
            validoiAnonyymiys(anonyymi, errors, input, sessioAsiakas.getId());

            if(!errors.isEmpty()) {
                req.session().attribute("errors", errors);
                req.session().attribute("validatedinput", input);
                res.redirect("/message/" + targetAsiakasId + "/send", 302);
                return "OK";
            }

            viestiDao.add(input);

            res.redirect("/mymessages?added=1", 302);


            return "OK";
        });

        get("/mymessages", (req, res) -> {
            HashMap map = new HashMap<>();
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());

            map.put("deleted", "1".equals(req.queryParams("deleted")));
            map.put("added", "1".equals(req.queryParams("added")));
            map.put("edited", "1".equals(req.queryParams("edited")));


            map.put("saadutviestit", viestiDao.findAllVastaanotettu(sessioAsiakas.getId()));
            map.put("lahetetytviestit", viestiDao.findAllLahetetty(sessioAsiakas.getId()));


            return new ModelAndView(map, "omatviestit");
        }, new ThymeleafTemplateEngine());

        get("/message/:messageId/edit", (req, res) -> {
            HashMap map = new HashMap<>();

            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            Integer messageId = Integer.parseInt(req.params("messageId"));
            Viesti muokattava = viestiDao.findOne(messageId);

            if(muokattava.getLahettaja() != sessioAsiakas.getId()) {
                res.redirect("/mymessages?error=1", 302);
            }
            map.put("targetAsiakas", asiakasDao.findOne(muokattava.getVastaanottaja()));

            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());
            map.put("tekeminen",  "Muokkaa viestiä henkilölle: ");
            map.put("sending", false);

            if(req.session().attribute("errors") != null) {
                map.put("errors", req.session().attribute("errors"));
            } else {
                map.put("errors", new ArrayList<String>());
            }

            if(req.session().attribute("validatedinput") != null) {
                map.put("validatedinput", req.session().attribute("validatedinput"));
            } else {
                map.put("validatedinput", muokattava);
            }

            req.session().removeAttribute("validatedinput");
            req.session().removeAttribute("errors");

            return new ModelAndView(map, "lahetaviesti");
        }, new ThymeleafTemplateEngine());

        post("/message/:messageId/editf", (req, res) -> {
            List<String> errors = new ArrayList<String>();

            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            Integer messageId = Integer.parseInt(req.params("messageId"));
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            Viesti oldViesti = viestiDao.findOne(messageId);
            Asiakas targetAsiakas = asiakasDao.findOne(oldViesti.getVastaanottaja());

            if(oldViesti.getLahettaja() != sessioAsiakas.getId()) {
                res.redirect("/mymessages?error=1", 302);
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Viesti input = oldViesti;
            input.setLahetetty(now);

            String sisalto = req.queryParams("sisalto");
            String anonyymi = req.queryParams("anonyymiys");

            validoiSisalto(sisalto, errors, input);
            validoiAnonyymiys(anonyymi, errors, input, sessioAsiakas.getId());

            if(!errors.isEmpty()) {
                req.session().attribute("errors", errors);
                req.session().attribute("validatedinput", input);
                res.redirect("/message/" + messageId + "/edit", 302);
                return "OK";
            }

            viestiDao.update(input);

            res.redirect("/mymessages?edited=1", 302);


            return "OK";
        });

        get("/message/:viestiId/delete", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer viestiId = Integer.parseInt(req.params("viestiId"));
            Viesti viesti = viestiDao.findOne(viestiId);
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null || viesti.getLahettaja() != sessioAsiakas.getId()) {
                res.redirect("/?norights=1", 302);
            }
            viestiDao.delete(viesti.getId());

            res.redirect("/mymessages?deleted=1", 302);


            return "OK";
        });
    }

    private void validoiAnonyymiys(String input, List<String> errors, Viesti userdata, int lahettajaID) {
        if(input == null || input.isEmpty()){
            userdata.setLahettaja(lahettajaID);
            return;
        }
        if(input.equals("on")) {

            return;
        }
        errors.add("Anonyymiys on väärässä muodossa! " + input);
    }

    private void validoiSisalto(String input, List<String> errors, Viesti userdata) {

        if(input == null || input.isEmpty()){
            errors.add("Sisältö ei voi olla tyhjä!");
            return;
        }
        if(input.length() > 500) {
            errors.add("Sisältö ei voi olla yli 500 pituinen!");
            return;
        }
        userdata.setSisalto(input);
    }
}
