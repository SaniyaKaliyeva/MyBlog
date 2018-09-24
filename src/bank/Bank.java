package bank;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class Bank extends Application {

    Connection connection;
    Statement st;
    InputStream ref2 = null;
    PasswordField password;
    TextField login;
    ResultSet rs;
    Circle circle;
    TextArea blogtxt;
    Button add;
    ResultSet posts = null;

    public Statement connectToDB() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank", "postgres", "1425");
            st = connection.createStatement();
        } catch (SQLException ex) {
        }
        return st;
    }

    @Override
    public void start(Stage loginStage) throws SQLException {
        Group loginroot = new Group();
        
        login = new TextField();
        login.setPromptText("login..");
        login.setTranslateX(125);
        login.setTranslateY(100);
        login.setStyle("-fx-border-color:green");
        login.setFocusTraversable(false);

        password = new PasswordField();
        password.setPromptText("password..");
        password.setTranslateX(125);
        password.setTranslateY(140);
        password.setStyle("-fx-border-color:green");
        password.setFocusTraversable(false);

        Button signin = new Button("Sign in");
        signin.setTranslateX(175);
        signin.setTranslateY(180);
        signin.setStyle("-fx-background-color:green");

        Text txt1 = new Text("No account?");
        txt1.setTranslateX(130);
        txt1.setTranslateY(235);

        Text txt2 = new Text("Register");
        txt2.setFill(Color.GREEN);
        txt2.setTranslateX(220);
        txt2.setTranslateY(235);

        Text error2 = new Text("Your login or password is incorrect");
        error2.setTranslateX(90);
        error2.setTranslateY(265);
        error2.setFill(Color.RED);
        error2.setVisible(false);

        signin.setOnAction(new EventHandler<ActionEvent>() {
            boolean isEdit = true;
            File  fileImage = null;
            @Override
            public void handle(ActionEvent event) {
                if (!login.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    String lg = login.getText().toString();
                    String pss = null;
                    String ref = null;
                    try {
                        ResultSet rs = connectToDB().executeQuery("select password from users where login='" + lg + "';");
                        while (rs.next()) {
                            if(password.getText().equals(rs.getString(1))){
                                pss = rs.getString(1);
                            }
                            else{
                                error2.setVisible(true);
                            }
                        }
                        rs.close();
                        st.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (pss.equals(password.getText().toString())) {
                        System.out.println("you are successfully signed in");
                        loginStage.close();
                        Stage profStage = new Stage();//Профиль
                        Group profroot = new Group();
                        profStage.setScene(new Scene(profroot, 900, 640));
                        profStage.setResizable(false);
                        profStage.show();
                        LinkedHashMap<String, String> postsOfUser = new LinkedHashMap<>();//Posts                            
                        
                        try {
                            posts = connectToDB().executeQuery("select blog, post_date from posts where user_log = '" + lg + "' order by post_date DESC");
                            while (posts.next()) {
                                postsOfUser.put(posts.getString(2), posts.getString(1));//key, value 
                                
                            }
                            posts.close();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        
                        Rectangle rec1 = new Rectangle(500, 550);//frame
                        rec1.setTranslateX(370);
                        rec1.setTranslateY(20);
                        rec1.setFill(Color.TRANSPARENT);
                        rec1.setStroke(Color.GREEN);
                        rec1.setArcHeight(75);
                        rec1.setArcWidth(75);
                        
                        Rectangle rec2 = new Rectangle(285, 355);//frame
                        rec2.setTranslateX(70);
                        rec2.setTranslateY(215);
                        rec2.setFill(Color.TRANSPARENT);
                        rec2.setStroke(Color.GREEN);
                        rec2.setArcHeight(75);
                        rec2.setArcWidth(75);

                        Text myblog = new Text("My blogs");
                        myblog.setTranslateX(390);
                        myblog.setTranslateY(50);
                        myblog.setStyle("-fx-font-size:20");
                        myblog.setFill(Color.GREEN);

                        blogtxt = new TextArea();
                        blogtxt.setTranslateX(390);
                        blogtxt.setTranslateY(60);
                        blogtxt.setStyle("-fx-font-size:120");
                        blogtxt.setPrefSize(460, 75);
                        blogtxt.setStyle("-fx-border-color:green");

                        add = new Button("  Add  ");
                        add.setTranslateX(790);
                        add.setTranslateY(140);
                        add.setStyle("-fx-background-color:green");
                        
                        Button apply = new Button("Apply");
                        apply.setTranslateX(730);
                        apply.setTranslateY(140);
                        apply.setStyle("-fx-background-color:CADETBLUE");
                        apply.setVisible(false);
                        
                        Button cancel2 = new Button("Cancel");
                        cancel2.setTranslateX(660);
                        cancel2.setTranslateY(140);
                        cancel2.setStyle("-fx-background-color:CADETBLUE");
                        cancel2.setVisible(false);

                        ListView list = new ListView();//Blogs in profileStage
                        list.setTranslateX(390);
                        list.setTranslateY(180);
                        list.setMaxHeight(370);
                        list.setMinWidth(460);
                        ObservableList<String> arr = FXCollections.observableArrayList();
                        for (Entry en : postsOfUser.entrySet()) {
                            arr.add(en.getValue().toString() + "\n" + en.getKey().toString());
                        }
                        list.setItems(arr);

                        Text ntext2 = new Text("Name:");
                        ntext2.setStyle("-fx-font-size:15");
                        ntext2.setFill(Color.GREEN);
                        ntext2.setTranslateX(80);
                        ntext2.setTranslateY(250);
                        Text ntext = new Text();
                        ntext.setTranslateX(190);
                        ntext.setTranslateY(250);
                        TextField pf1 = new TextField();
                        pf1.setTranslateX(190);
                        pf1.setTranslateY(230);
                        pf1.setMaxWidth(125);
                        pf1.setStyle("-fx-border-color:green");
                        pf1.setVisible(false);
                        Button edit1 = new Button("✎");
                        edit1.setTranslateX(315);
                        edit1.setTranslateY(230);

                        Text stext2 = new Text("Surname:");
                        stext2.setStyle("-fx-font-size:15");
                        stext2.setFill(Color.GREEN);
                        stext2.setTranslateX(80);
                        stext2.setTranslateY(300);
                        Text stext = new Text();
                        stext.setTranslateX(190);
                        stext.setTranslateY(300);
                        TextField pf2 = new TextField();
                        pf2.setTranslateX(190);
                        pf2.setTranslateY(280);
                        pf2.setMaxWidth(125);
                        pf2.setStyle("-fx-border-color:green");
                        pf2.setVisible(false);
                        Button edit2 = new Button("✎");
                        edit2.setTranslateX(315);
                        edit2.setTranslateY(280);

                        Text phtext2 = new Text("Phone number:");
                        phtext2.setStyle("-fx-font-size:15");
                        phtext2.setFill(Color.GREEN);
                        phtext2.setTranslateX(80);
                        phtext2.setTranslateY(350);
                        Text phtext = new Text();
                        phtext.setTranslateX(190);
                        phtext.setTranslateY(350);
                        TextField pf3 = new TextField();
                        pf3.setTranslateX(190);
                        pf3.setTranslateY(330);
                        pf3.setMaxWidth(125);
                        pf3.setStyle("-fx-border-color:green");
                        pf3.setVisible(false);
                        Button edit3 = new Button("✎");
                        edit3.setTranslateX(315);
                        edit3.setTranslateY(330);

                        Text datetext2 = new Text("Date of birth:");
                        datetext2.setStyle("-fx-font-size:15");
                        datetext2.setFill(Color.GREEN);
                        datetext2.setTranslateX(80);
                        datetext2.setTranslateY(400);
                        Text datetext = new Text();
                        datetext.setTranslateX(190);
                        datetext.setTranslateY(400);
                        TextField pf4 = new TextField();
                        pf4.setTranslateX(190);
                        pf4.setTranslateY(380);
                        pf4.setMaxWidth(125);
                        pf4.setStyle("-fx-border-color:green");
                        pf4.setVisible(false);
                        Button edit4 = new Button("✎");
                        edit4.setTranslateX(315);
                        edit4.setTranslateY(380);

                        Text postext2 = new Text("Position:");
                        postext2.setStyle("-fx-font-size:15");
                        postext2.setFill(Color.GREEN);
                        postext2.setTranslateX(80);
                        postext2.setTranslateY(450);
                        Text postext = new Text();
                        postext.setTranslateX(190);
                        postext.setTranslateY(450);
                        TextField pf5 = new TextField();
                        pf5.setTranslateX(190);
                        pf5.setTranslateY(430);
                        pf5.setMaxWidth(125);
                        pf5.setStyle("-fx-border-color:green");
                        pf5.setVisible(false);
                        Button edit5 = new Button("✎");
                        edit5.setTranslateX(315);
                        edit5.setTranslateY(430);

                        Text saltext2 = new Text("Salary:");
                        saltext2.setStyle("-fx-font-size:15");
                        saltext2.setFill(Color.GREEN);
                        saltext2.setTranslateX(80);
                        saltext2.setTranslateY(500);
                        Text saltext = new Text();
                        saltext.setTranslateX(190);
                        saltext.setTranslateY(500);
                        TextField pf6 = new TextField();
                        pf6.setTranslateX(190);
                        pf6.setTranslateY(480);
                        pf6.setMaxWidth(125);
                        pf6.setStyle("-fx-border-color:green");
                        pf6.setVisible(false);
                        Button edit6 = new Button("✎");
                        edit6.setTranslateX(315);
                        edit6.setTranslateY(480);

                        Button accept = new Button("Accept");
                        accept.setTranslateX(230);
                        accept.setTranslateY(580);
                        accept.setVisible(false);
                        accept.setStyle("-fx-background-color:green");

                        Button cancel = new Button("Cancel");
                        cancel.setTranslateX(150);
                        cancel.setTranslateY(580);
                        cancel.setVisible(false);
                        cancel.setStyle("-fx-background-color:CADETBLUE");

                        Button others = new Button("See more blogs...");
                        others.setTranslateX(725);
                        others.setTranslateY(580);
                        others.setStyle("-fx-background-color:green");
                        
                        Button deleteBlogs = new Button("Delete");
                        deleteBlogs.setTranslateX(619);
                        deleteBlogs.setTranslateY(580);
                        deleteBlogs.setStyle("-fx-background-color:CADETBLUE");
                        deleteBlogs.setVisible(false);
                        
                        Button editBlogs = new Button("Edit");
                        editBlogs.setTranslateX(680);
                        editBlogs.setTranslateY(580);
                        editBlogs.setStyle("-fx-background-color:CADETBLUE");
                        editBlogs.setVisible(false);
                        
                        Button exitFromProfile = new Button("exit");
                        exitFromProfile.setTranslateX(1);
                        exitFromProfile.setTranslateY(1);
                        exitFromProfile.setStyle("-fx-background-color:CADETBLUE");

                        add.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (!blogtxt.getText().equals("")) {
                                    blogtxt.setStyle("-fx-border-color:green");
                                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                                    ObservableList<String> tempList = FXCollections.observableArrayList();
                                    tempList.add(blogtxt.getText().toString() + "\n" + ts.toString());
                                    tempList.addAll(arr);
                                    arr.clear();
                                    arr.addAll(tempList);
                                    list.setItems(arr);

                                    try {
                                        connectToDB().executeUpdate("insert into posts values('" + lg + "','" + blogtxt.getText().toString() + "','" + ts.toString() + "');");
                                        st.close();
                                        connection.close();
                                    } catch (SQLException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                    blogtxt.clear();
                                } else {
                                    blogtxt.setStyle("-fx-border-color:red");
                                }
                            }
                        });
                        edit1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (isEdit) {
                                    isEdit = false;
                                    accept.setVisible(true);
                                    cancel.setVisible(true);
                                    pf1.setFocusTraversable(false);
                                    pf1.setText(ntext.getText());//name
                                    pf1.setVisible(true);
                                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            try {
                                                isEdit = true;
                                                connectToDB().executeUpdate("update users set name='" + pf1.getText().toString() + "' where login='" + lg + "';");
                                                ntext.setText(pf1.getText());
                                                pf1.setVisible(false);
                                                cancel.setVisible(false);
                                                accept.setVisible(false);
                                                st.close();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        edit2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (isEdit) {
                                    isEdit = false;
                                    accept.setVisible(true);
                                    cancel.setVisible(true);
                                    pf2.setFocusTraversable(false);
                                    pf2.setText(stext.getText());//surname
                                    pf2.setVisible(true);
                                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            isEdit = true;
                                            try {
                                                connectToDB().executeUpdate("update users set surname='" + pf2.getText().toString() + "' where login='" + lg + "';");
                                                stext.setText(pf2.getText());
                                                pf2.setVisible(false);
                                                cancel.setVisible(false);
                                                accept.setVisible(false);
                                                st.close();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        edit3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (isEdit) {
                                    isEdit = false;
                                    accept.setVisible(true);
                                    cancel.setVisible(true);
                                    pf3.setFocusTraversable(false);
                                    pf3.setText(phtext.getText());//phone_number
                                    pf3.setVisible(true);
                                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            isEdit = true;
                                            try {
                                                connectToDB().executeUpdate("update users set phone_number='" + pf3.getText().toString() + "' where login='" + lg + "';");
                                                phtext.setText(pf3.getText());
                                                pf3.setVisible(false);
                                                cancel.setVisible(false);
                                                accept.setVisible(false);
                                                st.close();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        edit4.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (isEdit) {
                                    isEdit = false;
                                    accept.setVisible(true);
                                    cancel.setVisible(true);
                                    pf4.setFocusTraversable(false);
                                    pf4.setText(datetext.getText());//dateofb
                                    pf4.setVisible(true);
                                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            isEdit = true;
                                            try {
                                                connectToDB().executeUpdate("update users set dateofb='" + pf4.getText().toString() + "' where login='" + lg + "';");
                                                datetext.setText(pf4.getText());
                                                pf4.setVisible(false);
                                                cancel.setVisible(false);
                                                accept.setVisible(false);
                                                st.close();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        edit5.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (isEdit) {
                                    isEdit = false;
                                    accept.setVisible(true);
                                    cancel.setVisible(true);
                                    pf5.setFocusTraversable(false);
                                    pf5.setText(postext.getText());//position
                                    pf5.setVisible(true);
                                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            isEdit = true;
                                            try {
                                                connectToDB().executeUpdate("update users set position='" + pf5.getText().toString() + "' where login='" + lg + "';");
                                                postext.setText(pf5.getText());
                                                pf5.setVisible(false);
                                                cancel.setVisible(false);
                                                accept.setVisible(false);
                                                st.close();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        edit6.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (isEdit) {
                                    isEdit = false;
                                    accept.setVisible(true);
                                    cancel.setVisible(true);
                                    pf6.setFocusTraversable(false);
                                    pf6.setText(saltext.getText());//salary
                                    pf6.setVisible(true);
                                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            isEdit = true;
                                            try {
                                                connectToDB().executeUpdate("update users set salary='" + pf6.getText().toString() + "' where login='" + lg + "';");
                                                saltext.setText(pf6.getText());
                                                pf6.setVisible(false);
                                                cancel.setVisible(false);
                                                accept.setVisible(false);
                                                st.close();
                                            } catch (SQLException ex) {
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                isEdit = true;
                                try {
                                    pf1.setVisible(false);
                                    pf2.setVisible(false);
                                    pf3.setVisible(false);
                                    pf4.setVisible(false);
                                    pf5.setVisible(false);
                                    pf6.setVisible(false);
                                    cancel.setVisible(false);
                                    accept.setVisible(false);
                                    st.close();
                                } catch (SQLException ex) {
                                }
                            }
                        });
                        exitFromProfile.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                profStage.close();
                                loginStage.show();
                                login.clear();
                                password.clear();
                            }
                        });
                        
                        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                editBlogs.setVisible(true);
                                deleteBlogs.setVisible(true);
                                int selectedIdx = list.getSelectionModel().getSelectedIndex(); 
                                String selectedItm = list.getSelectionModel().getSelectedItem().toString();
                                String dateString = selectedItm.substring(selectedItm.lastIndexOf("\n") + 1);
                                
                                deleteBlogs.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            list.getItems().remove(selectedIdx);
                                            try {
                                                connectToDB().executeUpdate("delete from posts where post_date='"+dateString+"'" );
                                            } catch (SQLException ex) {
                                                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });
                            }
                        });
                        editBlogs.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                apply.setVisible(true);
                                cancel2.setVisible(true);
                                String selectedItm = list.getSelectionModel().getSelectedItem().toString();
                                String blogString = selectedItm.substring(0, selectedItm.lastIndexOf("\n"));
                                blogtxt.setText(blogString);
                                add.setDisable(true);
                                
                            }
                        });
                        apply.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                apply.setVisible(false);
                                cancel2.setVisible(false);
                                String selectedItm = list.getSelectionModel().getSelectedItem().toString();
                                String dateString = selectedItm.substring(selectedItm.lastIndexOf("\n") + 1);
                                try {
                                        connectToDB().executeUpdate("update posts set blog='"+blogtxt.getText()+"' where post_date='"+dateString+"'" );
                                            } catch (SQLException ex) {
                                                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                arr.set(list.getSelectionModel().getSelectedIndex() , blogtxt.getText() +"\n"+dateString);
                                list.setItems(arr);
                                blogtxt.setText("");
                                add.setDisable(false);
                                
                            }
                        });
                        cancel2.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                apply.setVisible(false);
                                cancel2.setVisible(false);
                                blogtxt.setText("");
                                add.setDisable(false);
                            }
                        });

                        blogtxt.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                deleteBlogs.setVisible(false);
                                editBlogs.setVisible(false);
                            }
                        });
                      
                        others.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                profStage.close();
                                Stage allStage = new Stage();//All users
                                Group allroot = new Group();
                                allStage.setScene(new Scene(allroot, 900, 640));
                                allStage.setResizable(false);
                                allStage.show();
                                
                                Rectangle rec3 = new Rectangle(280, 520);//frame
                                rec3.setTranslateX(85);
                                rec3.setTranslateY(60);
                                rec3.setFill(Color.TRANSPARENT);
                                rec3.setStroke(Color.GREEN);
                                rec3.setArcHeight(75);
                                rec3.setArcWidth(75);
                                
                                Rectangle rec4 = new Rectangle(490, 520);//frame
                                rec4.setTranslateX(375);
                                rec4.setTranslateY(60);
                                rec4.setFill(Color.TRANSPARENT);
                                rec4.setStroke(Color.GREEN);
                                rec4.setArcHeight(75);
                                rec4.setArcWidth(75);
                        
                                ListView<Pane> profileOfUsers = new ListView<>();
                                profileOfUsers.setTranslateX(100);
                                profileOfUsers.setTranslateY(70);
                                profileOfUsers.setMinHeight(500);
                                profileOfUsers.setMinWidth(100);                  
                                profileOfUsers.setFocusTraversable(false);

                                ListView blogOfUser = new ListView<>();
                                blogOfUser.setTranslateX(390);
                                blogOfUser.setTranslateY(70);
                                blogOfUser.setMinHeight(500);
                                blogOfUser.setMinWidth(460);
                                blogOfUser.setFocusTraversable(false);
                                
                                Text text1 = new Text("All users");
                                text1.setTranslateX(170);
                                text1.setTranslateY(50);
                                text1.setStyle("-fx-font-size:20");
                                text1.setFill(Color.GREEN);
                                
                                Text text2 = new Text("Blogs");
                                text2.setTranslateX(570);
                                text2.setTranslateY(50);
                                text2.setStyle("-fx-font-size:20");
                                text2.setFill(Color.GREEN);
                    
                                Button back = new Button("back");
                                back.setTranslateX(1);
                                back.setTranslateY(1);
                                back.setStyle("-fx-background-color:CADETBLUE");

                                back.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        allStage.close();
                                        profStage.show();
                                        login.clear();
                                        password.clear();
                                    }
                                });
                                Pane pane = null;
                                String ref = null;
                                Circle imageOfUser = null;
                                Image img = null;
                                ObservableList<Pane> panes = FXCollections.observableArrayList();
                                Rectangle rec = new Rectangle(150,50);
                                
                                ArrayList<String> loginOfUsers = new ArrayList<>();
                                try {
                                    ResultSet rs = connectToDB().executeQuery("select login,image, name, surname from users where login <> '"+lg+"';");
                                    while (rs.next()) {  
                                        pane = new Pane();
                                        Text text = new Text(rs.getString(3)+" "+rs.getString(4));
                                        pane.setPrefSize(100, 50);
                                        imageOfUser = new Circle(24);
                                        imageOfUser.setStroke(Color.GREEN);
                                        loginOfUsers.add(rs.getString(1));
                                        ref2 = rs.getBinaryStream("image");//reference
                                        img = new Image(ref2);                                       
                                        imageOfUser.setFill(new ImagePattern(img));
                                        imageOfUser.setTranslateX(20);
                                        imageOfUser.setTranslateY(25);
                                        text.setTranslateX(65);
                                        text.setTranslateY(30);
                                        pane.getChildren().add(imageOfUser);
                                        pane.getChildren().add(text);
                                        panes.add(pane);
                                    }
                                    profileOfUsers.setItems(panes);
                                    rs.close();
                                    st.close();
                                } catch (SQLException ex) {
                                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                ResultSet posts = null;
                                LinkedHashMap<String, String> postsOfFirst = new LinkedHashMap<>();
                                try {
                                    posts = connectToDB().executeQuery("select blog, post_date from posts where user_log='" + loginOfUsers.get(0) + "' order by post_date DESC");
                                    while (posts.next()) {
                                        postsOfFirst.put(posts.getString(2), posts.getString(1));//key, value 
                                    }
                                    posts.close();
                                } catch (SQLException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                ObservableList<String> arr = FXCollections.observableArrayList();
                                for(Entry en : postsOfFirst.entrySet()){
                                    arr.add(en.getValue().toString()+"\n"+en.getKey().toString());
                                }                     
                                blogOfUser.setItems(arr);
                                profileOfUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        String loginForList = loginOfUsers.get(profileOfUsers.getSelectionModel().getSelectedIndex());
                                        LinkedHashMap<String, String> postsOfUser = new LinkedHashMap<>();//Posts 
                                        ResultSet posts = null;
                                        try {
                                            posts = connectToDB().executeQuery("select blog, post_date from posts where user_log='" + loginForList + "' order by post_date DESC");
                                            while (posts.next()) {
                                                postsOfUser.put(posts.getString(2), posts.getString(1));//key - date, value - blog                                           
                                            }
                                            posts.close();
                                        } catch (SQLException ex) {
                                            System.out.println(ex.getMessage());
                                        }
                                        ObservableList<String> arr = FXCollections.observableArrayList();
                                        for (Entry en : postsOfUser.entrySet()) {
                                            arr.add(en.getValue().toString() + "\n" + en.getKey().toString());
                                        }
                                        blogOfUser.setItems(arr);
                                    }
                                });
                                

                                allroot.getChildren().addAll(rec3,rec4,profileOfUsers,blogOfUser, text1,text2,back);
                                allStage.setTitle("All profiles");       
                            }   
                        });
                        profroot.getChildren().addAll(ntext, ntext2, stext, stext2,
                                phtext, phtext2, datetext, datetext2, postext, postext2, saltext, saltext2,
                                pf1, pf2, pf3, pf4, pf5, pf6, rec1,rec2, apply,cancel2,myblog, blogtxt, add, accept, cancel, list, others,
                                deleteBlogs,editBlogs,edit1, edit2, edit3, edit4, edit5, edit6,exitFromProfile);
                        profStage.setTitle("My profile");
                        Button changeImage = new Button();//Image
                        changeImage.setTranslateX(235);
                        changeImage.setTranslateY(175);
                        FileChooser fc = new FileChooser();
                        try {
                            Image imageCam = new Image(new FileInputStream("/Users/macbookair/NetBeansProjects/Bank/camera.png"));
                            ImageView imgvv = new ImageView(imageCam);
                            imgvv.setFitWidth(14);
                            imgvv.setFitHeight(16);
                            changeImage.setGraphic(imgvv);
                                    } catch (FileNotFoundException ex) {
                            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        
                        try {
                            ResultSet rs = connectToDB().executeQuery("select name, surname, phone_number, dateofb, position, salary,image from users where login='" + lg + "';");
                            while (rs.next()) {
                                ntext.setText(rs.getString(1));
                                stext.setText(rs.getString(2));
                                phtext.setText(rs.getString(3));
                                datetext.setText(rs.getString(4));
                                postext.setText(rs.getString(5));
                                saltext.setText(rs.getString(6));
                                ref2 = rs.getBinaryStream("image");
                                ref = rs.getString(7);//reference
                            }
                            circle = new Circle(200, 110, 90);
                            circle.setStroke(Color.GREEN);
                            Image im = new Image(ref2);
                            circle.setFill(new ImagePattern(im));
                            
                            profroot.getChildren().addAll(circle,changeImage);
                            rs.close();
                            st.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        changeImage.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Testttt");
                                fileImage = fc.showOpenDialog(profStage);
                                if(fileImage != null){
                                    FileInputStream stream = null;
                                    try {
                                        stream = new FileInputStream(fileImage);
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    try {
                                        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank", "postgres", "1425");
                                        PreparedStatement ps = con.prepareStatement("update users set image=? where login='"+lg+"'");
                                        

                                        ps.setBinaryStream(1, (InputStream) stream, (int) fileImage.length());
                                        ps.executeUpdate();
                                        
                                        ResultSet rs = connectToDB().executeQuery("select name,image from users where login='" + lg + "';");
                                        InputStream ref3 =null;
                                        while(rs.next()){
                                            ref3 = rs.getBinaryStream("image");
                                            System.out.println(rs.getString("name"));
                                        }
                                        System.out.println(ref3.toString());
                                       
                                        Image im = new Image(ref3);
                                        circle.setFill(new ImagePattern(im));
                                        ps.close();
                                        
                                    
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    
//                                    try{
                                       
//                                    }
//                                    catch(SQLException ex){
//                                        
//                                    }
                                    
                                }
                            }
                            
                        });
                    }
                } else {
                    error2.setVisible(true);
                }
            }
        });
                        
        txt2.setOnMouseClicked(new EventHandler<MouseEvent>() {//Регистрация
            File fileImage = null;
            @Override
            public void handle(MouseEvent event) {
                loginStage.close();
                Stage regStage = new Stage();//Регистрация
                Group regroot = new Group();
                Scene regScene = new Scene(regroot, 400, 600);

                TextField name = new TextField();
                name.setTranslateX(160);
                name.setTranslateY(82);
                Text nametext = new Text("*Name");
                nametext.setTranslateX(60);
                nametext.setTranslateY(100);

                TextField sname = new TextField();
                sname.setTranslateX(160);
                sname.setTranslateY(122);
                Text snametext = new Text("*Surname");
                snametext.setTranslateX(60);
                snametext.setTranslateY(140);

                DatePicker dateofb = new DatePicker();
                dateofb.setTranslateX(160);
                dateofb.setTranslateY(162);
                dateofb.setPrefSize(168, 1);
                Text dateofbtext = new Text("*Date of Birth");
                dateofbtext.setTranslateX(60);
                dateofbtext.setTranslateY(180);
                
                TextField log = new TextField();
                log.setTranslateX(160);
                log.setTranslateY(202);
                Text logtext = new Text("*Login");
                logtext.setTranslateX(60);
                logtext.setTranslateY(220);

                PasswordField pass = new PasswordField();
                pass.setTranslateX(160);
                pass.setTranslateY(242);
                Text passtext = new Text("*Password");
                passtext.setTranslateX(60);
                passtext.setTranslateY(260);

                TextField phonenumber = new TextField();
                Text phonenumbertext = new Text("Phone_number");
                phonenumbertext.setTranslateX(60);
                phonenumbertext.setTranslateY(300);
                phonenumber.setTranslateX(160);
                phonenumber.setTranslateY(282);

                TextField position = new TextField();
                Text positiontext = new Text("Position");
                positiontext.setTranslateX(60);
                positiontext.setTranslateY(340);
                position.setTranslateX(160);
                position.setTranslateY(322);

                TextField salary = new TextField();
                Text salarytext = new Text("Salary");
                salarytext.setTranslateX(60);
                salarytext.setTranslateY(380);
                salary.setTranslateX(160);
                salary.setTranslateY(362);

                TextField image = new TextField();
                image.setEditable(false);
                Text imagetext = new Text("*Image");
                imagetext.setTranslateX(60);
                imagetext.setTranslateY(420);
                image.setTranslateX(160);
                image.setTranslateY(402);
                image.setPrefSize(168,1);
                
                
                Button saveImage = new Button();//Image
                saveImage.setTranslateX(296);
                saveImage.setTranslateY(402);
                FileChooser fc = new FileChooser();
                try {
                    Image imageCam = new Image(new FileInputStream("/Users/macbookair/NetBeansProjects/Bank/camera.png"));
                    ImageView imgvv = new ImageView(imageCam);
                    imgvv.setFitWidth(14);
                    imgvv.setFitHeight(16);
                    saveImage.setGraphic(imgvv);
                            } catch (FileNotFoundException ex) {
                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                }
                

                Button reg = new Button("Register");
                reg.setStyle("-fx-background-color:green");
                reg.setTranslateX(190);
                reg.setTranslateY(460);//440

                Text error = new Text("You need to fill in all the required fields(*)");
                error.setTranslateX(80);
                error.setTranslateY(520);
                error.setFill(Color.RED);
                error.setVisible(false);
                
                Text error3 = new Text("This login already exist");
                error3.setTranslateX(140);
                error3.setTranslateY(540);
                error3.setFill(Color.RED);
                error3.setVisible(false);
                
                Text error4 = new Text("Password must contain at least 8 characters");
                error4.setTranslateX(80);
                error4.setTranslateY(540);
                error4.setFill(Color.RED);
                error4.setVisible(false);
                
                Button exitFromReg = new Button("exit");
                exitFromReg.setTranslateX(1);
                exitFromReg.setTranslateY(1);
                exitFromReg.setStyle("-fx-background-color:CADETBLUE");

                exitFromReg.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                    regStage.close();
                    loginStage.show();
                    }
                });
                
                ArrayList<String> allLogins = new ArrayList<>();
                try {
                     rs = connectToDB().executeQuery("select login from users");
                    while(rs.next()){
                        allLogins.add(rs.getString(1));
                        
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                }
                regScene.setOnMouseMoved(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            if(allLogins.contains(log.getText())){
                                error3.setVisible(true);                             
                                }
                            else{
                                error3.setVisible(false);
                            }
                        }
                    });
                
                Desktop desktop = Desktop.getDesktop();
                saveImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        fileImage = fc.showOpenDialog(regStage);
                        if(fileImage != null){
                            image.setText(fileImage.getName());                           
                        }
                    }
                });

                reg.setOnAction(new EventHandler<ActionEvent>() {//Логин
                    @Override
                    public void handle(ActionEvent event) {
                         
                        if (!name.getText().toString().equals("") || !dateofb.getValue().toString().equals("") || !sname.getText().toString().equals("") || 
                                !log.getText().toString().equals("") || !pass.getText().toString().equals("") || !image.getText().toString().equals("")) {
                            error.setVisible(false);    
                        } 
                        if(pass.getText().length()>8){
                            error4.setVisible(false);
                        }
                        if(pass.getText().length()<8){
                            error4.setVisible(true);
                        }
                        if (name.getText().toString().equals("") || dateofb.getValue().toString().equals("") 
                                || sname.getText().toString().equals("") || log.getText().toString().equals("") 
                                || pass.getText().toString().equals("") || image.getText().toString().equals("")
                                ) {
                            error.setVisible(true);    
                        }
                        if(!name.getText().toString().equals("") && !dateofb.getValue().toString().equals("") && !sname.getText().toString().equals("") && 
                                !log.getText().toString().equals("") && !pass.getText().toString().equals("") && !image.getText().toString().equals("") && pass.getText().length()>8) {
                            regStage.close();
                            loginStage.show();//Логин
                            String logdb = log.getText().toString();
                            String passdb = pass.getText().toString();
                            double phone_numbdb = Double.parseDouble(phonenumber.getText());
                            String namedb = name.getText().toString();
                            String surnamedb = sname.getText().toString();
                            String datedb = dateofb.getValue().toString();
                            String positiondb = position.getText().toString();
                            int salarydb = Integer.parseInt(salary.getText().toString());
                            String imgdb = image.getText().toString();
                            FileInputStream stream = null;
                            try {
                                stream = new FileInputStream(fileImage);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank", "postgres", "1425");
                                PreparedStatement ps = con.prepareStatement("insert into users(login, password, phone_number, name, surname, dateofb, position, salary, image) values('" + logdb + "','" + passdb + "','" + phone_numbdb + "','" + namedb + "','" + surnamedb + "','" + datedb + "','" + positiondb + "','" + salarydb + "',?)");

                                ps.setBinaryStream(1, (InputStream) stream, (int) fileImage.length());
                                ps.executeUpdate();
                                ps.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                regroot.getChildren().addAll(name, nametext, sname, snametext,
                        dateofb, dateofbtext, log, logtext, pass, passtext,
                        phonenumber, phonenumbertext, position, positiontext,
                        salary, salarytext, image, imagetext, reg, error,error3,error4,saveImage,exitFromReg);
                regStage.setTitle("Register");
                regStage.setScene(regScene);
                regStage.show();
            }
        });
        loginroot.getChildren().addAll(login, password, signin, txt1, txt2, error2);
        loginStage.setTitle("Blog");
        loginStage.setScene(new Scene(loginroot, 400, 400));
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}