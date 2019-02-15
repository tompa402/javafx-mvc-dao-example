CREATE SCHEMA POSTAJE;

CREATE TABLE POSTAJE.DRZAVA (
  ID       INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
  NAZIV    VARCHAR(50) NOT NULL,
  POVRSINA DOUBLE      NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE POSTAJE.ZUPANIJA (
  ID        INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
  NAZIV     VARCHAR(50) NOT NULL,
  DRZAVA_ID INT         NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (DRZAVA_ID) REFERENCES POSTAJE.DRZAVA (ID) ON DELETE CASCADE
);

CREATE TABLE POSTAJE.MJESTO (
  ID          INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
  NAZIV       VARCHAR(50) NOT NULL,
  VRSTA       VARCHAR(50) NOT NULL,
  ZUPANIJA_ID INT         NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (ZUPANIJA_ID) REFERENCES POSTAJE.ZUPANIJA (ID) ON DELETE CASCADE
);

CREATE TABLE POSTAJE.MJERNA_POSTAJA (
  ID        INT            NOT NULL GENERATED ALWAYS AS IDENTITY,
  NAZIV     VARCHAR(50)    NOT NULL,
  LAT       DECIMAL(11, 8) NOT NULL,
  LNG       DECIMAL(11, 8) NOT NULL,
  MJESTO_ID INT            NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (MJESTO_ID) REFERENCES POSTAJE.MJESTO (ID) ON DELETE CASCADE
);

CREATE TABLE POSTAJE.POSTAJA_TYPE (
  ID   INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
  NAME VARCHAR(24) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE POSTAJE.MJERNA_POSTAJA_SONDAZNA (
  ID              INT PRIMARY KEY REFERENCES POSTAJE.MJERNA_POSTAJA (ID) ON DELETE CASCADE,
  VISINA          INT NOT NULL,
  POSTAJA_TYPE_ID INT NOT NULL DEFAULT 1,
  FOREIGN KEY (POSTAJA_TYPE_ID) REFERENCES POSTAJE.POSTAJA_TYPE (ID)
);

CREATE TABLE POSTAJE.SENZOR (
  ID                INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
  MJERNA_JEDINICA   VARCHAR(10) NOT NULL,
  PRECIZNOST        DOUBLE      NOT NULL,
  VRIJEDNOST        DECIMAL(7, 2),
  RAD_SENZORA       VARCHAR(10),
  AKTIVAN           BOOLEAN     NOT NULL DEFAULT TRUE,
  MJERNA_POSTAJA_ID INT         NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (MJERNA_POSTAJA_ID) REFERENCES POSTAJE.MJERNA_POSTAJA (ID) ON DELETE CASCADE
);

CREATE TABLE POSTAJE.SENZOR_TYPE (
  ID   INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
  NAME VARCHAR(24) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE POSTAJE.SENZOR_TEMPERATURE (
  ID             INT PRIMARY KEY REFERENCES POSTAJE.SENZOR (ID) ON DELETE CASCADE,
  NAZIV          VARCHAR(128) NOT NULL,
  SENZOR_TYPE_ID INT          NOT NULL DEFAULT 1,
  FOREIGN KEY (SENZOR_TYPE_ID) REFERENCES POSTAJE.SENZOR_TYPE (ID)
);

CREATE TABLE POSTAJE.SENZOR_VLAGE (
  ID             INT PRIMARY KEY REFERENCES POSTAJE.SENZOR (ID) ON DELETE CASCADE,
  SENZOR_TYPE_ID INT NOT NULL DEFAULT 2,
  FOREIGN KEY (SENZOR_TYPE_ID) REFERENCES POSTAJE.SENZOR_TYPE (ID)
);

CREATE TABLE POSTAJE.SENZOR_TLAKA (
  ID             INT PRIMARY KEY REFERENCES POSTAJE.SENZOR (ID) ON DELETE CASCADE,
  SENZOR_TYPE_ID INT NOT NULL DEFAULT 3,
  FOREIGN KEY (SENZOR_TYPE_ID) REFERENCES POSTAJE.SENZOR_TYPE (ID)
);

-- initial data
-- if data existed in DB before first clear DB from all objects then run init data:
-- DROP ALL OBJECTS;
INSERT INTO POSTAJE.DRZAVA (NAZIV, POVRSINA)
VALUES ('Hrvatska', 56594),
       ('Slovenija', 20273);

INSERT INTO POSTAJE.ZUPANIJA (NAZIV, DRZAVA_ID)
VALUES ('Grad Zagreb', 1),
       ('Zadarska županija', 1),
       ('Ličko-senjska županija', 1),
       ('Brodsko-posavska županija', 1),
       ('Nova Gorica', 2),
       ('Novo Mesto', 2);

INSERT INTO POSTAJE.MJESTO (NAZIV, VRSTA, ZUPANIJA_ID)
VALUES ('Zagreb', 'OSTALO', 1),
       ('Zadar', 'GRAD', 2),
       ('Zavižan', 'SELO', 3),
       ('Otočac', 'GRAD', 3),
       ('Slavonski Brod', 'GRAD', 4);

INSERT INTO POSTAJE.MJERNA_POSTAJA (NAZIV, MJESTO_ID, LAT, LNG)
VALUES ('Maxsimir', 1, 45.8288, 16.0211),
       ('Zemunik', 2, 44.800000, 14.966667),
       ('Zavižan', 3, 21.802300, 123.965567);

INSERT INTO POSTAJE.POSTAJA_TYPE (NAME)
VALUES ('SONDAZNA');

INSERT INTO POSTAJE.MJERNA_POSTAJA_SONDAZNA (ID, VISINA)
VALUES (2, 55);

INSERT INTO POSTAJE.SENZOR_TYPE (NAME)
VALUES ('TEMP'),
       ('VLAGA'),
       ('TLAK');

INSERT INTO POSTAJE.SENZOR (MJERNA_JEDINICA, PRECIZNOST, VRIJEDNOST, RAD_SENZORA, MJERNA_POSTAJA_ID)
VALUES ('°C', 2, 22, 'OSTALO', 1),
       ('°C', 2, 12, 'PING', 2),
       ('°C', 2, 10, 'STREAMING', 3),
       ('%', 10, 80, 'PING', 1),
       ('%', 10, 20, 'STREAMING', 2),
       ('%', 10, 60, 'STREAMING', 3),
       ('hPa', 0, 990, 'PING', 1),
       ('hPa', 0, 1000, 'STREAMING', 2),
       ('hPa', 0, 1050, 'STREAMING', 3);

INSERT INTO POSTAJE.SENZOR_TEMPERATURE (ID, NAZIV)
VALUES (1, 'TMP36'),
       (2, 'DS18B20'),
       (3, 'DTH11');

INSERT INTO POSTAJE.SENZOR_VLAGE (ID)
VALUES (4),
       (5),
       (6);

INSERT INTO POSTAJE.SENZOR_TLAKA (ID)
VALUES (7),
       (8),
       (9);