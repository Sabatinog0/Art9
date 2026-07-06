
(function () {
    "use strict";

    function initFlashMessage() {
        var flash = document.getElementById("flash-message");
        if (!flash) return;
        var chiudi = flash.querySelector(".flash-chiudi");
        var nascondi = function () { flash.remove(); };
        if (chiudi) chiudi.addEventListener("click", nascondi);
        window.setTimeout(nascondi, 5000);
    }

    function initAccountMenu() {
        var menu = document.querySelector(".account-menu");
        if (!menu) return;
        var toggle = menu.querySelector(".account-toggle");
        toggle.addEventListener("click", function (event) {
            event.stopPropagation();
            var aperto = menu.classList.toggle("aperto");
            toggle.setAttribute("aria-expanded", aperto ? "true" : "false");
        });
        document.addEventListener("click", function () {
            menu.classList.remove("aperto");
            toggle.setAttribute("aria-expanded", "false");
        });
    }

    function initCategoryNav() {
        var toggle = document.querySelector(".category-nav-toggle");
        var lista = document.getElementById("category-nav-list");
        if (!toggle || !lista) return;
        toggle.addEventListener("click", function () {
            var aperta = lista.classList.toggle("aperta");
            toggle.setAttribute("aria-expanded", aperta ? "true" : "false");
        });
    }

    function initConfirmModal() {
        var overlay = document.getElementById("confirm-modal");
        if (!overlay) return;
        var testo = document.getElementById("confirm-modal-text");
        var btnOk = document.getElementById("confirm-modal-ok");
        var btnAnnulla = document.getElementById("confirm-modal-cancel");
        var callbackCorrente = null;

        function chiudi() {
            overlay.hidden = true;
            callbackCorrente = null;
        }

        btnOk.addEventListener("click", function () {
            var callback = callbackCorrente;
            chiudi();
            if (callback) callback();
        });
        btnAnnulla.addEventListener("click", chiudi);
        overlay.addEventListener("click", function (event) {
            if (event.target === overlay) chiudi();
        });

        window.Art9 = window.Art9 || {};
        window.Art9.confermaAzione = function (messaggio, callback) {
            testo.textContent = messaggio;
            callbackCorrente = callback;
            overlay.hidden = false;
        };

        document.querySelectorAll("[data-conferma]").forEach(function (elemento) {
            elemento.addEventListener("submit", function (event) {
                if (elemento.dataset.confermata === "true") return;
                event.preventDefault();
                window.Art9.confermaAzione(elemento.dataset.conferma, function () {
                    elemento.dataset.confermata = "true";
                    elemento.submit();
                });
            });
        });
    }

    function mostraToast(messaggio, tipo) {
        var precedente = document.getElementById("flash-message");
        if (precedente) precedente.remove();

        var toast = document.createElement("div");
        toast.id = "flash-message";
        toast.className = "flash-message " + (tipo === "errore" ? "flash-errore" : "flash-successo");

        var testo = document.createElement("span");
        testo.textContent = messaggio;

        var chiudi = document.createElement("button");
        chiudi.type = "button";
        chiudi.className = "flash-chiudi";
        chiudi.setAttribute("aria-label", "Chiudi messaggio");
        chiudi.textContent = "×";
        chiudi.addEventListener("click", function () { toast.remove(); });

        toast.appendChild(testo);
        toast.appendChild(chiudi);
        document.body.appendChild(toast);
        window.setTimeout(function () { toast.remove(); }, 4000);
    }

    document.addEventListener("DOMContentLoaded", function () {
        initFlashMessage();
        initAccountMenu();
        initCategoryNav();
        initConfirmModal();
    });

    window.Art9 = window.Art9 || {};
    window.Art9.mostraToast = mostraToast;
})();
