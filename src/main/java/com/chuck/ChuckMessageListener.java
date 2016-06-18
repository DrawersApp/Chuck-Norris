package com.chuck;

import com.chuck.models.listener.UserOperation;
import com.chuck.models.operation.Subscribe;
import com.chuck.models.operation.UnSubscribe;
import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.group.GroupMessage;
import com.drawers.dao.packets.listeners.GroupMessageListener;
import com.drawers.dao.packets.listeners.NewMessageListener;
import com.google.gson.Gson;
import org.drawers.bot.lib.*;
import org.drawers.bot.mqtt.DrawersBot;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nishant.pathak on 18/06/16.
 */
public class ChuckMessageListener implements NewMessageListener, GroupMessageListener {

    private enum OperationChuck {
        Subscribe,
        UnSubscribe
    }

    private static DrawersBotString subscribe;
    private static DrawersBotString unSubscribe;
    private static final Gson gson = new Gson();
    private static Map<String, UserOperation> operations = new HashMap<>();

    static {
        List<BotStringElement> subscribeElements = new LinkedList<>();
        subscribeElements.add(new BotStringElement(BotStringType.U, "Subscribe"));
        subscribeElements.add(new BotStringElement(BotStringType.S, "me", null));
        subscribe = new DrawersBotString(subscribeElements, OperationChuck.Subscribe.name());


        unSubscribe = new DrawersBotString(
                Collections.singletonList(new BotStringElement(BotStringType.U, "UnSubscribe")),
                OperationChuck.UnSubscribe.name());

        List<BotStringElement> unSubscribeElements = new LinkedList<>();

        unSubscribeElements.add(new BotStringElement(BotStringType.U, "Un-subscribe"));
        unSubscribeElements.add(new BotStringElement(BotStringType.S, "me", null));

        unSubscribe = new DrawersBotString(unSubscribeElements, OperationChuck.UnSubscribe.name());

        operations.put(OperationChuck.Subscribe.name(), new Subscribe());
        operations.put(OperationChuck.UnSubscribe.name(), new UnSubscribe());
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(subscribe);
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(unSubscribe);
        System.out.println(DrawersBotStringHelp.getDrawersBotStringHelp().toJsonString());

    }

    private final DrawersBot bot;
    private final String clientId;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public ChuckMessageListener(DrawersBot bot, String clientId) {
        this.bot = bot;
        this.clientId = clientId;
    }

    @Override
    public void receiveMessage(MqttChatMessage mqttChatMessage) {
        executorService.submit((Runnable) () -> {
            if (mqttChatMessage.chatType != ChatConstant.ChatType.QA) {
                return;
            }
            String message = null;
            try {
                message = URLDecoder.decode(mqttChatMessage.message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (message == null) {
                return;
            }
            DrawersBotString drawersBotString = DrawersBotString.fromString(message);
            Response response = operations.get(drawersBotString.getOperationsType()).operateUser(mqttChatMessage.senderUid, drawersBotString);

            String responseString = null;
            try {
                responseString = URLEncoder.encode(response.toUserString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (responseString == null) {
                return;
            }
            MqttChat mqttChat = new MqttChat(mqttChatMessage.senderUid, UUID.randomUUID().toString(), responseString, false, ChatConstant.ChatType.TEXT, clientId);
            mqttChat.sendStanza(bot);
        });
    }

    @Override
    public void acknowledgeStanza(MqttChatMessage mqttChatMessage) {

    }

    @Override
    public void messageSendAck(GroupMessage.GroupMessageContainer groupMessageContainer) {

    }

    @Override
    public void receiveMessage(GroupMessage.GroupMessageContainer groupMessageContainer, String s) {

    }
}
