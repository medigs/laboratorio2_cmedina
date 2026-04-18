import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.HashMap;

public class MantenimientoUsuarios extends JDialog {

    static HashSet<String> usuariosInactivos = new HashSet<>();
    static HashMap<String, String> roles = new HashMap<>();

    static {
        roles.put("admin", "Administrador");
        roles.put("usuario1", "Usuario");
        roles.put("usuario2", "Usuario");
        roles.put("usuario3", "Usuario");
        roles.put("usuario4", "Usuario");
        roles.put("usuario5", "Usuario");
    }

    private DefaultTableModel modeloActivos;
    private DefaultTableModel modeloInactivos;
    private JTable tablaActivos;
    private JTable tablaInactivos;
    private JTextField txtNuevoUsuario;
    private JPasswordField txtNuevaClave;
    private JPasswordField txtConfirmaClave;
    private JComboBox<String> cmbRol;

    private final boolean esAdmin;

    public MantenimientoUsuarios(JFrame parent, String usuarioSesion) {
        super(parent, "Mantenimiento de Usuarios", true);
        this.esAdmin = "admin".equals(usuarioSesion);
        setSize(660, 540);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelPrincipal.setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Gestión de Usuarios del Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 40, 60));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ── JTabbedPane ────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Tab Activos
        modeloActivos = new DefaultTableModel(new String[]{"Usuario", "Rol", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (String u : Login.usuarios.keySet()) {
            if (!usuariosInactivos.contains(u))
                modeloActivos.addRow(new Object[]{u, roles.getOrDefault(u, "Usuario"), "Activo"});
        }
        tablaActivos = crearTabla(modeloActivos);
        JScrollPane scrollActivos = new JScrollPane(tablaActivos);

        JPanel panelActivos = new JPanel(new BorderLayout(0, 8));
        panelActivos.setBackground(new Color(240, 244, 248));
        panelActivos.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        panelActivos.add(scrollActivos, BorderLayout.CENTER);

        JButton btnCambiarRol = boton("Cambiar Rol", new Color(155, 89, 182));
        btnCambiarRol.setEnabled(esAdmin);
        if (!esAdmin) btnCambiarRol.setToolTipText("Solo el administrador puede cambiar roles");

        JButton btnDesactivar = boton("Desactivar Usuario", new Color(231, 76, 60));
        btnDesactivar.setEnabled(esAdmin);
        if (!esAdmin) btnDesactivar.setToolTipText("Solo el administrador puede desactivar usuarios");

        JPanel pbActivos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        pbActivos.setBackground(new Color(240, 244, 248));
        pbActivos.add(btnCambiarRol);
        pbActivos.add(btnDesactivar);
        panelActivos.add(pbActivos, BorderLayout.SOUTH);
        tabs.addTab("Usuarios Activos", panelActivos);

        // Tab Inactivos
        modeloInactivos = new DefaultTableModel(new String[]{"Usuario", "Rol", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (String u : usuariosInactivos) {
            modeloInactivos.addRow(new Object[]{u, roles.getOrDefault(u, "Usuario"), "Inactivo"});
        }
        tablaInactivos = crearTabla(modeloInactivos);
        JScrollPane scrollInactivos = new JScrollPane(tablaInactivos);

        JPanel panelInactivos = new JPanel(new BorderLayout(0, 8));
        panelInactivos.setBackground(new Color(240, 244, 248));
        panelInactivos.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        panelInactivos.add(scrollInactivos, BorderLayout.CENTER);

        JButton btnReactivar = boton("Reactivar Usuario", new Color(46, 204, 113));
        btnReactivar.setEnabled(esAdmin);
        if (!esAdmin) btnReactivar.setToolTipText("Solo el administrador puede reactivar usuarios");

        JPanel pbInactivos = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        pbInactivos.setBackground(new Color(240, 244, 248));
        pbInactivos.add(btnReactivar);
        panelInactivos.add(pbInactivos, BorderLayout.SOUTH);
        tabs.addTab("Usuarios Inactivos", panelInactivos);

        panelPrincipal.add(tabs, BorderLayout.CENTER);

        // ── Formulario agregar (solo admin) ────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(225, 232, 240));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                esAdmin ? "Agregar Nuevo Usuario" : "Agregar Nuevo Usuario  [Solo administrador]"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        panelForm.add(etiqueta("Usuario:"), gbc);
        txtNuevoUsuario = new JTextField();
        txtNuevoUsuario.setEnabled(esAdmin);
        gbc.gridx = 1; gbc.weightx = 0.75;
        panelForm.add(txtNuevoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.25;
        panelForm.add(etiqueta("Contraseña:"), gbc);
        txtNuevaClave = new JPasswordField();
        txtNuevaClave.setEnabled(esAdmin);
        gbc.gridx = 1; gbc.weightx = 0.75;
        panelForm.add(txtNuevaClave, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.25;
        panelForm.add(etiqueta("Confirmar:"), gbc);
        txtConfirmaClave = new JPasswordField();
        txtConfirmaClave.setEnabled(esAdmin);
        gbc.gridx = 1; gbc.weightx = 0.75;
        panelForm.add(txtConfirmaClave, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.25;
        panelForm.add(etiqueta("Rol:"), gbc);
        cmbRol = new JComboBox<>(new String[]{"Usuario", "Administrador"});
        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbRol.setEnabled(esAdmin);
        gbc.gridx = 1; gbc.weightx = 0.75;
        panelForm.add(cmbRol, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        panelBotones.setBackground(new Color(240, 244, 248));
        JButton btnAgregar = boton("Agregar", new Color(52, 152, 219));
        btnAgregar.setEnabled(esAdmin);
        JButton btnCerrar = boton("Cerrar", new Color(149, 165, 166));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCerrar);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(new Color(240, 244, 248));
        panelSur.add(panelForm, BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal);

        // ── Eventos ────────────────────────────────────────────────
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnCerrar.addActionListener(e -> dispose());

        btnCambiarRol.addActionListener(e -> {
            int fila = tablaActivos.getSelectedRow();
            if (fila < 0) { error("Seleccione un usuario para cambiar su rol."); return; }
            String usuario = (String) modeloActivos.getValueAt(fila, 0);
            if ("admin".equals(usuario)) { error("No se puede cambiar el rol del administrador principal."); return; }
            String rolActual = roles.getOrDefault(usuario, "Usuario");
            String[] opciones = {"Usuario", "Administrador"};
            String nuevoRol = (String) JOptionPane.showInputDialog(this,
                    "Seleccione el nuevo rol para '" + usuario + "':",
                    "Cambiar Rol", JOptionPane.PLAIN_MESSAGE, null,
                    opciones, rolActual);
            if (nuevoRol != null && !nuevoRol.equals(rolActual)) {
                roles.put(usuario, nuevoRol);
                modeloActivos.setValueAt(nuevoRol, fila, 1);
                JOptionPane.showMessageDialog(this,
                        "Rol de '" + usuario + "' actualizado a '" + nuevoRol + "'.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnDesactivar.addActionListener(e -> {
            int fila = tablaActivos.getSelectedRow();
            if (fila < 0) { error("Seleccione un usuario activo para desactivar."); return; }
            String usuario = (String) modeloActivos.getValueAt(fila, 0);
            if ("admin".equals(usuario)) { error("No se puede desactivar al usuario administrador."); return; }
            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Desactivar al usuario '" + usuario + "'?\nPodrá reactivarlo desde la pestaña de inactivos.",
                    "Confirmar desactivación", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                usuariosInactivos.add(usuario);
                modeloActivos.removeRow(fila);
                modeloInactivos.addRow(new Object[]{usuario, roles.getOrDefault(usuario, "Usuario"), "Inactivo"});
            }
        });

        btnReactivar.addActionListener(e -> {
            int fila = tablaInactivos.getSelectedRow();
            if (fila < 0) { error("Seleccione un usuario inactivo para reactivar."); return; }
            String usuario = (String) modeloInactivos.getValueAt(fila, 0);
            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Reactivar al usuario '" + usuario + "'?",
                    "Confirmar reactivación", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                usuariosInactivos.remove(usuario);
                Login.intentosFallidos.put(usuario, 0);
                modeloInactivos.removeRow(fila);
                modeloActivos.addRow(new Object[]{usuario, roles.getOrDefault(usuario, "Usuario"), "Activo"});
            }
        });
    }

    private void agregarUsuario() {
        String usuario = txtNuevoUsuario.getText().trim();
        String clave = new String(txtNuevaClave.getPassword());
        String confirma = new String(txtConfirmaClave.getPassword());
        String rol = (String) cmbRol.getSelectedItem();

        if (usuario.isEmpty() || clave.isEmpty()) { error("Todos los campos son obligatorios."); return; }
        if (usuario.length() < 3) { error("El usuario debe tener al menos 3 caracteres."); return; }
        if (Login.usuarios.containsKey(usuario)) { error("El usuario '" + usuario + "' ya existe."); return; }
        if (!clave.equals(confirma)) { error("Las contraseñas no coinciden."); return; }
        if (!validarClave(clave)) {
            error("La contraseña debe tener mínimo 13 caracteres,\n1 letra mayúscula y 1 carácter especial.");
            return;
        }

        Login.usuarios.put(usuario, clave);
        roles.put(usuario, rol);
        modeloActivos.addRow(new Object[]{usuario, rol, "Activo"});
        txtNuevoUsuario.setText("");
        txtNuevaClave.setText("");
        txtConfirmaClave.setText("");
        cmbRol.setSelectedIndex(0);
        JOptionPane.showMessageDialog(this, "Usuario '" + usuario + "' agregado con rol '" + rol + "'.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean validarClave(String clave) {
        if (clave.length() < 13) return false;
        boolean tieneMayuscula = clave.chars().anyMatch(Character::isUpperCase);
        boolean tieneEspecial = clave.chars().anyMatch(c ->
                "!@#$%^&*()_+-=[]{}|;':\",./<>?".indexOf(c) >= 0);
        return tieneMayuscula && tieneEspecial;
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setRowHeight(26);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return t;
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
        btn.setPreferredSize(new Dimension(160, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
