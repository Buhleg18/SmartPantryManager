package com.richfield.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvSteps = findViewById(R.id.tvDetailSteps);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipeById(recipeId);
        if (recipe == null) {
            finish();
            return;
        }

        setTitle(recipe.getName());
        tvName.setText(recipe.getName());

        StringBuilder ing = new StringBuilder();
        List<String> lines = recipe.getIngredientLines();
        for (String line : lines) {
            ing.append("• ").append(line).append("\n");
        }
        tvIngredients.setText(ing.toString().trim());
        tvSteps.setText(recipe.getSteps());
    }
}
