import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



public class Main {
    public static void main(String[] args) {
        new USER_INTERFACE();
        // IP_API class is used in USER_INTERFACE.getText

    }
}

class IP_API {

    static String fetchIP(String ip) {
        StringBuilder url = new StringBuilder();
        url.append("https://ipinfo.io/");
        if(ip.equals("self"))
            url.append("ip");
        else {
            url.append("widget/demo/");
            url.append(ip);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url.toString()))
                .build();
        try {
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            url.setLength(0);
            return response.body();
        } catch (IOException | InterruptedException e) {
            if(e instanceof java.net.ConnectException)
            {
                return "Connection Err";
            }
            throw new RuntimeException(e);
        }
    }


}

class USER_INTERFACE extends JFrame {
    final private JTextField ip_input =  new JTextField();
    final private JTextArea displayArea = new JTextArea();
    final private String help_msg = """
            TYPE THE IP ADDRESS, YOU WANT TO SCAN.
            TYPE "SELF" TO GET YOUR OWN IP".
            HELP -> DISPLAY THIS MESSAGE""";
    USER_INTERFACE() {
        super();
        this.setSize(new Dimension(500, 400));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLUE);


        input();
        textarea();
        this.setVisible(true);
    }

    void getIP() {
        String ip = this.ip_input.getText();
        if(ip.equals("HELP") || ip.equals("help"))
            displayArea.setText(help_msg);
        else {
            String info = IP_API.fetchIP(this.ip_input.getText());
            if(info.equals("Connection Err"))
                displayArea.setText("Cant Connect");
            else
                displayArea.setText(info);
        }
    }

    void input() {
        this.ip_input.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        this.ip_input.setBackground(Color.black);
        this.ip_input.setForeground(Color.white);
        this.ip_input.addActionListener(e -> getIP());
        this.add(this.ip_input, BorderLayout.NORTH);
    }

    void textarea() {
        this.displayArea.setBorder(new LineBorder(Color.GREEN, 2));
        this.displayArea.setOpaque(false);
        this.displayArea.setEditable(false);
        this.displayArea.setForeground(Color.white);
        this.displayArea.setText(help_msg);
        JScrollPane scroll = new JScrollPane(this.displayArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        this.add(scroll);
    }
}