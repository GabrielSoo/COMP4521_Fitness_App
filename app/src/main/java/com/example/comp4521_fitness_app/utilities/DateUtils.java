package com.example.comp4521_fitness_app.utilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getRelativeDate(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = "";

        try {
            Date currentDate = new Date();
            Date givenDate = dateFormat.parse(inputDate);

            long diff = currentDate.getTime() - givenDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days == 0) {
                result = "Today";
            } else if (days == 1) {
                result = "Yesterday";
            } else {
                result = days + " days ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDateTime() {
        // Get the current date and time
        Date now = new Date();

        // Format the date and time as a string
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }

    public static String getDateLabel(String dateCreated) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat labelFormat = new SimpleDateFormat("dd/MM");
        Date date = null;
        try {
            date = dateFormat.parse(dateCreated);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return labelFormat.format(date);
    }
}

