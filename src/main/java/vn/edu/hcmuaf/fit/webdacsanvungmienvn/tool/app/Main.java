package vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.app;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.controller.Controller;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.view.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            new Controller(view);
            view.setVisible(true);
        });
    }
}
