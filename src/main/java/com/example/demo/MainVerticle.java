package com.example.demo;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {
    MongoClient mongoClinet;
    JsonObject databaseConfig;

    @Override
    public void start() throws Exception {
        mongoClinet = MongoClient.createShared(vertx, getDatabaseConfig());

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/add").handler(this::addNewBook);
        server.requestHandler(router::accept).listen(8080);
    }

    private void addNewBook(RoutingContext routingContext){
        HttpServerResponse response = routingContext.response();
        JsonObject body =  routingContext.getBodyAsJson();
        
        Book unsavedBook = Book.parseJSON(body);
        unsavedBook.saveToDatabase(mongoClinet, response);
    }

    private JsonObject getDatabaseConfig(){
        if(this.databaseConfig == null){
            databaseConfig = new JsonObject();
            databaseConfig.put("db_name", "vertxBookStore");
            databaseConfig.put("connection_string", "mongodb://arasharbabi.com/");
        }
        return this.databaseConfig;
    }
}
