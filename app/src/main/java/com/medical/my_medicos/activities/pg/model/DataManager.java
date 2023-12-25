package com.medical.my_medicos.activities.pg.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {
    private static DataManager instance;

    private ArrayList<QuizPGinsider> quizList;
    private HashMap<String, String> selectedOptionsMap; // Map to store selected options by question ID

    private DataManager() {
        // Private constructor to prevent instantiation
        quizList = new ArrayList<>();
        selectedOptionsMap = new HashMap<>();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<QuizPGinsider> getQuizList() {
        return quizList;
    }

    public void setQuizList(ArrayList<QuizPGinsider> quizList) {
        this.quizList = quizList;
    }

    public String getSelectedOption(String questionId) {
        return selectedOptionsMap.get(questionId);
    }

    public void setSelectedOption(String questionId, String selectedOption) {
        selectedOptionsMap.put(questionId, selectedOption);
    }
}

