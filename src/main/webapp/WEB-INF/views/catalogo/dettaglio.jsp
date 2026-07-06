<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<section class="product-detail">
    <div class="product-detail-img">
        <img src="${pageContext.request.contextPath}/${prodotto.immagine}" alt="${fn:escapeXml(prodotto.nome)}">
    </div>
    <div class="product-detail-info">
        <span class="product-card-categoria">${fn:escapeXml(prodotto.nomeCategoria)}</span>
        <h1>${fn:escapeXml(prodotto.nome)}</h1>
        <c:if test="${not empty prodotto.editore}">
            <p class="product-detail-editore">${fn:escapeXml(prodotto.editore)}</p>
        </c:if>

        <div class="rating-summary">
            <span class="star-rating">
                <c:forEach begin="1" end="5" var="stella">
                    <c:choose>
                        <c:when test="${stella <= mediaVotiArrotondata}">&#9733;</c:when>
                        <c:otherwise><span class="vuota">&#9733;</span></c:otherwise>
                    </c:choose>
                </c:forEach>
            </span>
            <span>
                <c:choose>
                    <c:when test="${not empty recensioni}"><fmt:formatNumber value="${mediaVoti}" maxFractionDigits="1"/> (${fn:length(recensioni)} recensioni)</c:when>
                    <c:otherwise>Nessuna recensione</c:otherwise>
                </c:choose>
            </span>
        </div>

        <div class="product-detail-prezzo">
            <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€"/>
            <small>IVA inclusa</small>
        </div>

        <div class="product-detail-stock">
            <c:choose>
                <c:when test="${prodotto.quantitaDisponibile > 5}"><span class="stock-ok">Disponibile</span> &mdash; ${prodotto.quantitaDisponibile} pezzi in magazzino</c:when>
                <c:when test="${prodotto.quantitaDisponibile > 0}"><span class="stock-basso">Ultimi ${prodotto.quantitaDisponibile} pezzi disponibili!</span></c:when>
                <c:when test="${prodotto.inPreordine}"><span class="stock-basso">Disponibile in preordine</span></c:when>
                <c:otherwise><span class="stock-off">Prodotto esaurito</span></c:otherwise>
            </c:choose>
        </div>

        <c:if test="${prodotto.quantitaDisponibile > 0}">
            <form class="add-to-cart-form" action="${pageContext.request.contextPath}/carrello/aggiungi" method="post">
                <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                <div class="qty-selector">
                    <button type="button" class="qty-meno" aria-label="Diminuisci quantita'">&minus;</button>
                    <input type="number" name="quantita" id="quantita-input" value="1" min="1" max="${prodotto.quantitaDisponibile}">
                    <button type="button" class="qty-piu" aria-label="Aumenta quantita'">&plus;</button>
                </div>
                <div class="product-actions">
                    <button type="submit" class="btn btn-primary">Aggiungi al carrello</button>
                </div>
            </form>
        </c:if>

        <c:if test="${not empty sessionScope.utente}">
            <form action="${pageContext.request.contextPath}/preferiti/${isPreferito ? 'rimuovi' : 'aggiungi'}" method="post" class="product-actions">
                <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                <input type="hidden" name="ritorno" value="/prodotto?id=${prodotto.idProdotto}">
                <button type="submit" class="btn btn-outline">
                    <c:choose>
                        <c:when test="${isPreferito}">&#9829; Rimuovi dai preferiti</c:when>
                        <c:otherwise>&#9825; Aggiungi ai preferiti</c:otherwise>
                    </c:choose>
                </button>
            </form>
        </c:if>

        <div class="product-detail-desc">
            <p>${fn:escapeXml(prodotto.descrizione)}</p>
        </div>
    </div>
</section>

<section id="recensioni">
    <div class="section-header"><h2>Recensioni</h2></div>

    <c:choose>
        <c:when test="${puoRecensire}">
            <form class="card js-validate" action="${pageContext.request.contextPath}/recensioni" method="post" style="margin-bottom:28px;">
                <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                <div class="form-group">
                    <label for="voto">Il tuo voto</label>
                    <select name="voto" id="voto" required>
                        <option value="5">5 - Eccellente</option>
                        <option value="4">4 - Molto buono</option>
                        <option value="3">3 - Buono</option>
                        <option value="2">2 - Sufficiente</option>
                        <option value="1">1 - Scarso</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="testo">La tua recensione</label>
                    <textarea name="testo" id="testo" maxlength="1000" required
                              data-pattern="testoLibero" data-errore-msg="Scrivi una recensione tra 2 e 1000 caratteri."
                              placeholder="Racconta la tua esperienza con questo prodotto..."></textarea>
                    <span class="errore-inline">Scrivi una recensione tra 2 e 1000 caratteri.</span>
                </div>
                <button type="submit" class="btn btn-primary">Pubblica recensione</button>
            </form>
        </c:when>
        <c:when test="${empty sessionScope.utente}">
            <p><a href="${pageContext.request.contextPath}/login">Accedi</a> per lasciare una recensione sui prodotti che hai acquistato.</p>
        </c:when>
    </c:choose>

    <c:choose>
        <c:when test="${empty recensioni}">
            <p>Nessuna recensione per questo prodotto: sii il primo a lasciarne una!</p>
        </c:when>
        <c:otherwise>
            <div class="review-list">
                <c:forEach var="r" items="${recensioni}">
                    <article class="review-item">
                        <div class="review-item-head">
                            <span class="review-item-autore">${fn:escapeXml(r.nomeUtente)}</span>
                            <span class="review-item-data"><fmt:formatDate value="${r.dataRecensioneAsDate}" pattern="dd/MM/yyyy"/></span>
                        </div>
                        <div class="star-rating">
                            <c:forEach begin="1" end="5" var="stella">
                                <c:choose>
                                    <c:when test="${stella <= r.voto}">&#9733;</c:when>
                                    <c:otherwise><span class="vuota">&#9733;</span></c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <p>${fn:escapeXml(r.testo)}</p>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<c:if test="${not empty correlati}">
    <section>
        <div class="section-header"><h2>Potrebbero interessarti</h2></div>
        <div class="product-grid">
            <c:forEach var="p" items="${correlati}">
                <article class="product-card">
                    <a href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}" class="product-card-img">
                        <img src="${pageContext.request.contextPath}/${p.immagine}" alt="${fn:escapeXml(p.nome)}" loading="lazy">
                    </a>
                    <div class="product-card-body">
                        <span class="product-card-categoria">${fn:escapeXml(p.nomeCategoria)}</span>
                        <a href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}" class="product-card-nome">${fn:escapeXml(p.nome)}</a>
                        <span class="product-card-prezzo"><fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€"/></span>
                    </div>
                </article>
            </c:forEach>
        </div>
    </section>
</c:if>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        var input = document.getElementById("quantita-input");
        var meno = document.querySelector(".qty-meno");
        var piu = document.querySelector(".qty-piu");
        if (!input) return;
        meno.addEventListener("click", function () {
            input.value = Math.max(parseInt(input.min, 10) || 1, (parseInt(input.value, 10) || 1) - 1);
        });
        piu.addEventListener("click", function () {
            input.value = Math.min(parseInt(input.max, 10) || 99, (parseInt(input.value, 10) || 1) + 1);
        });
    });
</script>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
