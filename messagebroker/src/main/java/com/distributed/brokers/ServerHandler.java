package com.distributed.brokers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.distributed.models.Chat;
import com.distributed.models.ChatMessage;
import com.distributed.models.FriendMessage;
import com.distributed.models.Message;
import com.distributed.models.User;

public class ServerHandler implements Runnable {
  private Socket socket;
  private ObjectInputStream inStream;

  public ServerHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      inStream = new ObjectInputStream(this.socket.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      while (true) {
        Message msg = (Message) inStream.readObject();
        if (msg == null) {
          socket.close();
          return;
        }

        User sender = msg.getSender();
        ClientCache cache = ClientCache.getInstance();
        switch (msg.getType()) {
          case GET_FRIENDS_SUCCESS:
          case ADD_FRIEND_SUCCESS:
          case ADD_FRIEND_FAILURE:
            ClientHandler cH = cache.getClient(sender.getEmail());
            cH.sendFriendResponse((FriendMessage) msg);
            break;

          case CHAT_MESSAGE:
            ChatMessage cMsg = (ChatMessage) msg;
            System.out.println("MessageBroker : Server (Chat):: " + cMsg);
            ArrayList<User> receivers = new ArrayList<User>();
            receivers.add(cMsg.getReceiver());
            receivers.add(cMsg.getSender());
            for (User receiver : receivers) {
              ClientHandler cHandler = cache.getClient(receiver.getEmail());
              if (cHandler == null) {
                // cache.addClient(receiver.getEmail(), new ClientHandler(receiver));
                // cHandler = cache.getClient(receiver.getEmail());
                continue;
              }
              cHandler.sendChatMessage(cMsg);
            }
            break;

          case GET_CHAT_MESSAGES_SUCCESS:
            Chat chatMsg = (Chat) msg;
            System.out.println("MessageBroker : Server (Chat):: " + chatMsg);
            ClientHandler cHandler = cache.getClient(chatMsg.getSender().getEmail());
            cHandler.sendChat(chatMsg);
            break;

          default:
            break;
        }

      }
    } catch (IOException e) {
      System.out.println("ServerHandler : Server disconnected");
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      System.out.println("ServerHandler : Server disconnected");
      e.printStackTrace();
    }

  }
}
