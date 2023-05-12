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
    private final long intervalInDays;
    private final NotificationManChannelConfig config;
    private final NotificationMan.Builder builder;

    public Notification(Context context, String activity, String title, String description, long intervalInDays) {
        this.context = context;
        this.activity = "com.example.comp4521_fitness_app." + activity;
        this.title = title;
        this.description = description;
        this.intervalInDays = intervalInDays;

        this.config = new NotificationManChannelConfig(
                null,
                null,
                NotificationImportanceLevel.HIGH,
                true
        );

        long intervalInSeconds = TimeUnit.DAYS.toSeconds(intervalInDays);

        this.builder = new NotificationMan.Builder(
                context,
                activity,
                title,
                description,
                "",
                intervalInSeconds,
                NotificationTypes.TEXT.getType(),
                config
        );
    }

    public void fire() {
        builder.fire();
    }
}
