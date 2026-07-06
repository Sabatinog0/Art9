<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="checkout-steps">
    <span>Carrello</span> &rsaquo; <span class="attivo">Checkout</span> &rsaquo; <span>Conferma</span>
</div>

<div class="section-header"><h2>Checkout</h2></div>

<form class="js-validate" action="${pageContext.request.contextPath}/checkout" method="post" novalidate id="checkout-form">
    <fieldset>
        <legend>Indirizzo di spedizione</legend>

        <c:forEach var="ind" items="${indirizzi}">
            <div class="checkbox-group" style="margin-bottom:10px;">
                <input type="radio" name="idIndirizzoSalvato" id="ind-${ind.idIndirizzo}" value="${ind.idIndirizzo}" class="scelta-indirizzo" ${ind.predefinito ? 'checked' : ''}>
                <label for="ind-${ind.idIndirizzo}" class="mb-0">${fn:escapeXml(ind.etichetta)}: ${fn:escapeXml(ind.formatoEsteso())}</label>
            </div>
        </c:forEach>
        <div class="checkbox-group" style="margin-bottom:16px;">
            <input type="radio" name="idIndirizzoSalvato" id="ind-nuovo" value="nuovo" class="scelta-indirizzo" ${empty indirizzi ? 'checked' : ''}>
            <label for="ind-nuovo" class="mb-0">Usa un nuovo indirizzo</label>
        </div>

        <div id="fieldset-nuovo-indirizzo">
            <div class="form-group">
                <label for="via">Via e numero civico</label>
                <input type="text" id="via" name="via" placeholder="Via Roma 15" data-pattern="via" data-errore-msg="Inserisci un indirizzo valido.">
                <span class="errore-inline ${not empty errori.via ? 'visibile' : ''}">${not empty errori.via ? errori.via : "Inserisci un indirizzo valido."}</span>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="citta">Citta'</label>
                    <input type="text" id="citta" name="citta" placeholder="Salerno" data-pattern="citta" data-errore-msg="Inserisci una citta' valida (2-80 caratteri).">
                    <span class="errore-inline ${not empty errori.citta ? 'visibile' : ''}">${errori.citta}</span>
                </div>
                <div class="form-group">
                    <label for="cap">CAP</label>
                    <input type="text" id="cap" name="cap" placeholder="84100" data-pattern="cap" data-errore-msg="Il CAP deve essere di 5 cifre.">
                    <span class="errore-inline ${not empty errori.cap ? 'visibile' : ''}">${not empty errori.cap ? errori.cap : "Il CAP deve essere di 5 cifre."}</span>
                </div>
                <div class="form-group">
                    <label for="provincia">Provincia</label>
                    <input type="text" id="provincia" name="provincia" placeholder="SA" maxlength="2" style="text-transform:uppercase;" data-pattern="provincia" data-errore-msg="Sigla provincia non valida (2 lettere).">
                    <span class="errore-inline ${not empty errori.provincia ? 'visibile' : ''}">${not empty errori.provincia ? errori.provincia : "Sigla provincia non valida (2 lettere)."}</span>
                </div>
            </div>
            <div class="form-group">
                <label for="nazione">Nazione</label>
                <input type="text" id="nazione" name="nazione" value="Italia" data-pattern="nazione" data-errore-msg="Inserisci una nazione valida.">
                <span class="errore-inline ${not empty errori.nazione ? 'visibile' : ''}">${errori.nazione}</span>
            </div>
        </div>
    </fieldset>

    <fieldset>
        <legend>Metodo di pagamento</legend>

        <c:forEach var="met" items="${metodiPagamento}">
            <div class="checkbox-group" style="margin-bottom:10px;">
                <input type="radio" name="idMetodoSalvato" id="met-${met.idMetodo}" value="${met.idMetodo}" class="scelta-pagamento" ${met.predefinito ? 'checked' : ''}>
                <label for="met-${met.idMetodo}" class="mb-0">${met.circuito} ${met.numeroMascherato} &mdash; scad. ${met.scadenza}</label>
            </div>
        </c:forEach>
        <div class="checkbox-group" style="margin-bottom:16px;">
            <input type="radio" name="idMetodoSalvato" id="met-nuovo" value="nuovo" class="scelta-pagamento" ${empty metodiPagamento ? 'checked' : ''}>
            <label for="met-nuovo" class="mb-0">Usa una nuova carta</label>
        </div>

        <div id="fieldset-nuovo-pagamento">
            <div class="form-group">
                <label for="intestatario">Intestatario carta</label>
                <input type="text" id="intestatario" name="intestatario" placeholder="Mario Rossi" data-pattern="nome" data-errore-msg="Inserisci l'intestatario della carta.">
                <span class="errore-inline ${not empty errori.intestatario ? 'visibile' : ''}">${not empty errori.intestatario ? errori.intestatario : "Inserisci l'intestatario della carta."}</span>
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
            <p class="text-center" style="font-size:.78rem;">Simulazione didattica: nessun dato di pagamento reale viene elaborato. Salviamo solo la carta mascherata.</p>
        </div>
    </fieldset>

    <div class="card">
        <h3>Riepilogo ordine</h3>
        <c:forEach var="item" items="${carrello.items}">
            <div class="cart-summary-row">
                <span>${fn:escapeXml(item.nome)} &times; ${item.quantita}</span>
                <span><fmt:formatNumber value="${item.subtotale}" type="currency" currencySymbol="€"/></span>
            </div>
        </c:forEach>
        <div class="cart-summary-row totale">
            <span>Totale</span>
            <span><fmt:formatNumber value="${carrello.totale}" type="currency" currencySymbol="€"/></span>
        </div>
    </div>

    <button type="submit" class="btn btn-primary btn-block" style="margin-top:20px;">Conferma ordine</button>
</form>

<script src="${pageContext.request.contextPath}/js/checkout.js"></script>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
