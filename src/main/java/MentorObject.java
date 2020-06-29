package com.google.sps.servlets;

public class MentorObject {
  private String name;
  private String description;

  public MentorObject(String nameInput, String descriptionInput) {
    name = nameInput;
    description = descriptionInput;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
