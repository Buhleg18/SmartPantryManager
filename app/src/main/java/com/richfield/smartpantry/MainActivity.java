package com.richfield.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Host activity with bottom navigation for Pantry and Suggested Recipes.
 * Settings is launched as a separate Activity.
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private FloatingActionButton fab;
    private PantryAdapter pantryAdapter;
    private RecipeAdapter recipeAdapter;
    private boolean showingPantry = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
        fab = findViewById(R.id.fabAdd);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pantryAdapter = new PantryAdapter(db.getAllPantryItems(), item -> {
            Intent i = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            i.putExtra("id", item.getId());
            startActivity(i);
        });

        recipeAdapter = new RecipeAdapter(db.getSuggestedRecipes(), recipe -> {
            Intent i = new Intent(MainActivity.this, RecipeDetailActivity.class);
            i.putExtra("recipeId", recipe.getId());
            startActivity(i);
        });

        // Default: show pantry
        showPantry();

        fab.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(i);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                showPantry();
                return true;
            } else if (id == R.id.nav_suggested) {
                showSuggested();
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh whichever list is visible after returning from Add/Edit or Settings
        if (showingPantry) {
            showPantry();
        } else {
            showSuggested();
        }
    }

    private void showPantry() {
        showingPantry = true;
        setTitle("My Pantry");
        fab.setVisibility(View.VISIBLE);
        List<PantryItem> items = db.getAllPantryItems();
        pantryAdapter.setItems(items);
        recyclerView.setAdapter(pantryAdapter);
        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("Your pantry is empty.\nTap + to add ingredients.");
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showSuggested() {
        showingPantry = false;
        setTitle("Suggested Recipes");
        fab.setVisibility(View.GONE);
        List<Recipe> recipes = db.getSuggestedRecipes();
        recipeAdapter.setRecipes(recipes);
        recyclerView.setAdapter(recipeAdapter);
        if (recipes.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getString(R.string.no_recipes));
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
