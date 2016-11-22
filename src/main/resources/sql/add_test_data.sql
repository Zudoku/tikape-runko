INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite)
VALUES ('Toto99112', 'salainensalasana','04.11.1997',1,'15.11.2016','jokukatu 11C, Espoo, Suomi');

INSERT INTO Asiakas (kayttajanimi, salasana, syntymaaika, sukupuoli, liittynyt, osoite) 
VALUES ('Gajaja2828', 'salainensalasana2','08.01.1993',1,'15.11.2016','jokukatu 12C, Espoo, Suomi');


INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) 
VALUES (1, 'Etsin jalkapallokaveria','Hei! Etsin kaikenikäistä kaveria pelaamaan kanssani jalkapalloa','15.11.2016','15.11.2016', true);

INSERT INTO Esittelysivu (omistaja_id, otsikko, leipateksti, luotu, muokattu, julkinen) 
VALUES (2, 'Etsin juttukaveria','Hei! Olen 27 vuotias ja kaipaan juttukaveria! laita viestiä!','15.11.2016','15.11.2016', true);


INSERT INTO Viesti (lahettaja, vastaanottaja, lahtetty, sisalto) 
VALUES (2, 1,'15.11.2016','Hei!  Vaikutat mukavalta! ');