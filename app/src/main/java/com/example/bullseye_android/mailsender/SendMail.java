package com.example.bullseye_android.mailsender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Message;
import android.util.Log;

import java.util.function.Function;

import io.github.mthli.sugartask.SugarTask;

public class SendMail {

    private ProgressDialog statusDialog;
    private Activity activity;

    public SendMail(Activity activity) {
        this.activity = activity;
    }

    public void sendMail(Function<String, Void> fxn, String sendingEmail, String password, String receivingEmail, String subject, String body) {
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
                        GMail androidEmail = new GMail(sendingEmail, password, receivingEmail, subject, body);
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
                        Thread.sleep(2000);
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
                .finish(result -> {
                    statusDialog.dismiss();
                    fxn.apply(body);
                })
                .broken(e -> {
                    Log.e("SendMailTask", e.getMessage(), e);
                    statusDialog.dismiss();
                    fxn.apply(null);
                }).execute();
    }
}
