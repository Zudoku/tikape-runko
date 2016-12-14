INSERT INTO HakuTarkoitus (nimi) VALUES ('Urheilukaveri');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Onlinekaveri');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Syvät keskustelut');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Väittely');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Baariseura');
INSERT INTO HakuTarkoitus (nimi) VALUES ('Hengailu');

INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite)
VALUES ('Toto99112', 'test',CURRENT_DATE,1,CURRENT_DATE,'jokukatu 11C, Espoo, Suomi');

INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite) 
VALUES ('Gajaja2828', 'salainensalasana2',CURRENT_DATE,1,CURRENT_DATE,'jokukatu 12C, Espoo, Suomi');


INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) 
VALUES (1, 'Etsin jalkapallokaveria','Hei! Etsin kaikenikäistä kaveria pelaamaan kanssani jalkapalloa',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, true);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) 
VALUES (2, 'Etsin juttukaveria','Hei! Olen 27 vuotias ja kaipaan juttukaveria! laita viestiä!',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, true);


INSERT INTO Viesti (lahettaja, vastaanottaja, lahetetty, sisalto)
VALUES (2, 1,CURRENT_TIMESTAMP,'Hei!  Vaikutat mukavalta! ');