package flex;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label logoHolder, prompt, usernameprompt, passwordprompt, emailprompt, invalid;
    @FXML
    private Button login, signup, xbutton, minbutton, submit;
    @FXML
    private Pane sidePane;
    @FXML
    private TextField username, password, email;
    @FXML
    private Rectangle rect1, rect2, rect3, rect4;
    @FXML
    private AnchorPane root;

    public static String FINAL_USER = null;
    private int usercheck = 0, passcheck = 0, emailcheck = 0;
    static String submitStyle;
    public boolean isOut = false, repeat = false, isBlue = true, completedMotion = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleLogoAnimation();
        addListeners();
        submitStyle = submit.getStyle();
    }

    private void handleLogoAnimation() {

        AnimationProfile handleLogo = new AnimationProfile(logoHolder);
        handleLogo.setFade(2300);
        logoHolder.setVisible(true);
        handleLogo.beginCombinedAnim(1, false);

        handleLogo.pt.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                beginPromptAnim();
            }
        });

    }

    private void beginPromptAnim() {
        AnimationProfile promptAnim = new AnimationProfile(prompt);
        promptAnim.setFade(1000);
        promptAnim.setTranslate(600, 0, 35);

        AnimationProfile fSignUp = new AnimationProfile(signup);
        fSignUp.setFade(1000);

        AnimationProfile fLogin = new AnimationProfile(login);
        fLogin.setFade(1000);

        prompt.setVisible(true);
        login.setVisible(true);
        signup.setVisible(true);

        promptAnim.beginCombinedAnim(1, false);
        fLogin.beginCombinedAnim(1, false);
        fSignUp.beginCombinedAnim(1, false);
        handleRectAnim();

    }

    private void addListeners() {
        username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("") && newValue != null) {
                    usercheck = 1;
                } else {
                    usercheck = 0;
                }
                verifyFields();
            }
        });

        password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("") && newValue != null) {
                    passcheck = 1;
                } else {
                    passcheck = 0;
                }
                verifyFields();
            }
        });

        username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("") && newValue != null) {
                    emailcheck = 1;
                } else {
                    emailcheck = 0;
                }
                verifyFields();
            }
        });
    }

    @FXML
    private void signUpHandle(ActionEvent event) {
        if (completedMotion && (!isOut || !isBlue)) {
            isBlue = true;
            offSubmit();
            usercheck = 0;
            passcheck = 0;
            emailcheck = 0;
            invalid.setText(null);
            movePane();
        }
    }

    @FXML
    private void loginHandle(ActionEvent event) {
        if (completedMotion && (!isOut || isBlue)) {
            isBlue = false;
            offSubmit();
            usercheck = 0;
            passcheck = 0;
            emailcheck = 0;
            invalid.setText(null);
            movePane();
        }
    }

    @FXML
    private void Xhandle(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void minHandle(ActionEvent event) {
        Stage localStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        localStage.setIconified(true);
    }

    @FXML
    private void submitHandle(ActionEvent event) throws SQLException {
        if (checkInput()) {
            handleData data1 = new handleData();
            if (isBlue) {
                data1.addUser(username.getText(), password.getText(), email.getText());
                FINAL_USER = username.getText();
                initSwitch();
            } else {
                boolean valid = data1.signIn(username.getText(), password.getText());
                if (valid) {
                    FINAL_USER = username.getText();
                    initSwitch();
                } else {
                    invalid.setText("Invalid username or\npassword");
                }
            }
            data1.closeConnection();
        }
    }

    private void verifyFields() {
        if (isBlue) {
            if (usercheck == 1 && passcheck == 1 && emailcheck == 1) {
                reSubmit();
            } else {
                offSubmit();
            }

        } else {
            if (usercheck == 1 && passcheck == 1) {
                reSubmit();
            } else {
                offSubmit();
            }
        }
    }

    private void reSubmit() {
        submit.setDisable(false);
        submit.setOpacity(1);
        submit.setStyle(submitStyle + "-fx-cursor: OPEN_HAND");
    }

    private void offSubmit() {
        submit.setDisable(true);
        submit.setOpacity(0.37);
        submit.setStyle(submitStyle + "-fx-cursor: NONE");
    }

    private boolean checkInput() {
        int invalidnum = 0;
        if (username.getText().length() < 6 || password.getText().length() < 6) {
            invalid.setText("User/Pass must be greater\nthan 5 characters.");
            invalid.setVisible(true);
            invalidnum++;
        } else if (username.getText().length() > 25 || password.getText().length() > 25) {
            invalid.setText("User/Pass must be less\nthan 30 characters.");
            invalid.setVisible(true);
            invalidnum++;
        }
        return (invalidnum == 0);
    }

    private void movePane() {
        sidePane.toBack();
        int scaleX = 40;
        if (isOut) {
            scaleX = -40;
            isOut = false;
            repeat = true;
            Fields(false, true);
        } else {
            isOut = true;
            repeat = false;
        }

        if (scaleX == 40) {
            if (!isBlue) {
                sidePane.setStyle("-fx-background-color: darkcyan");
            } else {
                sidePane.setStyle("-fx-background-color: steelBlue");
            }
        }
        completedMotion = false;
        AnimationProfile sPane = new AnimationProfile(sidePane);
        sPane.setScale(300, scaleX, 0);
        sPane.beginCombinedAnim(1, false);
        sPane.pt.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (isOut) {
                    Fields(true, false);
                }
                if (repeat) {
                    movePane();
                } else {
                    completedMotion = true;
                }
            }
        });
    }

    private void Fields(boolean vis, boolean dis) {

        //resettext if necessary
        if (isOut) {
            if (isBlue) {
                usernameprompt.setText("Enter username:");
                passwordprompt.setText("Enter password:");
                emailprompt.setVisible(true);
                emailprompt.setDisable(false);
                email.setText(null);
                email.setVisible(true);
                email.setDisable(false);

                submit.setDisable(false);
                submit.setVisible(true);
            } else {
                usernameprompt.setText("Username:");
                passwordprompt.setText("Password:");

            }
        }

        //enable/hide/show fields
        usernameprompt.setVisible(vis);
        passwordprompt.setVisible(vis);
        username.setVisible(vis);
        username.setText(null);
        password.setVisible(vis);
        password.setText(null);
        submit.setVisible(vis);

        usernameprompt.setDisable(dis);
        passwordprompt.setDisable(dis);
        username.setDisable(dis);
        password.setDisable(dis);

        if (!isOut) {
            emailprompt.setVisible(false);
            emailprompt.setDisable(true);
            email.setVisible(false);
            email.setDisable(true);
        }
    }

    private void handleRectAnim() {
        rect1.setVisible(true);
        rect2.setVisible(true);
        rect3.setVisible(true);
        rect4.setVisible(true);
        startAnimation();
    }

    private void startAnimation() {
        Node allNodes[] = {rect1, rect2, rect3, rect4};
        AnimationProfile[] fadeAnims = new AnimationProfile[allNodes.length];
        for (int i = 0; i < allNodes.length; i++) {
            fadeAnims[i] = new AnimationProfile(allNodes[i]);
            fadeAnims[i].setFade(600);
            fadeAnims[i].beginCombinedAnim(1, false);
        }

        AnimationProfile[] scaleAnims = new AnimationProfile[allNodes.length];
        for (int i = 0; i < allNodes.length; i++) {
            scaleAnims[i] = new AnimationProfile(allNodes[i]);
            int randMultiplier = (int) (Math.random() * 10) + 5;
            scaleAnims[i].setScale(randMultiplier / 3 * 280, -1 * randMultiplier * (i + 1), 0);
            scaleAnims[i].setScale(280, randMultiplier * (i + 1), 0);
            scaleAnims[i].beginCombinedAnim(INDEFINITE, true);
        }
    }
    
   private void initSwitch(){
       AnimationProfile fadeCurrentScene=new AnimationProfile (root);
       fadeCurrentScene.setTranslate(1000, 600, 0);
       fadeCurrentScene.beginCombinedAnim(1, false);
   }
}

class AnimationProfile {

    protected ArrayList<FadeTransition> ft;
    protected ArrayList<TranslateTransition> tt;
    protected ArrayList<ScaleTransition> st;
    protected ParallelTransition pt;
    protected SequentialTransition qt;

    protected Node node;

    public AnimationProfile(Node _node) {
        node = _node;
        ft = new ArrayList<FadeTransition>();
        tt = new ArrayList<TranslateTransition>();
        st = new ArrayList<ScaleTransition>();
    }

    protected void setFade(int durationInMs) {
        FadeTransition ft1 = new FadeTransition(Duration.millis(durationInMs), node);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);
        ft1.setCycleCount(1);
        ft1.setAutoReverse(false);
        ft.add(ft1);
    }

    protected void setTranslate(int duration, double xmove, double ymove) {
        TranslateTransition tt1 = new TranslateTransition(Duration.millis(duration), node);
        tt1.setByX(xmove);
        tt1.setByY(ymove);
        tt1.setCycleCount(1);
        tt1.setAutoReverse(false);
        tt.add(tt1);
    }

    protected void setScale(int duration, double xmove, double ymove) {
        ScaleTransition st1 = new ScaleTransition(Duration.millis(duration), node);
        st1.setByX(xmove);
        st1.setByY(ymove);
        st1.setCycleCount(1);
        st1.setAutoReverse(false);
        st.add(st1);
    }

    protected void beginCombinedAnim(int cycle, boolean isSeq) {
        pt = new ParallelTransition();
        qt = new SequentialTransition();
        if (ft != null) {
            if (isSeq) {
                for (FadeTransition ft1 : ft) {
                    qt.getChildren().add(ft1);
                }
            } else {
                for (FadeTransition ft1 : ft) {
                    pt.getChildren().add(ft1);
                }
            }
        }
        if (tt != null) {
            if (isSeq) {
                for (TranslateTransition tt1 : tt) {
                    qt.getChildren().add(tt1);
                }
            } else {
                for (TranslateTransition tt1 : tt) {
                    pt.getChildren().add(tt1);
                }
            }
        }
        if (st != null) {
            if (isSeq) {
                for (ScaleTransition st1 : st) {
                    qt.getChildren().add(st1);
                }
            } else {
                for (ScaleTransition st1 : st) {
                    pt.getChildren().add(st1);
                }
            }
        }
        if (isSeq) {
            qt.setCycleCount(cycle);
            qt.setAutoReverse(false);
            qt.play();
        } else {
            pt.setCycleCount(cycle);
            pt.setAutoReverse(false);
            pt.play();
        }
    }
}
