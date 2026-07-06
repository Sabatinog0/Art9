<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>I tuoi indirizzi</h2></div>

<div class="account-layout">
    <%@ include file="/WEB-INF/fragments/account-nav.jsp" %>

    <div>
        <c:forEach var="ind" items="${indirizzi}">
            <div class="list-item-card">
                <div>
                    <strong>${fn:escapeXml(ind.etichetta)}</strong>
                    <c:if test="${ind.predefinito}"><span class="badge badge-successo">Predefinito</span></c:if>
                    <p class="mb-0">${fn:escapeXml(ind.formatoEsteso())}</p>
                </div>
                <div style="display:flex; gap:8px;">
                    <c:if test="${!ind.predefinito}">
                        <form action="${pageContext.request.contextPath}/account/indirizzi/predefinito" method="post">
                            <input type="hidden" name="id" value="${ind.idIndirizzo}">
                            <button type="submit" class="btn btn-outline btn-sm">Predefinito</button>
                        </form>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/account/indirizzi/elimina" method="post" data-conferma="Eliminare questo indirizzo?">
                        <input type="hidden" name="id" value="${ind.idIndirizzo}">
                        <button type="submit" class="btn btn-danger btn-sm">Elimina</button>
                    </form>
                </div>
            </div>
        </c:forEach>

        <div class="card" style="margin-top:20px;">
            <h3>Aggiungi un nuovo indirizzo</h3>
            <form class="js-validate" action="${pageContext.request.contextPath}/account/indirizzi" method="post" novalidate>
                <div class="form-group">
                    <label for="etichetta">Nome indirizzo</label>
                    <input type="text" id="etichetta" name="etichetta" placeholder="Es. Casa, Ufficio" required>
                    <span class="errore-inline ${not empty errori.etichetta ? 'visibile' : ''}">${errori.etichetta}</span>
                </div>
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
                        <input type="text" id="provincia" name="provincia" placeholder="SA" maxlength="2" data-pattern="provincia" data-errore-msg="Sigla provincia non valida.">
                        <span class="errore-inline ${not empty errori.provincia ? 'visibile' : ''}">${not empty errori.provincia ? errori.provincia : "Sigla provincia non valida."}</span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="nazione">Nazione</label>
                    <input type="text" id="nazione" name="nazione" value="Italia" data-pattern="nazione" data-errore-msg="Inserisci una nazione valida.">
                    <span class="errore-inline ${not empty errori.nazione ? 'visibile' : ''}">${errori.nazione}</span>
                </div>
                <button type="submit" class="btn btn-primary">Aggiungi indirizzo</button>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
