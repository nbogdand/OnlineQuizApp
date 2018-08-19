package com.example.bogdan.onlinequiz.Model;

public class QuestionScore {

    private String question_score;
    private String user;
    private String score;
    private String CategoryId;
    private String CategoryName;

    public QuestionScore(){}

    public QuestionScore(String question_score, String user, String score, String categoryId, String categoryName) {
        this.question_score = question_score;
        this.user = user;
        this.score = score;
        CategoryId = categoryId;
        CategoryName = categoryName;
    }

    public String getQuestion_score() {
        return question_score;
    }

    public void setQuestion_score(String question_score) {
        this.question_score = question_score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
