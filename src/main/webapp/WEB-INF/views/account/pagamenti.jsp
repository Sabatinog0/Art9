<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>Metodi di pagamento</h2></div>

<div class="account-layout">
    <%@ include file="/WEB-INF/fragments/account-nav.jsp" %>

    <div>
        <c:forEach var="met" items="${metodiPagamento}">
            <div class="list-item-card">
                <div>
                    <strong>${met.circuito} ${met.numeroMascherato}</strong>
                    <c:if test="${met.predefinito}"><span class="badge badge-successo">Predefinito</span></c:if>
                    <p class="mb-0">Intestatario: ${fn:escapeXml(met.intestatario)} &mdash; Scadenza: ${met.scadenza}</p>
                </div>
                <div style="display:flex; gap:8px;">
                    <c:if test="${!met.predefinito}">
                        <form action="${pageContext.request.contextPath}/account/pagamenti/predefinito" method="post">
                            <input type="hidden" name="id" value="${met.idMetodo}">
                            <button type="submit" class="btn btn-outline btn-sm">Predefinito</button>
                        </form>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/account/pagamenti/elimina" method="post" data-conferma="Eliminare questo metodo di pagamento?">
                        <input type="hidden" name="id" value="${met.idMetodo}">
                        <button type="submit" class="btn btn-danger btn-sm">Elimina</button>
                    </form>
                </div>
            </div>
        </c:forEach>

        <div class="card" style="margin-top:20px;">
            <h3>Aggiungi una nuova carta</h3>
            <p style="font-size:.8rem;">Simulazione didattica: salviamo solo il numero mascherato, mai il PAN completo.</p>
            <form class="js-validate" action="${pageContext.request.contextPath}/account/pagamenti" method="post" novalidate>
                <div class="form-group">
                    <label for="intestatario">Intestatario carta</label>
                    <input type="text" id="intestatario" name="intestatario" placeholder="Mario Rossi" data-pattern="nome" data-errore-msg="Inserisci l'intestatario.">
                    <span class="errore-inline ${not empty errori.intestatario ? 'visibile' : ''}">${not empty errori.intestatario ? errori.intestatario : "Inserisci l'intestatario."}</span>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="numeroCarta">Numero carta</label>
                        <input type="text" id="numeroCarta" name="numeroCarta" placeholder="4242 4242 4242 4242" inputmode="numeric" data-pattern="numeroCarta" data-errore-msg="Numero carta non valido.">
                        <span class="errore-inline ${not empty errori.numeroCarta ? 'visibile' : ''}">${not empty errori.numeroCarta ? errori.numeroCarta : "Numero carta non valido."}</span>
                    </div>
                    <div class="form-group">
                        <label for="scadenza">Scadenza (MM/AA)</label>
                        <input type="text" id="scadenza" name="scadenza" placeholder="09/28" data-pattern="scadenzaCarta" data-errore-msg="Formato non valido (MM/AA).">
                        <span class="errore-inline ${not empty errori.scadenza ? 'visibile' : ''}">${not empty errori.scadenza ? errori.scadenza : "Formato non valido (MM/AA)."}</span>
                    </div>
                    <div class="form-group">
                        <label for="circuito">Circuito</label>
                        <select id="circuito" name="circuito">
                            <option value="VISA">Visa</option>
                            <option value="MASTERCARD">Mastercard</option>
                            <option value="AMEX">American Express</option>
                        </select>
                        <span class="errore-inline ${not empty errori.circuito ? 'visibile' : ''}">${errori.circuito}</span>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Aggiungi carta</button>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
