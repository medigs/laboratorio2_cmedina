import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JButton btnCancelar;

    static HashMap<String, String> usuarios = new HashMap<>();
    static HashMap<String, Integer> intentosFallidos = new HashMap<>();

    static {
        usuarios.put("admin", "Admin123!");
        usuarios.put("usuario1", "Clave2024@");
    }

    public Login() {
        setTitle("Laboratorio 2 - Iniciar Sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(30, 40, 60));

        JLabel lblTitulo = new JLabel("LABORATORIO 2", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(45, 58, 80));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelForm.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        panelForm.add(txtUsuario, gbc);

        JLabel lblClave = new JLabel("Contraseña:");
        lblClave.setForeground(Color.WHITE);
        lblClave.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelForm.add(lblClave, gbc);

        txtClave = new JPasswordField();
        txtClave.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        panelForm.add(txtClave, gbc);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panelBotones.setBackground(new Color(30, 40, 60));

        btnIngresar = new JButton("Ingresar");
        estilizarBoton(btnIngresar, new Color(52, 152, 219));

        btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnCancelar, new Color(149, 165, 166));

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnIngresar.addActionListener(e -> validarLogin());
        btnCancelar.addActionListener(e -> System.exit(0));
        txtClave.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) validarLogin();
            }
        });
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (usuario.isEmpty() || clave.isEmpty()) {
            mostrarError("Por favor ingrese usuario y contraseña.");
            return;
        }

        // Verificar si ya está desactivado antes de validar clave
        if (MantenimientoUsuarios.usuariosInactivos.contains(usuario)) {
            mostrarError("El usuario '" + usuario + "' está desactivado.\nContacte al administrador.");
            txtClave.setText("");
            return;
        }

        if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(clave)) {
            intentosFallidos.put(usuario, 0);
            dispose();
            new MenuPrincipal(usuario).setVisible(true);
        } else {
            int intentos = intentosFallidos.getOrDefault(usuario, 0) + 1;
            intentosFallidos.put(usuario, intentos);
            int restantes = 3 - intentos;

            if (intentos >= 3 && usuarios.containsKey(usuario)) {
                // Auto-desactivar usuario por exceso de intentos
                MantenimientoUsuarios.usuariosInactivos.add(usuario);
                intentosFallidos.put(usuario, 0);
                txtClave.setText("");
                txtUsuario.setText("");
                JOptionPane.showMessageDialog(this,
                        "Se ha excedido el número de intentos permitidos.\n" +
                        "El usuario '" + usuario + "' ha sido desactivado.\n" +
                        "Contacte al administrador para reactivar su cuenta.",
                        "Cuenta bloqueada", JOptionPane.ERROR_MESSAGE);
            } else {
                String msg = "Credenciales incorrectas.";
                if (usuarios.containsKey(usuario) && restantes > 0)
                    msg += "\nIntentos restantes: " + restantes;
                mostrarError(msg);
                txtClave.setText("");
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
    }

    private void estilizarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new Login().setVisible(true);
        });
    }
}
