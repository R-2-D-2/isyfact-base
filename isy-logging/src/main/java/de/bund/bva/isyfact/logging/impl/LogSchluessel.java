package de.bund.bva.isyfact.logging.impl;

/*
 * #%L
 * isy-logging
 * %%
 * 
 * %%
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * #L%
 */

/**
 * Auflistung aller Standard-Logeinträge und deren Nachrichten, die in IsyFact-Logging genutzt werden.
 * 
 */
public enum LogSchluessel {

    /** Aufruf einer Methode. */
    EPLILO01001("Methode {} wird aufgerufen."),
    /** Erfolgreiches Ende eines Methodenaufrufs. */
    EPLILO01002("Aufruf von {} erfolgreich beendet."),
    /** Nicht erfolgreiches Ende eines Methodenaufrufs. */
    EPLILO01003("Aufruf von {} mit Fehler beendet."),
    /** Erfolgreiches Ende eines Methodenaufrufs inklusive Dauer. */
    EPLILO01004("Aufruf von {} erfolgreich beendet. Der Aufruf dauerte {} ms."),
    /** Nicht erfolgreiches Ende eines Methodenaufrufs inklusive Dauer. */
    EPLILO01005("Aufruf von {} mit Fehler beendet. Der Aufruf dauerte {} ms."),
    /** Aufruf einer Methode inklusive Parameter. */
    DEBUG_LOGGE_DATEN("Die Methode {} wurde mit folgenden Parametern aufgerufen: {}"),
    /** Aufruf einer Methode inklusive Parameter - wobei keine Parameter übergeben wurden. */
    DEBUG_LOGGE_DATEN_OHNE_PARAMETER("Die Methode {} wurde ohne Parameter aufgerufen."),
    /** Aufruf eines Nachbarsystems inklusive URL. */
    EPLILO01011("Die Methode {} des Nachbarssystems {} wird unter der URL {} aufgerufen."),
    /** Erfolgreiches Ende eines Nachbarsystemaufrufs inklusive URL. */
    EPLILO01012("Aufruf von {} des Nachbarssystems {} unter der URL {} erfolgreich beendet."),
    /** Nicht erfolgreiches Ende eines Nachbarsystemaufrufs inklusive URL. */
    EPLILO01013("Aufruf von {} des Nachbarssystems {} unter der URL {} mit Fehler beendet."),
    /** Erfolgreiches Ende eines Nachbarsystemaufrufs inklusive URL und Dauer. */
    EPLILO01014(
            "Aufruf von {} des Nachbarssystems {} unter der URL {} erfolgreich beendet. Der Aufruf dauerte {} ms."),
    /** Nicht erfolgreiches Ende eines Nachbarsystemaufrufs inklusive URL und Dauer. */
    EPLILO01015(
            "Aufruf von {} des Nachbarssystems {} unter der URL {} mit Fehler beendet. Der Aufruf dauerte {} ms."),

    /** Starten des Spring-Application-Contexts mit Systemname, Systemart und auslösendes Spring-Event. */
    EPLILO02001("Der ApplicationContext des Systems {} ({}) wurde gestartet oder aktualisiert. Event: {}"),
    /** Stoppen des Spring-Application-Contexts mit Systemname, Systemart und auslösendes Spring-Event. */
    EPLILO02002("Der ApplicationContext des Systems {} ({}) wurde gestopped. Event: {}"),
    /** Version des Systems. */
    EPLILO02003("Die Systemversion ist {}."),
    /** Ausgabe eines Laufzeitparameters. */
    EPLILO02004("Der Laufzeitparameter {} besitzt den Wert {}.");

    /**
     * Die Nachricht des Logschlüssels.
     */
    private final String nachricht;

    /**
     * Konstruktor der Klasse.
     * 
     * @param nachricht
     *            die zum Logschlüssel gehörende Nachricht.
     */
    private LogSchluessel(String nachricht) {
        this.nachricht = nachricht;
    }

    /**
     * Liefert den Wert des Attributs 'nachricht'.
     * 
     * @return Wert des Attributs.
     */
    public String getNachricht() {
        return nachricht;
    }

}