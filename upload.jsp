<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <form action="Emp/save" enctype="multipart/form-data" method="post">        
        <input type="text" name="id">
        <input type="text" name="nom">
        <input type="text" name="prenom">
        <input type="text" name="age">
        <input type="text" name="salaire">
        <input type="file" name="image" placeholder="image"/>
        <input type="submit" value="Creer"/>
      </form>
</body>
</html>