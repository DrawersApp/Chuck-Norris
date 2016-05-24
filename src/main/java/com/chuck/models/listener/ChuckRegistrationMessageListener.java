package com.chuck.models.listener;

import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.listeners.NewMessageListener;
import com.google.gson.Gson;
import org.drawers.bot.lib.*;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.chuck.models.operation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nishant.pathak on 20/05/16.
 */
public class ChuckRegistrationMessageListener implements NewMessageListener {
    private final PublisherImpl mPublisher;
    private final String mClientId;

    public ChuckRegistrationMessageListener(PublisherImpl publisher, String clientId) {
        this.mPublisher = publisher;
        this.mClientId = clientId;
    }

    private enum OperationChuck {
        Subscribe,
        UnSubscribe
    }

    private static DrawersBotString subscribe;
    private static DrawersBotString unSubscribe;
    private static final Gson gson = new Gson();
    private static Map<String, UserOperation> operations = new HashMap<>();

    static {
        subscribe = new DrawersBotString(
                Collections.singletonList(new BotStringElement(BotStringType.U, "Subscribe", null)),
                OperationChuck.Subscribe.name());
        unSubscribe = new DrawersBotString(
                Collections.singletonList(new BotStringElement(BotStringType.U, "UnSubscribe", null)),
                OperationChuck.UnSubscribe.name());

        operations.put(OperationChuck.Subscribe.name(), new Subscribe());
        operations.put(OperationChuck.UnSubscribe.name(), new UnSubscribe());
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(subscribe);
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(unSubscribe);
        System.out.println(DrawersBotStringHelp.getDrawersBotStringHelp().toJsonString());

    }

    @Override
    public void receiveMessage(MqttChatMessage mqttChatMessage) {

        DrawersBotString drawersBotString = DrawersBotString.fromString(mqttChatMessage.message);

        Response response = operations.get(drawersBotString).operateUser(mqttChatMessage.senderUid, drawersBotString);

        MqttChatMessage reply = new MqttChatMessage(UUID.randomUUID().toString(),
                response.toUserString(), mClientId, ChatConstant.ChatType.TEXT, false);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(gson.toJson(reply).getBytes());
        mqttMessage.setQos(MqttChat.QOS);
        mPublisher.publish(mqttChatMessage.senderUid + MqttChat.NAMESPACE, mqttMessage, null, null);
    }

    @Override
    public void acknowledgeStanza(MqttChatMessage mqttChatMessage) {
        System.out.println("stanza acknowledge" + mqttChatMessage.toString());

    }
}
