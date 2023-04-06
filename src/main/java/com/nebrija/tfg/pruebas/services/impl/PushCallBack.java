package com.nebrija.tfg.pruebas.services.impl;

import com.nebrija.tfg.pruebas.config.MqttBeans;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
@Slf4j
public class PushCallBack implements MqttCallback {

    private MqttBeans mqttServer;

    public PushCallBack(MqttBeans mqttServer) {
        this.mqttServer = mqttServer;
    }

   /* public void ConnectionLost(Throwable cause) {
        System.out.println("Connection lost!");
        mqttServer.subscribeConnect();
        while (true){
            try {
                Thread.sleep(1000);
                break;
            } catch (InterruptedException e) {
                continue;
            }
        }
        cause.printStackTrace();
    }*/
    @Override
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
        log.info("connection lost");
    }

    @Override
    public void mqttErrorOccurred(MqttException e) {
        log.info("mqttErrorOccurred: "+ e.getMessage());

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Message arrived 2: "+s+" - "+new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttToken iMqttToken) {
        System.out.println("Delivery complete: "+iMqttToken.isComplete());
    }

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("connectComplete");
    }

    @Override
    public void authPacketArrived(int i, MqttProperties mqttProperties) {
        log.info("authPacketArrived");
    }
}
