# Setterek Használati Útmutató

Ez a dokumentáció a `CommandProcessor` osztályban található `processSetCommand` metódus által támogatott `set` parancs használatát mutatja be. A `set` parancs segítségével különböző objektumok tulajdonságait lehet módosítani.

> [!Important]
> A dokumentációt igyekszünk frissíteni, viszont ez nem egy prioritás, ezért ha valami nem működik úgy ahogy várnád, érdemes ránézni a `CommandProcessor.processSetCommand` függvényre.

## Általános Formátum

```
/set <object> <property> <value>
```

- `<object>`: Az objektum neve, amelynek a tulajdonságát módosítani szeretnéd.
- `<property>`: A módosítandó tulajdonság neve.
- `<value>`: Az új érték, amelyet be szeretnél állítani.

---

## Támogatott Objektumok és Tulajdonságok

### 1. **FungusThread**

- **Tulajdonságok:**
  - `addtekton`: Tekton hozzáadása.
  - `lifespan`: Élettartam beállítása (egész szám).
  - `bridge`: Híd állapotának beállítása (`true`/`false`).
  - `isdying`: Haldoklás állapotának beállítása (`true`/`false`).
  - `prevbody`: Előző gombatest beállítása.
  - `nextbody`: Következő gombatest beállítása.
  - `mybody`: Saját gombatest beállítása.
  - `species`: Faj beállítása.
  - `prev`: Előző szál beállítása.
  - `next`: Következő szál beállítása.

### 2. **FungusBody**

- **Tulajdonságok:**
  - `species`: Faj beállítása.
  - `sporecount`: Spórák száma (egész szám).
  - `tekton`: Tekton beállítása.
  - `addthread`: Szál hozzáadása.
  - `deletethread`: Szál eltávolítása.
  - `sporulateleft`: Sporulációk száma (egész szám).

### 3. **FungusSpecies**

- **Tulajdonságok:**
  - `addbody`: Gombatest hozzáadása.
  - `deletebody`: Gombatest eltávolítása.
  - `addthread`: Szál hozzáadása.
  - `deleteThread`: Szál eltávolítása.
  - `addscore`: Pontszám növelése (egész szám).
  - `descreasescore`: Pontszám csökkentése (egész szám).

### 4. **InsectSpecies**

- **Tulajdonságok:**
  - `addinsect`: Rovar hozzáadása.
  - `deleteinsect`: Rovar eltávolítása.
  - `addscore`: Pontszám növelése (egész szám).
  - `decreasescore`: Pontszám csökkentése (egész szám).

### 5. **Insect**

- **Tulajdonságok:**
  - `species`: Faj beállítása.
  - `thread`: Szál beállítása.
  - `tekton`: Tekton beállítása.
  - `decrease`: Csökkentés állapotának beállítása (`true`/`false`).
  - `effect`: Hatás beállítása (`stun`, `slow`, `fast`, `disablecut`).
  - `movingtimer`: Mozgási hatás időzítő (egész szám).
  - `abilitytimer`: Képesség hatás időzítő (egész szám).

### 6. **Spore**

- **Tulajdonságok:**
  - `nutrition`: Táplálkozási érték (egész szám).
  - `tekton`: Tekton beállítása.

### 7. **Tekton**

- **Tulajdonságok:**
  - `cangrowthread`: Szál növesztésének engedélyezése (`true`/`false`).
  - `cangrowbody`: Gombatest növesztésének engedélyezése (`true`/`false`).
  - `body`: Gombatest beállítása.
  - `addneighbour`: Szomszédos Tekton hozzáadása.
  - `removeneighbour`: Szomszédos Tekton eltávolítása.
  - `addinsect`: Rovar hozzáadása.
  - `deleteinsect`: Rovar eltávolítása.
  - `addspore`: Spóra hozzáadása.
  - `deletespore`: Spóra eltávolítása.
  - `addthread`: Szál hozzáadása.
  - `deletethread`: Szál eltávolítása.

---

## Példák

1. **FungusThread élettartamának beállítása:**

   ```
   /set th1 lifespan 10
   ```

2. **FungusBody fajának beállítása:**

   ```
   /set b1 species fungus1
   ```

3. **Insect hatásának beállítása:**

   ```
   /set i1 effect stun
   ```

4. **Tekton szomszéd hozzáadása:**
   ```
   /set t1 addneighbour t2
   ```

---

## Hibakezelés

- Ha az objektum nem létezik:

  ```
  Hiba: Nem létezik ilyen nevű objektum: <object>
  ```

- Ha a tulajdonság nem létezik:

  ```
  Hiba: Nem létezik ilyen tulajdonság: <property>
  ```

- Ha az érték típusa nem megfelelő:
  ```
  Hiba: Nem megfelelő objektum típus vagy nem létezik ilyen nevű objektum: <value>
  ```

---

Ez a dokumentáció segít eligazodni a `set` parancs használatában és az objektumok tulajdonságainak módosításában.
