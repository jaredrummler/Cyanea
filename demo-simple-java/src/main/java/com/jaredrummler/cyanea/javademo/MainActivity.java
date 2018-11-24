package com.jaredrummler.cyanea.javademo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsActivity;

public class MainActivity extends CyaneaAppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    FloatingActionButton fab = findViewById(R.id.fab);
    setSupportActionBar(toolbar);
    fab.setOnClickListener((v) ->
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    );
  }

  @Override public boolean onCreateOptionsMenu(@NonNull Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(this, CyaneaSettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}
