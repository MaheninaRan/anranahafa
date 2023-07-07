package models;

import java.util.ArrayList;
import etu1766.annotation.*;
import etu1766.framework.ModelView;
import etu1766.framework.FileUpload;
import etu1766.framework.Mapping;
import java.util.*;

@Scoop(value = "Singleton")
public class Emp {
    Integer id;
    String nom;
    String prenom;
    Integer age;
    Double salaire;
    FileUpload image;

    public Emp() {

    }

    public Emp(Integer id, String nom, String prenom, Integer age, Double salaire) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.salaire = salaire;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public FileUpload getImage() {
        return image;
    }

    public void setImage(FileUpload file) {
        this.image = file;
    }

    @Anno_Url(url = "/Emp/findall")
    public ModelView findall() {
        ModelView view  = new ModelView();
        view.setView("/emp-findall.jsp");
        ArrayList<Emp> employes = new ArrayList<Emp>();
        Emp a = new Emp(1, "Employe 1", "Mahefa", 22, 200.5);
        Emp b = new Emp(2, "Employe 2", "Mau", 18, 205.5);
        Emp c = new Emp(3, "Employe 3", "Aina", 19, 250.5);
        employes.add(a);
        employes.add(b);
        employes.add(c);
        view.addItem("list-emp",employes);

        return view;
    }

    @Anno_Url(url = "/Emp/insert")
    public ModelView insert(){
        ModelView view = new ModelView("/emp-insert.jsp");
        
        return view;
    }

    public void test(){
        
    }

    @Anno_Url(url = "/Emp/save")
    public ModelView save() {
        ModelView view = new ModelView();
        view.setView("/save.jsp");

        return view;
    }

    @Anno_Url(url = "/Emp/id", params = {"id", "prenom", "salaire"})
    public ModelView findById(Integer id, String nom, float salaire) {
        ModelView view = new ModelView();
        ModelView emp_all = findall();
        System.out.println("emp_all view correspondant: " + emp_all.getView());
        for(String key : emp_all.getData().keySet()) {
            ArrayList<Emp> listEmp = (ArrayList<Emp>) emp_all.getData().get(key);
            System.out.println("size = " + listEmp.size());

            for (int i = 0; i < listEmp.size(); i++) {
                System.out.println("id recu par la fonction findById: " + id);
                System.out.println("id dans listEmp: " + listEmp.get(i).getId());
                if(id == listEmp.get(i).getId()){
                    view.setData(new HashMap<String,Object>());
                    view.addItem("detail-emp", listEmp.get(i));
                }
            }
        }
        view.setView("/emp-detail.jsp");

        return view;
    }

    @Anno_Url(url = "/Emp/function")
    public ModelView testFunction() {
        ModelView view = new ModelView();

        return view;
    }
}