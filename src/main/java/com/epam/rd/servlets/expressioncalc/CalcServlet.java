package com.epam.rd.servlets.expressioncalc;

import com.epam.rd.servlets.expressioncalc.MathExpressions.CalcExpression;
import com.epam.rd.servlets.expressioncalc.MathExpressions.Expression;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Expression solution = new CalcExpression();
        String expression = req.getParameter("expression");
        String [] params = req.getQueryString().split("&");

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(solution.preparation(params,expression));
        printWriter.close();
    }
}