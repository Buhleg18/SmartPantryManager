package com.richfield.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * SQLiteOpenHelper for Smart Pantry Manager.
 * Handles pantry items and recipes with full CRUD and strict matching.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry";

    // Recipes table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_INGREDIENTS = "ingredients"; // "tomato:2:pcs|onion:1:pcs"
    public static final String COL_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY + " TEXT)";
        db.execSQL(createPantry);

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_INGREDIENTS + " TEXT NOT NULL, " +
                COL_STEPS + " TEXT NOT NULL)";
        db.execSQL(createRecipes);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    /** Pre-load 18 recipes so the app has data on first run. */
    private void seedRecipes(SQLiteDatabase db) {
        String[][] recipes = {
            {"Tomato Scramble", "egg:2:pcs|tomato:1:pcs|salt:1:pinch|butter:10:g", "1. Beat eggs.\n2. Dice tomato.\n3. Melt butter, scramble eggs with tomato.\n4. Season with salt."},
            {"Simple Omelette", "egg:3:pcs|cheese:30:g|butter:10:g|salt:1:pinch", "1. Beat eggs with salt.\n2. Melt butter in pan.\n3. Pour eggs, add cheese, fold."},
            {"Pasta Aglio e Olio", "pasta:100:g|garlic:2:pcs|olive oil:2:tbsp|salt:1:pinch", "1. Boil pasta.\n2. Fry garlic in oil.\n3. Toss pasta with garlic oil and salt."},
            {"Garlic Toast", "bread:2:slices|garlic:1:pcs|butter:15:g", "1. Toast bread.\n2. Spread butter.\n3. Rub with garlic."},
            {"Fried Rice", "rice:150:g|egg:1:pcs|onion:0.5:pcs|soy sauce:1:tbsp", "1. Cook rice.\n2. Scramble egg, set aside.\n3. Fry onion, add rice, egg and soy sauce."},
            {"Banana Smoothie", "banana:1:pcs|milk:200:ml|honey:1:tbsp", "1. Peel banana.\n2. Blend with milk and honey until smooth."},
            {"Cheese Sandwich", "bread:2:slices|cheese:40:g|butter:10:g", "1. Butter bread.\n2. Add cheese.\n3. Grill or toast until melted."},
            {"Tomato Salad", "tomato:2:pcs|onion:0.5:pcs|olive oil:1:tbsp|salt:1:pinch", "1. Slice tomato and onion.\n2. Dress with oil and salt."},
            {"Boiled Eggs", "egg:2:pcs|salt:1:pinch", "1. Place eggs in water.\n2. Boil 8 minutes.\n3. Peel and salt."},
            {"Mashed Potato", "potato:2:pcs|butter:20:g|milk:50:ml|salt:1:pinch", "1. Boil potatoes.\n2. Mash with butter, milk and salt."},
            {"Onion Soup", "onion:2:pcs|butter:20:g|water:400:ml|salt:1:pinch", "1. Slice onions.\n2. Fry in butter until soft.\n3. Add water and salt, simmer 20 min."},
            {"Garlic Butter Pasta", "pasta:100:g|butter:25:g|garlic:2:pcs|salt:1:pinch", "1. Cook pasta.\n2. Melt butter with garlic.\n3. Toss pasta, season."},
            {"Egg Fried Bread", "bread:1:slices|egg:1:pcs|butter:10:g|salt:1:pinch", "1. Beat egg with salt.\n2. Dip bread, fry in butter."},
            {"Yogurt Banana Bowl", "yogurt:150:g|banana:1:pcs|honey:1:tbsp", "1. Scoop yogurt.\n2. Slice banana on top.\n3. Drizzle honey."},
            {"Simple Stir-Fry Veg", "carrot:1:pcs|onion:1:pcs|olive oil:1:tbsp|salt:1:pinch", "1. Slice veg.\n2. Stir-fry in oil until tender.\n3. Season."},
            {"Milk Porridge", "oats:50:g|milk:250:ml|honey:1:tbsp", "1. Simmer oats in milk.\n2. Stir until thick.\n3. Sweeten with honey."},
            {"Tomato Pasta", "pasta:100:g|tomato:2:pcs|garlic:1:pcs|olive oil:1:tbsp|salt:1:pinch", "1. Cook pasta.\n2. Sauté garlic and diced tomato.\n3. Toss with pasta."},
            {"Cheese Omelette", "egg:2:pcs|cheese:40:g|butter:10:g|salt:1:pinch", "1. Beat eggs.\n2. Cook in butter, add cheese, fold."}
        };

        for (String[] r : recipes) {
            ContentValues cv = new ContentValues();
            cv.put(COL_RECIPE_NAME, r[0]);
            cv.put(COL_INGREDIENTS, r[1]);
            cv.put(COL_STEPS, r[2]);
            db.insert(TABLE_RECIPES, null, cv);
        }
    }

    // ---------- PANTRY CRUD ----------

    public long addIngredient(String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name.trim().toLowerCase(Locale.ROOT));
        cv.put(COL_QUANTITY, quantity);
        cv.put(COL_UNIT, unit.trim().toLowerCase(Locale.ROOT));
        cv.put(COL_EXPIRY, expiry == null || expiry.isEmpty() ? null : expiry);
        long id = db.insert(TABLE_PANTRY, null, cv);
        db.close();
        return id;
    }

    public int updateIngredient(int id, String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name.trim().toLowerCase(Locale.ROOT));
        cv.put(COL_QUANTITY, quantity);
        cv.put(COL_UNIT, unit.trim().toLowerCase(Locale.ROOT));
        cv.put(COL_EXPIRY, expiry == null || expiry.isEmpty() ? null : expiry);
        int rows = db.update(TABLE_PANTRY, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public int deleteIngredient(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_PANTRY, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null, COL_NAME + " ASC");
        if (c.moveToFirst()) {
            do {
                PantryItem item = new PantryItem(
                        c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                        c.getDouble(c.getColumnIndexOrThrow(COL_QUANTITY)),
                        c.getString(c.getColumnIndexOrThrow(COL_UNIT)),
                        c.isNull(c.getColumnIndexOrThrow(COL_EXPIRY)) ? null : c.getString(c.getColumnIndexOrThrow(COL_EXPIRY))
                );
                list.add(item);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public PantryItem getIngredientById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        PantryItem item = null;
        if (c.moveToFirst()) {
            item = new PantryItem(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_QUANTITY)),
                    c.getString(c.getColumnIndexOrThrow(COL_UNIT)),
                    c.isNull(c.getColumnIndexOrThrow(COL_EXPIRY)) ? null : c.getString(c.getColumnIndexOrThrow(COL_EXPIRY))
            );
        }
        c.close();
        db.close();
        return item;
    }

    // ---------- RECIPES ----------

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        if (c.moveToFirst()) {
            do {
                list.add(cursorToRecipe(c));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Recipe r = null;
        if (c.moveToFirst()) {
            r = cursorToRecipe(c);
        }
        c.close();
        db.close();
        return r;
    }

    private Recipe cursorToRecipe(Cursor c) {
        return new Recipe(
                c.getInt(c.getColumnIndexOrThrow(COL_RECIPE_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                c.getString(c.getColumnIndexOrThrow(COL_INGREDIENTS)),
                c.getString(c.getColumnIndexOrThrow(COL_STEPS))
        );
    }

    /**
     * STRICT MATCHING: A recipe is suggested only if EVERY required ingredient
     * is present in the pantry with at least the required quantity.
     * Names are normalised (lowercase, singular/plural simple handling).
     */
    public List<Recipe> getSuggestedRecipes() {
        List<PantryItem> pantry = getAllPantryItems();
        Map<String, Double> pantryMap = new HashMap<>();
        for (PantryItem p : pantry) {
            String key = normaliseName(p.getName());
            // Aggregate quantity if same ingredient appears more than once
            pantryMap.put(key, pantryMap.getOrDefault(key, 0.0) + p.getQuantity());
        }

        List<Recipe> all = getAllRecipes();
        List<Recipe> suggested = new ArrayList<>();

        for (Recipe recipe : all) {
            if (canMake(recipe, pantryMap)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    private boolean canMake(Recipe recipe, Map<String, Double> pantryMap) {
        String[] parts = recipe.getIngredientsRaw().split("\\|");
        for (String part : parts) {
            String[] tokens = part.split(":");
            if (tokens.length < 2) continue;
            String name = normaliseName(tokens[0]);
            double needed;
            try {
                needed = Double.parseDouble(tokens[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            Double have = pantryMap.get(name);
            if (have == null || have < needed) {
                return false;
            }
        }
        return true;
    }

    /** Simple normalisation: lowercase + remove trailing 's' for basic plural handling. */
    private String normaliseName(String name) {
        if (name == null) return "";
        String n = name.trim().toLowerCase(Locale.ROOT);
        if (n.endsWith("oes")) {
            // tomatoes -> tomato
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("s") && n.length() > 2) {
            n = n.substring(0, n.length() - 1);
        }
        return n;
    }
}
