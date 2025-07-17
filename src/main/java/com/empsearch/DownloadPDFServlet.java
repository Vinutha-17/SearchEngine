// File: src/com/empsearch/DownloadPDFServlet.java
package com.empsearch;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@SuppressWarnings({ "unused", "serial" })
public class DownloadPDFServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASS = "1417"; // replace this

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/pdf");
        res.setHeader("Content-Disposition", "attachment; filename=search_results.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, res.getOutputStream());
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontRow = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Employee Search Results", fontTitle));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6); // 6 columns (no ID)
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 2, 3, 2, 2, 3});

            // Add headers
            String[] headers = {"Name", "Department", "Email", "Author", "Category", "Tags"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Get results
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            String keyword = req.getParameter("q");
            StringBuilder query = new StringBuilder("SELECT * FROM employees WHERE 1=1");
            PreparedStatement stmt;

            if (keyword != null && !keyword.isEmpty()) {
                query.append(" AND (");
                query.append("MATCH(name, tags, category) AGAINST (?)");
                query.append(" OR tags LIKE ?");
                query.append(" OR author LIKE ?");
                query.append(" OR category LIKE ?");
                query.append(" OR DATE_FORMAT(joined_on, '%Y-%m-%d') LIKE ?");
                query.append(")");

                stmt = conn.prepareStatement(query.toString());
                String like = "%" + keyword + "%";
                stmt.setString(1, keyword);
                stmt.setString(2, like);
                stmt.setString(3, like);
                stmt.setString(4, like);
                stmt.setString(5, like);
            } else {
                stmt = conn.prepareStatement("SELECT * FROM employees");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                table.addCell(new Phrase(rs.getString("name"), fontRow));
                table.addCell(new Phrase(rs.getString("department"), fontRow));
                table.addCell(new Phrase(rs.getString("email"), fontRow));
                table.addCell(new Phrase(rs.getString("author"), fontRow));
                table.addCell(new Phrase(rs.getString("category"), fontRow));
                table.addCell(new Phrase(rs.getString("tags"), fontRow));
            }

            document.add(table);
            document.close();
            conn.close();
        } catch (Exception e) {
            res.getWriter().println("Error generating PDF: " + e.getMessage());
        }
    }
}
