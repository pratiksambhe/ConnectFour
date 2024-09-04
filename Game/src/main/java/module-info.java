module com.example.game {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
                        
    opens com.example.game to javafx.fxml;
    exports com.example.game;
}