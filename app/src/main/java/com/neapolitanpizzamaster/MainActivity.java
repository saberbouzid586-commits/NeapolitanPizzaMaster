package com.neapolitanpizzamaster;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Typeface;
import android.graphics.Color;
import android.text.TextUtils;

import java.util.Locale;

public class MainActivity extends Activity {

    private LinearLayout root;
    private String lang = "en";
    private EditText pizzaCount, ballWeight, timerMinutes;
    private TextView result, timerResult;
    private CountDownTimer countdown;

    private int bg = Color.rgb(250, 247, 240);
    private int red = Color.rgb(190, 45, 35);
    private int dark = Color.rgb(45, 40, 35);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUI();
    }

    private String tr(String en, String fr, String ar) {
        if (lang.equals("fr")) return fr;
        if (lang.equals("ar")) return ar;
        return en;
    }

    private TextView text(String value, int size, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(dark);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        t.setPadding(8, 10, 8, 10);
        return t;
    }

    private void addSpace() {
        TextView s = text("", 8, false);
        root.addView(s);
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(red)
        );
        b.setOnClickListener(v -> action.run());
        root.addView(b);
        return b;
    }

    private EditText input(String hint, String value) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setText(value);
        e.setHint(hint);
        e.setTextSize(16);
        e.setPadding(12, 8, 12, 8);
        root.addView(e);
        return e;
    }

    private void buildUI() {
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 16, 18, 24);
        root.setBackgroundColor(bg);
        scroll.addView(root);
        setContentView(scroll);

        root.setLayoutDirection(
            lang.equals("ar") ? View.LAYOUT_DIRECTION_RTL
                              : View.LAYOUT_DIRECTION_LTR
        );

        TextView title = text("🍕 NEAPOLITAN\nPIZZA MASTER", 27, true);
        title.setTextColor(red);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = text(
            tr("Your journey to authentic Neapolitan pizza",
               "Votre voyage vers la vraie pizza napolitaine",
               "رحلتك نحو البيتزا النابولية الأصلية"),
            15, false);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        addSpace();
        TextView languages = text("Language / Langue / اللغة", 16, true);
        root.addView(languages);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(row);

        addLanguageButton(row, "English", "en");
        addLanguageButton(row, "Français", "fr");
        addLanguageButton(row, "العربية", "ar");

        addSpace();
        root.addView(text(
            tr("DOUGH CALCULATOR", "CALCULATEUR DE PÂTE",
               "حاسبة العجين"), 22, true));

        root.addView(text(
            tr("Number of pizzas", "Nombre de pizzas", "عدد البيتزات"),
            15, false));
        pizzaCount = input("e.g. 4", "4");

        root.addView(text(
            tr("Dough ball weight (grams)", "Poids par boule (grammes)",
               "وزن كرة العجين (غرام)"), 15, false));
        ballWeight = input("e.g. 250", "250");

        button(tr("Calculate ingredients", "Calculer les ingrédients",
                  "احسب المقادير"), this::calculateDough);

        result = text(
            tr("Your ingredient quantities will appear here.",
               "Les quantités apparaîtront ici.",
               "ستظهر مقادير العجين هنا."), 17, true);
        root.addView(result);

        addSpace();
        root.addView(text(
            tr("AUTHENTIC DOUGH RECIPE", "RECETTE AUTHENTIQUE",
               "وصفة العجين الأصلية"), 22, true));

        root.addView(text(
            tr("• 100% flour\n• 65% water\n• 2.8% salt\n• 0.1% instant yeast\n\nMix the ingredients until combined. Knead until smooth, cover and let the dough rise. Divide into balls and allow them to ferment until relaxed and airy. Bake on a thoroughly heated pizza stone or steel at the highest safe oven temperature.",
               "• 100 % farine\n• 65 % eau\n• 2,8 % sel\n• 0,1 % levure instantanée\n\nMélangez les ingrédients. Pétrissez jusqu'à obtenir une pâte lisse, couvrez et laissez lever. Divisez en boules et laissez fermenter jusqu'à ce qu'elles soient souples et aérées. Cuisez sur une pierre ou une plaque préchauffée, à la température maximale sûre du four.",
               "• 100٪ دقيق\n• 65٪ ماء\n• 2.8٪ ملح\n• 0.1٪ خميرة فورية\n\nاخلط المكونات جيدًا ثم اعجن حتى تصبح العجينة ناعمة. غطّها واتركها تختمر. قسّمها إلى كرات واتركها ترتاح حتى تصبح مرنة ومنتفخة. اخبزها على حجر أو صفيحة بيتزا مسخنة جيدًا، بأعلى درجة حرارة آمنة للفرن."),
            16, false));

        addSpace();
        root.addView(text(
            tr("FERMENTATION TIMER", "MINUTEUR DE FERMENTATION",
               "مؤقّت التخمير"), 22, true));

        timerMinutes = input(
            tr("Minutes", "Minutes", "الدقائق"), "60");

        timerResult = text(
            tr("Timer ready", "Minuteur prêt", "المؤقّت جاهز"), 20, true);
        root.addView(timerResult);

        button(tr("Start timer", "Démarrer", "ابدأ المؤقّت"),
               this::startTimer);

        button(tr("Stop / Reset", "Arrêter / Réinitialiser",
                  "إيقاف / إعادة ضبط"), () -> {
            if (countdown != null) countdown.cancel();
            timerResult.setText(tr("Timer stopped", "Minuteur arrêté",
                                   "تم إيقاف المؤقّت"));
        });

        addSpace();
        TextView footer = text(
            "NEAPOLITAN PIZZA MASTER • v1.0", 12, true);
        footer.setGravity(Gravity.CENTER);
        root.addView(footer);
    }

    private void addLanguageButton(LinearLayout row,
                                  String label, String language) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(12);
        b.setPadding(2, 4, 2, 4);
        b.setOnClickListener(v -> {
            lang = language;
            buildUI();
        });
        row.addView(b, new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
    }

    private void calculateDough() {
        try {
            int count = Integer.parseInt(
                pizzaCount.getText().toString().trim());
            double weight = Double.parseDouble(
                ballWeight.getText().toString().trim());

            if (count <= 0 || weight <= 0 || count > 500
                    || weight > 2000) {
                result.setText(tr(
                    "Enter a valid positive quantity.",
                    "Entrez une quantité positive valide.",
                    "أدخل كمية موجبة وصحيحة."));
                return;
            }

            double total = count * weight;
            double flour = total / (1 + 0.65 + 0.028 + 0.001);
            double water = flour * 0.65;
            double salt = flour * 0.028;
            double yeast = flour * 0.001;

            String output = String.format(Locale.US,
                tr("Total dough: %.0f g\nFlour: %.1f g\nWater: %.1f g\nSalt: %.1f g\nInstant yeast: %.2f g",
                   "Pâte totale : %.0f g\nFarine : %.1f g\nEau : %.1f g\nSel : %.1f g\nLevure instantanée : %.2f g",
                   "إجمالي العجين: %.0f غ\nالدقيق: %.1f غ\nالماء: %.1f غ\nالملح: %.1f غ\nالخميرة الفورية: %.2f غ"),
                total, flour, water, salt, yeast);

            result.setText(output);
        } catch (Exception e) {
            result.setText(tr(
                "Please enter valid numbers.",
                "Veuillez saisir des nombres valides.",
                "يرجى إدخال أرقام صحيحة."));
        }
    }

    private void startTimer() {
        try {
            long minutes = Long.parseLong(
                timerMinutes.getText().toString().trim());

            if (minutes <= 0 || minutes > 10080) {
                timerResult.setText(tr(
                    "Enter 1–10080 minutes.",
                    "Entrez entre 1 et 10080 minutes.",
                    "أدخل مدة من 1 إلى 10080 دقيقة."));
                return;
            }

            if (countdown != null) countdown.cancel();

            countdown = new CountDownTimer(minutes * 60000L, 1000) {
                public void onTick(long millis) {
                    long seconds = millis / 1000;
                    timerResult.setText(String.format(
                        Locale.getDefault(), "%02d:%02d:%02d",
                        seconds / 3600, (seconds % 3600) / 60,
                        seconds % 60));
                }

                public void onFinish() {
                    timerResult.setText(tr(
                        "Time is up! Check your dough.",
                        "Temps écoulé ! Vérifiez votre pâte.",
                        "انتهى الوقت! تفقد العجين."));
                }
            }.start();

        } catch (Exception e) {
            timerResult.setText(tr(
                "Enter a valid number of minutes.",
                "Entrez un nombre de minutes valide.",
                "أدخل عددًا صحيحًا من الدقائق."));
        }
    }

    @Override
    protected void onDestroy() {
        if (countdown != null) countdown.cancel();
        super.onDestroy();
    }
}
