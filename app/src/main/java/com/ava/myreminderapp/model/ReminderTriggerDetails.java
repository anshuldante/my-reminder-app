package com.ava.myreminderapp.model;

import java.util.Date;
import java.util.UUID;

public class ReminderTriggerDetails {

  private String id;
  private String reminderId;
  private Date triggerDateTime;

  private Date createdDate;
  private Date modifiedDate;

  public ReminderTriggerDetails() {
    this.reminderId = UUID.randomUUID().toString();
    this.triggerDateTime = new Date();
    this.createdDate = new Date();
    this.modifiedDate = new Date();
  }

  private ReminderTriggerDetails(String id, String reminderId, Date triggerDateTime) {
    this();
    this.id = id;
    this.reminderId = reminderId;
    this.triggerDateTime = triggerDateTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getReminderId() {
    return reminderId;
  }

  public void setReminderId(String reminderId) {
    this.reminderId = reminderId;
  }

  public Date getTriggerDateTime() {
    return triggerDateTime;
  }

  public void setTriggerDateTime(Date triggerDateTime) {
    this.triggerDateTime = triggerDateTime;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }
}
