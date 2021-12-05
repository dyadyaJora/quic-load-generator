package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.scenarios.generator.BaseScenario;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;

public class HttpWrapper extends BaseProtocolWrapper {
    public HttpWrapper(BaseScenario scenario) {
        super(scenario);
    }

    @Override
    protected HttpClient buildHttpClient() {
        HttpClient.Builder builder = HttpClient.newBuilder();
        if (this.scenario.getOptions().isDisableCertificateVerification()) {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc;
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                return null;
            }
            builder.sslContext(sc);
        }
        return builder.build();
    }
}
