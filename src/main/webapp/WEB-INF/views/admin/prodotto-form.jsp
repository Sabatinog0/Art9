<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="admin-layout">
    <%@ include file="/WEB-INF/fragments/admin-sidebar.jsp" %>

    <div class="admin-content">
        <div class="section-header"><h2>${empty prodotto ? 'Nuovo prodotto' : 'Modifica prodotto'}</h2></div>

        <form class="js-validate card" style="max-width:640px;"
              action="${pageContext.request.contextPath}/admin/prodotti/${empty prodotto ? 'nuovo' : 'modifica'}" method="post" novalidate>
            <c:if test="${not empty prodotto}">
                <input type="hidden" name="id" value="${prodotto.idProdotto}">
            </c:if>

            <div class="form-group">
                <label for="nome">Nome prodotto</label>
                <input type="text" id="nome" name="nome" required value="${fn:escapeXml(prodotto.nome)}">
                <span class="errore-inline ${not empty errori.nome ? 'visibile' : ''}">${errori.nome}</span>
            </div>

            <div class="form-group">
                <label for="descrizione">Descrizione</label>
                <textarea id="descrizione" name="descrizione">${fn:escapeXml(prodotto.descrizione)}</textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="idCategoria">Categoria</label>
                    <select id="idCategoria" name="idCategoria" required>
                        <c:forEach var="cat" items="${categorie}">
                            <option value="${cat.idCategoria}" ${prodotto.idCategoria == cat.idCategoria ? 'selected' : ''}>${fn:escapeXml(cat.nome)}</option>
                        </c:forEach>
                    </select>
                    <span class="errore-inline ${not empty errori.idCategoria ? 'visibile' : ''}">${errori.idCategoria}</span>
                </div>
                <div class="form-group">
                    <label for="editore">Editore / Brand</label>
                    <input type="text" id="editore" name="editore" value="${fn:escapeXml(prodotto.editore)}">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="prezzo">Prezzo (IVA inclusa, €)</label>
                    <input type="text" id="prezzo" name="prezzo" required value="${prodotto.prezzo}">
                    <span class="errore-inline ${not empty errori.prezzo ? 'visibile' : ''}">${errori.prezzo}</span>
                </div>
                <div class="form-group">
                    <label for="ivaPercentuale">Aliquota IVA (%)</label>
                    <input type="text" id="ivaPercentuale" name="ivaPercentuale" required value="${empty prodotto.ivaPercentuale ? '22.00' : prodotto.ivaPercentuale}">
                    <span class="errore-inline ${not empty errori.ivaPercentuale ? 'visibile' : ''}">${errori.ivaPercentuale}</span>
                </div>
                <div class="form-group">
                    <label for="quantitaDisponibile">Quantita' disponibile</label>
                    <input type="text" id="quantitaDisponibile" name="quantitaDisponibile" required value="${prodotto.quantitaDisponibile}">
                    <span class="errore-inline ${not empty errori.quantitaDisponibile ? 'visibile' : ''}">${errori.quantitaDisponibile}</span>
                </div>
            </div>

            <div class="form-group">
                <label for="immagine">Percorso immagine</label>
                <input type="text" id="immagine" name="immagine" required placeholder="img/prodotti/nome-file.svg" value="${fn:escapeXml(prodotto.immagine)}">
                <span class="errore-inline ${not empty errori.immagine ? 'visibile' : ''}">${errori.immagine}</span>
            </div>

            <div class="checkbox-group" style="margin-bottom:20px;">
                <input type="checkbox" id="inPreordine" name="inPreordine" ${prodotto.inPreordine ? 'checked' : ''}>
                <label for="inPreordine" class="mb-0">Disponibile in preordine</label>
            </div>

            <button type="submit" class="btn btn-primary">${empty prodotto ? 'Crea prodotto' : 'Salva modifiche'}</button>
            <a href="${pageContext.request.contextPath}/admin/prodotti" class="btn btn-outline">Annulla</a>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
