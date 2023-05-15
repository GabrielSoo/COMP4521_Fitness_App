package com.example.comp4521_fitness_app.utilities;

import android.content.Context;

import com.notificationman.library.NotificationMan;
import com.notificationman.library.config.NotificationManChannelConfig;
import com.notificationman.library.model.NotificationImportanceLevel;
import com.notificationman.library.model.NotificationTypes;

import java.util.concurrent.TimeUnit;

public class Notification {
    private final Context context;
    private final String activity;
    private final String title;
    private final String description;
    private final long intervalInSeconds;
    private final NotificationManChannelConfig config;
    private NotificationMan.Builder builder;

    public Notification(Context context, String activity, String title, String description, long intervalInSeconds) {
        this.context = context;
        this.activity = "com.example.comp4521_fitness_app." + activity;
        this.title = title;
        this.description = description;
        this.intervalInSeconds = intervalInSeconds;

        this.config = new NotificationManChannelConfig(
                null,
                null,
                NotificationImportanceLevel.HIGH,
                true
        );

        this.builder = new NotificationMan.Builder(
                context,
                this.activity,
                title,
                description,
                "",
                intervalInSeconds,
                NotificationTypes.TEXT.getType(),
                config
        );
    }

    public void cancelNotifiation() {
        builder = null;
    }

    public void fire() {
        builder.fire();
    }
}
