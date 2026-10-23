# Smart Pantry Manager

A Java Android application that helps reduce food waste by tracking ingredients in your pantry and suggesting recipes you can make **strictly** with what you already have.

## Features
- Add, edit and delete pantry items (name, quantity, unit, optional expiry)
- RecyclerView list of all pantry items
- 18 pre-seeded recipes
- **Strict matching**: a recipe only appears if every required ingredient is present in sufficient quantity
- Recipe detail screen with ingredients and method
- Settings screen (expiring-soon alerts toggle, metric preference)
- Bottom navigation between Pantry, Suggested Recipes and Settings
- Full CRUD with SQLite (data persists after app is closed)
- Input validation on the add/edit form

## Database choice: SQLite
SQLite was chosen because:
- It is fully offline and requires no internet or external accounts
- It matches the persistent-data content covered in the module
- CRUD is straightforward with `SQLiteOpenHelper`
- Perfect for a single-user local pantry app

## How to run
1. Open Android Studio
2. File → Open → select this project folder
3. Wait for Gradle sync to finish
4. Run on an emulator (API 24+) or a physical device
5. First launch automatically seeds 18 recipes

## Strict-matching logic
See `DatabaseHelper.getSuggestedRecipes()` and `canMake()`.  
Names are normalised (lowercase + simple singular/plural handling) so "tomato" matches "tomatoes".

## Project structure
```
app/src/main/java/com/richfield/smartpantry/
  MainActivity.java              – host + bottom nav + lists
  AddEditIngredientActivity.java – add / edit / delete + validation
  RecipeDetailActivity.java
  SettingsActivity.java
  DatabaseHelper.java            – SQLite + seed + strict match
  PantryItem.java / Recipe.java  – models
  PantryAdapter.java / RecipeAdapter.java
```

## Author
Mobile App Development 700 – Practical Assignment
