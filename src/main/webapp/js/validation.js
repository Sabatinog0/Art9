
(function () {
    "use strict";

    var PATTERN = {
        email: /^[\w.+-]+@[\w-]+\.[a-zA-Z]{2,}$/,
        password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{8,}$/,
        nome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,50}$/,
        cap: /^\d{5}$/,
        provincia: /^[A-Z]{2}$/,
        via: /^.{5,150}$/,
        citta: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,80}$/,
        nazione: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,60}$/,
        numeroCarta: /^\d{13,19}$/,
        scadenzaCarta: /^(0[1-9]|1[0-2])\/\d{2}$/,
        testoLibero: /^.{2,1000}$/,
        voto: /^[1-5]$/
    };

    function trovaContenitoreErrore(input) {
        if (input.dataset.erroreTarget) {
            return document.getElementById(input.dataset.erroreTarget);
        }
        var successivo = input.nextElementSibling;
        if (successivo && successivo.classList.contains("errore-inline")) {
            return successivo;
        }
        return null;
    }

    function validaCampo(input) {
        var nomePattern = input.dataset.pattern;
        if (!nomePattern) return true;

        var valore = input.value.trim();
        var obbligatorio = input.hasAttribute("required");
        var contenitoreErrore = trovaContenitoreErrore(input);

        var valido;
        if (!obbligatorio && valore === "") {
            valido = true;
        } else {
            var regex = PATTERN[nomePattern];
            valido = regex ? regex.test(valore) : true;
            if (input.dataset.confermaDi) {
                var altroCampo = document.getElementById(input.dataset.confermaDi);
                if (altroCampo && altroCampo.value !== input.value) {
                    valido = false;
                }
            }
        }

        input.classList.toggle("campo-invalido", !valido);
        input.classList.toggle("campo-valido", valido && valore !== "");
        if (contenitoreErrore) {
            contenitoreErrore.classList.toggle("visibile", !valido);
            if (!valido && input.dataset.erroreMsg) {
                contenitoreErrore.textContent = input.dataset.erroreMsg;
            }
        }
        return valido;
    }

    function inizializzaForm(form) {
        var campi = form.querySelectorAll("[data-pattern]");

        campi.forEach(function (campo) {
            campo.addEventListener("input", function () { validaCampo(campo); });
            campo.addEventListener("blur", function () { validaCampo(campo); });
        });

        form.addEventListener("submit", function (event) {
            var tuttiValidi = true;
            var primoCampoInvalido = null;

            campi.forEach(function (campo) {
                var valido = validaCampo(campo);
                if (!valido && !primoCampoInvalido) {
                    primoCampoInvalido = campo;
                }
                tuttiValidi = tuttiValidi && valido;
            });

            if (!tuttiValidi) {
                event.preventDefault();
                if (primoCampoInvalido) primoCampoInvalido.focus();
            }
        });

        var primoCampo = form.querySelector("input:not([type=hidden]), select, textarea");
        if (primoCampo && form.dataset.autofocus !== "false") {
            primoCampo.focus();
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("form.js-validate").forEach(inizializzaForm);
    });

    window.Art9 = window.Art9 || {};
    window.Art9.Pattern = PATTERN;
    window.Art9.validaCampo = validaCampo;
})();
