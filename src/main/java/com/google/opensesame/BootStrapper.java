package com.google.opensesame.servlets;

import com.googlecode.objectify.ObjectifyService;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class BootStrapper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.register(PersonEntity.class);
    ObjectifyService.register(MentorEntity.class);
  }
}