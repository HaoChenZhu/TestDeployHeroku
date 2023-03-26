package com.nebrija.tfg.pruebas.config;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

@Slf4j
@Configuration
public class MqttBeans implements MqttCallback {

    private String host;
    private String mqttUserName;
    private String mqttPassword;
    private String caFilePath;
    private MemoryPersistence memoryPersistence;
    private MqttConnectionOptions mqttConnectionOptions;
    private MqttAsyncClient mqttAsyncClient;

    public MqttBeans(@Value("${qrnotify.broker.host}") String host, @Value("${qrnotify.broker.user}") String mqttUserName, @Value("${qrnotify.broker.password}") String mqttPassword, @Value("${qrnotify.broker.cert}") String caFilePath) {
        this.host = host;
        this.mqttUserName = mqttUserName;
        this.mqttPassword = mqttPassword;
        this.caFilePath = caFilePath;
        try {
            memoryPersistence = new MemoryPersistence();
            mqttConnectionOptions = new MqttConnectionOptions();
            mqttAsyncClient = new MqttAsyncClient(host, UUID.randomUUID().toString(), memoryPersistence);
            clientConnect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public void clientConnect() {
        try {
            mqttConnectionOptions.setUserName(mqttUserName);
            Charset utf8 = StandardCharsets.UTF_8;
            byte[] bytes = mqttPassword.getBytes(utf8);
            mqttConnectionOptions.setPassword(bytes);
            mqttConnectionOptions.setCleanStart(true);
            mqttConnectionOptions.setAutomaticReconnect(true);
           /* mqttConnectionOptions.setConnectionTimeout(90);
            mqttConnectionOptions.setKeepAliveInterval(90);*/
            log.info("Ruta certificado: " + caFilePath);
            SSLSocketFactory socketFactory = getSockFactory(caFilePath);
            mqttConnectionOptions.setSocketFactory(socketFactory);
            log.info("Mqtt conectando en el host: " + host);
            mqttAsyncClient.connect(mqttConnectionOptions);
            log.info("Conectado a mqtt");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos) {
        try {
            mqttAsyncClient.subscribe(topic, qos);
            log.info("Subscrito a topic: " + topic);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message, int qos, Long expirationTime) {
        try {
            //IMqttToken token = null;
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(false);
            mqttMessage.setDuplicate(false);
            MqttProperties mqttProperties = new MqttProperties();
            mqttProperties.setMessageExpiryInterval(expirationTime);
            mqttMessage.setProperties(mqttProperties);
            mqttAsyncClient.publish(topic, mqttMessage);
            //Wait for the publish to complete
            //token.waitForCompletion();
            log.info("Publicado en topic: " + topic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public void disconnect(){
        try{
            if(mqttAsyncClient != null){
                mqttAsyncClient.disconnect();
                log.info("Desconectado de mqtt");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
    @Override
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
        log.info("connection lost");
        disconnect();
    }

    @Override
    public void mqttErrorOccurred(MqttException e) {
        log.info("mqttErrorOccurred: "+ e.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("Message received: Topic: " + topic + " - Message: " + new String(mqttMessage.getPayload()));
        System.out.println("Message received: Topic: " + topic + " - Message: " + new String(mqttMessage.getPayload()));

    }

    @Override
    public void deliveryComplete(IMqttToken iMqttToken) {
        log.info("deliveryComplete");
    }

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("connectComplete");
    }

    @Override
    public void authPacketArrived(int i, MqttProperties mqttProperties) {
        log.info("authPacketArrived");
    }

    private static SSLSocketFactory getSockFactory(String caFile) throws Exception {
        try {
            Security.addProvider(new BouncyCastleProvider());

            // load CA certificate
            X509Certificate caCert = null;
            InputStream fis = new FileInputStream(caFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            int count = 0;
            // CA certificate is used to authenticate server
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null);

            try {
                while (bis.available() > 0) {
                    caCert = (X509Certificate) cf.generateCertificate(bis);
                    caKs.setCertificateEntry("ca-certificate" + count, caCert);
                    count++;
                }
            } catch (Exception e) {
                log.error("Problemas con la lectura del certificado");
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(caKs);

            // finally, create SSL socket factoryt
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, tmf.getTrustManagers(), null);

            return context.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
