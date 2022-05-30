package com.jmmnt.Entities;

public class Questions {

    private String question;
    private int answer;
    private String comment;

    public Questions(String question, int answer, String comment) {
        this.question = question;
        this.answer = answer;
        this.comment = comment;
    }

    public Questions() {
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
}
