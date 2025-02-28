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
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private TextView questionText, levelText, hintCounterText;
    private RadioGroup optionsGroup;
    private Button submitButton, hintButton, watchAdButton;
    
    // Game Logic
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private static final int MAX_LEVEL = 40;
    private static final int INITIAL_HINTS = 3;
    private static final int MAX_HINTS = 5;
    private int hintCounter = INITIAL_HINTS;
    
    // Audio Management
    private MediaPlayer backgroundMusic;
    private AudioManager audioManager;
    private int currentMusicIndex = 1;
    
    // Ad Management
    private AdView adView;
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
        optionsGroup = findViewById(R.id.options_group);
        submitButton = findViewById(R.id.submit_button);
        hintButton = findViewById(R.id.hint_button);
        watchAdButton = findViewById(R.id.watch_ad_button);
        adView = findViewById(R.id.adView);

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

        if (questions == null || questions.isEmpty()) {
            handleFatalError("No questions available");
            return;
        }

        updateHintDisplay();
        loadCurrentQuestion();
    }

    private void setupEventListeners() {
        submitButton.setOnClickListener(v -> handleAnswerSubmission());
        hintButton.setOnClickListener(v -> handleHintRequest());
        watchAdButton.setOnClickListener(v -> handleAdRewardRequest());
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
        levelText.setText(String.format("Level %d", currentQuestionIndex + 1));
        optionsGroup.removeAllViews();

        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            optionsGroup.addView(radioButton);
        }
    }

    private void handleAnswerSubmission() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            showToast("Please select an answer first!");
            return;
        }

        RadioButton selectedRadio = findViewById(selectedId);
        processAnswer(selectedRadio.getText().toString());
    }

    private void processAnswer(String userAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (userAnswer.equals(currentQuestion.getCorrectAnswer())) {
            score++;
            showToast("Correct!");
        } else {
            showToast("Wrong! Correct answer: " + currentQuestion.getCorrectAnswer());
        }

        currentQuestionIndex++;
        saveGameProgress();
        loadCurrentQuestion();
    }

    private void endGameSession() {
        questionText.setText("Stay tuned for updates!");
        levelText.setText("Game Completed!");
        optionsGroup.removeAllViews();
        submitButton.setEnabled(false);
        hintButton.setEnabled(false);
    }
    //endregion

    //region Hint System
    private void handleHintRequest() {
        if (hintCounter > 0) {
            showQuestionHint();
            hintCounter--;
            updateHintDisplay();
        } else {
            showToast("Watch an ad to get more hints!");
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
    }

    private void updateHintDisplay() {
        hintCounter = Math.min(Math.max(hintCounter, 0), MAX_HINTS);
        hintCounterText.setText(String.format("Hints: %d", hintCounter));
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

    private void handleAdRewardRequest() {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                hintCounter++;
                updateHintDisplay();
                loadRewardedAd();
            });
        } else {
            showToast("Ad not ready. Please try again later.");
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
        prefs.edit().putInt("current_level", currentQuestionIndex + 1).apply();
    }

    private int loadSavedProgress() {
        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);
        return prefs.getInt("current_level", 1);
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
