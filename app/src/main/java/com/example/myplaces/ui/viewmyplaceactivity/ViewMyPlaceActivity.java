package com.example.myplaces.ui.viewmyplaceactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplaces.AboutActivity;
import com.example.myplaces.MyPlacesList;
import com.example.myplaces.R;
import com.example.myplaces.data.MyPlace;
import com.example.myplaces.data.MyPlacesData;

public class ViewMyPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_place);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int position = -1;

        try {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            position = positionBundle.getInt("position");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        if (position >= 0) {
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            TextView twName = (TextView) findViewById(R.id.viewmyplace_name_text);
            twName.setText(place.getName());

            TextView twDesc = (TextView) findViewById(R.id.viewmyplace_desc_text);
            twDesc.setText(place.getDescription());

            TextView twLong = (TextView) findViewById(R.id.viewmyplace_lon_text);
            twLong.setText(place.getLongitude());

            TextView twLat = (TextView) findViewById(R.id.viewmyplace_lat_text);
            twLat.setText(place.getLatitude());
        }

        final Button finishedButton = (Button) findViewById(R.id.viewmyplace_finished_button);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_my_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent i;
        switch (id) {
            case R.id.show_map_item:
                Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_places_list_item:
                i = new Intent(this, MyPlacesList.class);
                startActivity(i);
                break;
            case R.id.about_item:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}