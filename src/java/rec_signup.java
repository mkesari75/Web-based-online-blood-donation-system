import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mayur Kesari
 */
public class rec_signup extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String id = request.getParameter("id");
            String fname = request.getParameter("fname");
            String mname = request.getParameter("mname");
            String lname = request.getParameter("lname");
            String dob = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String pincode = request.getParameter("pincode");
            String phone = request.getParameter("phone");
            String bldgrp = request.getParameter("bldgrp");
            String pass = request.getParameter("pass");
            
            ServletContext sc = getServletContext();
            sc.setAttribute("id",id);
            sc.setAttribute("fname",fname);
            sc.setAttribute("mname",mname);
            sc.setAttribute("lname",lname);
            sc.setAttribute("dob",dob);
            sc.setAttribute("gender",gender);
            sc.setAttribute("email",email);
            sc.setAttribute("address",address);
            sc.setAttribute("state",state);
            sc.setAttribute("city",city);
            sc.setAttribute("pincode",pincode);
            sc.setAttribute("phone",phone);
            sc.setAttribute("bldgrp",bldgrp);
            sc.setAttribute("pass",pass);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "");

            String dbemail = null;
            String dbphone = null;

            PreparedStatement ps = null;
            ps = con.prepareStatement("select * from rec_signup where email=? OR phone=?");
            ps.setString(1, email);
            ps.setString(2, phone);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dbemail = rs.getString("email");
                dbphone = rs.getString("phone");

            }

            if (email.equals(dbemail)) {
                response.sendRedirect("Receiver_signup.jsp?msg=invalidemail");

            } else if (phone.equals(dbphone)) {
                response.sendRedirect("Receiver_signup.jsp?msg=invalidphone");
            } else {
                String name, subject;
                Random rand = new Random();

                int rand_int1 = rand.nextInt(1000000);
                String rand1 = Integer.toString(rand_int1);
                sc.setAttribute("randotp",rand1);

                name = fname;
                subject = "Test";

                final String username = "mkesari75@gmail.com";
                final String password = "Mayur@80";
                Properties props = new Properties();
                props.put("mail.smtp.auth", true);
                props.put("mail.smtp.starttls.enable", true);
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(email));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    MimeBodyPart textPart = new MimeBodyPart();
                    Multipart multipart = new MimeMultipart();
                    String final_Text = name+ ", your Otp for Receiver Registeration is " + rand1;
                    textPart.setText(final_Text);
                    message.setSubject(subject);
                    multipart.addBodyPart(textPart);
                    message.setContent(multipart);
                    message.setSubject("Otp Verification");
                    //out.println("Sending");
                    Transport.send(message);
                } catch (Exception e) {
                    System.out.print(e);
                    response.sendRedirect("Receiver_signup.jsp?msg=exception");
                }
                finally{
                    response.sendRedirect("rec_otppage.jsp");
                }
            }
        } catch (Exception e) {
            System.out.print(e);
            response.sendRedirect("Receiver_signup.jsp?msg=exception");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
