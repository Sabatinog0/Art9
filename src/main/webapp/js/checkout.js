
(function () {
    "use strict";

    function collegaGruppo(selettoreRadio, contenitoreId, valoreNuovo) {
        var radios = document.querySelectorAll(selettoreRadio);
        var contenitore = document.getElementById(contenitoreId);
        if (radios.length === 0 || !contenitore) return;

        function aggiorna() {
            var selezionato = document.querySelector(selettoreRadio + ":checked");
            var mostraNuovo = !selezionato || selezionato.value === valoreNuovo;
            contenitore.style.display = mostraNuovo ? "" : "none";
            contenitore.querySelectorAll("input, select").forEach(function (campo) {
                campo.disabled = !mostraNuovo;
            });
        }

        radios.forEach(function (radio) { radio.addEventListener("change", aggiorna); });
        aggiorna();
    }

    document.addEventListener("DOMContentLoaded", function () {
        collegaGruppo(".scelta-indirizzo", "fieldset-nuovo-indirizzo", "nuovo");
        collegaGruppo(".scelta-pagamento", "fieldset-nuovo-pagamento", "nuovo");
    });
})();
