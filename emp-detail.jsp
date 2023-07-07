<%@page import="etu1766.framework.servlet.*"%>
<%@page import="models.Emp"%>
<%@page import="java.util.*"%>
<% Emp emp = (Emp) request.getAttribute("detail-emp"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Details employes</title>
</head>
<body>
    <h2>Details de l'employer</h2>
    <%
        out.println("Prenom: " + emp.getPrenom());
        out.println("Age = " + emp.getAge());
        out.println("Salaire = "+emp.getSalaire()); 
    %>
</body>
</html>