package id.nurazlib.ztebaktebakan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class Question {
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public Question(String questionText, String[] options, String correctAnswer) {
        if (questionText == null || questionText.isEmpty()) {
            throw new IllegalArgumentException("Teks pertanyaan tidak boleh kosong.");
        }
        if (options == null || options.length < 2) {
            throw new IllegalArgumentException("Pilihan jawaban harus memiliki minimal 2 pilihan.");
        }
        if (correctAnswer == null || correctAnswer.isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar tidak boleh kosong.");
        }
        if (!Arrays.asList(options).contains(correctAnswer)) {
            throw new IllegalArgumentException("Jawaban benar harus ada di dalam pilihan jawaban.");
        }

        this.questionText = questionText;
        this.options = new ArrayList<>(Arrays.asList(options));
        this.correctAnswer = correctAnswer;

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

    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equals(answer);
    }
}
