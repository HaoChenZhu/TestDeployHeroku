package com.nebrija.tfg.pruebas.config;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.eclipse.paho.mqttv5.client.*;

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

@Component
@Slf4j
public class MqttConnect {

    @Autowired
    private MQTTConfig mqttConfig;

    public MqttConnect(MQTTConfig mqttConfig) {
        this.mqttConfig = mqttConfig;
    }

    public MqttConnectionOptions getOptions() {
        try {
            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setUserName(mqttConfig.getMqttUserName());
            Charset utf8 = StandardCharsets.UTF_8;
            byte[] bytes = mqttConfig.getMqttPassword().getBytes(utf8);
            options.setPassword(bytes);
            options.setCleanStart(mqttConfig.isCleanSession());
            SSLSocketFactory socketFactory = getSockFactory(mqttConfig.getCaFilePath());
            options.setSocketFactory(socketFactory);
            return options;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public MqttConnectionOptions getOptions(MqttConnectionOptions mqttConnectionOptions) {
        try {
            mqttConnectionOptions.setUserName(mqttConfig.getMqttUserName());
            Charset utf8 = StandardCharsets.UTF_8;
            byte[] bytes = mqttConfig.getMqttPassword().getBytes(utf8);
            mqttConnectionOptions.setPassword(bytes);
            mqttConnectionOptions.setCleanStart(mqttConfig.isCleanSession());
            SSLSocketFactory socketFactory = getSockFactory(mqttConfig.getCaFilePath());
            mqttConnectionOptions.setSocketFactory(socketFactory);
            return mqttConnectionOptions;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
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
