CREATE TABLE Asiakas (
    id SERIAL PRIMARY KEY,
    kayttajanimi varchar(20) NOT NULL,
    salasana varchar(100) NOT NULL,
    syntymaaika date NOT NULL,
    sukupuoli integer NOT NULL,
    liittynyt date NOT NULL,
    osoite varchar(120) NOT NULL
);

CREATE TABLE Hakutarkoitus (
    id SERIAL PRIMARY KEY,
    nimi varchar(20) NOT NULL
);

CREATE TABLE Asiakashakutarkoitus (
    hakutarkoitus_id integer REFERENCES Hakutarkoitus (id),
    asiakas_id integer REFERENCES Asiakas (id)
);

CREATE TABLE Ystavapari (
    asiakas_1 integer REFERENCES Asiakas (id),
    asiakas_2 integer REFERENCES Asiakas (id),
    aika timestamp NOT NULL
);

CREATE TABLE Viesti (
    id SERIAL PRIMARY KEY,
    lahettaja integer NOT NULL,
    vastaanottaja integer REFERENCES Asiakas (id),
    lahetetty timestamp NOT NULL,
    sisalto varchar(500) NOT NULL
);

CREATE TABLE Esittelysivu (
    sivu_id SERIAL PRIMARY KEY,
    omistaja_id integer REFERENCES Asiakas (id),
    otsikko varchar(100) NOT NULL,
    leipateksti varchar(1000) NOT NULL,
    luotu timestamp NOT NULL,
    muokattu timestamp,
    julkinen boolean NOT NULL
);

CREATE TABLE Esittelysivuasiakas (
    sivu_id integer REFERENCES Esittelysivu (sivu_id),
    asiakas_id integer REFERENCES Asiakas (id)
);

CREATE TABLE Yllapitaja (
    id SERIAL PRIMARY KEY,
    kayttajanimi varchar(20) NOT NULL,
    salasana varchar(100) NOT NULL
);

CREATE TABLE Lasku (
    id SERIAL PRIMARY KEY,
    asiakas_id integer REFERENCES Asiakas (id),
    yllapitaja_id integer REFERENCES Yllapitaja (id),
    laskutusaika timestamp NOT NULL
);

