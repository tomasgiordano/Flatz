package com.example.flatz.Lindas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.loginscreen.R;

import java.util.ArrayList;
import java.util.List;

public class GraficoLindoActivity extends AppCompatActivity {

    AnyChartView anyChartView;
    String[] usuarios = {"usuario@usuario","admin@admin","tester@tester"};
    int[] votos = {1,2,3};
    ImageView home,foto;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_lindo);

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        anyChartView = findViewById(R.id.any_chart_view);

        setupPieChart();

        home = findViewById(R.id.ivHome);
        foto = findViewById(R.id.ivCamera);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(v.getContext(),VerGaleriaLindaActivity.class);
                p.putExtra("email",email);
                startActivity(p);
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(v.getContext(), AgregarFotoLindaActivity.class);
                p.putExtra("email",email);
                startActivity(p);
            }
        });
    }

    public void setupPieChart()
    {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i=0;i<usuarios.length;i++)
        {
            dataEntries.add(new ValueDataEntry(usuarios[i],votos[i]));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
}