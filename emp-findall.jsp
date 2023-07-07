<%@page import="etu1766.framework.servlet.*"%>
<%@page import="java.util.*"%>
<%@page import="models.Emp"  %>
<% ArrayList<Emp> emp = (ArrayList<Emp>) request.getAttribute("list-emp"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Welcome to emp-findall</h1>
    <h3>Tous les employes</h3>
    <%
        for(int i=0; i<emp.size(); i++) { %>
            <a href="id?id=<% out.print(emp.get(i).getId()); %>&&prenom=<% out.print(emp.get(i).getPrenom()); %>&&salaire=<% out.print(emp.get(i).getSalaire()); %>"><% out.println(emp.get(i).getNom()); %></a>
        <% }
    %>
</body>
</html>