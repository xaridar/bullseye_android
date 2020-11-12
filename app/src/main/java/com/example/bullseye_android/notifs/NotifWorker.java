//Coded by Aakash Sell
package com.example.bullseye_android.notifs;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bullseye_android.activities.AdminDashboardActivity;
import com.example.bullseye_android.activities.SurveyActivity;
import com.example.bullseye_android.activities.UsersActivity;
import com.example.bullseye_android.database.survey.Survey;
import com.example.bullseye_android.database.survey.SurveyViewModel;
import com.example.bullseye_android.mailsender.SendMail;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class NotifWorker extends Worker {

    Notify notification;
    Context context;

    public NotifWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {
        notification = new Notify(getApplicationContext());
        notification.createNotification(getApplicationContext(), "Where have you been??", "We have missed you. Come play some games with bullseye!!", UsersActivity.class);
        //just testing sending email with a workManager
        //currently doesn't work
        //sendSurveyEmail();
        return Result.success();
    }
   /* public void sendSurveyEmail(){
        String subject = "Survey Results";
        final StringBuilder body = new StringBuilder();
        body.append("<html>");
        body.append("Survey Results. General Feeling About App First. User Responses Second");
        SurveyViewModel viewModel = ViewModelProviders.of((FragmentActivity) this.context).get(SurveyViewModel.class);
        LiveData<List<Survey>> data = viewModel.getAll();
        data.observe((LifecycleOwner) this.context, new Observer<List<Survey>>() {
            @Override
            public void onChanged(List<Survey> surveys) {
                Log.i("Database change", surveys.toString());
                for (Survey survey : surveys) {

                    body.append("<br>");
                    body.append(" ").append(SurveyActivity.getRadioAnswers(survey.getRadioAnswer()));
                    body.append(" ").append(survey.getInputText());
                    body.append("<br>");
                }
                body.append("</html>");
                new SendMail().sendMail(new Function<Boolean, Void>() {
                    @Override
                    public Void apply(Boolean aBoolean) {
                        return null;
                    }
                }, "bullseyeapp.no.reply@gmail.com", "B7nuXx\"3}A", "aakashsell@gmail.com", subject, body.toString(), null);
                data.removeObserver(this);
            }

        });

    }*/
}
