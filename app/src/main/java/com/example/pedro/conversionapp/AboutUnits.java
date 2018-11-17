package com.example.pedro.conversionapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class AboutUnits extends AppCompatActivity {
    private static String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window);
        ((TextView) findViewById(R.id.info)).setText(info);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

    }

    public static void setText(String unit) {
        switch (unit) {
            case "Ångstrom":
                Log.d("App","success");
                info="O Ångstrom é a unidade de medida comumente utilizada na Física para lidar com " +
                        "grandezas da ordem do átomo ou dos espaçamentos entre dois planos cristalinos.\n" +
                        "O nome deriva de Anders Jonas Ångström, físico sueco, que desenvolveu estudos " +
                        "sobre espectroscopia, magnetismo. Foi na primeira área que mais se destacou, " +
                        "completando as pesquisas de Gustav Kirchhoff sobre o espectro solar e estudando " +
                        "os espectros das auroras boreais.";
                break;
            case "Smoot":
                info="O Smoot é uma unidade de comprimento criada no contexto de uma brincadeira numa " +
                        "fraternidade do MIT. Deriva de Oliver R. Smoot que em 1958 se deitou na ponte " +
                        "Harvard sucessivamente para que os seus companheiros pudessem usar a sua altura " +
                        "como medida de comprimento da ponte.";
                break;

            case "inch":
                info="A polegada é uma unidade de medida do sistema imperial de medidas. Teve origem" +
                        " na época dos Romanos, na qual estes recorriam ao próprio polegar para medições. " +
                        "2.54cm é a largura de um polegar humano normal medida na base da unha.";
                break;
            case "foot":
                info="1ft=0.3048m é uma unidade usada no sistema imperial e americano. Historicamente, " +
                        "o \"foot\" foi usado em muitas civilizações como na Grécia, na China e no Reino unidade. " +
                        "O seu comprimento variava de cidade para cidade e normalmente tinha valores compreeendidos " +
                        "entre 0.250m e 0.335m.";
                break;
            case "yard":
                info="1yard=0.9144m é uma unidade do sistema britânico e do sistema americano. " +
                        "Uma barra metálica com este comprimento foi utilizada como referência para " +
                        "establecer o que era 1 yard.";
                break;
            case "mile":
                info="A mile é uma unidade de medida britânica equivalente a 5280feet, 1760yards e " +
                        "foi definida como exatamente 1609,344 métodos em 1959 por acordo internacional.";
                break;
            case "nautical mile":
                info="Uma nautical mile é uma unidade de medida definida como 1852metros. " +
                        "Historicamente foi definida como 1min de latitude. Hoje ainda é utilizada " +
                        "em navegação marinha e aérea e na definição de águas internacionais.";
                break;
            case"mil":
                info="O mil é a mais pequena unidade de comprimento no sistema inglês de medidas. " +
                        "Equivale a 0,0254 milímetros.É usado para medir essencialmente em áreas " +
                        "técnicas como a pintura e a ciência.";
                break;

            case "rod":
                info="O rod, ou em português Vara é utilizado em medições de terrenos e corresponde a 5,0292m.";
                break;

            case "fathom":
                info="O fathom é uma unidade de medida muito pouco usada atualmente.3000 fathom corresponde a uma légua.";
                break;

            case"furlong":
                info="O furlong é uma unidade de comprimento do sistema imperial de medidas. " +
                        "O nome completo da unidade é surveyor furlong, e equivale a 201,168 metros.";
                break;

            case"km":
                info="O km é uma unidade de medida de comprimento que deriva do metro que pertence ao Sistema Internacional de Unidades (SI). " +
                        "A palavra \"quilómetro\" resulta da combinação do prefixo kilo (1000) com a palavra metro.";
                break;

            case"m":
                info="O metro (m) é a unidade de medida de comprimento do Sistema Internacional. " +
                        "É definido como \"o comprimento do trajeto percorrido pela luz no vácuo " +
                        "durante um intervalo de tempo de 1/299 792 458 segundos.";
                break;

            case"cm":
                info="O cm é uma unidade de medida de comprimento que deriva do metro que pertence " +
                        "ao Sistema Internacional de Unidades (SI). A palavra \"centimetro\" resulta " +
                        "da combinação do prefixo centi (10^-2) com a palavra metro.";
                break;

            case"mm":
                info="O mm é uma unidade de medida de comprimento que deriva do metro que pertence " +
                        "ao Sistema Internacional de Unidades (SI). A palavra \"milimetro\" resulta " +
                        "da combinação do prefixo mili (10^-3) com a palavra metro.";
                break;

            case"μm":
                info="O µm é uma unidade de medida de comprimento que deriva do metro que pertence " +
                        "ao Sistema Internacional de Unidades (SI). A palavra \"micrometro\" resulta " +
                        "da combinação do prefixo micro (10^-6) com a palavra metro.";
                break;

            case"nm":
                info="O nm é uma unidade de medida de comprimento que deriva do metro que pertence " +
                        "ao Sistema Internacional de Unidades (SI). A palavra \"nanómetro\" resulta " +
                        "da combinação do prefixo nano (10^-9) com a palavra metro.";
                break;

        }

    }
}
