<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="card card-narrow">
    <h1 class="text-center">Crea il tuo account</h1>
    <p class="text-center">Registrati per completare gli acquisti, salvare i preferiti e lasciare recensioni.</p>

    <form class="js-validate" action="${pageContext.request.contextPath}/registrazione" method="post" novalidate>
        <input type="hidden" name="redirect" value="${fn:escapeXml(param.redirect)}">

        <div class="form-row">
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" required placeholder="Es. Mario"
                       value="${fn:escapeXml(valori.nome)}" data-pattern="nome" data-errore-msg="Inserisci un nome valido (2-50 lettere).">
                <span class="errore-inline ${not empty errori.nome ? 'visibile' : ''}">${not empty errori.nome ? errori.nome : "Inserisci un nome valido (2-50 lettere)."}</span>
            </div>
            <div class="form-group">
                <label for="cognome">Cognome</label>
                <input type="text" id="cognome" name="cognome" required placeholder="Es. Rossi"
                       value="${fn:escapeXml(valori.cognome)}" data-pattern="nome" data-errore-msg="Inserisci un cognome valido (2-50 lettere).">
                <span class="errore-inline ${not empty errori.cognome ? 'visibile' : ''}">${not empty errori.cognome ? errori.cognome : "Inserisci un cognome valido (2-50 lettere)."}</span>
            </div>
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required placeholder="nome@esempio.it"
                   value="${fn:escapeXml(valori.email)}" data-pattern="email" data-errore-target="errore-email"
                   data-errore-msg="Inserisci un indirizzo email valido.">
            <span class="errore-inline ${not empty errori.email ? 'visibile' : ''}" id="errore-email">${not empty errori.email ? errori.email : "Inserisci un indirizzo email valido."}</span>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required placeholder="Almeno 8 caratteri, maiuscola, numero e simbolo"
                   data-pattern="password" data-errore-msg="La password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un simbolo.">
            <span class="errore-inline ${not empty errori.password ? 'visibile' : ''}">${not empty errori.password ? errori.password : "La password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un simbolo."}</span>
        </div>

        <div class="form-group">
            <label for="confermaPassword">Conferma password</label>
            <input type="password" id="confermaPassword" name="confermaPassword" required placeholder="Ripeti la password"
                   data-pattern="password" data-conferma-di="password" data-errore-msg="Le password inserite non coincidono.">
            <span class="errore-inline ${not empty errori.confermaPassword ? 'visibile' : ''}">${not empty errori.confermaPassword ? errori.confermaPassword : "Le password inserite non coincidono."}</span>
        </div>

        <button type="submit" class="btn btn-primary btn-block">Registrati</button>
    </form>

    <p class="text-center" style="margin-top:18px;">
        Hai gia' un account?
        <c:choose>
            <c:when test="${not empty param.redirect}">
                <a href="${pageContext.request.contextPath}/login?redirect=${param.redirect}">Accedi</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Accedi</a>
            </c:otherwise>
        </c:choose>
    </p>
</div>

<script src="${pageContext.request.contextPath}/js/registrazione.js"></script>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
