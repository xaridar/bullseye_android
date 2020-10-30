package com.example.bullseye_android.mailsender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Message;
import android.util.Log;

import io.github.mthli.sugartask.SugarTask;

public class SendMail {

    private ProgressDialog statusDialog;
    private Activity activity;

    public SendMail(Activity activity) {
        this.activity = activity;
    }

    public void sendMail(Object... args) {
        statusDialog = new ProgressDialog(activity);
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
        SugarTask.with(activity)
                .assign(() -> {
                    try {
                        Log.i("SendMailTask", "About to instantiate GMail...");
                        Message message = Message.obtain();
                        message.obj = ("Processing input....");
                        SugarTask.post(message);
                        GMail androidEmail = new GMail(args[0].toString(),
                                args[1].toString(), args[2].toString(), args[3].toString(),
                                args[4].toString());
                        Message message2 = Message.obtain();
                        message2.obj = ("Preparing mail message....");
                        SugarTask.post(message2);
                        androidEmail.createEmailMessage();
                        Message message3 = Message.obtain();
                        message3.obj = ("Sending email....");
                        SugarTask.post(message3);
                        androidEmail.sendEmail();
                        Message message4 = Message.obtain();
                        message4.obj = ("Email Sent.");
                        SugarTask.post(message4);
                        Log.i("SendMailTask", "Mail Sent.");
                    } catch (Exception e) {
                        Message message5 = Message.obtain();
                        message5.obj = (e.getMessage());
                        SugarTask.post(message5);
                        Log.e("SendMailTask", e.getMessage(), e);
                    }
                    return null;
                })
                .handle(message -> {
                    statusDialog.setMessage(message.obj.toString());
                    Log.i("SendMail", message.toString());
                })
                .finish(result -> statusDialog.dismiss())
                .broken(e -> {
                    Log.e("SendMailTask", e.getMessage(), e);
                    statusDialog.dismiss();
                })
                .execute();
    }
}