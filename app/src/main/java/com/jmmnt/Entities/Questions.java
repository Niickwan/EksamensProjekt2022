package com.jmmnt.Entities;

import java.util.ArrayList;

public class Questions {

    private String question;
    private int answer;
    private String comment;
    private ArrayList<String> images;

    public Questions(String question, int answer, String comment) {
        this.question = question;
        this.answer = answer;
        this.comment = comment;
        this.images = new ArrayList<>();
    }

    public Questions() {
        this.images = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Questions{" +
                "question='" + question + '\'' +
                ", answer=" + answer +
                ", comment='" + comment + '\'' +
                '}';
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(String imagePath) {
        this.images.add(imagePath);
    }
}
