
(function () {
    "use strict";

    var RITARDO_DEBOUNCE = 250;
    var MIN_CARATTERI = 2;

    document.addEventListener("DOMContentLoaded", function () {
        var input = document.getElementById("search-input");
        var lista = document.getElementById("search-suggestions");
        if (!input || !lista) return;

        var timer = null;
        var contextPath = document.body.dataset.contextPath || "";

        function nascondi() {
            lista.hidden = true;
            lista.innerHTML = "";
        }

        function formattaPrezzo(prezzo) {
            return prezzo.toLocaleString("it-IT", { style: "currency", currency: "EUR" });
        }

        function mostraRisultati(prodotti) {
            lista.innerHTML = "";
            if (prodotti.length === 0) {
                var vuoto = document.createElement("li");
                vuoto.className = "suggestion-empty";
                vuoto.textContent = "Nessun prodotto trovato";
                lista.appendChild(vuoto);
            } else {
                prodotti.forEach(function (prodotto) {
                    var li = document.createElement("li");
                    var a = document.createElement("a");
                    a.href = contextPath + "/prodotto?id=" + prodotto.idProdotto;

                    var img = document.createElement("img");
                    img.src = contextPath + "/" + prodotto.immagine;
                    img.alt = "";

                    var nome = document.createElement("span");
                    nome.className = "suggestion-nome";
                    nome.textContent = prodotto.nome;

                    var prezzo = document.createElement("span");
                    prezzo.className = "suggestion-prezzo";
                    prezzo.textContent = formattaPrezzo(prodotto.prezzo);

                    a.appendChild(img);
                    a.appendChild(nome);
                    a.appendChild(prezzo);
                    li.appendChild(a);
                    lista.appendChild(li);
                });
            }
            lista.hidden = false;
        }

        input.addEventListener("input", function () {
            var query = input.value.trim();
            window.clearTimeout(timer);

            if (query.length < MIN_CARATTERI) {
                nascondi();
                return;
            }

            timer = window.setTimeout(function () {
                fetch(contextPath + "/catalogo/suggerimenti?q=" + encodeURIComponent(query))
                    .then(function (risposta) {
                        if (!risposta.ok) throw new Error("Errore di rete");
                        return risposta.json();
                    })
                    .then(mostraRisultati)
                    .catch(nascondi);
            }, RITARDO_DEBOUNCE);
        });

        document.addEventListener("click", function (event) {
            if (!lista.contains(event.target) && event.target !== input) {
                nascondi();
            }
        });
        input.addEventListener("keydown", function (event) {
            if (event.key === "Escape") nascondi();
        });
    });
})();
