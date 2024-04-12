module dev.cesarele23.chatudp {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.cesarele23.chatudp to javafx.fxml;
    exports dev.cesarele23.chatudp;
}