/*
 * Copyright (C) 2019 - 2019-2021 Vishnu Sanal. T
 *
 * This file is part of Quotes Status Creator.
 *
 * Quotes Status Creator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package phone.vishnu.quotes.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import phone.vishnu.quotes.R;
import phone.vishnu.quotes.activity.MainActivity;
import phone.vishnu.quotes.model.Quote;
import phone.vishnu.quotes.repository.QuotesRepository;

public class NotificationHelper {

    private final int NOTIFICATION_REQUEST_CODE = 2222;

    private final String NOTIFICATION_CHANNEL_ID = "phone.vishnu.quotes";
    private final String NOTIFICATION_CHANNEL_NAME = "QuotesNotificationChannel";

    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void createNotification() {

        new QuotesRepository()
                .getRandomQuote(
                        quote -> {
                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_quotes_round)
                                            .setAutoCancel(true)
                                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                            .setContentIntent(getSharePendingIntent(quote))
                                            .addAction(
                                                    new NotificationCompat.Action(
                                                            R.drawable.ic_share,
                                                            "Share",
                                                            getSharePendingIntent(quote)))
                                            .addAction(
                                                    new NotificationCompat.Action(
                                                            R.drawable.ic_favorite,
                                                            "Add to Favorites",
                                                            getFavPendingIntent(quote)))
                                            .setContentTitle("Today's Quote")
                                            .setContentText(
                                                    quote.getQuote()
                                                            + "\n"
                                                            + " - "
                                                            + quote.getAuthor())
                                            .setStyle(
                                                    new NotificationCompat.BigTextStyle()
                                                            .bigText(
                                                                    quote.getQuote()
                                                                            + " - "
                                                                            + quote.getAuthor()));

                            NotificationManager notificationManager =
                                    (NotificationManager)
                                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                            if (android.os.Build.VERSION.SDK_INT
                                    >= android.os.Build.VERSION_CODES.O) {

                                builder.setChannelId(NOTIFICATION_CHANNEL_ID);

                                notificationManager.createNotificationChannel(
                                        new NotificationChannel(
                                                NOTIFICATION_CHANNEL_ID,
                                                NOTIFICATION_CHANNEL_NAME,
                                                NotificationManager.IMPORTANCE_HIGH));
                            }

                            if (notificationManager != null)
                                notificationManager.notify(
                                        NOTIFICATION_REQUEST_CODE, builder.build());
                        });
    }

    private PendingIntent getFavPendingIntent(Quote quote) {
        return getPendingIntent(1, "FavButton", quote);
    }

    private PendingIntent getSharePendingIntent(Quote quote) {
        return getPendingIntent(2, "ShareButton", quote);
    }

    private PendingIntent getPendingIntent(int i, String actionName, Quote quote) {
        return PendingIntent.getActivity(
                context,
                i,
                new Intent(context, MainActivity.class)
                        .putExtra("NotificationClick", true)
                        .putExtra(actionName, true)
                        .putExtra("quote", quote.getQuote())
                        .putExtra("author", quote.getAuthor()),
                PendingIntent.FLAG_ONE_SHOT);
    }
}
