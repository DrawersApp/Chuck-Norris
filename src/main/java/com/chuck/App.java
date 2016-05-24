package com.chuck;

import com.chuck.models.entities.Joke;
import com.chuck.models.listener.ChuckRegistrationMessageListener;
import com.chuck.models.rest.RestDataSource;
import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.MqttProviderManager;
import com.google.gson.Gson;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.mqtt.DrawersBot;
import org.drawers.bot.util.SendMail;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rx.Observable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 */
public class App implements DrawersMessageListener {
    private static DrawersBot bot;
    private static App client;
    private static String clientId;
    private static String password;
    private static String adminEmail;

    public static Map<String, User> users = new ConcurrentHashMap<>();

    public App(String clientId, String password) {
        bot = new DrawersBot(clientId, password, this);
        MqttProviderManager.getInstanceFor(bot).addMessageListener(new ChuckRegistrationMessageListener(bot, clientId));
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java DrawersClientCli <clientId> <password> <admin-email-id>");
        } else {
            clientId = args[0];
            password = args[1];
            adminEmail = args[2];
            SendMail.getInstance().setAdminEmail(adminEmail);
            SendMail.getInstance().sendMail("Welcome to Drawers Bot", "Your bot is up and running now.");
            client = new App(clientId, password);
            client.startBot();
            Runtime.getRuntime().addShutdownHook(new Thread(client::stopBot));
        }
    }

    private void stopBot() {
        System.out.println("Good bye");
        bot.unsubscribe(clientId + MqttChat.NAMESPACE, null, null);
        bot.getExecutorService().shutdown();
    }

    private void startBot() {
        bot.start();

        timer.schedule(hourlyTask, 0l, 1000 * 10);

/*
        try {
            bot.getExecutorService().awaitTermination(100000000L, TimeUnit.SECONDS);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }
*/
    }

    Timer timer = new Timer();
    TimerTask hourlyTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " sending joke");
            client.sendJoke();
        }
    };

    private static RestDataSource restDataSource = new RestDataSource();
    private static Gson gson = new Gson();

    private void sendJoke() {
        Observable<List<Joke>> joke = restDataSource.getRandomJoke();

        joke.subscribe(jokes -> {
            MqttChatMessage reply = new MqttChatMessage(UUID.randomUUID().toString(),
                    jokes.get(0).toString(), clientId, ChatConstant.ChatType.TEXT, false);

            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(gson.toJson(reply).getBytes());
            mqttMessage.setQos(MqttChat.QOS);
            for(String userId: users.keySet()){
                bot.publish(userId + MqttChat.NAMESPACE, mqttMessage, null, null);
            }
        });

    }

    @Override
    public void onConnected() {
        // Bot connected
    }
}
