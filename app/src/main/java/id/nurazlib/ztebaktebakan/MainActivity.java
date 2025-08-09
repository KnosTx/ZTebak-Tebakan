package id.nurazlib.ztebaktebakan;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // --- Konstanta ---
    private static final String TAG = "MainActivity"; // Tag untuk logging
    private static final int MAX_LEVEL = 40;
    private static final int INITIAL_HINTS = 3;
    private static final int MAX_HINTS = 5;
    private static final int INITIAL_COINS = 250;
    private static final int COINS_FOR_CORRECT_ANSWER = 10;
    private static final int COINS_FOR_HINT = 50;
    private static final int HINTS_FROM_AD = 2;
    private static final int COINS_FROM_AD = 25;
    private static final int AD_TRIGGER_INTERVAL = 5; // Tampilkan iklan setiap 5 pertanyaan

    // --- Konstanta untuk SharedPreferences ---
    private static final String PREFS_NAME = "GameProgress";
    private static final String PREF_CURRENT_LEVEL = "current_level";
    private static final String PREF_HINT_COUNT = "hint_count";
    private static final String PREF_COIN_COUNT = "coin_count";

    // --- Komponen UI ---
    private TextView questionText, levelText, hintCounterText, coinText;
    private GridLayout optionsGrid;
    private MaterialButton submitButton, hintButton;
    private MaterialCardView questionCard;
    private AdView adView;
    private LinearProgressIndicator progressBar;

    // --- Logika Game ---
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int hintCounter = INITIAL_HINTS;
    private int coinCounterValue = INITIAL_COINS;
    private String selectedAnswer = null; // Menyimpan jawaban yang dipilih pengguna

    // --- Manajemen Audio ---
    private MediaPlayer backgroundMusic;
    private AudioManager audioManager;
    private int currentMusicIndex = 1;

    // --- Manajemen Iklan ---
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeApplication();
    }

    /**
     * Menginisialisasi semua komponen utama aplikasi.
     */
    private void initializeApplication() {
        setupLoggingSystem();
        initializeMobileAds();
        initializeUIComponents();
        setupAudioSystem();
        loadGameData();
        setupEventListeners();
        loadAdvertisements();
    }

    /**
     * Menginisialisasi komponen UI dan memuat iklan banner.
     */
    private void initializeUIComponents() {
        questionText = findViewById(R.id.question_text);
        levelText = findViewById(R.id.level_text);
        hintCounterText = findViewById(R.id.hint_counter);
        coinText = findViewById(R.id.coin_text);
        optionsGrid = findViewById(R.id.options_grid);
        submitButton = findViewById(R.id.submit_button);
        hintButton = findViewById(R.id.hint_button);
        progressBar = findViewById(R.id.progress_bar);
        adView = findViewById(R.id.adView);
        questionCard = findViewById(R.id.question_card);

        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerAdRequest);
    }

    /**
     * Mengatur listener untuk tombol dan pilihan jawaban.
     */
    private void setupEventListeners() {
        submitButton.setOnClickListener(v -> handleAnswerSubmission());
        hintButton.setOnClickListener(v -> handleHintRequest());

        // PERBAIKAN: Menyederhanakan listener untuk pilihan jawaban.
        // Cukup satu listener untuk setiap kartu, tidak perlu loop lagi saat submit.
        for (int i = 0; i < optionsGrid.getChildCount(); i++) {
            final MaterialCardView optionCard = (MaterialCardView) optionsGrid.getChildAt(i);
            optionCard.setOnClickListener(view -> {
                resetOptionCardsUI(); // Reset tampilan semua kartu
                highlightSelectedCard(optionCard); // Sorot kartu yang dipilih
                TextView optionText = (TextView) optionCard.getChildAt(0);
                selectedAnswer = optionText.getText().toString(); // Simpan jawaban yang dipilih
            });
        }
    }

    // --- Region: Logika Game & UI ---

    /**
     * Memuat progres game yang tersimpan atau memulai game baru.
     */
    private void loadGameData() {
        questions = QuestionBank.getQuestions();
        if (questions == null || questions.isEmpty()) {
            handleFatalError("Gagal memuat bank soal. Aplikasi akan ditutup.");
            return;
        }

        loadSavedProgress();
        updateUI();
        loadCurrentQuestion();
    }

    /**
     * Memuat pertanyaan saat ini ke UI.
     */
    private void loadCurrentQuestion() {
        if (currentQuestionIndex >= questions.size() || currentQuestionIndex >= MAX_LEVEL) {
            endGameSession();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        displayQuestion(currentQuestion);
        checkForAdTrigger();
    }

    /**
     * Menampilkan data pertanyaan (teks, pilihan) ke UI.
     * @param question Objek pertanyaan yang akan ditampilkan.
     */
    private void displayQuestion(Question question) {
        // Animasi fade out sebelum mengubah teks
        questionCard.animate().alpha(0f).setDuration(200).withEndAction(() -> {
            questionText.setText(question.getQuestionText());
            updateLevelAndProgressUI();

            // Atur teks pilihan jawaban
            List<String> options = question.getOptions();
            for (int i = 0; i < optionsGrid.getChildCount() && i < options.size(); i++) {
                MaterialCardView optionCard = (MaterialCardView) optionsGrid.getChildAt(i);
                TextView optionText = (TextView) optionCard.getChildAt(0);
                optionText.setText(options.get(i));
            }

            resetOptionCardsUI();
            selectedAnswer = null; // Reset jawaban terpilih

            // Animasi fade in setelah teks diubah
            questionCard.animate().alpha(1f).setDuration(200).start();
        }).start();
    }

    /**
     * Memproses jawaban yang dipilih oleh pengguna.
     */
    private void handleAnswerSubmission() {
        if (selectedAnswer == null) {
            showToast("Silakan pilih jawaban terlebih dahulu!");
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            coinCounterValue += COINS_FOR_CORRECT_ANSWER;
            showToast(String.format("Benar! +%d koin", COINS_FOR_CORRECT_ANSWER));
        } else {
            showToast("Salah! Jawaban yang benar: " + currentQuestion.getCorrectAnswer());
        }

        currentQuestionIndex++;
        saveGameProgress();
        updateUI();
        loadCurrentQuestion();
    }

    /**
     * Menangani permintaan bantuan (hint).
     */
    private void handleHintRequest() {
        if (hintCounter > 0) {
            showQuestionHint();
            hintCounter--;
            saveGameProgress();
            updateUI();
        } else {
            showToast("Hint habis! Tonton iklan atau gunakan koin untuk mendapatkannya.");
            showHintAcquisitionOptions();
        }
    }

    /**
     * Mengakhiri sesi permainan ketika semua level selesai.
     */
    private void endGameSession() {
        questionText.setText("Selamat! Anda telah menyelesaikan semua level!");
        levelText.setText("Permainan Selesai!");
        optionsGrid.setVisibility(View.GONE);
        submitButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    /**
     * Memperbarui semua elemen UI yang dinamis (koin, hint, level).
     */
    private void updateUI() {
        hintCounterText.setText(String.valueOf(hintCounter));
        coinText.setText(String.valueOf(coinCounterValue));
        hintButton.setEnabled(hintCounter > 0);
        updateLevelAndProgressUI();
    }

    /**
     * Memperbarui teks level dan progress bar.
     */
    private void updateLevelAndProgressUI() {
        if (currentQuestionIndex < questions.size()) {
            int level = (currentQuestionIndex / 10) + 1;
            int questionInLevel = (currentQuestionIndex % 10) + 1;
            levelText.setText(String.format(Locale.getDefault(), "Level %d • Soal %d/10", level, questionInLevel));
            progressBar.setProgress(questionInLevel * 10, true);
        }
    }

    /**
     * Mereset tampilan semua kartu pilihan ke keadaan default.
     */
    private void resetOptionCardsUI() {
        for (int i = 0; i < optionsGrid.getChildCount(); i++) {
            MaterialCardView card = (MaterialCardView) optionsGrid.getChildAt(i);
            card.setCardBackgroundColor(getColor(R.color.material_dynamic_surface_container_high));
            card.setStrokeWidth(1);
            card.setStrokeColor(getColor(R.color.material_dynamic_outline));
        }
    }

    /**
     * Menyorot kartu yang dipilih oleh pengguna.
     * @param selectedCard Kartu yang akan disorot.
     */
    private void highlightSelectedCard(MaterialCardView selectedCard) {
        selectedCard.setCardBackgroundColor(getColor(R.color.material_dynamic_secondary_container));
        selectedCard.setStrokeWidth(4);
        selectedCard.setStrokeColor(getColor(R.color.material_dynamic_primary));
    }

    // --- Region: Popup & Dialog ---

    /**
     * Menampilkan popup yang berisi hint.
     */
    private void showQuestionHint() {
        View hintView = getLayoutInflater().inflate(R.layout.custom_popup_hint, null);
        PopupWindow hintPopup = createPopupWindow(hintView);

        TextView hintText = hintView.findViewById(R.id.hint_text);
        hintText.setText(questions.get(currentQuestionIndex).getHint());

        hintView.findViewById(R.id.close_popup_button).setOnClickListener(v -> hintPopup.dismiss());

        showPopupWindow(hintPopup, hintView);
    }

    /**
     * Menampilkan popup dengan opsi untuk mendapatkan hint.
     */
    private void showHintAcquisitionOptions() {
        View optionsView = getLayoutInflater().inflate(R.layout.custom_hint_options, null);
        PopupWindow optionsPopup = createPopupWindow(optionsView);

        Button watchAdButton = optionsView.findViewById(R.id.watch_ad_button);
        Button useCoinsButton = optionsView.findViewById(R.id.use_coins_button);

        watchAdButton.setOnClickListener(v -> {
            handleAdRewardRequest();
            optionsPopup.dismiss();
        });

        useCoinsButton.setText(String.format(Locale.getDefault(), "Gunakan %d Koin", COINS_FOR_HINT));
        useCoinsButton.setOnClickListener(v -> {
            if (coinCounterValue >= COINS_FOR_HINT) {
                coinCounterValue -= COINS_FOR_HINT;
                hintCounter++;
                saveGameProgress();
                updateUI();
                showToast(String.format("1 hint dibeli seharga %d koin", COINS_FOR_HINT));
            } else {
                showToast("Koin tidak cukup!");
            }
            optionsPopup.dismiss();
        });

        optionsView.findViewById(R.id.cancel_button).setOnClickListener(v -> optionsPopup.dismiss());
        showPopupWindow(optionsPopup, optionsView);
    }

    /**
     * Helper untuk membuat objek PopupWindow standar.
     */
    private PopupWindow createPopupWindow(View view) {
        return new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
    }

    /**
     * Helper untuk menampilkan PopupWindow dengan animasi.
     */
    private void showPopupWindow(PopupWindow popupWindow, View view) {
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        view.setAlpha(0f);
        view.animate().alpha(1f).setDuration(300).start();
    }


    // --- Region: Manajemen Iklan (AdMob) ---

    private void initializeMobileAds() {
        MobileAds.initialize(this, initializationStatus -> Log.d(TAG, "MobileAds diinisialisasi."));
    }

    private void loadAdvertisements() {
        loadInterstitialAd();
        loadRewardedAd();
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-4186599691041011/7680150324", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        Log.i(TAG, "Iklan Interstitial berhasil dimuat.");
                        configureInterstitialCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        Log.e(TAG, "Gagal memuat Iklan Interstitial: " + error.getMessage());
                        interstitialAd = null;
                    }
                });
    }

    private void configureInterstitialCallbacks() {
        if (interstitialAd == null) return;
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Iklan Interstitial ditampilkan.");
                pauseAudio();
                interstitialAd = null; // Iklan hanya bisa digunakan sekali
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "Iklan Interstitial ditutup.");
                resumeAudio();
                loadInterstitialAd(); // Muat iklan baru untuk nanti
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                Log.e(TAG, "Gagal menampilkan Iklan Interstitial: " + error.getMessage());
                resumeAudio();
                loadInterstitialAd(); // Coba muat lagi
            }
        });
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-4186599691041011/8469643922", adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.i(TAG, "Iklan Rewarded berhasil dimuat.");
                        configureRewardedAdCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        Log.e(TAG, "Gagal memuat Iklan Rewarded: " + error.getMessage());
                        rewardedAd = null;
                    }
                });
    }

    private void configureRewardedAdCallbacks() {
        if (rewardedAd == null) return;
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Iklan Rewarded ditampilkan.");
                pauseAudio();
                rewardedAd = null; // Iklan hanya bisa digunakan sekali
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "Iklan Rewarded ditutup.");
                resumeAudio();
                loadRewardedAd(); // Muat iklan baru untuk nanti
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                Log.e(TAG, "Gagal menampilkan Iklan Rewarded: " + error.getMessage());
                resumeAudio();
                loadRewardedAd(); // Coba muat lagi
            }
        });
    }

    private void handleAdRewardRequest() {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                // Beri hadiah kepada pengguna
                hintCounter = Math.min(hintCounter + HINTS_FROM_AD, MAX_HINTS);
                coinCounterValue += COINS_FROM_AD;
                saveGameProgress();
                updateUI();
                showToast(String.format(Locale.getDefault(), "Anda mendapat %d hint dan %d koin!", HINTS_FROM_AD, COINS_FROM_AD));
            });
        } else {
            showToast("Iklan belum siap. Coba lagi nanti.");
            loadRewardedAd(); // Coba muat lagi jika belum siap
        }
    }

    private void checkForAdTrigger() {
        // Tampilkan iklan interstitial jika sudah dimuat dan pada interval yang tepat
        if (interstitialAd != null && currentQuestionIndex > 0 && currentQuestionIndex % AD_TRIGGER_INTERVAL == 0) {
            interstitialAd.show(this);
        }
    }

    // --- Region: Manajemen Audio ---

    private void setupAudioSystem() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initializeBackgroundMusic();
    }

    private void initializeBackgroundMusic() {
        try {
            backgroundMusic = MediaPlayer.create(this, R.raw.background_music1);
            if (backgroundMusic == null) {
                Log.e(TAG, "Gagal membuat MediaPlayer. Resource tidak ditemukan?");
                return;
            }
            backgroundMusic.setLooping(true); // PERBAIKAN: Gunakan looping agar lebih simpel
            if (isAudioEnabled()) {
                backgroundMusic.start();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saat inisialisasi background music", e);
        }
    }

    private boolean isAudioEnabled() {
        return audioManager != null && audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0;
    }

    private void pauseAudio() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    private void resumeAudio() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying() && isAudioEnabled()) {
            backgroundMusic.start();
        }
    }

    // --- Region: Penyimpanan & Lifecycle ---

    private void saveGameProgress() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putInt(PREF_CURRENT_LEVEL, currentQuestionIndex)
                .putInt(PREF_HINT_COUNT, hintCounter)
                .putInt(PREF_COIN_COUNT, coinCounterValue)
                .apply();
        Log.d(TAG, "Progres game disimpan pada level: " + currentQuestionIndex);
    }

    private void loadSavedProgress() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentQuestionIndex = prefs.getInt(PREF_CURRENT_LEVEL, 0);
        hintCounter = prefs.getInt(PREF_HINT_COUNT, INITIAL_HINTS);
        coinCounterValue = prefs.getInt(PREF_COIN_COUNT, INITIAL_COINS);
        Log.d(TAG, "Progres game dimuat. Level: " + currentQuestionIndex);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) adView.pause();
        pauseAudio();
        saveGameProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
        resumeAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    private void releaseResources() {
        if (adView != null) adView.destroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    // --- Region: Utilitas & Logging ---

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleFatalError(String errorMessage) {
        Log.e(TAG, "FATAL ERROR: " + errorMessage);
        showToast(errorMessage);
        finishAffinity(); // Menutup aplikasi sepenuhnya
    }

    /**
     * Inisialisasi sistem logging ke file.
     * Catatan: Untuk debugging, lebih disarankan menggunakan Logcat bawaan Android.
     */
    private void setupLoggingSystem() {
        try {
            File logFile = new File(getExternalFilesDir(null), "app_log.txt");
            FileWriter writer = new FileWriter(logFile, true);
            writer.append("Log dimulai pada: ").append(new Date().toString()).append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, "Gagal menulis ke file log", e);
        }
    }
}

