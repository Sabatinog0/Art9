package com.art9.util;

import java.util.regex.Pattern;


public final class ValidationUtil {

    public static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public static final Pattern PASSWORD =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$");

    public static final Pattern NOME =
            Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,50}$");

    public static final Pattern CAP =
            Pattern.compile("^\\d{5}$");

    public static final Pattern PROVINCIA =
            Pattern.compile("^[A-Z]{2}$");

    public static final Pattern VIA =
            Pattern.compile("^.{5,150}$");

    public static final Pattern CITTA =
            Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,80}$");

    public static final Pattern NAZIONE =
            Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,60}$");

    public static final Pattern NUMERO_CARTA =
            Pattern.compile("^\\d{13,19}$");

    public static final Pattern SCADENZA_CARTA =
            Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");

    public static final Pattern INTESTATARIO_CARTA = NOME;

    private ValidationUtil() {
    }

    public static boolean isValidEmail(String value) {
        return value != null && EMAIL.matcher(value).matches();
    }

    public static boolean isValidPassword(String value) {
        return value != null && PASSWORD.matcher(value).matches();
    }

    public static boolean isValidNome(String value) {
        return value != null && NOME.matcher(value).matches();
    }

    public static boolean isValidCap(String value) {
        return value != null && CAP.matcher(value).matches();
    }

    public static boolean isValidProvincia(String value) {
        return value != null && PROVINCIA.matcher(value).matches();
    }

    public static boolean isValidVia(String value) {
        return value != null && VIA.matcher(value).matches();
    }

    public static boolean isValidCitta(String value) {
        return value != null && CITTA.matcher(value).matches();
    }

    public static boolean isValidNazione(String value) {
        return value != null && NAZIONE.matcher(value).matches();
    }

    public static boolean isValidNumeroCarta(String value) {
        return value != null && NUMERO_CARTA.matcher(value.replace(" ", "")).matches();
    }

    public static boolean isValidScadenzaCarta(String value) {
        return value != null && SCADENZA_CARTA.matcher(value).matches();
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
