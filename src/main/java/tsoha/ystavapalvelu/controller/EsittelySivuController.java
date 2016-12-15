package tsoha.ystavapalvelu.controller;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tsoha.ystavapalvelu.database.Database;
import tsoha.ystavapalvelu.models.page.EsittelySivu;
import tsoha.ystavapalvelu.models.page.EsittelySivuDao;
import tsoha.ystavapalvelu.models.user.Asiakas;
import tsoha.ystavapalvelu.models.user.AsiakasDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class EsittelySivuController {


    private Database database;
    private EsittelySivuDao esittelySivuDao;
    private AsiakasDao asiakasDao;

    public EsittelySivuController(Database database) {
        this.database = database;
        this.esittelySivuDao = new EsittelySivuDao(database);
        this.asiakasDao = new AsiakasDao(database);
        this.init();
    }

    private void init(){
        get("/mypages", (req, res) -> {
            HashMap map = new HashMap<>();
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            map.put("edited", "1".equals(req.queryParams("edited")));
            map.put("added", "1".equals(req.queryParams("added")));
            map.put("deleted", "1".equals(req.queryParams("deleted")));
            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());


            map.put("esittelysivut",esittelySivuDao.findAllWithOmistajaId(sessioAsiakas.getId()));

            return new ModelAndView(map, "omatesittelysivut");
        }, new ThymeleafTemplateEngine());

        get("/page/:pageId", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer pageId = Integer.parseInt(req.params("pageId"));
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            EsittelySivu sivu = esittelySivuDao.findOne(pageId);
            if(sessioAsiakas == null || (sessioAsiakas.getId() != sivu.getOmistaja_id() && !sivu.isJulkinen())) {
                res.redirect("/?norights=1", 302);
            }

            map.put("mypage", sessioAsiakas.getId() == sivu.getOmistaja_id());

            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());

            map.put("esittelysivu", sivu);

            return new ModelAndView(map, "esittelysivukatselma");
        }, new ThymeleafTemplateEngine());

        get("/page/:pageId/edit", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer pageId = Integer.parseInt(req.params("pageId"));
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            EsittelySivu sivu = esittelySivuDao.findOne(pageId);
            if(sessioAsiakas == null || (sessioAsiakas.getId() != sivu.getOmistaja_id() && !sivu.isJulkinen())) {
                res.redirect("/?norights=1", 302);
            }

            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());

            map.put("esittelysivu", sivu);

            if(req.session().attribute("errors") != null) {
                map.put("errors", req.session().attribute("errors"));
            } else {
                map.put("errors", new ArrayList<String>());
            }

            req.session().removeAttribute("errors");

            return new ModelAndView(map, "esittelysivumuokkaus");
        }, new ThymeleafTemplateEngine());

        get("/pagelist", (req, res) -> {
            HashMap map = new HashMap<>();
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            String haku = req.queryParams("haku");
            if(haku == null) {
                haku = "";
            }


            map.put("haku", haku);
            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());

            map.put("esittelysivut", esittelySivuDao.search(haku, asiakasDao.findInterestingUsers(sessioAsiakas.getId())));

            return new ModelAndView(map, "esittelysivulistaus");
        }, new ThymeleafTemplateEngine());

        get("/sharedpages", (req, res) -> {
            HashMap map = new HashMap<>();
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());

            return new ModelAndView(map, "esittelysivujaetut");
        }, new ThymeleafTemplateEngine());

        get("/newpage", (req, res) -> {
            HashMap map = new HashMap<>();

            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }

            map.put("kirjautunut", false);
            map.put("kayttaja",  sessioAsiakas.getKayttajanimi());
            map.put("kayttajaid",  sessioAsiakas.getId());

            if(req.session().attribute("errors") != null) {
                map.put("errors", req.session().attribute("errors"));
            } else {
                map.put("errors", new ArrayList<String>());
            }

            if(req.session().attribute("validatedinput") != null) {
                map.put("validatedinput", req.session().attribute("validatedinput"));
            } else {
                map.put("validatedinput", new EsittelySivu(-1, -1, "", "", null, null, false));
            }

            req.session().removeAttribute("validatedinput");
            req.session().removeAttribute("errors");

            return new ModelAndView(map, "esittelysivuuusi");
        }, new ThymeleafTemplateEngine());

        post("/newpage", (req, res) -> {
            List<String> errors = new ArrayList<String>();


            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null) {
                res.redirect("/?norights=1", 302);
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            EsittelySivu input = new EsittelySivu(-1, sessioAsiakas.getId(), "","", now, now, false);

            String otsikko = req.queryParams("otsikko");
            String leipateksti = req.queryParams("leipateksti");
            String julkinen = req.queryParams("julkinen");

            validoiOtsikko(otsikko, errors, input);
            validoiLeipateksti(leipateksti, errors, input);
            validoiJulkinen(julkinen, errors, input);

            if(!errors.isEmpty()) {
                req.session().attribute("errors", errors);
                req.session().attribute("validatedinput", input);
                res.redirect("/newpage", 302);
                return "OK";
            }

            esittelySivuDao.add(input);

            res.redirect("/mypages?added=1", 302);


            return "OK";
        });

        post("/page/:postId/edit", (req, res) -> {
            List<String> errors = new ArrayList<String>();
            Integer postId = Integer.parseInt(req.params("postId"));
            EsittelySivu sivu = esittelySivuDao.findOne(postId);
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null || sivu == null || sivu.getOmistaja_id() != sessioAsiakas.getId()) {
                res.redirect("/?norights=1", 302);
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            EsittelySivu input = new EsittelySivu(sivu.getSivu_id(), sivu.getOmistaja_id(), sivu.getOtsikko(), sivu.getLeipateksti(),
                    sivu.getLuotu(), now, sivu.isJulkinen());

            String otsikko = req.queryParams("otsikko");
            String leipateksti = req.queryParams("leipateksti");
            String julkinen = req.queryParams("julkinen");

            validoiJulkinen(julkinen, errors, input);
            validoiOtsikko(otsikko, errors, input);
            validoiLeipateksti(leipateksti, errors, input);

            if(!errors.isEmpty()) {
                req.session().attribute("errors", errors);
                res.redirect("/page/" + postId + "/edit", 302);
                return "OK";
            }

            esittelySivuDao.update(input);

            res.redirect("/mypages?edited=1", 302);


            return "OK";
        });

        get("/page/:postId/delete", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer postId = Integer.parseInt(req.params("postId"));
            EsittelySivu sivu = esittelySivuDao.findOne(postId);
            Asiakas sessioAsiakas = req.session().attribute("asiakas");
            if(sessioAsiakas == null || sivu.getOmistaja_id() != sessioAsiakas.getId()) {
                res.redirect("/?norights=1", 302);
            }
            esittelySivuDao.delete(sivu.getSivu_id());

            res.redirect("/mypages?deleted=1", 302);


            return "OK";
        });
        post("/pagesearch", (req, res) -> {


            String haku = req.queryParams("haku");


            res.redirect("/pagelist?haku=" + haku, 302);


            return "OK";
        });

    }

    private void validoiLeipateksti(String input, List<String> errors, EsittelySivu userdata){
        if(input == null || input.isEmpty()){
            errors.add("Leipäteksti ei voi olla tyhjä!");
            return;
        }
        if(input.length() > 1000) {
            errors.add("Leipäteksti ei voi olla yli 1000 pituinen!");
            return;
        }
        userdata.setLeipateksti(input);
    }

    private void validoiOtsikko(String input, List<String> errors, EsittelySivu userdata){
        if(input == null || input.isEmpty()){
            errors.add("Otsikko ei voi olla tyhjä!");
            return;
        }
        if(input.length() > 100) {
            errors.add("Otsikko ei voi olla yli 100 pituinen!");
            return;
        }
        userdata.setOtsikko(input);
    }

    private void validoiJulkinen(String input, List<String> errors, EsittelySivu userdata){
        if(input == null || input.isEmpty()){
            userdata.setJulkinen(false);
            return;
        }
        if(input.equals("on")) {
            userdata.setJulkinen(true);
            return;
        }
        errors.add("Julkisuus on väärässä muodossa! " + input);
    }
}
