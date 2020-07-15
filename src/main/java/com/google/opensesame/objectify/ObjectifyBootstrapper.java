package com.google.opensesame.objectify;

import com.google.opensesame.servlets.MentorEntity;
import com.google.opensesame.servlets.PersonEntity;
import com.googlecode.objectify.ObjectifyService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ObjectifyBootstrapper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.register(PersonEntity.class);
    ObjectifyService.register(MentorEntity.class);
  }
}
