<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>Il mio account</h2></div>

<div class="account-layout">
    <%@ include file="/WEB-INF/fragments/account-nav.jsp" %>

    <div>
        <div class="card" style="margin-bottom:24px;">
            <h3>Dati personali</h3>
            <form class="js-validate" action="${pageContext.request.contextPath}/account" method="post" novalidate>
                <input type="hidden" name="azione" value="profilo">
                <div class="form-row">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" required value="${sessionScope.utente.nome}" data-pattern="nome" data-errore-msg="Nome non valido.">
                        <span class="errore-inline ${not empty erroriProfilo.nome ? 'visibile' : ''}">${not empty erroriProfilo.nome ? erroriProfilo.nome : "Nome non valido."}</span>
                    </div>
                    <div class="form-group">
                        <label for="cognome">Cognome</label>
                        <input type="text" id="cognome" name="cognome" required value="${sessionScope.utente.cognome}" data-pattern="nome" data-errore-msg="Cognome non valido.">
                        <span class="errore-inline ${not empty erroriProfilo.cognome ? 'visibile' : ''}">${not empty erroriProfilo.cognome ? erroriProfilo.cognome : "Cognome non valido."}</span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required value="${sessionScope.utente.email}" data-pattern="email" data-errore-msg="Email non valida.">
                    <span class="errore-inline ${not empty erroriProfilo.email ? 'visibile' : ''}">${not empty erroriProfilo.email ? erroriProfilo.email : "Email non valida."}</span>
                </div>
                <button type="submit" class="btn btn-primary">Salva modifiche</button>
            </form>
        </div>

        <div class="card">
            <h3>Cambia password</h3>
            <form class="js-validate" action="${pageContext.request.contextPath}/account" method="post" novalidate data-autofocus="false">
                <input type="hidden" name="azione" value="password">
                <div class="form-group">
                    <label for="passwordAttuale">Password attuale</label>
                    <input type="password" id="passwordAttuale" name="passwordAttuale" required>
                    <span class="errore-inline ${not empty erroriPassword.passwordAttuale ? 'visibile' : ''}">${erroriPassword.passwordAttuale}</span>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="nuovaPassword">Nuova password</label>
                        <input type="password" id="nuovaPassword" name="nuovaPassword" required
                               data-pattern="password" data-errore-msg="Almeno 8 caratteri, maiuscola, minuscola, numero e simbolo.">
                        <span class="errore-inline ${not empty erroriPassword.nuovaPassword ? 'visibile' : ''}">${not empty erroriPassword.nuovaPassword ? erroriPassword.nuovaPassword : "Almeno 8 caratteri, maiuscola, minuscola, numero e simbolo."}</span>
                    </div>
                    <div class="form-group">
                        <label for="confermaNuovaPassword">Conferma nuova password</label>
                        <input type="password" id="confermaNuovaPassword" name="confermaNuovaPassword" required
                               data-pattern="password" data-conferma-di="nuovaPassword" data-errore-msg="Le password non coincidono.">
                        <span class="errore-inline ${not empty erroriPassword.confermaNuovaPassword ? 'visibile' : ''}">${not empty erroriPassword.confermaNuovaPassword ? erroriPassword.confermaNuovaPassword : "Le password non coincidono."}</span>
                    </div>
                </div>
                <button type="submit" class="btn btn-outline">Aggiorna password</button>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
