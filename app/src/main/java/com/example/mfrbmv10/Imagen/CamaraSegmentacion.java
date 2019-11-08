package com.example.mfrbmv10.Imagen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mfrbmv10.R;

public class CamaraSegmentacion extends AppCompatActivity {

    private LinearLayout rootLayout;
    private TextView textViewTitle;
    private TextView textViewBody;
    private ImageView imageView;

    private Palette.Swatch vibrantSwatch;
    private Palette.Swatch lightVibrantSwatch;
    private Palette.Swatch darkVibrantSwatch;
    private Palette.Swatch mutedSwatch;
    private Palette.Swatch lightMutedSwatch;
    private Palette.Swatch darkMutedSwatch;

    private int swatchNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.root_layout);
        textViewTitle = findViewById(R.id.text_view_title);
        textViewBody = findViewById(R.id.text_view_body);
        imageView = findViewById(R.id.image_view);

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        Palette.from(bitmap).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                vibrantSwatch = palette.getVibrantSwatch();
                lightVibrantSwatch = palette.getLightVibrantSwatch();
                darkVibrantSwatch = palette.getDarkVibrantSwatch();
                mutedSwatch = palette.getMutedSwatch();
                lightMutedSwatch = palette.getLightMutedSwatch();
                darkMutedSwatch = palette.getDarkMutedSwatch();
            }
        });
    }

    public void nextSwatch(View v) {
        Palette.Swatch currentSwatch = null;

        switch (swatchNumber) {
            case 0:
                currentSwatch = vibrantSwatch;
                textViewTitle.setText("Vibrant");
                break;
            case 1:
                currentSwatch = lightVibrantSwatch;
                textViewTitle.setText("Light Vibrant");
                break;
            case 2:
                currentSwatch = darkVibrantSwatch;
                textViewTitle.setText("Dark Vibrant");
                break;
            case 3:
                currentSwatch = mutedSwatch;
                textViewTitle.setText("Muted");
                break;
            case 4:
                currentSwatch = lightMutedSwatch;
                textViewTitle.setText("Light Muted");
                break;
            case 5:
                currentSwatch = darkMutedSwatch;
                textViewTitle.setText("Dark Muted");
                break;
        }

        if (currentSwatch != null) {
            rootLayout.setBackgroundColor(currentSwatch.getRgb());
            textViewTitle.setTextColor(currentSwatch.getTitleTextColor());
            textViewBody.setTextColor(currentSwatch.getBodyTextColor());
        } else {
            rootLayout.setBackgroundColor(Color.WHITE);
            textViewTitle.setTextColor(Color.RED);
            textViewBody.setTextColor(Color.RED);
        }

        if (swatchNumber < 5) {
            swatchNumber++;
        } else {
            swatchNumber = 0;
        }
    }
}