package com.jaydeep.ishaai;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;

public class MainActivity extends Activity {

    private LinearLayout root, chat;
    private EditText input;
    private TextToSpeech tts;

    private final int bg = Color.rgb(4, 8, 18);
    private final int cyan = Color.rgb(60, 225, 255);

    private static final int MIC_PERMISSION = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
            }
        });

        buildUI();
        addMessage("ISHA AI",
            "Systems online. Hello Jaydeep! Main ISHA AI hoon.");
    }

    private GradientDrawable panel(int color, int border) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(28);
        if (border != 0) d.setStroke(2, border);
        return d;
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    private void buildUI() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 18, 18, 12);
        root.setBackgroundColor(bg);
        setContentView(root);

        TextView title = text(
            "I S H A   A I   //   INTELLIGENCE",
            17, cyan
        );
        title.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        root.addView(title, new LinearLayout.LayoutParams(-1, 55));

        TextView orb = text("◉", 78, cyan);
        orb.setBackground(panel(Color.rgb(9, 27, 43), cyan));

        LinearLayout.LayoutParams orbParams =
            new LinearLayout.LayoutParams(165, 165);
        orbParams.gravity = Gravity.CENTER;
        orbParams.setMargins(0, 12, 0, 12);
        root.addView(orb, orbParams);

        TextView status = text(
            "VOICE  •  AI  •  SYSTEMS",
            12, Color.LTGRAY
        );
        status.setTypeface(Typeface.MONOSPACE);
        root.addView(status, new LinearLayout.LayoutParams(-1, 36));

        ScrollView scroll = new ScrollView(this);
        chat = new LinearLayout(this);
        chat.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(chat);

        root.addView(scroll, new LinearLayout.LayoutParams(
            -1, 0, 1
        ));

        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);

        input = new EditText(this);
        input.setSingleLine(true);
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.GRAY);
        input.setHint("Ask ISHA anything...");
        input.setPadding(12, 0, 12, 0);
        input.setBackground(panel(Color.rgb(17, 25, 40), cyan));

        row.addView(input, new LinearLayout.LayoutParams(0, 54, 1));

        Button send = new Button(this);
        send.setText("SEND");
        send.setTextColor(bg);
        send.setBackground(panel(cyan, 0));
        row.addView(send, new LinearLayout.LayoutParams(83, 54));

        Button mic = new Button(this);
        mic.setText("MIC");
        mic.setTextColor(cyan);
        mic.setBackground(panel(Color.rgb(17, 25, 40), cyan));
        row.addView(mic, new LinearLayout.LayoutParams(70, 54));

        root.addView(row);

        TextView footer = text(
            "DEVELOPER: JAYDEEP YADAV",
            11, Color.GRAY
        );
        footer.setTypeface(Typeface.MONOSPACE);
        root.addView(footer, new LinearLayout.LayoutParams(-1, 38));

        send.setOnClickListener(v -> submit());
        mic.setOnClickListener(v -> startVoice());
        input.setOnEditorActionListener((v, a, e) -> {
            submit();
            return true;
        });
    }

    private void addMessage(String who, String message) {
        TextView bubble = new TextView(this);
        bubble.setText(who + "\n" + message);
        bubble.setTextSize(15);
        bubble.setTextColor(Color.WHITE);
        bubble.setPadding(15, 13, 15, 13);
        bubble.setBackground(panel(
            Color.rgb(15, 25, 41), cyan
        ));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 7, 0, 7);
        chat.addView(bubble, p);
    }

    private void submit() {
        String q = input.getText().toString().trim();
        if (q.isEmpty()) return;

        input.setText("");

        InputMethodManager imm =
            (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE
            );

        if (imm != null) {
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }

        addMessage("YOU", q);

        String s = q.toLowerCase(Locale.ROOT);
        String answer;

        if (s.contains("your name") || s.contains("tumhara naam")) {
            answer = "Mera naam ISHA AI hai. Mere developer Jaydeep Yadav hain.";
        } else if (s.contains("developer") || s.contains("kisne banaya")) {
            answer = "Mujhe Jaydeep Yadav develop kar rahe hain.";
        } else if (s.contains("time")) {
            answer = "Time: " + DateFormat.getTimeInstance().format(new Date());
        } else if (s.contains("date")) {
            answer = "Date: " + DateFormat.getDateInstance().format(new Date());
        } else if (s.contains("hello") || s.contains("hi")) {
            answer = "Hello Jaydeep! Main ISHA AI hoon. Kaise madad karun?";
        } else {
            answer = "Tumne kaha: " + q
                + "\n\nAI brain connect karna abhi baaki hai. "
                + "Filhaal main basic offline prototype hoon.";
        }

        addMessage("ISHA AI", answer);

        if (tts != null) {
            tts.speak(
                answer,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "isha-response"
            );
        }
    }

    private void startVoice() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                new String[]{Manifest.permission.RECORD_AUDIO},
                MIC_PERMISSION
            );
            return;
        }

        listen();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] results) {
        super.onRequestPermissionsResult(
            requestCode, permissions, results
        );

        if (requestCode == MIC_PERMISSION
                && results.length > 0
                && results[0] == PackageManager.PERMISSION_GRANTED) {
            listen();
        } else if (requestCode == MIC_PERMISSION) {
            addMessage("ISHA AI", "Voice use karne ke liye microphone permission chahiye.");
        }
    }

    private void listen() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            addMessage("ISHA AI", "Speech recognition available nahi hai.");
            return;
        }

        Intent intent = new Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        );

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "hi-IN"
        );
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "Talk to ISHA AI"
        );

        try {
            startActivityForResult(intent, 102);
        } catch (Exception e) {
            addMessage("ISHA AI", "Voice input start nahi ho saka.");
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102 && resultCode == RESULT_OK
                && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            );

            if (results != null && !results.isEmpty()) {
                input.setText(results.get(0));
                submit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}