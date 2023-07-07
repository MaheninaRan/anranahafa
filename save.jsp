<%@page import="etu1766.framework.servlet.*"%>
<%@page import="java.util.*"%>
<%@page import="models.Emp"%>

<% HashMap<String, Object> objects = (HashMap<String, Object>) request.getAttribute("data-form"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <%
        for (String key: objects.keySet()) {
            Emp e = (Emp) objects.get(key);
            out.println(e.getNom() + " " + e.getAge() + "<br>");
        }
    %>
</body>
</html>