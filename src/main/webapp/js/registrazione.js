
(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", function () {
        var emailInput = document.getElementById("email");
        if (!emailInput) return;

        var erroreEmail = document.getElementById("errore-email");
        var contextPath = document.body.dataset.contextPath || "";
        var timer = null;
        var emailGiaRegistrata = false;

        emailInput.addEventListener("input", function () {
            emailGiaRegistrata = false;
            window.clearTimeout(timer);

            var valore = emailInput.value.trim();
            if (!window.Art9.Pattern.email.test(valore)) {
                return;
            }

            timer = window.setTimeout(function () {
                fetch(contextPath + "/registrazione/verifica-email?email=" + encodeURIComponent(valore))
                    .then(function (risposta) { return risposta.json(); })
                    .then(function (dati) {
                        emailGiaRegistrata = dati.esiste;
                        if (dati.esiste) {
                            emailInput.classList.add("campo-invalido");
                            emailInput.classList.remove("campo-valido");
                            if (erroreEmail) {
                                erroreEmail.textContent = "Questo indirizzo email e' gia' registrato.";
                                erroreEmail.classList.add("visibile");
                            }
                        }
                    })
                    .catch(function () { });
            }, 400);
        });

        var form = emailInput.closest("form");
        if (form) {
            form.addEventListener("submit", function (event) {
                if (emailGiaRegistrata) {
                    event.preventDefault();
                    emailInput.focus();
                }
            });
        }
    });
})();
