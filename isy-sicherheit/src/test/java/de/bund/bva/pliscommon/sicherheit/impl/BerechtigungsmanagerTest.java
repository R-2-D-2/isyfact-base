/*
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
 */
package de.bund.bva.pliscommon.sicherheit.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.bund.bva.pliscommon.aufrufkontext.AufrufKontext;
import de.bund.bva.pliscommon.aufrufkontext.impl.AufrufKontextImpl;
import de.bund.bva.pliscommon.aufrufkontext.impl.AufrufKontextVerwalterImpl;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;
import de.bund.bva.pliscommon.sicherheit.Berechtigungsmanager;
import de.bund.bva.pliscommon.sicherheit.Recht;
import de.bund.bva.pliscommon.sicherheit.common.exception.RollenRechteMappingException;

/**
 * Testet die Implementierung des Berechtigungsmanagers.
 *
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BerechtigungsmanagerTest {

    /** der zu testende Berechtigungsmanager. */
    private BerechtigungsmanagerImpl berechtigungsmanager;

    /** der Nutzerkennung des Aufrufkontext auf dem der Berechtigungsmanager arbeitet. */
    private static final String AUFRUF_KONTEXT_NUTZERKENNUNG = "test02@test.de";

    /** das Behördenkennzeichen des Aufrufkontext auf dem der Berechtigungsmanager arbeitet. */
    private static final String AUFRUF_KONTEXT_BHKNZ = "123456";

    /** der Rollen des Aufrufkontext auf dem der Berechtigungsmanager arbeitet. */
    private static final String[] AUFRUF_KONTEXT_ROLLEN = new String[] {};

    /** der Pfad zur Rollen-Rechte-Mapping Datei. */
    private static final String ROLLENRECHTE_PFAD = "/resources/sicherheit/rollenrechte.xml";

    /** der AufrufKontext, aus dem der Berechtigungsmanager erzeugt wird. */
    private AufrufKontext aufrufKontext;

    /** die geparste RollenRechteMapping-Datei. */
    private RollenRechteMapping mapping;

    @Autowired
    private Konfiguration konfiguration;

    /**
     * Erstellt den zu Testenden Berechtigungsmanager.
     */
    @Before
    public void setup() {
        this.aufrufKontext = new AufrufKontextImpl();
        this.aufrufKontext.setDurchfuehrenderBenutzerKennung(AUFRUF_KONTEXT_NUTZERKENNUNG);
        this.aufrufKontext.setDurchfuehrendeBehoerde(AUFRUF_KONTEXT_BHKNZ);
        this.aufrufKontext.setRolle(AUFRUF_KONTEXT_ROLLEN);
        BerechtigungsmanagerImpl berechtigungsmanager =
            new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        XmlAccess access = new XmlAccess();
        this.mapping = access.parseRollenRechteFile(ROLLENRECHTE_PFAD);
        berechtigungsmanager.setRollenRechteMapping(this.mapping);
        this.berechtigungsmanager = berechtigungsmanager;
    }

    @Test
    public void testGetRechte() {
        Set<Recht> rechte = this.berechtigungsmanager.getRechte();
        assertTrue("RechteSet nicht korrekt", rechte.equals(new HashSet<Recht>()));

        String[] abc = new String[] { "Recht_A", "Recht_B", "Recht_C" };
        Set<Recht> expected = new HashSet<Recht>();
        for (String recht : abc) {
            expected.add(new RechtImpl(recht, null));
        }
        this.aufrufKontext.setRolle(new String[] { "Rolle_ABC" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        rechte = this.berechtigungsmanager.getRechte();
        assertTrue("RechteSet nicht korrekt", rechte.equals(expected));

        String[] a = new String[] { "Recht_A" };
        expected = new HashSet<Recht>();
        for (String recht : a) {
            expected.add(new RechtImpl(recht, null));
        }
        this.aufrufKontext.setRolle(new String[] { "Rolle_A" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        rechte = this.berechtigungsmanager.getRechte();
        assertTrue("RechteSet nicht korrekt", rechte.equals(expected));

        String[] ac = new String[] { "Recht_A", "Recht_C" };
        expected = new HashSet<Recht>();
        for (String recht : ac) {
            expected.add(new RechtImpl(recht, null));
        }
        this.aufrufKontext.setRolle(new String[] { "Rolle_A", "Rolle_C" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        rechte = this.berechtigungsmanager.getRechte();
        assertTrue("RechteSet nicht korrekt", rechte.equals(expected));

    }

    @Test
    public void testHatRecht() {
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_A"));
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_B"));
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_C"));

        this.aufrufKontext.setRolle(new String[] { "Rolle_ABC" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        assertTrue("Recht wurde nicht gefunden.", this.berechtigungsmanager.hatRecht("Recht_A"));
        assertTrue("Recht wurde nicht gefunden.", this.berechtigungsmanager.hatRecht("Recht_B"));
        assertTrue("Recht wurde nicht gefunden.", this.berechtigungsmanager.hatRecht("Recht_C"));

        this.aufrufKontext.setRolle(new String[] { "Rolle_Keine" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_A"));
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_B"));
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_C"));

        this.aufrufKontext.setRolle(new String[] { "Rolle_C" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_A"));
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_B"));
        assertTrue("Recht wurde nicht gefunden.", this.berechtigungsmanager.hatRecht("Recht_C"));

        this.aufrufKontext.setRolle(new String[] { "Rolle_A", "Rolle_C" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        assertTrue("Recht wurde nicht gefunden.", this.berechtigungsmanager.hatRecht("Recht_A"));
        assertFalse("Falsches Recht wurde gefunden.", this.berechtigungsmanager.hatRecht("Recht_B"));
        assertTrue("Recht wurde nicht gefunden.", this.berechtigungsmanager.hatRecht("Recht_C"));

    }

    @Test
    public void testGetRecht() {
        this.aufrufKontext.setRolle(new String[] { "Rolle_ABC" });
        this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
        this.berechtigungsmanager.setRollenRechteMapping(this.mapping);
        assertNotNull("Recht nicht gefunden.", this.berechtigungsmanager.getRecht("Recht_A"));
        assertNotNull("Recht nicht gefunden.", this.berechtigungsmanager.getRecht("Recht_B"));
        assertNotNull("Recht nicht gefunden.", this.berechtigungsmanager.getRecht("Recht_C"));
        assertNull("Nicht erwartetes Recht gefunden.", this.berechtigungsmanager.getRecht("Rolle_A"));
        try {
            this.berechtigungsmanager.getRecht(null);
            fail("Erwartete Exception wird nicht ausgelöst");
        } catch (IllegalArgumentException e) {
            // hier ist alles in Ordnung
        }
        try {
            this.berechtigungsmanager = new BerechtigungsmanagerImpl(this.aufrufKontext.getRolle());
            // berechtigungsmanager.setRollenRechteMapping wird nicht aufgerufen - Fehler!
            this.berechtigungsmanager.getRecht("Recht_A");
            fail("Erwartete Exception wird nicht ausgelöst");
        } catch (RollenRechteMappingException e) {
            // hier ist alles in Ordnung
        }
    }

    @Test
    public void testKeineRollen() {
        new BerechtigungsmanagerImpl(null);
    }

    @Test(expected = RollenRechteMappingException.class)
    public void testRechtWurdeNichtKonfiguriert() throws Exception {
        AufrufKontextImpl aufrufKontext = new AufrufKontextImpl();
        aufrufKontext.setDurchfuehrenderBenutzerKennung(AUFRUF_KONTEXT_NUTZERKENNUNG);
        aufrufKontext.setDurchfuehrendeBehoerde(AUFRUF_KONTEXT_BHKNZ);
        aufrufKontext.setRolle(AUFRUF_KONTEXT_ROLLEN);
        aufrufKontext.setRollenErmittelt(true);

        SicherheitImpl sicherheit = new SicherheitImpl();
        AufrufKontextVerwalterImpl aufrufKontextVerwalter = new AufrufKontextVerwalterImpl();
        aufrufKontextVerwalter.setAufrufKontext(aufrufKontext);
        sicherheit.setAufrufKontextVerwalter(aufrufKontextVerwalter);
        sicherheit.setRollenRechteDateiPfad(ROLLENRECHTE_PFAD);
        try {
            sicherheit.afterPropertiesSet();
        } catch (NullPointerException e) {
            // Tue nichts. Dieser Test hat keine Springkonfiguration und die Methode afterPropertiesSet
            // braucht zwingend eine Instanz von Konfiguration.
        }
        Berechtigungsmanager berechtigungsManager = sicherheit.getBerechtigungsManager();
        berechtigungsManager.hatRecht("Dieses Recht kommt in der RollenRechteMappingDatei nicht vor.");
    }
}
