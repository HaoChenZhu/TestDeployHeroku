package com.nebrija.tfg.pruebas.config;

import com.nebrija.tfg.pruebas.services.impl.PushCallBack;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.springframework.stereotype.Component;

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


@Slf4j
@Component
public class MqttBeans {

    private String host;
    private String mqttUserName;
    private String mqttPassword;
    private String caFilePath;
    private String clientId;
    private MemoryPersistence memoryPersistence;
    private MqttConnectionOptions mqttConnectionOptions;
    private MqttAsyncClient mqttAsyncClient;
    private String messageReceived;



    public MqttBeans(@Value("${qrnotify.broker.host}") String host, @Value("${qrnotify.broker.user}") String mqttUserName, @Value("${qrnotify.broker.password}") String mqttPassword, @Value("${qrnotify.broker.cert}") String caFilePath, @Value(value = "di") String clientId) {
        this.host = host;
        this.mqttUserName = mqttUserName;
        this.mqttPassword = mqttPassword;
        this.caFilePath = caFilePath;
        try {
            memoryPersistence = new MemoryPersistence();
            mqttConnectionOptions = new MqttConnectionOptions();
            log.info("Client ID: " + clientId);
            mqttAsyncClient = new MqttAsyncClient(host, clientId, memoryPersistence);
            log.info("Mqtt conectando en el host: " + host);
            clientConnect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public void clientConnect() {
        try {
            mqttConnectionOptions.setUserName(this.mqttUserName);
            Charset utf8 = StandardCharsets.UTF_8;
            byte[] bytes = this.mqttPassword.getBytes(utf8);
            mqttConnectionOptions.setPassword(bytes);
            mqttConnectionOptions.setCleanStart(true);
            mqttConnectionOptions.setAutomaticReconnect(true);
            mqttConnectionOptions.setConnectionTimeout(90);
            mqttConnectionOptions.setKeepAliveInterval(90);
            log.info("Ruta certificado: " + caFilePath);
            SSLSocketFactory socketFactory = getSockFactory(caFilePath);
            mqttConnectionOptions.setSocketFactory(socketFactory);
            log.info("Mqtt conectando en el host: " + host);
            mqttAsyncClient.connect(mqttConnectionOptions);
            log.info("Conectado a mqtt");
            Thread.sleep(5000); // wait until connection is complete
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();

        }
    }
    public String getClientId() {
        return mqttAsyncClient.getClientId();
    }
    public void subscribe(String topic, int qos) {
        try {
                mqttAsyncClient.subscribe(topic, qos);
                mqttAsyncClient.setCallback( new PushCallBack(MqttBeans.this));
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

    public String getMessageReceived() {
        return messageReceived;
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
