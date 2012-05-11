package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

import com.util.*;

@Entity
public class Task extends Model {
    
  @Id
  public Long id;
  
  @Required
  public String label;
  
  public String dueDate;
  public String dueTime;
  
  public String phone;
  
  public static Finder<Long, Task> find = new Finder(
    Long.class, Task.class
  );
  
  // show all tasks
  public static List<Task> all() {
    return find.all();
  }
  
  // create task
  public static void create(Task task) {
    task.save();
  }
  
  // delete task
  public static void delete(Long id) {
    find.ref(id).delete();
  }
  
  // validates if the phone is correct length
  public boolean invalidPhone() {
    
    if (phone.length() > 0 && phone.length() != 10){
      return true;
    }
    return false;
  }

  // validates if the date is correct
  public boolean invalidDate() {

    // if either is blank
    if((dueDate.length() == 0) ^ (dueTime.length() == 0)) return true;
    
    // if user entered both date and time
    if(dueDate.length() > 0 && dueTime.length() > 0) {
      Helper helper = new Helper();
      Date dueDateTime = helper.convertStringToDate(dueDate, dueTime);
      Date now = new Date();
      
      if (dueDateTime.getTime() <= now.getTime()) {
        return true;
      }

      // if (dueDateTime.before(now)) {
      //   return true;
      // }
    }
    return false;
  }
  
  // if user entered phone but without the phone number
  public boolean phoneWithoutDate() {
    
    if(phone.length() > 0) {
      if(dueDate.length() == 0 || dueTime.length() == 0) return true;
    }
    return false;
    
  }
  
  
}