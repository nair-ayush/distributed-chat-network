package com.distributed.db;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import com.distributed.models.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

public class DB {
  private static DB instance = null;
  private MongoDatabase database;

  DB() {
    String mongoUri = System.getenv("DISTRIBUTED_MONGODB_URI");
    String mongoPassString = System.getenv("DISTRIBUTED_MONGODB_PASSWORD");

    if (mongoUri != null && mongoPassString != null) {
      mongoUri = mongoUri.replace("<password>", mongoPassString);
    }

    try {
      MongoClient mongoClient = MongoClients.create(mongoUri);
      database = mongoClient.getDatabase(System.getenv("DISTRIBUTED_MONGODB_DATABASE"));
    } catch (Exception e) {
      System.out.println("Error connecting to MongoDB");
      e.printStackTrace();
    }
  }

  public static DB getInstance() {
    if (instance == null) {
      instance = new DB();
    }
    return instance;
  }

  public boolean isConnected() {
    return database != null;
  }

  public boolean doesEmailExist(String email) {

    MongoCollection<Document> col = database.getCollection("users");
    Document doc = col.find(eq("email", email)).first();
    return doc != null;
  }

  public void createUser(String email, String name) {
    MongoCollection<Document> col = database.getCollection("users");
    Document doc = new Document("email", email)
        .append("name", name)
        .append("createdOn", new Date())
        .append("friends", new ArrayList<String>());
    col.insertOne(doc);
  }

  public void addFriend(String email, String friendEmail) {
    MongoCollection<Document> col = database.getCollection("users");
    col.updateOne(eq("email", email), Updates.push("friends", friendEmail));
    col.updateOne(eq("email", friendEmail), Updates.push("friends", email));
  }

  public ArrayList<User> getFriends(String email) {
    MongoCollection<Document> col = database.getCollection("users");
    Document doc = col.find(eq("email", email)).first();
    ArrayList<String> friendEmails = (ArrayList<String>) doc.get("friends");
    ArrayList<User> friends = new ArrayList<User>();
    for (String friendEmail : friendEmails) {
      Document friendDoc = col.find(eq("email", friendEmail)).first();
      friends.add(new User(friendDoc.getString("email"), friendDoc.getString("email"), friendDoc.getString("name")));
    }
    return friends;
  }

  public void addChatMessage(String senderEmail, String receiverEmail, String message, long timestamp) {
    MongoCollection<Document> col = database.getCollection("chats");
    // create string key using sender and receiver emails to ensure uniqueness
    String key = senderEmail.compareTo(receiverEmail) < 0 ? senderEmail + "|" + receiverEmail
        : receiverEmail + "|" + senderEmail;
    // check if document exists
    Document doc = col.find(eq("key", key)).first();
    Document messageDoc = new Document("sender", senderEmail)
        .append("message", message)
        .append("timestamp", timestamp);
    if (doc == null) {
      ArrayList<Document> messages = new ArrayList<Document>();
      messages.add(messageDoc);
      doc = new Document("key", key)
          .append("messages", messages);
      col.insertOne(doc);
    } else {
      col.updateOne(eq("key", key), Updates.push("messages", messageDoc));
    }
  }
}
