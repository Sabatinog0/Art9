
USE art9;

INSERT INTO categoria (nome, slug, descrizione) VALUES
    ('Manga',           'manga',          'Serie manga giapponesi, volumi singoli e cofanetti'),
    ('Fumetti',         'fumetti',        'Fumetti americani, italiani e graphic novel'),
    ('Action Figures',  'action-figures', 'Statue, Funko Pop, Nendoroid e figure da collezione'),
    ('Merchandising',   'merchandising',  'Abbigliamento e accessori a tema'),
    ('Gadget',          'gadget',         'Piccoli oggetti da collezione e da scrivania');


INSERT INTO utente (email, password_hash, nome, cognome, ruolo) VALUES
    ('admin@art9.it', '$2a$10$WK3grKReuVb13u0i80K6qeLh1VOg4.ZitRFhqLpcCjQu5YO2FjkdG', 'Gaetano', 'Sabatino', 'ADMIN'),
    ('mario.rossi@mail.it', '$2a$10$RI0opWBWe4Wb4NCJ6uOTPu8c0cbPSJfKD8vGAwwJhJBdcYojS2//a', 'Mario', 'Rossi', 'CLIENTE');

INSERT INTO indirizzo (id_utente, etichetta, via, citta, cap, provincia, nazione, predefinito) VALUES
    (2, 'Casa', 'Via Roma 15', 'Salerno', '84100', 'SA', 'Italia', TRUE);

INSERT INTO metodo_pagamento (id_utente, intestatario, numero_mascherato, scadenza, circuito, predefinito) VALUES
    (2, 'Mario Rossi', '**** **** **** 4242', '09/28', 'VISA', TRUE);


INSERT INTO prodotto (id_categoria, nome, descrizione, prezzo, iva_percentuale, quantita_disponibile, in_preordine, editore, immagine) VALUES
    (1, 'One Piece Vol. 102', 'Il capitolo conclusivo della saga di Wano Kuni.', 5.90, 4.00, 40, FALSE, 'Star Comics', 'img/prodotti/one-piece-102.svg'),
    (1, 'Jujutsu Kaisen Vol. 20', 'Il duello finale tra Yuji e Sukuna si avvicina.', 6.50, 4.00, 35, FALSE, 'Star Comics', 'img/prodotti/jujutsu-kaisen-20.svg'),
    (1, 'Chainsaw Man Vol. 14', 'La seconda parte della serie di Tatsuki Fujimoto.', 6.90, 4.00, 30, TRUE, 'Star Comics', 'img/prodotti/chainsaw-man-14.svg'),
    (1, 'Berserk Deluxe Vol. 5', 'Edizione deluxe a colori con sovraccoperta.', 24.90, 4.00, 15, FALSE, 'Panini Comics', 'img/prodotti/berserk-deluxe-5.svg'),
    (1, 'My Hero Academia Vol. 38', 'Il gran finale della serie di Kohei Horikoshi.', 5.20, 4.00, 25, FALSE, 'Star Comics', 'img/prodotti/mha-38.svg'),

    (2, 'Batman: Anno Uno', 'La celebre origin story firmata Frank Miller.', 19.90, 4.00, 20, FALSE, 'Panini DC Italia', 'img/prodotti/batman-anno-uno.svg'),
    (2, 'The Amazing Spider-Man: Il Ritorno di Kraven', 'Kraven il Cacciatore torna a New York.', 17.50, 4.00, 18, FALSE, 'Panini Marvel Italia', 'img/prodotti/spiderman-kraven.svg'),
    (2, 'Dylan Dog n.451', 'L''Indagatore dell''Incubo nel nuovo episodio mensile.', 3.90, 4.00, 50, FALSE, 'Sergio Bonelli Editore', 'img/prodotti/dylan-dog-451.svg'),
    (2, 'Watchmen', 'Il capolavoro di Alan Moore e Dave Gibbons.', 29.90, 4.00, 12, FALSE, 'Panini DC Italia', 'img/prodotti/watchmen.svg'),
    (2, 'Saga Vol. 1', 'Space opera pluripremiata di Brian K. Vaughan.', 15.00, 4.00, 22, FALSE, 'BAO Publishing', 'img/prodotti/saga-1.svg'),

    (3, 'Funko Pop! Luffy Gear 5', 'Rufy nella sua trasformazione Gear 5.', 19.99, 22.00, 30, FALSE, 'Funko', 'img/prodotti/funko-luffy-gear5.svg'),
    (3, 'Figure Batman Hush 1/6', 'Statua da collezione in scala 1:6.', 45.00, 22.00, 8, TRUE, 'McFarlane Toys', 'img/prodotti/batman-hush-figure.svg'),
    (3, 'Dragon Ball Ichiban Kuji - Goku Ultra Istinto', 'Figure premio della lotteria Ichiban Kuji.', 55.00, 22.00, 6, FALSE, 'Bandai', 'img/prodotti/goku-ultra-istinto.svg'),
    (3, 'Action Figure Iron Man Mark 85', 'Riproduzione ad altissimo dettaglio.', 289.00, 22.00, 3, FALSE, 'Hot Toys', 'img/prodotti/ironman-mk85.svg'),
    (3, 'Nendoroid Nezuko Kamado', 'Nendoroid ufficiale da Demon Slayer.', 49.90, 22.00, 10, FALSE, 'Good Smile Company', 'img/prodotti/nendoroid-nezuko.svg'),

    (4, 'Zaino Naruto Akatsuki', 'Zaino scuola/tempo libero con logo Akatsuki.', 39.90, 22.00, 25, FALSE, 'Art 9 Merch', 'img/prodotti/zaino-akatsuki.svg'),
    (4, 'Felpa Attack on Titan Survey Corps', 'Felpa unisex con stemma del Corpo di Ricerca.', 34.90, 22.00, 20, FALSE, 'Art 9 Merch', 'img/prodotti/felpa-aot.svg'),
    (4, 'Tazza Termosensibile Dragon Ball', 'Cambia immagine al contatto con liquidi caldi.', 12.90, 22.00, 60, FALSE, 'Art 9 Merch', 'img/prodotti/tazza-dragonball.svg'),
    (4, 'Poster One Piece - Ciurma di Cappello di Paglia', 'Stampa di alta qualita'' formato 61x91cm.', 9.90, 22.00, 45, FALSE, 'Art 9 Merch', 'img/prodotti/poster-onepiece.svg'),
    (4, 'Portafoglio Death Note', 'Portafoglio in ecopelle con stampa Death Note.', 22.90, 22.00, 15, FALSE, 'Art 9 Merch', 'img/prodotti/portafoglio-deathnote.svg'),

    (5, 'Portachiavi Pokemon Pikachu 3D', 'Portachiavi in gomma sagomata.', 6.90, 22.00, 80, FALSE, 'Art 9 Gadget', 'img/prodotti/portachiavi-pikachu.svg'),
    (5, 'Set Spille Studio Ghibli', 'Set da 5 spille smaltate a tema Ghibli.', 8.90, 22.00, 40, FALSE, 'Art 9 Gadget', 'img/prodotti/spille-ghibli.svg'),
    (5, 'Mousepad Cyberpunk Edgerunners XL', 'Tappetino mouse extra large da gaming.', 14.90, 22.00, 30, FALSE, 'Art 9 Gadget', 'img/prodotti/mousepad-cyberpunk.svg'),
    (5, 'Lampada LED Logo Marvel', 'Lampada da scrivania con logo Marvel luminoso.', 24.90, 22.00, 18, FALSE, 'Art 9 Gadget', 'img/prodotti/lampada-marvel.svg');


INSERT INTO ordine (id_utente, id_metodo_pagamento, data_ordine, stato, totale, indirizzo_spedizione) VALUES
    (2, 1, '2026-06-10 10:15:00', 'CONSEGNATO', 25.79, 'Via Roma 15, 84100 Salerno (SA), Italia'),
    (2, 1, '2026-06-25 18:42:00', 'SPEDITO',    64.99, 'Via Roma 15, 84100 Salerno (SA), Italia'),
    (2, 1, '2026-07-01 09:05:00', 'IN_ATTESA',  19.90, 'Via Roma 15, 84100 Salerno (SA), Italia');

INSERT INTO riga_ordine (id_ordine, id_prodotto, nome_prodotto, prezzo_unitario, iva_percentuale, quantita) VALUES
    (1, 1, 'One Piece Vol. 102', 5.90, 4.00, 1),
    (1, 8, 'Dylan Dog n.451', 3.90, 4.00, 1),
    (1, 19, 'Poster One Piece - Ciurma di Cappello di Paglia', 9.90, 22.00, 1),
    (1, 21, 'Portachiavi Pokemon Pikachu 3D', 6.90, 22.00, 1),

    (2, 11, 'Funko Pop! Luffy Gear 5', 19.99, 22.00, 1),
    (2, 4, 'Berserk Deluxe Vol. 5', 24.90, 4.00, 1),
    (2, 17, 'Felpa Attack on Titan Survey Corps', 34.90, 22.00, 1),

    (3, 6, 'Batman: Anno Uno', 19.90, 4.00, 1);


INSERT INTO recensione (id_utente, id_prodotto, voto, testo, data_recensione) VALUES
    (2, 1, 5, 'Capitolo pazzesco, Oda non delude mai. Spedizione velocissima!', '2026-06-14 12:00:00'),
    (2, 11, 5, 'Qualita'' Funko top, dettagli incredibili sulla Gear 5.', '2026-06-27 09:30:00'),
    (2, 4, 4, 'Edizione deluxe bellissima, un po'' cara ma vale il prezzo.', '2026-06-28 20:15:00');
