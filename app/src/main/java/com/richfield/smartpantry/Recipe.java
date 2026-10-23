package com.richfield.smartpantry;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String ingredientsRaw; // "tomato:2:pcs|onion:1:pcs"
    private String steps;

    public Recipe(int id, String name, String ingredientsRaw, String steps) {
        this.id = id;
        this.name = name;
        this.ingredientsRaw = ingredientsRaw;
        this.steps = steps;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIngredientsRaw() { return ingredientsRaw; }
    public String getSteps() { return steps; }

    /** Returns a human-readable ingredient list. */
    public List<String> getIngredientLines() {
        List<String> lines = new ArrayList<>();
        String[] parts = ingredientsRaw.split("\\|");
        for (String p : parts) {
            String[] t = p.split(":");
            if (t.length >= 3) {
                lines.add(t[1] + " " + t[2] + " " + t[0]);
            } else if (t.length == 2) {
                lines.add(t[1] + " " + t[0]);
            }
        }
        return lines;
    }
}
