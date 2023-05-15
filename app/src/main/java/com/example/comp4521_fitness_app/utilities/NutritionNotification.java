package com.example.comp4521_fitness_app.utilities;

import android.content.Context;
import android.util.TimeUtils;

import java.util.concurrent.TimeUnit;

public class NutritionNotification {
    private static Notification nutritionNotification;

    public static void startNotification(Context context) {
        if (nutritionNotification == null) {
            nutritionNotification = new Notification(
                    context,
                    "NutritionManagementActivity",
                    "Nutrition Log Reminder",
                    "Please log your calories.",
                    TimeUnit.DAYS.toSeconds(1)
            );
            nutritionNotification.fire();
        }
    }

    public static void cancelNotification() {
        if (nutritionNotification != null) {
            nutritionNotification.cancelNotifiation();
            nutritionNotification = null;
        }
    }
}
