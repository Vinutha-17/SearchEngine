// File: src/com/empsearch/SearchEmployeeServlet.java
package com.empsearch;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@SuppressWarnings("unused")
public class SearchEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Update credentials as per your setup
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASS = "1417";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String keyword = req.getParameter("q");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Search Results</title>");
        out.println("<link rel='stylesheet' type='text/css' href='style.css'>");
        out.println("</head><body>");
        out.println("<h2>Search Results</h2>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            StringBuilder query = new StringBuilder("SELECT * FROM employees WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                query.append(" AND (");
                query.append("MATCH(name, tags, category) AGAINST (?)");
                query.append(" OR tags LIKE ?");
                query.append(" OR author LIKE ?");
                query.append(" OR category LIKE ?");
                query.append(" OR DATE_FORMAT(joined_on, '%Y-%m-%d') LIKE ?");
                query.append(")");

                String likePattern = "%" + keyword + "%";
                params.add(keyword);
                params.add(likePattern);
                params.add(likePattern);
                params.add(likePattern);
                params.add(likePattern);
            }

            PreparedStatement stmt = conn.prepareStatement(query.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            boolean found = false;

            if (rs.isBeforeFirst()) {
                out.println("<div style='text-align:center;'>");
                out.println("<div class='result-container'>");
                out.println("<table border = '1' cellspacing='0' cellpadding = '3'>");
                out.println("<tr><th>Name</th><th>Department</th><th>Email</th><th>Author</th><th>Category</th><th>Tags</th><th>Joined On</th></tr>");

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("department") + "</td>");
                    out.println("<td>" + rs.getString("email") + "</td>");
                    out.println("<td>" + rs.getString("author") + "</td>");
                    out.println("<td>" + rs.getString("category") + "</td>");
                    out.println("<td>" + rs.getString("tags") + "</td>");
                    out.println("<td>" + rs.getDate("joined_on") + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
                out.println("</div></div>");
            } else {
                out.println("<p>No results found.</p>");
            }

            out.println("<div style='margin-top: 30px;'>");
            out.println("<a href='index.html'><button class='return-button'>Return to Search</button></a>");
            out.println("</div>");

            out.println("</body></html>");
            conn.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            out.println("<div style='margin-top: 30px;'><a href='index.html'><button class='return-button'>Return to Search</button></a></div>");
        }
    }
}
