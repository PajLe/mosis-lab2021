package com.example.myplaces.ui.editmyplaceactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplaces.AboutActivity;
import com.example.myplaces.MyPlacesList;
import com.example.myplaces.R;
import com.example.myplaces.data.MyPlace;
import com.example.myplaces.data.MyPlacesData;
import com.example.myplaces.ui.googlemapsactivity.GoogleMapsActivity;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {
    boolean editMode = true;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);

        try {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            if(positionBundle != null)
                position = positionBundle.getInt("position");
            else
                editMode = false;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button finishedButton = (Button) findViewById(R.id.editmyplace_finished_button);
        Button cancelButton = (Button) findViewById(R.id.editmyplace_cancel_button);
        EditText nameEditText = (EditText) findViewById(R.id.editmyplace_name_edit);
        EditText descEditText = (EditText) findViewById(R.id.editmyplace_desc_edit);
        EditText longEditText = (EditText) findViewById(R.id.editmyplace_lon_edit);
        EditText latEditText = (EditText) findViewById(R.id.editmyplace_lat_edit);

        if (!editMode) {
            finishedButton.setEnabled(true);
            finishedButton.setText("Add");
        } else if (position >= 0) {
            finishedButton.setText("Save");
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            nameEditText.setText(place.getName());
            descEditText.setText(place.getDescription());
            longEditText.setText(place.getLongitude());
            latEditText.setText(place.getLatitude());
        }

        finishedButton.setOnClickListener(this);
        finishedButton.setEnabled(false);
        cancelButton.setOnClickListener(this);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length() > 0);
            }
        });

        descEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(nameEditText.length() > 0);
            }
        });
        Button locationButton = (Button)findViewById(R.id.editmyplace_location_button);
        locationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editmyplace_finished_button: {
                EditText etName = (EditText) findViewById(R.id.editmyplace_name_edit);
                String name = etName.getText().toString();

                EditText etDesc = (EditText) findViewById(R.id.editmyplace_desc_edit);
                String desc = etDesc.getText().toString();

                EditText latEdit = (EditText) findViewById(R.id.editmyplace_lat_edit);
                String lat = latEdit.getText().toString();

                EditText lonEdit = (EditText) findViewById(R.id.editmyplace_lon_edit);
                String lon = lonEdit.getText().toString();

                if(!editMode) {
                    MyPlace place = new MyPlace(name, desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().addNewPlace(place);
                } else {
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    place.setName(name);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    place.setDescription(desc);
                }

                setResult(Activity.RESULT_OK);
                finish();
                break;
            }

            case R.id.editmyplace_cancel_button: {
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            }

            case R.id.editmyplace_location_button:
            {
                Intent i = new Intent(this, GoogleMapsActivity.class);
                i.putExtra("state", GoogleMapsActivity.SELECT_COORDINATES);
                startActivityForResult(i, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String lon = data.getExtras().getString("lon");
                EditText lonText = (EditText)findViewById(R.id.editmyplace_lon_edit);
                lonText.setText(lon);

                String lat = data.getExtras().getString("lat");
                EditText latText = (EditText)findViewById(R.id.editmyplace_lat_edit);
                latText.setText(lat);
            }
        } catch(Exception exception) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_my_place, menu);
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