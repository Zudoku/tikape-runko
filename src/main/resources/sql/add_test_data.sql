INSERT INTO HakuTarkoitus (nimi) VALUES ('Urheilukaveri');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Onlinekaveri');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Syvät keskustelut');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Väittely');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Baariseura');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Hengailu');

INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite)
VALUES ('toto', 'test',CURRENT_DATE,1,CURRENT_DATE,'jokukatu 11C, Espoo, Suomi');

INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite)
VALUES ('Gajaja2828', 'salainensalasana2',CURRENT_DATE,1,CURRENT_DATE,'jokukatu 12C, Joensuu, Suomi');

INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite)
VALUES ('Gannigala', 'salainensalasana3',CURRENT_DATE,1,CURRENT_DATE,'jokukatu 13C, Espoo, Suomi');

INSERT INTO Yllapitaja (kayttajanimi, salasana) VALUES ('admin', 'admin2');


INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) 
VALUES (1, 'Etsin juttuseuraa','Hei! Laita viestiä ja kerro itsestäsi.',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, true);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (1, 'Hello world','Hei! Taidat olla ohjelmoija, kun jaoin tämän sivun kanssasi. Ohjelmointikieli x on paras!',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, false);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (1, 'Salaisimmat salaisuuteni','Suurin salaisuuteni on ____ .',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, false);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) 
VALUES (2, 'Lähtiskö kukaan mun kanssa lenkille','Oon Joensuusta. Voidaan juosta sun tahtiin...',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, true);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (2, 'Lenkki-info','Asun siis Joensuussa, kadulla XYZ. Meen juoksee aina 7 aikaan, tavattaisko vaikka alepalla?',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, false);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (2, 'Jätä mut rauhaan','Älä pliis enää ota yhteyttä.',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, false);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (3, 'Onks kukaan kokeillu poistaa omaa käyttäjää?','Voiks sitä sit enää palauuttaa takasin?',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, true);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (3, 'Tuu juttelee','Jos näät tän viestin niin sit tuu juttelee. Jaoin tän sulle, koska vaikutat mukavalta.',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, false);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen)
VALUES (2, 'Eiks täää palvelu oo vähä kallis?','Ku jonku kanssa juttelee ni heti tulee laskua.. mitä S****NAA ?',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, true);


INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (1, 1);
INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (2, 1);
INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (3, 1);
INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (4, 1);
INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (5, 1);
INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (6, 1);

INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (1, 2);
INSERT INTO Asiakashakutarkoitus (hakutarkoitus_id, asiakas_id) VALUES (5, 3);


INSERT INTO Viesti (lahettaja, vastaanottaja, lahetetty, sisalto)
VALUES (3, 1,CURRENT_TIMESTAMP,'Terwe! asukko pk seudulla? haluukko lähteä baariin?');

INSERT INTO Viesti (lahettaja, vastaanottaja, lahetetty, sisalto)
VALUES (1, 3,CURRENT_TIMESTAMP,'Asun joo, taidat olla nimes väärti, missä tavattais?');

INSERT INTO Viesti (lahettaja, vastaanottaja, lahetetty, sisalto)
VALUES (2, 1,CURRENT_TIMESTAMP,'Mitä tykkäät harrastaa?');

INSERT INTO Viesti (lahettaja, vastaanottaja, lahetetty, sisalto)
VALUES (3, 2,CURRENT_TIMESTAMP,'Mitä ihmettä?');

INSERT INTO Viesti (lahettaja, vastaanottaja, lahetetty, sisalto)
VALUES (NULL, 1,CURRENT_TIMESTAMP,'Vaikutat mukavalta mutta mua ujostuttaa laittaa viestiä. Laita sun esittelysivuun joku siisti fakta itestäs ni ehkä uskaltaisin...');

INSERT INTO Ystavapari (asiakas_1, asiakas_2, aika) VALUES (1, 2, CURRENT_TIMESTAMP);
INSERT INTO Ystavapari (asiakas_1, asiakas_2, aika) VALUES (2, 1, CURRENT_TIMESTAMP);

INSERT INTO Ystavapari (asiakas_1, asiakas_2, aika) VALUES (1, 3, CURRENT_TIMESTAMP);
INSERT INTO Ystavapari (asiakas_1, asiakas_2, aika) VALUES (3, 1, CURRENT_TIMESTAMP);

INSERT INTO Ystavapari (asiakas_1, asiakas_2, aika) VALUES (2, 3, CURRENT_TIMESTAMP);
INSERT INTO Ystavapari (asiakas_1, asiakas_2, aika) VALUES (3, 2, CURRENT_TIMESTAMP);

INSERT INTO Esittelysivuasiakas (sivu_id, asiakas_id) VALUES (2, 2);

INSERT INTO Esittelysivuasiakas (sivu_id, asiakas_id) VALUES (5, 1);
INSERT INTO Esittelysivuasiakas (sivu_id, asiakas_id) VALUES (7, 1);

INSERT INTO Lasku (asiakas_id, yllapitaja_id, laskutusaika) VALUES (1, 1, CURRENT_TIMESTAMP);

