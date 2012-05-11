package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import play.data.*;
import models.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import play.libs.Akka;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;

import com.twilio.*;
import com.util.*;

public class Application extends Controller {

  static Form<Task> taskForm = form(Task.class);
  
  public static Result index() {
    return redirect(routes.Application.tasks());
  }

  public static Result tasks() {
    return ok(
      views.html.index.render(Task.all(), taskForm,  false, false)
    );
  }

  public static Result newTask() {
    // get form data
    Form<Task> filledForm = taskForm.bindFromRequest();
    if(filledForm.hasErrors()) {
      return badRequest(
        views.html.index.render(Task.all(), filledForm,  false, false)
      );
    } 
    else if(filledForm.get().invalidPhone()) { // check if phone is invalid, ie. 10-digit
      return badRequest(
        views.html.index.render(Task.all(), filledForm, true, false)
      );
    } 
    else if(filledForm.get().invalidDate()) { // check if date is invalid, ie. need both date and due date and must be later than now
      return badRequest(
        views.html.index.render(Task.all(), filledForm,  false, true)
      );
    }
    else if(filledForm.get().phoneWithoutDate()) { // check if the user only entered the phone but without the due date/time
      return badRequest(
        views.html.index.render(Task.all(), filledForm,  false, true)
      );
    } else {

      // Get filled form data
      final String label = filledForm.get().label;
      final String phone = filledForm.get().phone;
      final String dueDate = filledForm.get().dueDate;
      final String dueTime = filledForm.get().dueTime;
      
      // If user entered the optional fields -> phone and due date
      if(dueDate.length() > 0 && dueTime.length() > 0 && phone.length() > 0) {
        
        // Get whenToSend
        final double toSendAfter = getToSendAfter(dueDate, dueTime);
        final double remainingTime = toSendAfter / 4;

        // Schedule to send a sms
        Akka.system().scheduler().scheduleOnce(
          Duration.create(toSendAfter, TimeUnit.SECONDS),
          new Runnable() {
            public void run() {
              // Send sms notification
              TodoNotification todoNotification = new TodoNotification();
              todoNotification.send("Your todo \"" + label + "\" is due in " + (int)remainingTime/60 + " minutes", phone);
            }
          }
        );
      }

      // Create task and save in db
      Task.create(filledForm.get());
      return redirect(routes.Application.tasks());  
    }
  }
  
  // not finished
  public static Result updateTask() {
    Form<Task> filledForm = taskForm.bindFromRequest();
    System.out.println(filledForm.get().label);
    if(filledForm.hasErrors()) {
      return badRequest(
        views.html.index.render(Task.all(), filledForm,  false, false)
      );
    } 
    else {
      // update
      // Task.updateTodo(filledForm.get());
      filledForm.get().update(); // optimistic exception
      return redirect(routes.Application.tasks());  
    }
  }

  // delete this task
  public static Result deleteTask(Long id) {
    Task.delete(id);
    return redirect(routes.Application.tasks());
  }
  
  // Compute the 80% later (in seconds)
  public static double getToSendAfter(String date, String time) {
    
    Date now = new Date();
    Helper helper = new Helper();
    Date dueDateTime = helper.convertStringToDate(date, time);
    double diffInSeconds = (((double)dueDateTime.getTime() - (double)now.getTime()) / 1000) * 0.8;
    
    return diffInSeconds;
  }
  
}


