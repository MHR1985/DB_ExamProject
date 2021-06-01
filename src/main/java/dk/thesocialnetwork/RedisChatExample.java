package dk.thesocialnetwork;

import dk.thesocialnetwork.logic.ChatClient;
import dk.thesocialnetwork.logic.ChatClientHandler;

public class RedisChatExample {

    private static String[] names = new String[]{"martin", "kenneth", "simon","frederik"};

    public static void createChats(){
        ChatClientHandler handler = new ChatClientHandler();
        ChatClient client1 = handler.createClient(names[0],names[1]);
        ChatClient client2 = handler.createClient(names[1],names[0]);
        ChatClient client3 = handler.createClient(names[2],names[3]);
        ChatClient client4 = handler.createClient(names[3],names[2]);
        generateChat(client1, client2);
        generateChat(client3, client4);


    }

    private static void generateChat(ChatClient client3, ChatClient client4) {
        client3.sendMessage("Hi there!");
        client4.sendMessage("Hello back!");
        client3.sendMessage("How are you?");
        client4.sendMessage("I am good, how are you?");
        client3.sendMessage("I am also good, thanks for asking");
        client4.sendMessage("You are welcome :)");
        client3.sendMessage("Nice weather today");
        client4.sendMessage("Yes I agree. Very hot!");
        client3.sendMessage("I think I will go to the beach, want to come?");
        client4.sendMessage("Yes what a great idea! I would love to!");
        client3.sendMessage("That is very nice, see you there!");
    }
}
