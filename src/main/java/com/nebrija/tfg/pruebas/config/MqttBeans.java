package com.nebrija.tfg.pruebas.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

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
public class MqttBeans {

    @Value("${qrnotify.broker.host}")
    private String host;
    //    @Value("${etna.broker.clientId}")
//    private String clientId;
    @Value("${qrnotify.broker.user}")
    private String mqttUserName;
    @Value("${qrnotify.broker.password}")
    private String mqttPassword;
    @Value("${qrnotify.broker.cert}")
    private String caFilePath;
    public MqttClient mqttClient() {
        try{
            String clientId = UUID.randomUUID().toString();
            MqttClient client = new MqttClient(host, clientId, new MemoryPersistence());

            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setUserName(mqttUserName);
            Charset utf8 = StandardCharsets.UTF_8;
            byte[] bytes = mqttPassword.getBytes(utf8);
            options.setPassword(bytes);
            options.setCleanStart(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(90);
            options.setKeepAliveInterval(90);

          //  options.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE); // Agregue esta línea para desactivar la verificación del nombre de host en SSL/TLS

            log.info("Ruta certificado: " + caFilePath);
            SSLSocketFactory socketFactory = getSockFactory(caFilePath);
            options.setSocketFactory(socketFactory);
            log.info("Mqtt conectando en el host: " + host);
            client.connect(options);
            log.info("Conectado a mqtt");
            return client;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
            return null;
        }
    }

    private static SSLSocketFactory getSockFactory(String caFile) throws Exception{
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
