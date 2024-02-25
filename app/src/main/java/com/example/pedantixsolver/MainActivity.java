package com.example.pedantixsolver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private TextView scoreTextView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.scoreTextView);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            PedantixSolver pedantixSolver = new PedantixSolver();
            ConcurrentHashMap<Integer, String> results = new ConcurrentHashMap<>();

            IntStream.range(0, 10)
                    .parallel()
                    .forEach(i -> {
                        String resultIndex = null;
                        try {
                            resultIndex = pedantixSolver.solve(Integer.toString(i));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        results.put(i, resultIndex);

                        StringBuilder stringBuilder = new StringBuilder();
                        results.entrySet().stream()
                                .sorted(java.util.Map.Entry.comparingByKey())
                                .forEach(entry -> stringBuilder
                                        .append(entry.getValue())
                                        .append(" "));
                        String result = stringBuilder.toString();
                        handler.post(() -> {
                            scoreTextView.setText(result);
                        });
                    });
            handler.post(() -> {
                progressBar.setVisibility(View.INVISIBLE);
            });
        });

    }

}
