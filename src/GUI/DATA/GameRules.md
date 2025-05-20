# Fungorium – Játékszabályok

## Alapok

- **A színes keret** a játéktábla körül mutatja, hogy éppen melyik játékos van soron.
- **Minden játékos csak a saját színével jelölt objektumokat** (gombatest, fonal, rovar, spóra) kezelheti, azaz csak a saját dolgait növesztheti, mozgathatja vagy használhatja fel.

---

## Főbb fogalmak

- **Tekton**: A pálya mezői, amelyeken gombák és rovarok élhetnek.
- **Gombatest (FungusBody)**: Egy tektonon legfeljebb egy nőhet.
- **Gombafonal (FungusThread)**: A gombatestből nőhet, összeköti a tektonokat.
- **Spóra (Spore)**: A gombatestek termelik, különböző hatásokat fejtenek ki.
- **Rovar (Insect)**: A rovarfajok egyedei, mozoghatnak a fonalakon és tektonokon.

---

## Növesztés szabályai

### Gombatest növesztése (`growbody`)

- Csak olyan tektonon lehet új gombatestet növeszteni, amelyen még nincs gombatest.
- A tektonnak szomszédosnak kell lennie egy olyan tektonnal, ahol már van saját gombafonal vagy gombatest.
- Egyes tekton típusokon nem lehet gombatestet növeszteni (pl. OnlyThreadTekton).
- A növesztéshez ki kell választani egy megfelelő tekton objektumot a térképen.

### Gombafonal növesztése (`growthread`)

- Csak olyan tektonra lehet fonalat növeszteni, amely szomszédos a kiindulási gombatesttel vagy fonallal.
- Egy tektonon több fonal is lehet, de bizonyos típusok (pl. OneThreadTekton) csak egyet engednek.
- A növesztéshez először ki kell választani a kiinduló objektumot (gombatest vagy fonal), majd a cél tekton objektumot.

### Spóraszórás (`sporulate`)

- Csak akkor lehet spórát szórni, ha a gombatest már elég ideje létezik (sporulateleft > 0).
- A spóra a kiválasztott gombatest szomszédos tektonjaira kerülhet.

---

## Rovarok és akcióik

- **Mozgás**: A rovarok a fonalakon keresztül mozoghatnak, a mozgás sebességét befolyásolhatják a spórák.
- **Fonal elvágása**: Egy rovar elvághat egy fonalat, ha azon tartózkodik.
- **Rovar megevése**: Egy gombafonál megehet egy rovart, ha az bénítva van és rajta tartózkodik.

---

## Gombok és kiválasztás

- **Objektum kiválasztása**: Kattints a térképen egy objektumra (tekton, fonal, gombatest, rovar, spóra), hogy kijelöld.
- **Akciógombok**: A kijelölt objektum(ok) alapján jelennek meg a lehetséges akciók (pl. "Növesztés", "Spóraszórás", "Mozgás" stb.).
- **Cél kiválasztása**: Egyes akciók (pl. fonal növesztése, rovar mozgatása) után a játék felszólít, hogy válassz cél objektumot a térképen.

---

## Játék menete

1. **Körök**: A játék körökre oszlik, minden játékos egymás után lép.
2. **Lépés**: Egy körben a játékos kiválaszt egy vagy több objektumot, majd végrehajt egy akciót (pl. növeszt, mozgat, spórát szór).
3. **Feltételek ellenőrzése**: Az akciók csak akkor hajthatók végre, ha teljesülnek a feltételek (pl. szomszédos tekton, nincs már ott gombatest stb.).
4. **Kör vége**: A játékos a "Kör vége" gombbal zárhatja a körét, ekkor a következő játékos következik.
5. **Győzelem**: A játék végén a legtöbb pontot szerző faj nyer.

---

## Tippek

- Mindig figyeld, hogy melyik objektum van kijelölve, mert az határozza meg, milyen akciókat hajthatsz végre!
- Ha egy akció nem hajtható végre, a játék hibaüzenetet ír ki.
- A színes keret mutatja, hogy éppen melyik játékos van soron.
