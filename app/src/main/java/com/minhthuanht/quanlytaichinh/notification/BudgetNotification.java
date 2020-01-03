package com.minhthuanht.quanlytaichinh.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.budget.activity.DetailBudgetActivity;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.DateRange;

public class BudgetNotification {
    private static String CHANNEL_ID = "Budget_NotificationsChannel";

    private static final int ID_NOTIFICATION = 0x000A;

    private static final String REQUEST_DETAIL_BUDGET = "sendDetailBudgetActivity";

    private Context mContext;

    public BudgetNotification(Context context) {
        super();
        if (context != null){
            mContext = context;
        }

    }

    public void notifyBudgetExpired(Budget budget) {
        String nameBudget = budget.getCategory().getCategory();
        DateRange dateRange = new DateRange(budget.getTimeStart(), budget.getTimeEnd());
        String timeBudget = dateRange.toString();
        String text_notification = mContext.getResources().getString(R.string.budget) + " " + nameBudget + " từ " + timeBudget + " sắp hết hạn";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle(mContext.getResources().getString(R.string.budget));
        builder.setContentText(text_notification);
        builder.setAutoCancel(true);

        Intent intent = new Intent(mContext, DetailBudgetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(REQUEST_DETAIL_BUDGET, budget);
        intent.putExtra(REQUEST_DETAIL_BUDGET, bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notificationManager.notify(ID_NOTIFICATION, notification);
    }

    public void notifyBudgetOverSpend(Budget budget) {
        String nameBudget = budget.getCategory().getCategory();
        DateRange dateRange = new DateRange(budget.getTimeStart(), budget.getTimeEnd());
        String timeBudget = dateRange.toString();
        String text_notification = mContext.getResources().getString(R.string.budget) + " " + nameBudget + " từ " + timeBudget + " sắp hết";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle(mContext.getResources().getString(R.string.budget));
        builder.setContentText(text_notification);
        builder.setAutoCancel(true);

        Intent intent = new Intent(mContext, DetailBudgetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(REQUEST_DETAIL_BUDGET, budget);
        intent.putExtra(REQUEST_DETAIL_BUDGET, bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notificationManager.notify(ID_NOTIFICATION, notification);
    }
}
