package com.mitrais.rms.controller;

import com.mitrais.rms.model.User;
import com.mitrais.rms.service.LoginService;
import com.mitrais.rms.service.impl.LoginServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.servlet.ServletContext;

@WebServlet("/login")
public class LoginServlet extends AbstractController{

    private final LoginService loginService = LoginServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
        String path = getTemplatePath(req.getServletPath());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
        String uname = req.getParameter("username");
        String upass = req.getParameter("userpass");
        boolean isUserExist = loginService.findUserData(uname, upass);
        if(isUserExist == true){
            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("currentuser", new User(uname, upass));
            res.sendRedirect(getContextRoot() + "/index.jsp");
        }else{
                String path = getTemplatePath(req.getServletPath());
                req.setAttribute("errorMessage", "Username or Password Incorrect !");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
                requestDispatcher.forward(req, res);
        }
    }
    
    private String getContextRoot(){
        ServletContext servletContext = getServletContext();
        return servletContext.getContextPath();
    }
}
