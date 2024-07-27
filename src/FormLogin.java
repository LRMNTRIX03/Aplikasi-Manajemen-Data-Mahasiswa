import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FormLogin extends JFrame {
    private JButton btnLogin;
    private JTextField etUsername;
    private JLabel judul;
    private JLabel txtPW;
    private JLabel txtUsername;
    private JPasswordField etPW;
    private JPanel Main;

    public FormLogin(){
        setTitle("Form Login");
        setContentPane(Main);
        setSize(300,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = etUsername.getText().trim();
                String password = etPW.getText().trim();
                if(username.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(FormLogin.this, "Username dan Password Tidak Boleh Kosong!");
                }
                else{
                validateLogin(username, password);
            }
            }
        });
    }
    public void validateLogin(String u, String P){
        Koneksi koneksi = new Koneksi();
        Connection connection = koneksi.getConnection();
        try{
        String sql = "Select username, password from admin where username = ? and password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, u);
        preparedStatement.setString(2, P);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            FormMahasiswa formMahasiswa = new FormMahasiswa();
            dispose();
            formMahasiswa.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(FormLogin.this, "User Tidak Ditemukan");
        }

        }
        catch (Exception er){
            JOptionPane.showMessageDialog(FormLogin.this, "Error : " + er.getMessage());
        }


    }
}
