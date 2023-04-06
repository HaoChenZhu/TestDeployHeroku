package com.nebrija.tfg.pruebas.config;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MQTTConfig {
    private String host;
    private String clientid;
    private String mqttUserName;
    private String mqttPassword;
    private String caFilePath;
    private boolean cleanSession;

    public MQTTConfig(@Value("${qrnotify.broker.host}") String host, @Value("${qrnotify.broker.user}") String mqttUserName, @Value("${qrnotify.broker.password}") String mqttPassword, @Value("${qrnotify.broker.cert}") String caFilePath) {
        this.host = host;
        this.mqttUserName = mqttUserName;
        this.mqttPassword = mqttPassword;
        this.caFilePath = caFilePath;
        this.clientid = UUID.randomUUID().toString();
        this.cleanSession = true;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getCaFilePath() {
        return caFilePath;
    }

    public void setCaFilePath(String caFilePath) {
        this.caFilePath = caFilePath;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }
}
