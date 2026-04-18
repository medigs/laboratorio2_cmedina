import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReinicioClave extends JDialog {

    private JPasswordField txtClaveActual;
    private JPasswordField txtNuevaClave;
    private JPasswordField txtConfirmaClave;
    private JComboBox<String> cmbUsuario;
    private JButton btnCambiar;
    private JButton btnCancelar;

    private final String usuarioSesion;
    private final boolean esAdmin;

    public ReinicioClave(JFrame parent, String usuarioSesion) {
        super(parent, "Reinicio de Clave", true);
        this.usuarioSesion = usuarioSesion;
        this.esAdmin = "admin".equals(usuarioSesion);
        setSize(480, esAdmin ? 430 : 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelPrincipal.setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Cambio de Contraseña", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTitulo.setForeground(new Color(30, 40, 60));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(225, 232, 240));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                esAdmin ? "Modo Administrador — Cambiar clave de cualquier usuario"
                        : "Usuario: " + usuarioSesion));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        int fila = 0;

        // Admin ve selector de usuario; usuario normal no
        if (esAdmin) {
            gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.4;
            panelForm.add(etiqueta("Usuario a cambiar:"), gbc);

            cmbUsuario = new JComboBox<>();
            for (String u : Login.usuarios.keySet()) {
                if (!MantenimientoUsuarios.usuariosInactivos.contains(u))
                    cmbUsuario.addItem(u);
            }
            cmbUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
            gbc.gridx = 1; gbc.weightx = 0.6;
            panelForm.add(cmbUsuario, gbc);
            fila++;

            // Admin no necesita ingresar la clave actual
            JLabel lblInfo = new JLabel("<html><i>Como administrador no necesita ingresar la clave actual.</i></html>");
            lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblInfo.setForeground(new Color(100, 100, 150));
            gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
            panelForm.add(lblInfo, gbc);
            gbc.gridwidth = 1;
            fila++;
        } else {
            // Usuario normal ingresa su clave actual
            gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.4;
            panelForm.add(etiqueta("Contraseña actual:"), gbc);
            txtClaveActual = new JPasswordField();
            gbc.gridx = 1; gbc.weightx = 0.6;
            panelForm.add(txtClaveActual, gbc);
            fila++;
        }

        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.4;
        panelForm.add(etiqueta("Nueva contraseña:"), gbc);
        txtNuevaClave = new JPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.6;
        panelForm.add(txtNuevaClave, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.4;
        panelForm.add(etiqueta("Confirmar nueva clave:"), gbc);
        txtConfirmaClave = new JPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.6;
        panelForm.add(txtConfirmaClave, gbc);
        fila++;

        JLabel lblRequisitos = new JLabel("<html><b>Requisitos:</b> mínimo 13 caracteres, 1 mayúscula, 1 carácter especial</html>");
        lblRequisitos.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRequisitos.setForeground(new Color(80, 80, 80));
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        panelForm.add(lblRequisitos, gbc);
        fila++;

        // Barra de fortaleza
        JProgressBar barraFortaleza = new JProgressBar(0, 100);
        barraFortaleza.setStringPainted(true);
        barraFortaleza.setString("Sin evaluar");
        JPanel panelBarra = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBarra.setBackground(new Color(225, 232, 240));
        panelBarra.add(new JLabel("Fortaleza:"));
        panelBarra.add(barraFortaleza);
        gbc.gridy = fila; gbc.gridwidth = 2;
        panelForm.add(panelBarra, gbc);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 244, 248));
        btnCambiar = boton("Cambiar Clave", new Color(52, 152, 219));
        btnCancelar = boton("Cancelar", new Color(149, 165, 166));
        panelBotones.add(btnCambiar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnCambiar.addActionListener(e -> cambiarClave());
        btnCancelar.addActionListener(e -> dispose());

        txtNuevaClave.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String clave = new String(txtNuevaClave.getPassword());
                int score = calcularFortaleza(clave);
                barraFortaleza.setValue(score);
                if (score < 40) {
                    barraFortaleza.setForeground(new Color(231, 76, 60));
                    barraFortaleza.setString("Débil");
                } else if (score < 70) {
                    barraFortaleza.setForeground(new Color(230, 126, 34));
                    barraFortaleza.setString("Media");
                } else {
                    barraFortaleza.setForeground(new Color(46, 204, 113));
                    barraFortaleza.setString("Fuerte");
                }
            }
        });
    }

    private void cambiarClave() {
        String nueva = new String(txtNuevaClave.getPassword());
        String confirma = new String(txtConfirmaClave.getPassword());

        if (nueva.isEmpty() || confirma.isEmpty()) {
            error("Los campos de nueva contraseña son obligatorios.");
            return;
        }

        // Determinar a qué usuario se le cambia la clave
        String usuarioObjetivo = esAdmin ? (String) cmbUsuario.getSelectedItem() : usuarioSesion;

        if (!esAdmin) {
            String actual = new String(txtClaveActual.getPassword());
            if (actual.isEmpty()) { error("Ingrese su contraseña actual."); return; }
            if (!actual.equals(Login.usuarios.get(usuarioObjetivo))) {
                error("La contraseña actual es incorrecta.");
                txtClaveActual.setText("");
                return;
            }
            if (nueva.equals(actual)) {
                error("La nueva contraseña no puede ser igual a la actual.");
                return;
            }
        }

        if (!nueva.equals(confirma)) {
            error("Las contraseñas nuevas no coinciden.");
            txtConfirmaClave.setText("");
            return;
        }

        if (!validarClave(nueva)) {
            error("La contraseña no cumple los requisitos:\n• Mínimo 13 caracteres\n• Al menos 1 mayúscula\n• Al menos 1 carácter especial");
            return;
        }

        Login.usuarios.put(usuarioObjetivo, nueva);
        JOptionPane.showMessageDialog(this,
                "Contraseña del usuario '" + usuarioObjetivo + "' actualizada exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private boolean validarClave(String clave) {
        if (clave.length() < 13) return false;
        boolean tieneMayuscula = clave.chars().anyMatch(Character::isUpperCase);
        boolean tieneEspecial = clave.chars().anyMatch(c ->
                "!@#$%^&*()_+-=[]{}|;':\",./<>?".indexOf(c) >= 0);
        return tieneMayuscula && tieneEspecial;
    }

    private int calcularFortaleza(String clave) {
        int score = 0;
        if (clave.length() >= 8) score += 20;
        if (clave.length() >= 13) score += 20;
        if (clave.chars().anyMatch(Character::isUpperCase)) score += 20;
        if (clave.chars().anyMatch(Character::isLowerCase)) score += 10;
        if (clave.chars().anyMatch(Character::isDigit)) score += 15;
        if (clave.chars().anyMatch(c -> "!@#$%^&*()_+-=[]{}|;':\",./<>?".indexOf(c) >= 0)) score += 15;
        return Math.min(score, 100);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    private JButton boton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
