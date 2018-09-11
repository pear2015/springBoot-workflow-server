package com.gs.workflow.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Locale;

/**
 * Created by xiaodiming on 2016/5/20.
 */
public class AppInit implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${language}")
    private String language;

    /**
     * onApplicationEvent
     *
     * @param event ContextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            setLocale();
        }
    }

    /**
     * Sets locale by config language.
     *
     * @return the locale
     */
    public String setLocale() {
        String[] lan = language.split("_");
        if (lan.length > 1) {
            Locale locale;
            locale = new Locale(lan[0], lan[1]);
            Locale.setDefault(locale);
        }
        return null;
    }
}
