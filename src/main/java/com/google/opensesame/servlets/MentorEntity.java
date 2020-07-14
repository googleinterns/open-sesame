package com.google.opensesame.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Subclass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Subclass(index=true)
public class MentorEntity extends PersonEntity {
    ArrayList<Long> projectIDs;
    ArrayList<String> menteeIDs;

    public MentorEntity(PersonEntity person, ArrayList<Long> projectIDs, ArrayList<String> menteeIDs) {
      super(person.id, person.interestTags, person.email);
      this.projectIDs = projectIDs;
      this.menteeIDs = menteeIDs;
    }
}