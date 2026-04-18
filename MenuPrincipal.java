import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {

    private String usuarioActual;

    public MenuPrincipal(String usuario) {
        this.usuarioActual = usuario;
        setTitle("Laboratorio 2 - Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // ── JMenuBar ──────────────────────────────────────────────
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 40, 60));
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JMenuItem itemMantenimiento = crearMenuItem("Mantenimiento de Usuarios");
        JMenuItem itemReinicio = crearMenuItem("Reinicio de Clave");
        JMenu menuCerrar = crearMenu("Cerrar Sesión");

        menuBar.add(itemMantenimiento);
        menuBar.add(itemReinicio);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuCerrar);

        setJMenuBar(menuBar);

        // ── Panel principal ───────────────────────────────────────
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(240, 244, 248));

        // Bienvenida
        JPanel panelBienvenida = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelBienvenida.setBackground(new Color(52, 73, 94));
        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioActual);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelBienvenida.add(lblBienvenida);
        panelCentral.add(panelBienvenida, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentro.setBackground(new Color(240, 244, 248));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        JPanel tarjetaMantenimiento = crearTarjeta("Mantenimiento de Usuarios", "Agregar y eliminar usuarios", new Color(52, 152, 219));
        JPanel tarjetaReinicio = crearTarjeta("Reinicio de Clave", "Cambiar contraseña de usuario", new Color(46, 204, 113));

        tarjetaMantenimiento.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new MantenimientoUsuarios(MenuPrincipal.this, usuarioActual).setVisible(true);
            }
        });
        tarjetaReinicio.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new ReinicioClave(MenuPrincipal.this, usuarioActual).setVisible(true);
            }
        });

        panelCentro.add(tarjetaMantenimiento);
        panelCentro.add(tarjetaReinicio);
        panelCentral.add(panelCentro, BorderLayout.CENTER);

        // Barra de estado
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        panelEstado.setBackground(new Color(189, 195, 199));
        JLabel lblEstado = new JLabel("Usuario activo: " + usuarioActual + "   |   Sesión iniciada");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        panelEstado.add(lblEstado);
        panelCentral.add(panelEstado, BorderLayout.SOUTH);

        add(panelCentral);

        // ── Eventos ───────────────────────────────────────────────
        itemMantenimiento.addActionListener(e ->
                new MantenimientoUsuarios(this, usuarioActual).setVisible(true));

        itemReinicio.addActionListener(e ->
                new ReinicioClave(this, usuarioActual).setVisible(true));

        menuCerrar.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent e) { cerrarSesion(); }
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea cerrar la sesión actual?", "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }

    private JMenu crearMenu(String nombre) {
        JMenu menu = new JMenu(nombre);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("SansSerif", Font.BOLD, 13));
        return menu;
    }

    private JMenuItem crearMenuItem(String nombre) {
        JMenuItem item = new JMenuItem(nombre);
        item.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return item;
    }

    private JPanel crearTarjeta(String titulo, String descripcion, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(color);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>", SwingConstants.CENTER);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(230, 230, 230));

        tarjeta.add(lblTitulo, BorderLayout.CENTER);
        tarjeta.add(lblDesc, BorderLayout.SOUTH);
        return tarjeta;
    }
}
