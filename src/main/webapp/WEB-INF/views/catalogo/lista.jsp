<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<c:if test="${mostraHero}">
    <section class="hero-banner">
        <div>
            <span class="hero-badge">Nuovi arrivi ogni settimana</span>
            <h1>Il tuo universo <span>nerd</span> in un unico posto</h1>
            <p>Fumetti, manga, action figures e gadget da collezione. Scegli tra un catalogo sempre aggiornato e ricevi i tuoi articoli preferiti direttamente a casa.</p>
            <a href="${pageContext.request.contextPath}/catalogo?categoria=manga" class="btn btn-primary">Esplora il catalogo</a>
        </div>
    </section>
</c:if>

<div class="section-header">
    <h2>
        <c:choose>
            <c:when test="${not empty query}">Risultati per &quot;${fn:escapeXml(query)}&quot;</c:when>
            <c:when test="${not empty categoriaCorrente}">${fn:escapeXml(categoriaCorrente.nome)}</c:when>
            <c:otherwise>Prodotti in evidenza</c:otherwise>
        </c:choose>
    </h2>
    <span>${totaleProdotti} prodotti</span>
</div>

<c:choose>
    <c:when test="${empty prodotti}">
        <div class="empty-state">
            <p>Nessun prodotto trovato con i filtri selezionati.</p>
            <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-outline">Torna al catalogo completo</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach var="p" items="${prodotti}">
                <article class="product-card">
                    <c:if test="${not empty sessionScope.utente}">
                        <c:set var="isPref" value="${not empty preferitiUtente and preferitiUtente.contains(p.idProdotto)}"/>
                        <form action="${pageContext.request.contextPath}/preferiti/${isPref ? 'rimuovi' : 'aggiungi'}" method="post">
                            <input type="hidden" name="idProdotto" value="${p.idProdotto}">
                            <input type="hidden" name="ritorno" value="${percorsoCorrente}">
                            <button type="submit" class="wishlist-btn ${isPref ? 'attivo' : ''}" aria-label="${isPref ? 'Rimuovi dai preferiti' : 'Aggiungi ai preferiti'}">${isPref ? '&#9829;' : '&#9825;'}</button>
                        </form>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}" class="product-card-img">
                        <img src="${pageContext.request.contextPath}/${p.immagine}" alt="${fn:escapeXml(p.nome)}" loading="lazy">
                    </a>
                    <div class="product-card-body">
                        <span class="product-card-categoria">${fn:escapeXml(p.nomeCategoria)}</span>
                        <a href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}" class="product-card-nome">${fn:escapeXml(p.nome)}</a>
                        <div class="product-card-footer">
                            <span class="product-card-prezzo"><fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€"/></span>
                            <c:choose>
                                <c:when test="${p.quantitaDisponibile > 0}">
                                    <form class="add-to-cart-form" action="${pageContext.request.contextPath}/carrello/aggiungi" method="post">
                                        <input type="hidden" name="idProdotto" value="${p.idProdotto}">
                                        <input type="hidden" name="quantita" value="1">
                                        <button type="submit" class="btn btn-primary btn-sm">Aggiungi</button>
                                    </form>
                                </c:when>
                                <c:when test="${p.inPreordine}">
                                    <span class="badge badge-preordine">Preordine</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-esaurito">Esaurito</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </article>
            </c:forEach>
        </div>

        <c:if test="${totalePagine > 1}">
            <nav class="pagination" aria-label="Paginazione risultati">
                <c:forEach begin="1" end="${totalePagine}" var="numeroPagina">
                    <a href="${pageContext.request.contextPath}/catalogo?pagina=${numeroPagina}<c:if test="${not empty categoriaCorrente}">&categoria=${categoriaCorrente.slug}</c:if><c:if test="${not empty query}">&q=${query}</c:if>"
                       class="${numeroPagina == paginaCorrente ? 'attiva' : ''}">${numeroPagina}</a>
                </c:forEach>
            </nav>
        </c:if>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
