package swingutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFrame extends JFrame implements Runnable {
    public static final int DEFAULT_HEIGHT = 720;
    public static final int DEFAULT_WIDTH = DEFAULT_HEIGHT * 16 / 9;

    private final HashMap<String, Container> stateMap;

    private boolean isDark;
    private boolean isFullScreen;

    public MainFrame(String title, boolean dark) {
        super(title);

        stateMap = new HashMap<>();
        isDark = dark;
        isFullScreen = false;

        init();
    }

    private void init() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setLocationRelativeTo(null);
        setLocationByPlatform(false);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.exit(-1);
        }

        handleNimbusProperties();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAction();
            }
        });

        handleMenuBar();
        handleSystemTray();
    }

    private void handleNimbusProperties() {
        if (isDark) {
            UIManager.put("control", Color.DARK_GRAY.darker());
            UIManager.put("info", new Color(128,128,128));
            UIManager.put("nimbusBase", new Color( 18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color( 248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color( 128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115,164,209));
            UIManager.put("nimbusGreen", new Color(176,179,50));
            UIManager.put("nimbusInfoBlue", new Color( 66, 139, 221));
            UIManager.put("nimbusLightBackground", Color.DARK_GRAY);
            UIManager.put("nimbusOrange", new Color(191,98,4));
            UIManager.put("nimbusRed", new Color(169,46,34) );
            UIManager.put("nimbusSelectedText", new Color( 255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color( 104, 93, 156));
            UIManager.put("text", new Color( 230, 230, 230));
        } else {
            UIManager.put("control", new Color(214,217,223));
            UIManager.put("info", new Color(242,242,189));
            UIManager.put("nimbusBase", new Color( 51,98,140));
            UIManager.put("nimbusAlertYellow", new Color( 255,220,35));
            UIManager.put("nimbusDisabledText", new Color( 142,143,145));
            UIManager.put("nimbusFocus", new Color(115,164,209));
            UIManager.put("nimbusGreen", new Color(176,179,50));
            UIManager.put("nimbusInfoBlue", new Color( 47,92,180));
            UIManager.put("nimbusLightBackground", new Color(255,255,255));
            UIManager.put("nimbusOrange", new Color(191,98,4));
            UIManager.put("nimbusRed", new Color(169,46,34) );
            UIManager.put("nimbusSelectedText", new Color( 255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color( 57,105,138));
            UIManager.put("text", new Color( 0, 0, 0));
        }
    }

    private void handleMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu helpMenu = new JMenu("Help");
        JMenu toolsMenu = new JMenu("Tools");
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu analyzeMenu = new JMenu("Analyze");

        JMenuItem toggleDarkTheme = new JMenuItem("Toggle Dark Theme");
        toggleDarkTheme.addActionListener(e -> toggleDarkTheme());
        toggleDarkTheme.setAccelerator(KeyStroke.getKeyStroke('t', InputEvent.ALT_DOWN_MASK));
        viewMenu.add(toggleDarkTheme);

        JMenuItem fullScreen = new JMenuItem("Full Screen");
        fullScreen.addActionListener(e -> toggleFullScreen());
        fullScreen.setAccelerator(KeyStroke.getKeyStroke("F11"));
        viewMenu.add(fullScreen);

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> closeAction());
        quit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.ALT_DOWN_MASK));
        fileMenu.add(quit);

        JMenuItem tray = new JMenuItem("System Tray");
        tray.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
        helpMenu.add(tray);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(viewMenu);
        menuBar.add(analyzeMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void handleSystemTray() {
        TrayIcon trayIcon;
        SystemTray tray;
        String tooltip = getTitle();
        String exitText = "Exit";
        String openText = "Open";
        PopupMenu popupMenu;

        if (!SystemTray.isSupported()) return;

        tray = SystemTray.getSystemTray();
        popupMenu = new PopupMenu();

        trayIcon = new TrayIcon(new ImageIcon(".\\res\\icon.png").getImage(), tooltip, popupMenu);
        JFrame main = this;
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    main.setVisible(true);
                    main.setState(Frame.NORMAL);
                }
            }
        });
        trayIcon.setImageAutoSize(true);

        MenuItem defaultItem = new MenuItem(exitText);
        defaultItem.addActionListener(e -> System.exit(0));
        popupMenu.add(defaultItem);

        defaultItem = new MenuItem(openText);
        defaultItem.addActionListener(e -> {
            this.setVisible(true);
            this.setExtendedState(JFrame.NORMAL);
        });
        popupMenu.add(defaultItem);

        this.add(popupMenu);

        this.addWindowStateListener(windowEvent -> {

            if (windowEvent.getNewState() == Frame.MAXIMIZED_BOTH) {
                tray.remove(trayIcon);
                main.setVisible(true);
            }

            if (windowEvent.getNewState() == Frame.NORMAL) {
                tray.remove(trayIcon);
                main.setVisible(true);
            }

        });

        getJMenuBar().getMenu(4).getItem(0).addActionListener(e -> {
            try {
                tray.add(trayIcon);
                main.setVisible(false);
            } catch (AWTException ignore) {}
        });

        trayIcon.addActionListener(e -> tray.remove(trayIcon));

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (main.isVisible()) tray.remove(trayIcon);
        }, AWTEvent.ACTION_EVENT_MASK + AWTEvent.WINDOW_EVENT_MASK);

    }

    public void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        setVisible(false);
        if (isFullScreen) {
            dispose();
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        } else {
            dispose();
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
            setUndecorated(false);
            setExtendedState(JFrame.NORMAL);
            setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            setVisible(true);
        }
    }

    public void toggleDarkTheme() {
        isDark = !isDark;
        handleNimbusProperties();
        getAllComponents(this).stream().map(e -> (JComponent) e).forEach(JComponent::updateUI);
        repaint();
    }

    protected void closeAction() {
        System.exit(0);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        if (fullScreen != isFullScreen)
            toggleFullScreen();
    }

    public void setDark(boolean dark) {
        if (dark != isDark)
            toggleDarkTheme();
    }

    public boolean isDark() {
        return isDark;
    }

    public void addState(String stateName, Container stateContainer) {
        stateMap.put(stateName, stateContainer);
    }

    public Container getState(String stateName) {
        return stateMap.get(stateName);
    }

    public List<String> getStateNames() {
        return new ArrayList<>(stateMap.keySet());
    }

    protected void setState(String stateName) {
        setContentPane(stateMap.get(stateName));
        repaint();
        revalidate();
    }

    public static List<Component> getAllComponents(Container container) {
        Component[] components = container.getComponents();
        List<Component> componentList = new ArrayList<>();
        for (Component component : components) {
            componentList.add(component);
            if (component instanceof Container)
                componentList.addAll(getAllComponents((Container) component));
        }

        return componentList;
    }

    @Override
    public final void run() {
        setVisible(true);
    }
}
