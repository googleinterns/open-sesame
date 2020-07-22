package com.google.opensesame.objectify;

import com.google.opensesame.projects.ProjectEntity;
import com.google.opensesame.user.UserEntity;
import com.googlecode.objectify.ObjectifyService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ObjectifyBootstrapper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.register(UserEntity.class);
    ObjectifyService.register(ProjectEntity.class);
  }
}
