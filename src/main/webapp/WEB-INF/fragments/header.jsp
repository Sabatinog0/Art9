<%@ include file="taglibs.jspf" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}" default="Art 9"/> | Art 9</title>
    <link rel="icon" href="${pageContext.request.contextPath}/img/favicon.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">

<c:if test="${not empty sessionScope.flash}">
    <div class="flash-message flash-${sessionScope.flash.tipo == 'SUCCESSO' ? 'successo' : 'errore'}" id="flash-message">
        <span>${sessionScope.flash.testo}</span>
        <button type="button" class="flash-chiudi" aria-label="Chiudi messaggio">&times;</button>
    </div>
    <c:remove var="flash" scope="session"/>
</c:if>

<header class="site-header">
    <div class="header-top container">
        <a href="${pageContext.request.contextPath}/catalogo" class="logo">ART<span>9</span></a>

        <form class="search-form" role="search" action="${pageContext.request.contextPath}/catalogo" method="get" autocomplete="off">
            <input type="search" name="q" id="search-input" placeholder="Cerca fumetti, manga, action figures..."
                   value="${fn:escapeXml(param.q)}" aria-label="Cerca prodotti">
            <button type="submit" class="search-btn" aria-label="Cerca">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="7"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
            </button>
            <ul id="search-suggestions" class="search-suggestions" hidden></ul>
        </form>

        <div class="header-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.utente}">
                    <div class="account-menu">
                        <button type="button" class="account-toggle" aria-haspopup="true" aria-expanded="false">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                            <span>${sessionScope.utente.nome}</span>
                        </button>
                        <div class="account-dropdown">
                            <c:if test="${sessionScope.utente.admin}">
                                <a href="${pageContext.request.contextPath}/admin">Pannello Admin</a>
                                <hr>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/account">Il mio account</a>
                            <a href="${pageContext.request.contextPath}/account/indirizzi">Indirizzi</a>
                            <a href="${pageContext.request.contextPath}/account/pagamenti">Metodi di pagamento</a>
                            <a href="${pageContext.request.contextPath}/ordini">Storico ordini</a>
                            <a href="${pageContext.request.contextPath}/preferiti">Preferiti</a>
                            <hr>
                            <a href="${pageContext.request.contextPath}/logout">Esci</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="header-link">Accedi</a>
                    <a href="${pageContext.request.contextPath}/registrazione" class="header-link header-link-cta">Registrati</a>
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/preferiti" class="icon-link" aria-label="Preferiti">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.6l-1-1a5.5 5.5 0 0 0-7.8 7.8l1 1L12 21l7.8-7.8 1-1a5.5 5.5 0 0 0 0-7.8z"/></svg>
            </a>
            <a href="${pageContext.request.contextPath}/carrello" class="icon-link cart-link" aria-label="Carrello">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.7 13.4a2 2 0 0 0 2 1.6h9.7a2 2 0 0 0 2-1.6L23 6H6"/></svg>
                <span class="cart-badge" id="cart-badge">${empty carrello ? 0 : carrello.numeroArticoli}</span>
            </a>
        </div>
    </div>
</header>
