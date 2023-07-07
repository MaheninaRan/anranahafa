package etu1766.framework;

import java.lang.reflect.*;
import java.util.*;
import java.sql.Date;

import etu1766.framework.Utilitaire;

public class ModelView {
  String view;
  HashMap<String,Object> data = new HashMap<String, Object>();

  public HashMap<String, Object> getData() {
    return data;
  }

  public void setData(HashMap<String, Object> data) {
    this.data = data;
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  
  public ModelView(String view) {
    this.view = view;
  }

  public ModelView(){

  }


  public static ModelView loadView(String url, HashMap<String, Mapping> mappingUrls, Vector<String> values) throws Exception {
    Set<String> set = mappingUrls.keySet();
    if (!set.contains(url)) {
      throw new Exception("404 not found!");
    }

    String className = mappingUrls.get(url).getClassName();
    String methodName = mappingUrls.get(url).getMethod();
    Class<?> classe = Class.forName(className);
    Constructor<?> constructor = classe.getDeclaredConstructor();
    Object object = constructor.newInstance();
    Method method = null;
    ModelView view = null;
    Object[] vals = new Object[values.size()];
    if(values.size() != 0) {
      System.out.println(values.size() + " 222");
      method = new Utilitaire().getMethod(object, methodName);
      Class<?>[] paramsType = new Utilitaire().getParameters(method);
      System.out.println("taille: "+paramsType.length);
      
      for (int i = 0; i < paramsType.length; i++) {
        if(paramsType[i].getSimpleName().equalsIgnoreCase("integer") ) {
          int val = Integer.parseInt(values.get(i));
          vals[i] = val;
        } else if(paramsType[i].getSimpleName().equalsIgnoreCase("double")) {
          double val = Double.parseDouble(values.get(i));
          vals[i] = val;
        } else if(paramsType[i].getSimpleName().equalsIgnoreCase("float")) {
          float val = Float.parseFloat(values.get(i));
          vals[i] = val;
        } else if(paramsType[i].getSimpleName().equalsIgnoreCase("date")) {
          Date val = Date.valueOf(values.get(i));
          vals[i] = val;
        } else {
          vals[i] = values.get(i);
        }
      }

      method = classe.getDeclaredMethod(methodName, paramsType);
      System.out.println(method);
      view = (ModelView) method.invoke(object, vals);
    } else { 
      method = classe.getDeclaredMethod(methodName);
      view = (ModelView) method.invoke(object);
    }
    
    return view;
  }

  public void addItem(String key, Object value) {
    this.getData().put(key,value);
  }
 
}