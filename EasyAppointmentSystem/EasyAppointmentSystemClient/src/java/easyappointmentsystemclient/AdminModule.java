/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import entity.AdminEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author vanshiqa
 */
public class AdminModule {
     private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private AdminEntity loggedAdmin;

    public AdminModule() {
    }

    public AdminModule(AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote) {
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
    }
    
    public void adminStartMenu(){
        Scanner sc = new Scanner(System.in);
        int response = 0;
        
        while(true){
            System.out.println("*** Welcome to Admin terminal ***");
            System.out.println("1: Login");
            System.out.println("2: Exit");
            System.out.println();
            System.out.print("> ");
            response = 0;
            
            while(response < 1 ||  response > 2){
                if(sc.hasNextInt()){
                    response =  sc.nextInt();
                    if(response == 1 ){
                        adminLoginMenu();
                    }  else if(response == 2){
                        break;
                    }   
                } else {
                    System.out.println("Please enter valid integer input.\n")
                }
            }
         }
        
       
    }
    
    public void adminLoginMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Login ***");
        System.out.println();

            try {
                System.out.print("Enter Email Address> ");
                String email = sc.nextLine().trim();
                System.out.print("Enter Password>");
                String password = sc.nextLine().trim();
                loggedAdmin = adminEntitySessionBeanRemote.adminLogin(email, password);
                System.out.println("Login successful!");
                System.out.println();
                adminMainMenu();

            } catch (NullPointerException | InvalidLoginException ex){
                System.out.println("Login unsuccessful. Please enter valid login details.\n");
                
                adminStartMenu();              
            }
       
    }

    private void adminMainMenu() {
        System.out.println("*** Admin terminal :: Main ***");
        System.out.println("You are login as " + loggedAdmin.getFirstName());
        System.out.println("1: View Appointments for customers\n" +
"2: View Appointments for service providers\n" +
"3: View service providers\n" +
"4: Approve service provider\n" +
"5: Block service provider\n" +
"6: Add Business category\n" +
"7: Remove Business category\n" +
"8: Send reminder email\n" +
"9: Logout");
        
    }
    
    
}
