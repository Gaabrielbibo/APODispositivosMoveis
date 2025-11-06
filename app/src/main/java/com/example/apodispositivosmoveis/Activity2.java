package com.example.apodispositivosmoveis;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity2 extends AppCompatActivity {

    private TextInputEditText locationInput;
    private TextView weatherResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        locationInput = findViewById(R.id.location_input);
        Button searchButton = findViewById(R.id.search_button);
        weatherResult = findViewById(R.id.weather_result);

        locationInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(locationInput, InputMethodManager.SHOW_IMPLICIT);
        }

        locationInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String cityName = Objects.requireNonNull(locationInput.getText()).toString();
                if (cityName.isEmpty()) {
                    Toast.makeText(Activity2.this, getString(R.string.please_enter_city_toast), Toast.LENGTH_SHORT).show();
                } else {
                    fetchWeatherData(cityName);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(locationInput.getWindowToken(), 0);
                    }
                }
                return true;
            }
            return false;
        });

        searchButton.setOnClickListener(v -> {
            String cityName = Objects.requireNonNull(locationInput.getText()).toString();
            if (cityName.isEmpty()) {
                Toast.makeText(Activity2.this, getString(R.string.please_enter_city_toast), Toast.LENGTH_SHORT).show();
                return;
            }
            fetchWeatherData(cityName);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchWeatherData(String cityName) {
        APITempo apiService = RetrofitClient.getClient().create(APITempo.class);
        String apiKey = "90edfe0989cf474b6226a5789a0e8f42";
        Call<WeatherResponse> call = apiService.getTempo(cityName, apiKey, "metric");

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    String result = getString(R.string.weather_result_format,
                            weatherResponse.name,
                            weatherResponse.main.temp,
                            weatherResponse.main.humidity,
                            weatherResponse.weather.get(0).description);
                    weatherResult.setText(result);
                } else {
                    Toast.makeText(Activity2.this, getString(R.string.city_not_found_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                String message = getString(R.string.request_failed_toast, t.getMessage());
                Toast.makeText(Activity2.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
