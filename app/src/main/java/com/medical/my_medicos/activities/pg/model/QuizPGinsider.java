package com.medical.my_medicos.activities.pg.model;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class QuizPGinsider implements Item, Serializable {

    private String question, optionA, optionB, optionC, optionD, correctAnswer;

    private String selectedOption; // Add this field

    private String description; // Added field

    private String image;

    public QuizPGinsider(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer, String image, String description) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.image = image;
        this.description = description;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOptionB() {
        return optionB;
    }


    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public boolean isCorrect() {
        return correctAnswer.equals(selectedOption);
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public BigDecimal getItemPrice() {
        return null;
    }

    @Override
    public String getItemName() {
        return question;
    }
}