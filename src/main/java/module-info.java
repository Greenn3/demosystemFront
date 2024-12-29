module com.example.demosystemfront {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.calendarfx.view;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.pdfbox;
  //  requires org.apache.poi.poi;
  //  requires org.apache.poi.ooxml;



   // requires org.apache.poi.poi.ooxml;
   // requires org.apache.xmlbeans;
    requires org.apache.commons.collections4;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop; // If you're using it
   // requires org.apache.commons.math3; // If you're using it

    opens com.example.demosystemfront to javafx.fxml, com.google.gson;
   // opens java.time to com.google.gson;
    exports com.example.demosystemfront;
    exports com.example.demosystemfront.Entities;
    opens com.example.demosystemfront.Entities to com.google.gson, javafx.fxml;
    exports com.example.demosystemfront.Controllers;
    opens com.example.demosystemfront.Controllers to com.google.gson, javafx.fxml;
}
