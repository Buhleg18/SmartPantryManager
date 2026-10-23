package com.richfield.smartpantry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for adding a new pantry item or editing/deleting an existing one.
 * Includes basic input validation.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etUnit, etExpiry;
    private Button btnSave, btnDelete;
    private DatabaseHelper db;
    private int editId = -1; // -1 means new item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        if (getIntent().hasExtra("id")) {
            editId = getIntent().getIntExtra("id", -1);
            setTitle(R.string.edit_ingredient);
            btnDelete.setVisibility(android.view.View.VISIBLE);
            loadItem(editId);
        } else {
            setTitle(R.string.add_ingredient);
            btnDelete.setVisibility(android.view.View.GONE);
        }

        btnSave.setOnClickListener(v -> save());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadItem(int id) {
        PantryItem item = db.getIngredientById(id);
        if (item != null) {
            etName.setText(item.getName());
            etQuantity.setText(String.valueOf(item.getQuantity()));
            etUnit.setText(item.getUnit());
            if (item.getExpiry() != null) {
                etExpiry.setText(item.getExpiry());
            }
        }
    }

    private void save() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (qtyStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }
        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
            if (qty <= 0) {
                etQuantity.setError("Quantity must be greater than 0");
                etQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }
        if (unit.isEmpty()) {
            etUnit.setError("Unit is required (e.g. pcs, g, ml)");
            etUnit.requestFocus();
            return;
        }

        if (editId == -1) {
            db.addIngredient(name, qty, unit, expiry);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateIngredient(editId, name, qty, unit, expiry);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient?")
                .setMessage("This cannot be undone.")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteIngredient(editId);
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
