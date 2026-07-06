
(function () {
    "use strict";

    function contextPath() {
        return document.body.dataset.contextPath || "";
    }

    function aggiornaBadge(numeroArticoli) {
        var badge = document.getElementById("cart-badge");
        if (badge) badge.textContent = numeroArticoli;
    }

    function postJson(url, dati) {
        return fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams(dati).toString()
        }).then(function (risposta) {
            if (!risposta.ok) throw new Error("Errore di rete");
            return risposta.json();
        });
    }

    function initAggiungiAlCarrello() {
        document.querySelectorAll(".add-to-cart-form").forEach(function (form) {
            form.addEventListener("submit", function (event) {
                event.preventDefault();
                var bottone = form.querySelector("button[type=submit]");
                var testoOriginale = bottone ? bottone.textContent : null;
                if (bottone) { bottone.disabled = true; bottone.textContent = "Aggiunta in corso..."; }

                var quantitaInput = form.querySelector("input[name=quantita]");
                postJson(form.action, {
                    idProdotto: form.querySelector("input[name=idProdotto]").value,
                    quantita: quantitaInput ? quantitaInput.value : 1
                }).then(function (risposta) {
                    if (risposta.success) {
                        aggiornaBadge(risposta.numeroArticoli);
                        window.Art9.mostraToast(risposta.messaggio, "successo");
                    } else {
                        window.Art9.mostraToast(risposta.messaggio, "errore");
                    }
                }).catch(function () {
                    window.Art9.mostraToast("Impossibile completare l'operazione, riprova.", "errore");
                }).finally(function () {
                    if (bottone) { bottone.disabled = false; bottone.textContent = testoOriginale; }
                });
            });
        });
    }

    function ricalcolaRigaCarrello(riga, dati) {
        var subtotale = riga.querySelector(".riga-subtotale");
        if (subtotale) subtotale.textContent = dati.subtotaleRiga;
    }

    function aggiornaTotaliCarrello(dati) {
        var totale = document.getElementById("cart-totale");
        if (totale) totale.textContent = dati.totale;
        var contatore = document.getElementById("cart-numero-articoli");
        if (contatore) contatore.textContent = dati.numeroArticoli;
        aggiornaBadge(dati.numeroArticoli);
        if (dati.numeroArticoli === 0) {
            window.location.reload();
        }
    }

    function initPaginaCarrello() {
        var tabella = document.getElementById("cart-table");
        if (!tabella) return;

        tabella.querySelectorAll(".qty-input").forEach(function (input) {
            input.addEventListener("change", function () {
                var riga = input.closest("tr");
                var idProdotto = input.dataset.idProdotto;
                var quantita = Math.max(1, parseInt(input.value, 10) || 1);
                input.value = quantita;

                postJson(contextPath() + "/carrello/aggiorna", { idProdotto: idProdotto, quantita: quantita })
                    .then(function (risposta) {
                        if (risposta.success) {
                            ricalcolaRigaCarrello(riga, risposta);
                            aggiornaTotaliCarrello(risposta);
                        } else {
                            window.Art9.mostraToast(risposta.messaggio, "errore");
                        }
                    })
                    .catch(function () {
                        window.Art9.mostraToast("Impossibile aggiornare la quantita'.", "errore");
                    });
            });
        });

        tabella.querySelectorAll(".remove-link").forEach(function (link) {
            link.addEventListener("click", function (event) {
                event.preventDefault();
                var riga = link.closest("tr");
                var idProdotto = link.dataset.idProdotto;

                postJson(contextPath() + "/carrello/rimuovi", { idProdotto: idProdotto })
                    .then(function (risposta) {
                        if (risposta.success) {
                            riga.remove();
                            aggiornaTotaliCarrello(risposta);
                        }
                    })
                    .catch(function () {
                        window.Art9.mostraToast("Impossibile rimuovere il prodotto.", "errore");
                    });
            });
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        initAggiungiAlCarrello();
        initPaginaCarrello();
    });
})();
