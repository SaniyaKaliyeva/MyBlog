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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class Bank extends Application {

    Connection connection;
    Statement st;
     ListView<Item> profileOfUsers;
                    InputStream ref2 = null;

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
        TextField login = new TextField();
        login.setPromptText("login..");
        login.setTranslateX(125);
        login.setTranslateY(100);
        login.setStyle("-fx-border-color:green");
        login.setFocusTraversable(false);

        PasswordField password = new PasswordField();
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

            @Override
            public void handle(ActionEvent event) {
                if (!login.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    String lg = login.getText().toString();
                    String pss = null;
                    String ref = null;
                    try {
                        ResultSet rs = connectToDB().executeQuery("select password from users where login='" + lg + "';");
                        while (rs.next()) {
                            System.out.println(rs.getString(1));
                            pss = rs.getString(1);

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
                        ResultSet posts = null;
                        try {
                            posts = connectToDB().executeQuery("select blog, post_date from posts where user_log = '" + lg + "' order by post_date DESC");
                            while (posts.next()) {
                                postsOfUser.put(posts.getString(2), posts.getString(1));//key, value                                           
                            }
                            posts.close();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        

                        Rectangle rec = new Rectangle(500, 550);//frame
                        rec.setTranslateX(370);
                        rec.setTranslateY(20);
                        rec.setFill(Color.TRANSPARENT);
                        rec.setStroke(Color.GRAY);
                        rec.setArcHeight(75);
                        rec.setArcWidth(75);

                        Text myblog = new Text("My blogs");
                        myblog.setTranslateX(390);
                        myblog.setTranslateY(50);
                        myblog.setStyle("-fx-font-size:20");
                        myblog.setFill(Color.GREEN);

                        TextArea blogtxt = new TextArea();
                        blogtxt.setTranslateX(390);
                        blogtxt.setTranslateY(60);
                        blogtxt.setStyle("-fx-font-size:120");
                        blogtxt.setPrefSize(460, 75);
                        blogtxt.setStyle("-fx-border-color:green");

                        Button add = new Button("  Add  ");
                        add.setTranslateX(790);
                        add.setTranslateY(140);
                        add.setStyle("-fx-background-color:green");

                        ListView list = new ListView();
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
                        ntext2.setTranslateX(92);
                        ntext2.setTranslateY(250);
                        Text ntext = new Text();
                        ntext.setTranslateX(200);
                        ntext.setTranslateY(250);
                        TextField pf1 = new TextField();
                        pf1.setTranslateX(200);
                        pf1.setTranslateY(230);
                        pf1.setMaxWidth(125);
                        pf1.setStyle("-fx-border-color:green");
                        pf1.setVisible(false);
                        Button edit1 = new Button("✎");
                        edit1.setTranslateX(325);
                        edit1.setTranslateY(230);

                        Text stext2 = new Text("Surname:");
                        stext2.setStyle("-fx-font-size:15");
                        stext2.setFill(Color.GREEN);
                        stext2.setTranslateX(92);
                        stext2.setTranslateY(300);
                        Text stext = new Text();
                        stext.setTranslateX(200);
                        stext.setTranslateY(300);
                        TextField pf2 = new TextField();
                        pf2.setTranslateX(200);
                        pf2.setTranslateY(280);
                        pf2.setMaxWidth(125);
                        pf2.setStyle("-fx-border-color:green");
                        pf2.setVisible(false);
                        Button edit2 = new Button("✎");
                        edit2.setTranslateX(325);
                        edit2.setTranslateY(280);

                        Text phtext2 = new Text("Phone number:");
                        phtext2.setStyle("-fx-font-size:15");
                        phtext2.setFill(Color.GREEN);
                        phtext2.setTranslateX(92);
                        phtext2.setTranslateY(350);
                        Text phtext = new Text();
                        phtext.setTranslateX(200);
                        phtext.setTranslateY(350);
                        TextField pf3 = new TextField();
                        pf3.setTranslateX(200);
                        pf3.setTranslateY(330);
                        pf3.setMaxWidth(125);
                        pf3.setStyle("-fx-border-color:green");
                        pf3.setVisible(false);
                        Button edit3 = new Button("✎");
                        edit3.setTranslateX(325);
                        edit3.setTranslateY(330);

                        Text datetext2 = new Text("Date of birth:");
                        datetext2.setStyle("-fx-font-size:15");
                        datetext2.setFill(Color.GREEN);
                        datetext2.setTranslateX(92);
                        datetext2.setTranslateY(400);
                        Text datetext = new Text();
                        datetext.setTranslateX(200);
                        datetext.setTranslateY(400);
                        TextField pf4 = new TextField();
                        pf4.setTranslateX(200);
                        pf4.setTranslateY(380);
                        pf4.setMaxWidth(125);
                        pf4.setStyle("-fx-border-color:green");
                        pf4.setVisible(false);
                        Button edit4 = new Button("✎");
                        edit4.setTranslateX(325);
                        edit4.setTranslateY(380);

                        Text postext2 = new Text("Position:");
                        postext2.setStyle("-fx-font-size:15");
                        postext2.setFill(Color.GREEN);
                        postext2.setTranslateX(92);
                        postext2.setTranslateY(450);
                        Text postext = new Text();
                        postext.setTranslateX(200);
                        postext.setTranslateY(450);
                        TextField pf5 = new TextField();
                        pf5.setTranslateX(200);
                        pf5.setTranslateY(430);
                        pf5.setMaxWidth(125);
                        pf5.setStyle("-fx-border-color:green");
                        pf5.setVisible(false);
                        Button edit5 = new Button("✎");
                        edit5.setTranslateX(325);
                        edit5.setTranslateY(430);

                        Text saltext2 = new Text("Salary:");
                        saltext2.setStyle("-fx-font-size:15");
                        saltext2.setFill(Color.GREEN);
                        saltext2.setTranslateX(92);
                        saltext2.setTranslateY(500);
                        Text saltext = new Text();
                        saltext.setTranslateX(200);
                        saltext.setTranslateY(500);
                        TextField pf6 = new TextField();
                        pf6.setTranslateX(200);
                        pf6.setTranslateY(480);
                        pf6.setMaxWidth(125);
                        pf6.setStyle("-fx-border-color:green");
                        pf6.setVisible(false);
                        Button edit6 = new Button("✎");
                        edit6.setTranslateX(325);
                        edit6.setTranslateY(480);

                        Button accept = new Button("Accept");
                        accept.setTranslateX(230);
                        accept.setTranslateY(540);
                        accept.setVisible(false);
                        accept.setStyle("-fx-background-color:green");

                        Button cancel = new Button("Cancel");
                        cancel.setTranslateX(150);
                        cancel.setTranslateY(540);
                        cancel.setVisible(false);

                        Button others = new Button("See more blogs...");
                        others.setTranslateX(570);
                        others.setTranslateY(580);
                        others.setStyle("-fx-background-color:green");
                        
                        Button exitFromProfile = new Button("Exit");
                        exitFromProfile.setTranslateX(1);
                        exitFromProfile.setTranslateY(1);
                        exitFromProfile.setStyle("-fx-background-color:green");

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
                                    System.out.println("hello");
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

                               
                                try {
                                    profileOfUsers = new ListView<>( FXCollections.observableArrayList(new Item("Sobachka Dadly",new Image(new FileInputStream("/Users/macbookair/NetBeansProjects/JavaFXExample/game1.jpg"))),
                                            new Item("Sobachka Sadly",new Image(new FileInputStream("/Users/macbookair/NetBeansProjects/JavaFXExample/game1.jpg")))));
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                profileOfUsers.setTranslateX(100);
                                profileOfUsers.setTranslateY(50);
                                profileOfUsers.setMinHeight(500);
                                profileOfUsers.setMinWidth(100);
                                profileOfUsers.setFocusTraversable(false);

                                ListView blogOfUser = new ListView<>();
                                blogOfUser.setTranslateX(390);
                                blogOfUser.setTranslateY(50);
                                blogOfUser.setMinHeight(500);
                                blogOfUser.setMinWidth(460);
                                blogOfUser.setFocusTraversable(false);
                                
                                 profileOfUsers.setCellFactory(listView -> new ListCell<Item>() {
                                    @Override
                                    public void updateItem(Item item, boolean empty) {
                                            if (empty) {
                                                setText("");
                                                setGraphic(null);
                                            } else {
                                                setText(item.text);
                                                setGraphic(item.imgv);
                                            }
                                        }

                                    });
                                ArrayList<String> loginOfUsers = new ArrayList<>();
                                
                                Button back = new Button("Back to profile");
                                back.setTranslateX(742);
                                back.setTranslateY(560);
                                back.setStyle("-fx-background-color:green");
                                
                                Button exitFromBlogs = new Button("Exit");
                                exitFromBlogs.setTranslateX(1);
                                exitFromBlogs.setTranslateY(1);
                                exitFromBlogs.setStyle("-fx-background-color:green");

                                back.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        allStage.close();
                                        profStage.show();
                                    }
                                });

                                String ref = null;
                                Circle imageOfUser = null;

                                Image img = null;
                                ObservableList<Item> items = FXCollections.observableArrayList();
                                ObservableList<Circle> arr2 = FXCollections.observableArrayList();
                                Rectangle rec = new Rectangle(150,50);
                                
                                try {
                                    ResultSet rs = connectToDB().executeQuery("select login,image from users where login <> '"+lg+"';");
                                    while (rs.next()) {
                                        
                                        imageOfUser = new Circle(30);
                                        imageOfUser.setStroke(Color.GREEN);
                                        loginOfUsers.add(rs.getString(1));
                                        ref2 = rs.getBinaryStream("image");//reference
                                        System.out.println(ref2.toString());
                                        img = new Image(ref2);
                                        
                                        imageOfUser.setFill(new ImagePattern(img));
                                        arr2.add(imageOfUser);
                                        
                                        
                                        
                                        items.add(new Item(rs.getString(1),new Image(ref2)));
                                        
                                        
                                    }
                                    
                                   


                                    //profileOfUsers.setItems(arr2);
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
                                        System.out.println(loginForList);
                                        ResultSet posts = null;
                                        try {
                                            posts = connectToDB().executeQuery("select blog, post_date from posts where user_log='" + loginForList + "' order by post_date DESC");
                                            while (posts.next()) {
                                                postsOfUser.put(posts.getString(2), posts.getString(1));//key, value                                           
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
                                
                                exitFromBlogs.setOnMouseClicked(new EventHandler<MouseEvent>(){
                                    @Override
                                    public void handle(MouseEvent event) {
                                        allStage.close();
                                    }
                        });

                                allroot.getChildren().addAll(profileOfUsers, blogOfUser, back, exitFromBlogs);
                                allStage.setTitle("All profiles");
                                
                            }   
                        });

                        profroot.getChildren().addAll(ntext, ntext2, stext, stext2,
                                phtext, phtext2, datetext, datetext2, postext, postext2, saltext, saltext2,
                                pf1, pf2, pf3, pf4, pf5, pf6, rec, myblog, blogtxt, add, accept, cancel, list, others,
                                edit1, edit2, edit3, edit4, edit5, edit6,exitFromProfile);
                        profStage.setTitle("My profile");

                        try {
                            ResultSet rs = connectToDB().executeQuery("select name, surname, phone_number, dateofb, position, salary,image from users where login='" + lg + "';");
                            while (rs.next()) {
                                System.out.println(rs.getString(1));
                                ntext.setText(rs.getString(1));
                                stext.setText(rs.getString(2));
                                phtext.setText(rs.getString(3));
                                datetext.setText(rs.getString(4));
                                postext.setText(rs.getString(5));
                                saltext.setText(rs.getString(6));
                                ref2 = rs.getBinaryStream("image");
                                ref = rs.getString(7);//reference
                            }
                            System.out.println(ref);
                            Circle circle = new Circle(200, 110, 90);
                            Circle imageOfUser = new Circle(30);
                            circle.setStroke(Color.GREEN);
                            Image im = new Image(ref2);
                            circle.setFill(new ImagePattern(im));
                            imageOfUser.setFill(new ImagePattern(im));
                            profroot.getChildren().addAll(circle);
                            rs.close();
                            st.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                        }
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

                TextField name = new TextField();
                Text nametext = new Text("*Name");

                TextField sname = new TextField();
                Text snametext = new Text("*Surname");

                DatePicker dateofb = new DatePicker();
                Text dateofbtext = new Text("*Date of Birth");
                dateofb.maxWidth(300);
                dateofb.setStyle("-fx-font-size:11");

                TextField log = new TextField();
                Text logtext = new Text("*Login");

                PasswordField pass = new PasswordField();
                Text passtext = new Text("*Password");

                TextField phonenumber = new TextField();
                Text phonenumbertext = new Text("Phone_number");

                TextField position = new TextField();
                Text positiontext = new Text("Position");

                TextField salary = new TextField();
                Text salarytext = new Text("Salary");

                TextField image = new TextField();
                Text imagetext = new Text("*Image");

                Button saveImage = new Button("Select Image");
                FileChooser fc = new FileChooser();

                Button reg = new Button("Register");
                reg.setStyle("-fx-background-color:green");

                nametext.setTranslateX(60);
                nametext.setTranslateY(100);
                name.setTranslateX(160);
                name.setTranslateY(82);

                snametext.setTranslateX(60);
                snametext.setTranslateY(140);
                sname.setTranslateX(160);
                sname.setTranslateY(122);

                dateofbtext.setTranslateX(60);
                dateofbtext.setTranslateY(180);
                dateofb.setTranslateX(160);
                dateofb.setTranslateY(162);

                logtext.setTranslateX(60);
                logtext.setTranslateY(220);
                log.setTranslateX(160);
                log.setTranslateY(202);

                passtext.setTranslateX(60);
                passtext.setTranslateY(260);
                pass.setTranslateX(160);
                pass.setTranslateY(242);

                phonenumbertext.setTranslateX(60);
                phonenumbertext.setTranslateY(300);
                phonenumber.setTranslateX(160);
                phonenumber.setTranslateY(282);

                positiontext.setTranslateX(60);
                positiontext.setTranslateY(340);
                position.setTranslateX(160);
                position.setTranslateY(322);

                salarytext.setTranslateX(60);
                salarytext.setTranslateY(380);
                salary.setTranslateX(160);
                salary.setTranslateY(362);

                imagetext.setTranslateX(60);
                imagetext.setTranslateY(420);
                image.setTranslateX(160);
                image.setTranslateY(402);

                reg.setTranslateX(180);
                reg.setTranslateY(440);

                Text error = new Text("You need to fill in all the required fields(*)");
                error.setTranslateX(80);
                error.setTranslateY(520);
                error.setFill(Color.RED);
                error.setVisible(false);
                
                ArrayList<String> allLogins = new ArrayList<>();
                try {
                    ResultSet rs = connectToDB().executeQuery("select login from users");
                    while(rs.next()){
                        allLogins.add(rs.getString(1));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                }

                Desktop desktop = Desktop.getDesktop();
                saveImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        fileImage = fc.showOpenDialog(regStage);

                    }
                });

                reg.setOnAction(new EventHandler<ActionEvent>() {//Логин

                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(fileImage.getName());
                        if (name.getText().toString().equals("") || dateofb.getValue().toString().equals("") || sname.getText().toString().equals("") || log.getText().toString().equals("") || pass.getText().toString().equals("") || image.getText().toString().equals("")) {
                            error.setVisible(true);
                        } else {
                            regStage.close();
                            loginStage.show();//Логин
                            String logdb = log.getText().toString();
                            String passdb = pass.getText().toString();
                            int phone_numbdb = Integer.parseInt(phonenumber.getText().toString());
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

                                ps.setBinaryStream(1, (FileInputStream) stream, (int) fileImage.length());
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
                        salary, salarytext, image, imagetext, reg, error, saveImage);
                regStage.setTitle("Register");
                regStage.setScene(new Scene(regroot, 400, 600));
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
class Item{
    String text;
    ImageView imgv;
    
    public Item(String text, Image img){
        this.text = text;
        this.imgv = new ImageView(img);
        imgv.setFitWidth(70);
        imgv.setFitHeight(70);
    }
}