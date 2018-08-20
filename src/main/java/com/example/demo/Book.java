package com.example.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

class Book {
    private String title;
    private String author;
    private int price;
    private JsonObject JsonRepresentation = new JsonObject();

    public void saveToDatabase(MongoClient mongoClient, HttpServerResponse response){
        mongoClient.save("books", this.JsonRepresentation, res -> {
            if(res.succeeded()){
                response.end("Added to Database!");
            }
            else{
                res.cause().printStackTrace();
            }
        });
    }
    public Book(String title, String author, int price){
        this.title = title;
        this.author = author;
        this.price = price;
        JsonRepresentation.put("title", title);
        JsonRepresentation.put("author", author);
        JsonRepresentation.put("price", price);
    }
    
    public static Book parseJSON(JsonObject jsonObject){
        String bookTitle = (String) jsonObject.getValue("title");
        String bookAuthor = (String) jsonObject.getValue("author");
        int bookPrice = Integer.parseInt((String)jsonObject.getValue("price"));
        return new Book(bookTitle, bookAuthor, bookPrice);
    }
}