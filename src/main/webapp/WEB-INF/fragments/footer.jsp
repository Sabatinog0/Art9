<%@ include file="taglibs.jspf" %>
</main>

<footer class="site-footer">
    <div class="container footer-grid">
        <div class="footer-brand">
            <span class="logo">ART<span>9</span></span>
            <p>Il negozio online per chi non ha mai smesso di essere nerd. Fumetti, manga, action figures e gadget da collezione.</p>
        </div>
        <div class="footer-col">
            <h3>Categorie</h3>
            <ul>
                <c:forEach var="cat" items="${categorieMenu}">
                    <li><a href="${pageContext.request.contextPath}/catalogo?categoria=${cat.slug}">${fn:escapeXml(cat.nome)}</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="footer-col">
            <h3>Il tuo account</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/ordini">Storico ordini</a></li>
                <li><a href="${pageContext.request.contextPath}/preferiti">Preferiti</a></li>
                <li><a href="${pageContext.request.contextPath}/account">Dati personali</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>Art 9</h3>
            <ul>
                <li>Progetto d'esame Tecnologie Software per il Web</li>
                <li>Universita' degli Studi di Salerno</li>
            </ul>
        </div>
    </div>
    <div class="footer-bottom container">
        <p>&copy; 2026 Art 9 &mdash; Procrastinator 3000. Progetto didattico, nessuna transazione reale.</p>
    </div>
</footer>

<div class="modal-overlay" id="confirm-modal" hidden>
    <div class="modal-box" role="alertdialog" aria-modal="true" aria-labelledby="confirm-modal-title">
        <h2 id="confirm-modal-title">Conferma operazione</h2>
        <p id="confirm-modal-text"></p>
        <div class="modal-actions">
            <button type="button" class="btn btn-outline" id="confirm-modal-cancel">Annulla</button>
            <button type="button" class="btn btn-primary" id="confirm-modal-ok">Conferma</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/js/search.js"></script>
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
