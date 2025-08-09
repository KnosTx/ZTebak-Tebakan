package id.nurazlib.ztebaktebakan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {
    private final String questionText;
    private final List<String> options;
    private final String correctAnswer;
    private final String hint;

    public Question(String questionText, String[] options, String correctAnswer, String hint) {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Teks pertanyaan tidak boleh kosong.");
        }
        if (options == null || options.length < 2) {
            throw new IllegalArgumentException("Pilihan jawaban harus memiliki minimal 2 pilihan.");
        }
        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar tidak boleh kosong.");
        }
        if (hint == null) {
            throw new IllegalArgumentException("Hint tidak boleh null.");
        }

        this.questionText = questionText.trim();
        this.correctAnswer = correctAnswer.trim();
        this.hint = hint.trim();

        List<String> trimmedOptions = new ArrayList<>();
        boolean matchFound = false;
        for (String option : options) {
            if (option != null) {
                String trimmedOption = option.trim();
                trimmedOptions.add(trimmedOption);
                if (trimmedOption.equalsIgnoreCase(this.correctAnswer)) {
                    matchFound = true;
                }
            }
        }

        if (!matchFound) {
            throw new IllegalArgumentException("Jawaban benar '" + this.correctAnswer + "' tidak ditemukan di dalam pilihan jawaban yang diberikan untuk pertanyaan: '" + this.questionText + "'");
        }

        this.options = trimmedOptions;
        Collections.shuffle(this.options);
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getHint() {
        return hint;
    }

    public boolean isCorrectAnswer(String answer) {
        return answer != null && correctAnswer.equalsIgnoreCase(answer.trim());
    }
}
            
