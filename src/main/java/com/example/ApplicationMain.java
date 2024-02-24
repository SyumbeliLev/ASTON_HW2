package com.example;

import com.example.servlet.TaskServlet;
import com.example.servlet.UserServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

import static com.example.db.DatabaseInitializer.*;

public class ApplicationMain {
    public static void main(String[] args) throws LifecycleException {
        initialize();
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setPort(8080);

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());

        Tomcat.addServlet(context, "userServlet", new UserServlet());
        context.addServletMappingDecoded("/users/*", "userServlet");

        Tomcat.addServlet(context, "taskServlet", new TaskServlet());
        context.addServletMappingDecoded("/tasks/*", "taskServlet");

        tomcat.start();
    }
}
