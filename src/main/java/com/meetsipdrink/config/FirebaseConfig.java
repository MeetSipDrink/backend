package com.meetsipdrink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init(){
        try{
            InputStream serviceAccount = new ClassPathResource("worryboxFirebaseKey.json").getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { // FirebaseApp이 이미 초기화되어 있지 않은 경우에만 초기화 실행
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
