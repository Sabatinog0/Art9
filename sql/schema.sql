CREATE DATABASE IF NOT EXISTS art9
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE art9;

CREATE TABLE categoria (
    id_categoria    INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(60)  NOT NULL,
    slug            VARCHAR(60)  NOT NULL,
    descrizione     VARCHAR(255),
    CONSTRAINT uq_categoria_nome UNIQUE (nome),
    CONSTRAINT uq_categoria_slug UNIQUE (slug)
) ENGINE=InnoDB;


CREATE TABLE utente (
    id_utente           INT AUTO_INCREMENT PRIMARY KEY,
    email               VARCHAR(150) NOT NULL,
    password_hash       VARCHAR(60)  NOT NULL,
    nome                VARCHAR(80)  NOT NULL,
    cognome             VARCHAR(80)  NOT NULL,
    ruolo               ENUM('CLIENTE', 'ADMIN') NOT NULL DEFAULT 'CLIENTE',
    data_registrazione  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_utente_email UNIQUE (email)
) ENGINE=InnoDB;


CREATE TABLE indirizzo (
    id_indirizzo    INT AUTO_INCREMENT PRIMARY KEY,
    id_utente       INT NOT NULL,
    etichetta       VARCHAR(40)  NOT NULL DEFAULT 'Casa',
    via             VARCHAR(150) NOT NULL,
    citta           VARCHAR(80)  NOT NULL,
    cap             VARCHAR(10)  NOT NULL,
    provincia       VARCHAR(2)   NOT NULL,
    nazione         VARCHAR(60)  NOT NULL DEFAULT 'Italia',
    predefinito     BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_indirizzo_utente FOREIGN KEY (id_utente)
        REFERENCES utente (id_utente) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE metodo_pagamento (
    id_metodo           INT AUTO_INCREMENT PRIMARY KEY,
    id_utente            INT NOT NULL,
    intestatario          VARCHAR(120) NOT NULL,
    numero_mascherato    VARCHAR(20)  NOT NULL,
    scadenza             VARCHAR(5)   NOT NULL,
    circuito             ENUM('VISA', 'MASTERCARD', 'AMEX') NOT NULL,
    predefinito           BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_metodo_pagamento_utente FOREIGN KEY (id_utente)
        REFERENCES utente (id_utente) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE prodotto (
    id_prodotto             INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria            INT NOT NULL,
    nome                     VARCHAR(150) NOT NULL,
    descrizione              TEXT,
    prezzo                   DECIMAL(10,2) NOT NULL,
    iva_percentuale          DECIMAL(4,2)  NOT NULL DEFAULT 22.00,
    quantita_disponibile     INT NOT NULL DEFAULT 0,
    in_preordine             BOOLEAN NOT NULL DEFAULT FALSE,
    editore                  VARCHAR(100),
    immagine                 VARCHAR(255),
    data_creazione           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prodotto_categoria FOREIGN KEY (id_categoria)
        REFERENCES categoria (id_categoria) ON DELETE RESTRICT,
    CONSTRAINT chk_prodotto_prezzo CHECK (prezzo >= 0),
    CONSTRAINT chk_prodotto_quantita CHECK (quantita_disponibile >= 0)
) ENGINE=InnoDB;

CREATE INDEX idx_prodotto_nome ON prodotto (nome);
CREATE INDEX idx_prodotto_categoria ON prodotto (id_categoria);
CREATE FULLTEXT INDEX idx_prodotto_ricerca ON prodotto (nome, descrizione);


CREATE TABLE ordine (
    id_ordine               INT AUTO_INCREMENT PRIMARY KEY,
    id_utente                INT NOT NULL,
    id_metodo_pagamento     INT,
    data_ordine              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stato                    ENUM('PREORDINE','IN_ATTESA','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO')
                              NOT NULL DEFAULT 'IN_ATTESA',
    totale                   DECIMAL(10,2) NOT NULL,
    indirizzo_spedizione     VARCHAR(300) NOT NULL,
    CONSTRAINT fk_ordine_utente FOREIGN KEY (id_utente)
        REFERENCES utente (id_utente) ON DELETE CASCADE,
    CONSTRAINT fk_ordine_metodo_pagamento FOREIGN KEY (id_metodo_pagamento)
        REFERENCES metodo_pagamento (id_metodo) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_ordine_utente ON ordine (id_utente);
CREATE INDEX idx_ordine_data ON ordine (data_ordine);


CREATE TABLE riga_ordine (
    id_riga             INT AUTO_INCREMENT PRIMARY KEY,
    id_ordine           INT NOT NULL,
    id_prodotto         INT,
    nome_prodotto       VARCHAR(150) NOT NULL,
    prezzo_unitario     DECIMAL(10,2) NOT NULL,
    iva_percentuale     DECIMAL(4,2)  NOT NULL,
    quantita            INT NOT NULL,
    CONSTRAINT fk_riga_ordine_ordine FOREIGN KEY (id_ordine)
        REFERENCES ordine (id_ordine) ON DELETE CASCADE,
    CONSTRAINT fk_riga_ordine_prodotto FOREIGN KEY (id_prodotto)
        REFERENCES prodotto (id_prodotto) ON DELETE SET NULL,
    CONSTRAINT chk_riga_ordine_quantita CHECK (quantita > 0)
) ENGINE=InnoDB;

CREATE INDEX idx_riga_ordine_ordine ON riga_ordine (id_ordine);


CREATE TABLE recensione (
    id_recensione       INT AUTO_INCREMENT PRIMARY KEY,
    id_utente           INT NOT NULL,
    id_prodotto         INT NOT NULL,
    voto                TINYINT NOT NULL,
    testo               VARCHAR(1000),
    data_recensione     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recensione_utente FOREIGN KEY (id_utente)
        REFERENCES utente (id_utente) ON DELETE CASCADE,
    CONSTRAINT fk_recensione_prodotto FOREIGN KEY (id_prodotto)
        REFERENCES prodotto (id_prodotto) ON DELETE CASCADE,
    CONSTRAINT uq_recensione_utente_prodotto UNIQUE (id_utente, id_prodotto),
    CONSTRAINT chk_recensione_voto CHECK (voto BETWEEN 1 AND 5)
) ENGINE=InnoDB;

CREATE INDEX idx_recensione_prodotto ON recensione (id_prodotto);


CREATE TABLE preferito (
    id_utente       INT NOT NULL,
    id_prodotto     INT NOT NULL,
    data_aggiunta   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_utente, id_prodotto),
    CONSTRAINT fk_preferito_utente FOREIGN KEY (id_utente)
        REFERENCES utente (id_utente) ON DELETE CASCADE,
    CONSTRAINT fk_preferito_prodotto FOREIGN KEY (id_prodotto)
        REFERENCES prodotto (id_prodotto) ON DELETE CASCADE
) ENGINE=InnoDB;
