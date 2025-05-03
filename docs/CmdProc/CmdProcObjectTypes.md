# Objektum Típusok Használati Útmutató

Ez a dokumentáció bemutatja a `CommandProcessor` által támogatott objektumtípusokat, azok jellemzőit és használatát. Az objektumok különböző típusai a játék logikájában különböző szerepeket töltenek be.

---

## Alapértelmezett Objektumtípusok

### 1. **FungusBody**

- **Leírás:** A gombák testét reprezentálja.
- **Tulajdonságok:**
  - `species`: A gombatesthez tartozó faj.
  - `sporecount`: A gombatest által termelt spórák száma.
  - `tekton`: A gombatesthez tartozó Tekton.
  - `threads`: A gombatesthez kapcsolódó szálak listája.
  - `sporulateleft`: A hátralévő sporulációk száma.

---

### 2. **FungusThread**

- **Leírás:** A gombák szálait reprezentálja, amelyek összekötik a gombatesteket és a Tektonokat.
- **Tulajdonságok:**
  - `prevbody`: Az előző gombatest.
  - `nextbody`: A következő gombatest.
  - `mybody`: A szálhoz tartozó gombatest.
  - `species`: A szálhoz tartozó faj.
  - `tektons`: A szálhoz kapcsolódó Tektonok listája.
  - `lifespan`: A szál élettartama.
  - `isdying`: A szál haldoklásának állapota.
  - `bridge`: Hídként funkcionál-e.

---

### 3. **FungusSpecies**

- **Leírás:** A gombafajokat reprezentálja.
- **Tulajdonságok:**
  - `bodies`: A fajhoz tartozó gombatestek listája.
  - `threads`: A fajhoz tartozó szálak listája.
  - `score`: A faj pontszáma.

---

### 4. **Insect**

- **Leírás:** Egy rovar példányt reprezentál.
- **Tulajdonságok:**
  - `species`: A rovarhoz tartozó faj.
  - `thread`: A szál, amelyen a rovar tartózkodik.
  - `tekton`: A rovar által legutóbb meglátogatott Tekton.
  - `effect`: A rovar aktuális hatása (pl. `stun`, `slow`, `fast`, `disablecut`).
  - `movingtimer`: Mozgási hatás időzítője.
  - `abilitytimer`: Képesség hatás időzítője.

---

### 5. **InsectSpecies**

- **Leírás:** A rovarfajokat reprezentálja.
- **Tulajdonságok:**
  - `insects`: A fajhoz tartozó rovarok listája.
  - `score`: A faj pontszáma.

---

### 6. **Spore**

- **Leírás:** A gombák által termelt spórákat reprezentálja.
- **Tulajdonságok:**
  - `nutrition`: A spóra táplálkozási értéke.
  - `tekton`: A spóra által elfoglalt Tekton.

#### Egyedi Spóra Típusok:

- **FastSpore:** Gyorsítja a rovarokat.
- **MultiplyInsectSpore:** Új rovarokat hoz létre.
- **SlowSpore:** Lassítja a rovarokat.
- **StunSpore:** Elkábítja a rovarokat.
- **DisableCutSpore:** Megakadályozza a szálak elvágását.

---

### 7. **Tekton**

- **Leírás:** A játék alapvető építőeleme, amelyhez gombatestek, szálak és spórák kapcsolódhatnak.
- **Tulajdonságok:**
  - `cangrowthread`: Engedélyezi-e a szál növesztését.
  - `cangrowbody`: Engedélyezi-e a gombatest növesztését.
  - `body`: A Tektonhoz tartozó gombatest.
  - `neighbours`: Szomszédos Tektonok listája.
  - `insects`: A Tektonon tartózkodó rovarok listája.
  - `spores`: A Tektonon található spórák listája.
  - `threads`: A Tektonhoz kapcsolódó szálak listája.

#### Egyedi Tekton Típusok:

- **DecomposingTekton:** Lebomló Tekton.
- **DecreasingTekton:** Csökkenő Tekton.
- **FeedThreadTekton:** Szálakat tápláló Tekton.
- **OneThreadTekton:** Egyetlen szálat támogató Tekton.
- **OnlyThreadTekton:** Csak szálakat támogató Tekton.

---

## Példák

1. **Gombatest létrehozása:**

   ```
   /create body b1
   ```

2. **Szál létrehozása:**

   ```
   /create thread th1
   ```

3. **Rovar létrehozása:**

   ```
   /create insect i1
   ```

4. **Tekton létrehozása:**

   ```
   /create tekton t1
   ```

5. **Spóra létrehozása:**
   ```
   /create spore s1
   ```

---

Ez a dokumentáció segít eligazodni az objektumtípusok között, és megérteni azok szerepét a játékban.
