package id.nurazlib.ztebaktebakan;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private TextView questionText, levelText, hintCounterText, coinText;
    private GridLayout optionsGrid;
    private MaterialButton submitButton, hintButton;
    private MaterialCardView questionCard;
    private AdView adView;
    private LinearProgressIndicator progressBar;
    private ImageView coinCounter;

    // Game Logic
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private static final int MAX_LEVEL = 40;
    private static final int INITIAL_HINTS = 3;
    private static final int MAX_HINTS = 5;
    private static final int INITIAL_COINS = 250;
    private static final String PREF_HINT_COUNT = "hint_count";
    private static final String PREF_COIN_COUNT = "coin_count";
    private int hintCounter = INITIAL_HINTS;
    private int coinCounterValue = INITIAL_COINS;

    // Audio Management
    private MediaPlayer backgroundMusic;
    private AudioManager audioManager;
    private int currentMusicIndex = 1;

    // Ad Management
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeApplication();
    }

    private void initializeApplication() {
        setupLoggingSystem();
        initializeMobileAds();
        initializeUIComponents();
        setupAudioSystem();
        initializeGameData();
        setupEventListeners();
        loadAdvertisements();
    }

    private void setupLoggingSystem() {
        try {
            File logFile = new File(getExternalFilesDir(null), "app_log.txt");
            FileWriter writer = new FileWriter(logFile, true);
            writer.append("Log started at: ").append(new Date().toString()).append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeMobileAds() {
        MobileAds.initialize(this, initializationStatus -> {});
    }

    private void initializeUIComponents() {
        questionText = findViewById(R.id.question_text);
        levelText = findViewById(R.id.level_text);
        hintCounterText = findViewById(R.id.hint_counter);
        coinText = findViewById(R.id.coin_text);
        coinCounter = findViewById(R.id.coin_counter);
        optionsGrid = findViewById(R.id.options_grid);
        submitButton = findViewById(R.id.submit_button);
        hintButton = findViewById(R.id.hint_button);
        progressBar = findViewById(R.id.progress_bar);
        adView = findViewById(R.id.adView);
        questionCard = findViewById(R.id.question_card);

        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerAdRequest);
    }

    private void setupAudioSystem() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initializeBackgroundMusic();
    }

    private void initializeBackgroundMusic() {
        try {
            backgroundMusic = MediaPlayer.create(this, R.raw.background_music1);
            backgroundMusic.setOnCompletionListener(mp -> playNextTrack());
            if (isAudioEnabled()) {
                backgroundMusic.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeGameData() {
        questions = QuestionBank.getQuestions();
        currentQuestionIndex = loadSavedProgress() - 1;

        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);
        hintCounter = prefs.getInt(PREF_HINT_COUNT, INITIAL_HINTS);
        coinCounterValue = prefs.getInt(PREF_COIN_COUNT, INITIAL_COINS);

        if (questions == null || questions.isEmpty()) {
            handleFatalError("No questions available");
            return;
        }

        updateHintDisplay();
        updateCoinDisplay();
        loadCurrentQuestion();
    }

    private void setupEventListeners() {
        submitButton.setOnClickListener(v -> handleAnswerSubmission());
        hintButton.setOnClickListener(v -> handleHintRequest());
        
        // Set click listeners for all option cards
        for (int i = 0; i < optionsGrid.getChildCount(); i++) {
            MaterialCardView optionCard = (MaterialCardView) optionsGrid.getChildAt(i);
            optionCard.setOnClickListener(v -> {
                // Highlight selected option
                resetOptionCards();
                optionCard.setCardBackgroundColor(getColor(R.color.material_dynamic_secondary_container));
                optionCard.setStrokeWidth(4);
                optionCard.setStrokeColor(getColor(R.color.material_dynamic_primary));
                
                // Store selected answer
                TextView optionText = (TextView) optionCard.getChildAt(0);
                optionCard.setTag(optionText.getText().toString());
            });
        }
    }

    private void resetOptionCards() {
        for (int i = 0; i < optionsGrid.getChildCount(); i++) {
            MaterialCardView optionCard = (MaterialCardView) optionsGrid.getChildAt(i);
            optionCard.setCardBackgroundColor(getColor(R.color.material_dynamic_surface_container_high));
            optionCard.setStrokeWidth(1);
            optionCard.setStrokeColor(getColor(R.color.material_dynamic_outline));
        }
    }

    private void loadAdvertisements() {
        loadInterstitialAd();
        loadRewardedAd();
    }

    //region Game Logic Methods
    private void loadCurrentQuestion() {
        if (currentQuestionIndex < MAX_LEVEL && currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            displayQuestion(currentQuestion);
            checkForAdTrigger();
        } else {
            endGameSession();
        }
    }

    private void displayQuestion(Question question) {
        questionText.setText(question.getQuestionText());
        levelText.setText(String.format("Level %d • Question %d/%d", 
                (currentQuestionIndex / 10) + 1, 
                (currentQuestionIndex % 10) + 1, 
                10));
        
        // Update progress bar
        progressBar.setProgress(((currentQuestionIndex % 10) + 1) * 10);
        
        resetOptionCards();
        
        // Set options to the cards
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size() && i < optionsGrid.getChildCount(); i++) {
            MaterialCardView optionCard = (MaterialCardView) optionsGrid.getChildAt(i);
            TextView optionText = (TextView) optionCard.getChildAt(0);
            optionText.setText(options.get(i));
            optionCard.setTag(null); // Clear previous selection
        }

        // Animation for question transition
        questionCard.animate().alpha(0f).setDuration(200).withEndAction(() -> {
            questionText.setText(question.getQuestionText());
            questionCard.animate().alpha(1f).setDuration(200).start();
        }).start();
    }

    private void handleAnswerSubmission() {
        String selectedAnswer = null;
        
        // Find which option was selected
        for (int i = 0; i < optionsGrid.getChildCount(); i++) {
            MaterialCardView optionCard = (MaterialCardView) optionsGrid.getChildAt(i);
            if (optionCard.getTag() != null) {
                selectedAnswer = optionCard.getTag().toString();
                break;
            }
        }

        if (selectedAnswer == null) {
            showToast("Please select an answer first!");
            return;
        }

        processAnswer(selectedAnswer);
    }

    private void processAnswer(String userAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (userAnswer.equals(currentQuestion.getCorrectAnswer())) {
            score++;
            coinCounterValue += 10; // Reward coins for correct answer
            updateCoinDisplay();
            showToast("Correct! +10 coins");
        } else {
            showToast("Wrong! Correct answer: " + currentQuestion.getCorrectAnswer());
        }

        currentQuestionIndex++;
        saveGameProgress();
        loadCurrentQuestion();
    }

    private void endGameSession() {
        questionText.setText("Congratulations! You've completed all levels!");
        levelText.setText("Game Completed!");
        optionsGrid.setVisibility(View.GONE);
        submitButton.setEnabled(false);
        hintButton.setEnabled(false);
    }
    //endregion

    //region Hint System
    private void handleHintRequest() {
        if (hintCounter > 0) {
            showQuestionHint();
            hintCounter--;
            saveHintCount();
            updateHintDisplay();
        } else {
            showToast("Not enough hints! You can earn more by watching ads or using coins.");
            showHintAcquisitionOptions();
        }
    }

    private void showQuestionHint() {
        LayoutInflater inflater = getLayoutInflater();
        View hintView = inflater.inflate(R.layout.custom_popup_hint, null);
        PopupWindow hintPopup = new PopupWindow(hintView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        TextView hintText = hintView.findViewById(R.id.hint_text);
        hintText.setText("Hint: " + questions.get(currentQuestionIndex).getHint());

        Button closeButton = hintView.findViewById(R.id.close_popup_button);
        closeButton.setOnClickListener(v -> hintPopup.dismiss());

        hintPopup.showAtLocation(hintView, Gravity.CENTER, 0, 0);
        hintView.setAlpha(0f);
        hintView.animate().alpha(1f).setDuration(300).start();
    }

    private void showHintAcquisitionOptions() {
        LayoutInflater inflater = getLayoutInflater();
        View optionsView = inflater.inflate(R.layout.custom_hint_options, null);
        PopupWindow optionsPopup = new PopupWindow(optionsView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        Button watchAdButton = optionsView.findViewById(R.id.watch_ad_button);
        Button useCoinsButton = optionsView.findViewById(R.id.use_coins_button);
        Button cancelButton = optionsView.findViewById(R.id.cancel_button);

        watchAdButton.setOnClickListener(v -> {
            handleAdRewardRequest();
            optionsPopup.dismiss();
        });

        useCoinsButton.setOnClickListener(v -> {
            if (coinCounterValue >= 50) {
                coinCounterValue -= 50;
                hintCounter++;
                updateCoinDisplay();
                updateHintDisplay();
                saveGameProgress();
                showToast("1 hint purchased for 50 coins");
            } else {
                showToast("Not enough coins!");
            }
            optionsPopup.dismiss();
        });

        cancelButton.setOnClickListener(v -> optionsPopup.dismiss());

        optionsPopup.showAtLocation(optionsView, Gravity.CENTER, 0, 0);
    }

    private void updateHintDisplay() {
        hintCounterText.setText(String.format("Hints: %d", hintCounter));
        hintButton.setEnabled(hintCounter > 0);
    }

    private void updateCoinDisplay() {
        coinText.setText(String.valueOf(coinCounterValue));
    }
    //endregion

    //region Ad Management
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-4186599691041011/7680150324", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        configureInterstitialCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        interstitialAd = null;
                    }
                });
    }

    private void configureInterstitialCallbacks() {
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    pauseAudio();
                    interstitialAd = null;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    resumeAudio();
                    loadInterstitialAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError error) {
                    resumeAudio();
                    loadInterstitialAd();
                }
            });
        }
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-4186599691041011/8469643922", adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        configureRewardedAdCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        rewardedAd = null;
                    }
                });
    }

    private void configureRewardedAdCallbacks() {
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    pauseAudio();
                    rewardedAd = null;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    resumeAudio();
                    loadRewardedAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError error) {
                    resumeAudio();
                    loadRewardedAd();
                }
            });
        }
    }

    private void handleAdRewardRequest() {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                hintCounter = Math.min(hintCounter + 2, MAX_HINTS); // Give 2 hints for watching ad
                coinCounterValue += 25; // Bonus coins for watching ad
                saveGameProgress();
                updateHintDisplay();
                updateCoinDisplay();
                loadRewardedAd();
                showToast("You earned 2 hints and 25 coins!");
            });
        } else {
            showToast("Ad not ready. Please try again later.");
            loadRewardedAd();
        }
    }

    private void checkForAdTrigger() {
        if (interstitialAd != null && currentQuestionIndex % 5 == 0 && currentQuestionIndex != 0) {
            interstitialAd.show(this);
        }
    }
    //endregion

    //region Audio Management
    private void playNextTrack() {
        try {
            currentMusicIndex = (currentMusicIndex == 1) ? 2 : 1;
            int resId = (currentMusicIndex == 1) ? R.raw.background_music1 : R.raw.background_music2;

            if (backgroundMusic != null) {
                backgroundMusic.release();
            }

            backgroundMusic = MediaPlayer.create(this, resId);
            if (backgroundMusic != null) {
                backgroundMusic.setOnCompletionListener(mp -> playNextTrack());
                if (isAudioEnabled()) {
                    backgroundMusic.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAudioEnabled() {
        return audioManager != null &&
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0;
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
    //endregion

    //region Persistence
    private void saveGameProgress() {
        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);
        prefs.edit()
                .putInt("current_level", currentQuestionIndex + 1)
                .putInt(PREF_HINT_COUNT, hintCounter)
                .putInt(PREF_COIN_COUNT, coinCounterValue)
                .apply();
    }

    private int loadSavedProgress() {
        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);
        return prefs.getInt("current_level", 1);
    }

    private void saveHintCount() {
        saveGameProgress(); // Now handled in saveGameProgress
    }
    //endregion

    //region Lifecycle Management
    @Override
    protected void onPause() {
        super.onPause();
        pauseAudio();
        saveGameProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    private void releaseResources() {
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
        if (adView != null) {
            adView.destroy();
        }
    }
    //endregion

    //region Utility Methods
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleFatalError(String errorMessage) {
        showToast(errorMessage);
        finishAffinity();
    }
    //endregion
}
