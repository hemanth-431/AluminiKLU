package com.example.aluminiklu.Model;

public class CommentModel {

    private String comment;
    private String publisher;
    public CommentModel(String comment,String publisher)
    {
        this.comment=comment;
        this.publisher=publisher;
    }

    public CommentModel(){

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
