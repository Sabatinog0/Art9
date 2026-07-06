<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="card card-narrow">
    <h1 class="text-center">Accedi</h1>
    <p class="text-center">Bentornato su Art 9! Inserisci le tue credenziali per continuare.</p>

    <c:if test="${not empty erroreLogin}">
        <p class="errore-inline visibile" style="text-align:center;">${erroreLogin}</p>
    </c:if>

    <form class="js-validate" action="${pageContext.request.contextPath}/login" method="post" novalidate>
        <input type="hidden" name="redirect" value="${fn:escapeXml(not empty redirect ? redirect : param.redirect)}">

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required placeholder="nome@esempio.it"
                   value="${fn:escapeXml(valori.email)}" data-pattern="email" data-errore-msg="Inserisci un indirizzo email valido.">
            <span class="errore-inline">Inserisci un indirizzo email valido.</span>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required placeholder="La tua password">
        </div>

        <button type="submit" class="btn btn-primary btn-block">Accedi</button>
    </form>

    <p class="text-center" style="margin-top:18px;">
        Non hai un account?
        <c:choose>
            <c:when test="${not empty param.redirect}">
                <a href="${pageContext.request.contextPath}/registrazione?redirect=${param.redirect}">Registrati</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/registrazione">Registrati</a>
            </c:otherwise>
        </c:choose>
    </p>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
