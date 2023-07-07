package etu1766.framework;

import jakarta.servlet.http.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.sql.Date;
import etu1766.annotation.*;
import etu1766.framework.Mapping;
import etu1766.framework.ModelView;
import java.lang.annotation.*;

public class Utilitaire{
        
    public Utilitaire() {
    }

    public static String getUrl(String url) {
        String[] decompose = url.split("/");
        String reponse = "";
        int itterator = 0;
        for(int i = decompose.length - 1; i > 3; i --) {
          if(itterator != 0) {
            reponse = "/" + reponse;
          }
          decompose[i] = decompose[i].replace('?', '=');
          String[] tableau = decompose[i].split("=");
          reponse = tableau[0] + reponse;
          itterator += 1;
        }
        
        return "/" + reponse;
    }

    public String getPath(){
        String path = "";
        File file = new File(new Utilitaire().getClass().getSimpleName()+".java");
        String[] chemins = file.getAbsolutePath().replace("\\", "/").split("/");
        for(int i = 0; i<chemins.length - 2; i++) {
            path += chemins[i] + "/";
        }
        path += chemins[chemins.length - 2];
        return path;
    }

    public static ArrayList<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file,packageName + "." + file.getName()));
            } 
            else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName+ '.'+ file.getName().substring(0,file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static HashMap<String,Mapping> getHashMap(String path) throws ClassNotFoundException{
        HashMap<String,Mapping> mappingUrls = new HashMap<String,Mapping>();
        ArrayList<Class<?>> classes = Utilitaire.findClasses(new File(path), "models");
        ArrayList<Method> methods = new ArrayList<>();
        Anno_Url anno = null;
        for(int i=0; i<classes.size(); i++) {
            Method[] methodes = classes.get(i).getDeclaredMethods();
            for (Method m: methodes) {
                methods.add(m);
            }
            for(int j=0; j<methods.size(); j++) {
                if(methods.get(j).isAnnotationPresent(Anno_Url.class)) {
                    anno = methods.get(j).getAnnotation(Anno_Url.class);
                    mappingUrls.put(anno.url(),new Mapping(classes.get(i).getName(), methods.get(j).getName()));
                }
            }
        }
        return mappingUrls;
    }

    public static HashMap<String, Object> singletons(String path) throws Exception{
        HashMap<String, Object> singletons = new HashMap<String, Object>();
        ArrayList<Class<?>> classes = Utilitaire.findClasses(new File(path), "models");
        Scoop annotation = null;
        for (int i = 0; i < classes.size(); i++) {
            if(classes.get(i).isAnnotationPresent(Scoop.class)) {
                annotation = classes.get(i).getAnnotation(Scoop.class);
                String valeur = (String) annotation.value();
                if(valeur.compareToIgnoreCase("singleton") == 0) {
                    singletons.put(classes.get(i).getName(), classes.get(i).getDeclaredConstructor().newInstance());
                }
            }
        }

        return singletons;
    }

    public static Vector<String> paramsAnnotation(HashMap<String, Mapping> mapppingUrls, String url) {
        Vector<String> params = new Vector<String>();
        try {
            for(String key : mapppingUrls.keySet()) {
                if(url.equals(key)) {
                    Class<?> myClass = Class.forName(mapppingUrls.get(key).getClassName());
                    Method[] methods = myClass.getMethods();
                    for(Method method : methods) {
                        if(method.getName().equals(mapppingUrls.get(key).getMethod())) {
                            Annotation annoParams = method.getAnnotation(Anno_Url.class);
                            
                            if(annoParams instanceof Anno_Url) {
                                Anno_Url modelAnnoParams = (Anno_Url) annoParams;
        
                                for(int i = 0; i < modelAnnoParams.params().length; i++) {
                                    params.add(modelAnnoParams.params()[i]);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return params;
    }
    
    public static void afficher_MappingUrls(HashMap<String,Mapping> mappingUrls,PrintWriter out){
        ArrayList<String> keys =  new ArrayList<String>(mappingUrls.keySet());
        for(int i=0;i<keys.size();i++) {
            out.println("key:"+keys.get(i)+" , "+"(classname = "+mappingUrls.get(keys.get(i)).getClassName() + "; methosd = "+mappingUrls.get(keys.get(i)).getMethod()+")");
        }
    }

    public String getSetters(String setters) {
        String rep = "set";
        rep += setters.toUpperCase().substring(0, 1) + setters.substring(1, setters.length());
        return rep;
    }

    public String getGetters(String getters) {
        String rep = "get";
        rep += getters.toUpperCase().substring(0, 1) + getters.substring(1, getters.length());
        return rep;
    }

    public String[] getAttribute(Object objet) {
        String[] attributes;
        Class<?> classe = objet.getClass();
        Field[] fields = classe.getDeclaredFields();
        attributes = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            attributes[i] = fields[i].getName();
        }

        return attributes;
    }

    public String setFields(Object ob, String setters, String values, String fieldName) {
        Method result = null;
        System.out.println(fieldName);
        try {            
            Field field = ob.getClass().getDeclaredField(fieldName);
            // System.out.println(field.getType().getSimpleName());
            if (field.getType().getSimpleName().equalsIgnoreCase("integer")) {
                result = ob.getClass().getDeclaredMethod(setters, Integer.class);
                result.invoke(ob, Integer.valueOf(values));
            } else if (field.getType().getSimpleName().equalsIgnoreCase("double")) {
                result = ob.getClass().getDeclaredMethod(setters, Double.class);
                result.invoke(ob, Double.valueOf(values));
            } else if (field.getType().getSimpleName().equalsIgnoreCase("date")) {
                result = ob.getClass().getDeclaredMethod(setters, Date.class);
                result.invoke(ob, Date.valueOf(values));
            } else if (field.getType().getSimpleName().equalsIgnoreCase("float")) {
                result = ob.getClass().getDeclaredMethod(setters, Float.class);
                result.invoke(ob, Float.valueOf(values));
            } else {
                result = ob.getClass().getDeclaredMethod(setters, String.class);
                try {
                    result.invoke(ob, values);
                } catch (NullPointerException e) {
                    
                }
                
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.getName();
    }

    public Object getFields(Object ob, String setters) {
        Method result = null;
        Object rep = null;
        try {
            result = ob.getClass().getDeclaredMethod(setters);
            rep = result.invoke(ob);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rep;
    }

    public Method getMethod(Object ob, String methodName) {
        Class<?> classe = ob.getClass();
        Method method = null;
        Method[] methods = classe.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if(methods[i].getName().equals(methodName)) {
                method = methods[i];
            }
        }

        return method;
    }

    public Class<?>[] getParameters(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
    
        return parameters;
    }

    //Upload file
    ////maka fields rehetra
    public static Vector<String> fields(String className) throws Exception {
        Vector<String> reponse = new Vector<String>();
        Field[] fields = Class.forName(className).getDeclaredFields();
        for (Field field : fields) {
          reponse.add(field.getName());
        }
    
        return reponse;
    }
    ////manisa anle field avy nalaina
    public static int countRequestFields(HttpServletRequest request, String className) throws Exception {
        int reponse = 0;
        Vector<String> fields = Utilitaire.fields(className);
        for (String field: fields) {
            System.out.println(field);
            if (request.getPart(field) != null) {
                reponse++;
            }
        }
    
        return reponse;
    }

    public static boolean isSave(HttpServletRequest request, HashMap<String, Mapping> mappingUrls) throws Exception {
        Set<String> keys = mappingUrls.keySet();
        for (String key : keys) {
          String className = mappingUrls.get(key).getClassName();
          if (Utilitaire.countRequestFields(request, className) == Utilitaire.fields(className).size()) {
            return true;
          }
        }
        return false;
    }

      ////maka ny anaranle classe ho savena
    public static String classToSave(HttpServletRequest request, HashMap<String, Mapping> mappingUrls) throws Exception {
        String reponse = "";
        Set<String> keys = mappingUrls.keySet();
        for (String key : keys) {
            String className = mappingUrls.get(key).getClassName();
            System.out.println("class: "+className);
            System.out.println(Utilitaire.countRequestFields(request, className));
            System.out.println(Utilitaire.fields(className).size());            
            if (Utilitaire.countRequestFields(request, className) == Utilitaire.fields(className).size()) {
                return className;
            }
        }
        return reponse;
    }
    
    public static void uploadFile(Object reponse, HttpServletRequest request, String parameter, Field field) throws Exception {
        Part filePart;
        try {
            filePart = request.getPart(parameter);
        } catch (Exception e) {
            throw e;
        }
        InputStream inputStream = filePart.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] fileBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        inputStream.close();
        String filename = filePart.getSubmittedFileName();
        String path = "D:/Logiciel/Tomcat/webapps/Sprint10/Fichiers/" + filename;
        filePart.write(path);
        FileUpload fileUpload = new FileUpload(filename, path, fileBytes);
        field.set(reponse, fileUpload);
    }

    public static void setValue(Object object, HttpServletRequest request, String parameter, Field field)
    throws Exception {
        String value = request.getParameter(parameter);
        if (field.getGenericType().getTypeName().compareTo("java.lang.Integer") == 0) {
            field.set(object, Integer.valueOf(value));
        } else if (field.getGenericType().getTypeName().compareTo("java.lang.Double") == 0) {
            field.set(object, Double.valueOf(value));
        } else if (field.getGenericType().getTypeName().compareToIgnoreCase("java.lang.Float") == 0) {
            field.set(object, Float.valueOf(value));
        } else if (field.getGenericType().getTypeName().compareTo("java.sql.Date") == 0) {
            field.set(object, Date.valueOf(value));
        } else {
            field.set(object, value);
        }
    }

    public static void runObject(Object object, HttpServletRequest request, Vector<String> parameters) throws Exception {
        for (String parameter: parameters) {
            Field field = object.getClass().getDeclaredField(parameter);
            field.setAccessible(true);
        
            if (object.getClass().getDeclaredField(parameter).getType().getSimpleName()
                .compareToIgnoreCase("FileUpload") == 0) {
                Utilitaire.uploadFile(object, request, parameter, field);
            } else {
                Utilitaire.setValue(object, request, parameter, field);
            }
        }
    }

    /////misave anle fichier
    public static Object save(HttpServletRequest request, HashMap<String, Mapping> mappingUrls) throws Exception {
        String className = Utilitaire.classToSave(request, mappingUrls);
        System.out.println("name of classes: " + className);
        Vector<String> parameters = Utilitaire.fields(className);
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor();
    
        Object reponse = constructor.newInstance();
        Utilitaire.runObject(reponse, request, parameters);   
    
        return reponse;
    }

    public static void save(HttpServletRequest request, HashMap<String, Mapping> mappingUrls, Object object) throws Exception {
        Vector<String> parameters = Utilitaire.fields(object.getClass().getName());
        Utilitaire.runObject(object, request, parameters);
    }
    //Upload file

    //reset setters
    public static void resetSet(Object ob) throws Exception{
        Field[] fields = ob.getClass().getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
            field.set(ob, null);
        }
    }
    //reset setters

    // public static void main(String[] args){
    //     // try {
    //     //     String path = "D:\\Logiciel\\Tomcat\\webapps\\Sprint8\\WEB-INF\\classes";
    //     //     HashMap<String, Mapping> mappingUrls = Utilitaire.getHashMap(path);

    //     //     Vector<String> parameters = Utilitaire.paramsAnnotation(mappingUrls, "/Emp/id");
    //     //     System.out.println(parameters.size());
    
    //     // } catch (Exception e) {
    //     //     // TODO: handle exception
    //     //     System.out.println(e);
    //     // }    
    // }
}
