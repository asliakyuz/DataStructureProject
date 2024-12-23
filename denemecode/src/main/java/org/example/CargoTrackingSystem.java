package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Stack;

public class CargoTrackingSystem {
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static final Map<Integer, Cargo> cargos = new HashMap<>();
    private static final Stack<Cargo> recentCargos = new Stack<>(); // For last 5 sent cargos
    private static final PriorityQueue<Cargo> cargoQueue = new PriorityQueue<>(Comparator.comparingInt(Cargo::getDeliveryDate));
    private static final Random random = new Random();
    private static JFrame frame;

    public static void main(String[] args) {
        initializePredefinedData();
        SwingUtilities.invokeLater(CargoTrackingSystem::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Kargo Takip Sistemi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(230, 230, 250));



        showMainMenu();
        frame.setVisible(true);
    }

    private static void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Menü", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(139, 0, 139));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(7, 1, 5, 5)); // Updated grid layout for extra menu option
        menuPanel.setBackground(new Color(230, 230, 250));

        JButton[] menuButtons = new JButton[7];
        String[] menuOptions = {
                "1. Müşteri Ekle",
                "2. Müşteriye Kargo Ekle",
                "3. Müşteri Gönderim Geçmişi Göster",
                "4. Kargo Durumu Sorgula",
                "5. Kargo Rotasını Göster",
                "6. Tüm Kargoları Sıralayarak Göster",
                "7. Kargoları Öncelikli İşleme Al",
                "8. Şehir ve İlçeleri Göster"
        };


        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JButton(menuOptions[i]);
            menuButtons[i].setBackground(new Color(216, 191, 216));
            menuButtons[i].setForeground(Color.BLACK);
            int option = i + 1;
            menuButtons[i].addActionListener(e -> handleMenuSelection(option));  // Directly calling method without using 'this'
            menuPanel.add(menuButtons[i]);
        }

        frame.add(menuPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }


    private static void handleMenuSelection(int option) {
        switch (option) {
            case 1 -> addCustomer();
            case 2 -> addCargoToCustomer();
            case 3 -> showCustomerHistory();
            case 4 -> queryCargoStatus();
            case 5 -> showCargoRoute();
            case 6 -> showAllCargosSorted();
            case 7 -> processPriorityCargos();
            case 8 -> displayCityTree(); // Yeni seçenek
        }
    }


    // Örnek: TreeNode sınıfı
    static class TreeNode {
        private final String name;
        private final List<TreeNode> children;

        public TreeNode(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<TreeNode> getChildren() {
            return children;
        }

        public void addChild(TreeNode child) {
            children.add(child);
        }
    }
    private static TreeNode buildCityTree() {
        TreeNode root = new TreeNode("Türkiye");

        // Marmara Bölgesi
        TreeNode marmara = new TreeNode("Marmara");
        TreeNode istanbul = new TreeNode("İstanbul");
        istanbul.addChild(new TreeNode("Kadıköy"));
        istanbul.addChild(new TreeNode("Beşiktaş"));
        marmara.addChild(istanbul);

        TreeNode bursa = new TreeNode("Bursa");
        bursa.addChild(new TreeNode("Nilüfer"));
        bursa.addChild(new TreeNode("Osmangazi"));
        marmara.addChild(bursa);

        // Ege Bölgesi
        TreeNode ege = new TreeNode("Ege");
        TreeNode izmir = new TreeNode("İzmir");
        izmir.addChild(new TreeNode("Bornova"));
        izmir.addChild(new TreeNode("Konak"));
        ege.addChild(izmir);

        TreeNode manisa = new TreeNode("Manisa");
        manisa.addChild(new TreeNode("Şehzadeler"));
        manisa.addChild(new TreeNode("Yunusemre"));
        ege.addChild(manisa);

        // Akdeniz Bölgesi
        TreeNode akdeniz = new TreeNode("Akdeniz");
        TreeNode antalya = new TreeNode("Antalya");
        antalya.addChild(new TreeNode("Kepez"));
        antalya.addChild(new TreeNode("Muratpaşa"));
        akdeniz.addChild(antalya);

        TreeNode mersin = new TreeNode("Mersin");
        mersin.addChild(new TreeNode("Mezitli"));
        mersin.addChild(new TreeNode("Tarsus"));
        akdeniz.addChild(mersin);

        // İç Anadolu Bölgesi
        TreeNode icAnadolu = new TreeNode("İç Anadolu");
        TreeNode ankara = new TreeNode("Ankara");
        ankara.addChild(new TreeNode("Çankaya"));
        ankara.addChild(new TreeNode("Keçiören"));
        icAnadolu.addChild(ankara);

        TreeNode konya = new TreeNode("Konya");
        konya.addChild(new TreeNode("Meram"));
        konya.addChild(new TreeNode("Selçuklu"));
        icAnadolu.addChild(konya);

        // Karadeniz Bölgesi
        TreeNode karadeniz = new TreeNode("Karadeniz");
        TreeNode samsun = new TreeNode("Samsun");
        samsun.addChild(new TreeNode("Atakum"));
        samsun.addChild(new TreeNode("İlkadım"));
        karadeniz.addChild(samsun);

        TreeNode trabzon = new TreeNode("Trabzon");
        trabzon.addChild(new TreeNode("Ortahisar"));
        trabzon.addChild(new TreeNode("Akçaabat"));
        karadeniz.addChild(trabzon);

        // Doğu Anadolu Bölgesi
        TreeNode doguAnadolu = new TreeNode("Doğu Anadolu");
        TreeNode erzurum = new TreeNode("Erzurum");
        erzurum.addChild(new TreeNode("Yakutiye"));
        erzurum.addChild(new TreeNode("Palandöken"));
        doguAnadolu.addChild(erzurum);

        TreeNode van = new TreeNode("Van");
        van.addChild(new TreeNode("İpekyolu"));
        van.addChild(new TreeNode("Edremit"));
        doguAnadolu.addChild(van);

        // Güneydoğu Anadolu Bölgesi
        TreeNode guneydoguAnadolu = new TreeNode("Güneydoğu Anadolu");
        TreeNode gaziantep = new TreeNode("Gaziantep");
        gaziantep.addChild(new TreeNode("Şahinbey"));
        gaziantep.addChild(new TreeNode("Şehitkamil"));
        guneydoguAnadolu.addChild(gaziantep);

        TreeNode adana = new TreeNode("Adana");
        adana.addChild(new TreeNode("Seyhan"));
        adana.addChild(new TreeNode("Yüreğir"));
        guneydoguAnadolu.addChild(adana);

        // Türkiye'ye şehirleri ekle
        root.addChild(marmara);
        root.addChild(ege);
        root.addChild(akdeniz);
        root.addChild(icAnadolu);
        root.addChild(karadeniz);
        root.addChild(doguAnadolu);
        root.addChild(guneydoguAnadolu);

        return root;
    }



    private static void displayCityTree() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JTextArea treeDisplay = new JTextArea();
        treeDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(treeDisplay);

        JButton backButton = new JButton("Geri");
        backButton.addActionListener(e -> showMainMenu());

        TreeNode cityTree = buildCityTree();
        StringBuilder treeRepresentation = new StringBuilder();
        displayTreeRecursively(cityTree, 0, treeRepresentation);

        treeDisplay.setText(treeRepresentation.toString());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    private static void displayTreeRecursively(TreeNode node, int level, StringBuilder builder) {
        builder.append("  ".repeat(level)).append("- ").append(node.getName()).append("\n");
        for (TreeNode child : node.getChildren()) {
            displayTreeRecursively(child, level + 1, builder);
        }
    }

    private static String[] getCityOptions() {
        TreeNode cityTree = buildCityTree();
        List<String> options = new ArrayList<>();

        for (TreeNode region : cityTree.getChildren()) { // Sadece şehir düğümlerini ekle
            for (TreeNode city : region.getChildren()) {
                options.add(city.getName());
            }
        }
        return options.toArray(new String[0]);
    }


    private static void collectCitiesRecursively(TreeNode node, List<String> options) {
        options.add(node.getName());
        for (TreeNode child : node.getChildren()) {
            collectCitiesRecursively(child, options);
        }
    }


    private static void addCustomer() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(new Color(230, 230, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("İsim:");
        JTextField nameField = new JTextField();

        JLabel surnameLabel = new JLabel("Soyisim:");
        JTextField surnameField = new JTextField();

        JButton saveButton = new JButton("Kaydet");
        JButton backButton = new JButton("Geri");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(surnameLabel);
        panel.add(surnameField);
        panel.add(saveButton);
        panel.add(backButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            int customerId = random.nextInt(900) + 100;
            customers.put(customerId, new Customer(customerId, name, surname));
            JOptionPane.showMessageDialog(frame, "Müşteri Eklendi:\nID: " + customerId + "\nİsim: " + name + "\nSoyisim: " + surname);
            showMainMenu();
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // frame'i statik yap

    private static void addCargoToCustomer() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(new Color(230, 230, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel idLabel = new JLabel("Müşteri ID:");
        JTextField idField = new JTextField();

        JLabel sendLabel = new JLabel("Gönderme Yeri:");
        JComboBox<String> sendComboBox = new JComboBox<>(getCityOptions());

        JLabel receiveLabel = new JLabel("Teslim Yeri:");
        JComboBox<String> receiveComboBox = new JComboBox<>(getCityOptions());

        JButton saveButton = new JButton("Kaydet");
        JButton backButton = new JButton("Geri");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(sendLabel);
        panel.add(sendComboBox);
        panel.add(receiveLabel);
        panel.add(receiveComboBox);
        panel.add(saveButton);
        panel.add(backButton);

        saveButton.addActionListener(e -> {
            try {
                int customerId = Integer.parseInt(idField.getText());
                if (!customers.containsKey(customerId)) {
                    JOptionPane.showMessageDialog(frame, "Müşteri Bulunamadı!");
                    return;
                }
                String sendLocation = (String) sendComboBox.getSelectedItem();
                String receiveLocation = (String) receiveComboBox.getSelectedItem();
                int cargoId = random.nextInt(900) + 100;
                Cargo cargo = new Cargo(cargoId, customerId, sendLocation, receiveLocation, random.nextInt(11));

                // Müşteri objesini al
                Customer customer = customers.get(customerId);

                // Kargo geçmişine yeni kargo ekle
                Stack<Cargo> customerCargoHistory = customer.getRecentCargos();
                customerCargoHistory.push(cargo);

                // Yığındaki öğe sayısını 5 ile sınırlıyoruz
                if (customerCargoHistory.size() > 5) {
                    customerCargoHistory.remove(0); // Yığındaki en eski öğeyi çıkar
                }

                cargos.put(cargoId, cargo);  // Kargo veritabanına ekle
                cargoQueue.add(cargo);        // Öncelikli kargo kuyruğuna ekle
                JOptionPane.showMessageDialog(frame, "Kargo Eklendi:\nKargo ID: " + cargoId);
                showMainMenu();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Geçerli bir müşteri ID giriniz!");
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }


    private static void processPriorityCargos() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JTextArea processingArea = new JTextArea("Öncelikli Kargolar:\n\n");
        JScrollPane scrollPane = new JScrollPane(processingArea);

        JButton processButton = new JButton("Kargo İşle");
        JButton backButton = new JButton("Geri");

        processButton.addActionListener(e -> {
            if (!cargoQueue.isEmpty()) {
                Cargo nextCargo = cargoQueue.poll();
                Customer customer = customers.get(nextCargo.getCustomerId());  // Kargo sahibini al

                processingArea.append("İşlenen Kargo: " + nextCargo.getId() +
                        " - Gönderim: " + nextCargo.getSendLocation() +
                        " -> Teslimat: " + nextCargo.getReceiveLocation() +
                        " - Teslim Süresi: " + nextCargo.getDeliveryDate() + " gün" +
                        " - Sahibi: " + (customer != null ? customer.getName() + " " + customer.getSurname() : "Bilgi Yok") +
                        " (ID: " + nextCargo.getCustomerId() + ")\n");
            } else {
                JOptionPane.showMessageDialog(frame, "İşlenecek Kargo Yok!");
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(processButton, BorderLayout.NORTH);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }


    private static Stack<Cargo> getRecentCargosForCustomer(int customerId) {
        // Müşteri objesini al
        Customer customer = customers.get(customerId);

        if (customer == null) {
            return new Stack<>(); // Müşteri bulunamadıysa boş bir stack döndür
        }

        return customer.getRecentCargos();  // Müşterinin kargo geçmişini al
    }



    private static void showCustomerHistory() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel promptLabel = new JLabel("Müşteri ID giriniz:");
        JTextField idField = new JTextField();
        JButton queryButton = new JButton("Sorgula");
        JButton backButton = new JButton("Geri");

        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        inputPanel.add(promptLabel);
        inputPanel.add(idField);
        inputPanel.add(queryButton);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        queryButton.addActionListener(e -> {
            try {
                int customerId = Integer.parseInt(idField.getText());
                if (!customers.containsKey(customerId)) {
                    resultArea.setText("Müşteri bulunamadı.");
                    return;
                }

                Stack<Cargo> customerRecentCargos = getRecentCargosForCustomer(customerId);

                if (customerRecentCargos.isEmpty()) {
                    resultArea.setText("Bu müşterinin gönderim geçmişi boş.");
                    return;
                }

                StringBuilder history = new StringBuilder();
                for (Cargo cargo : customerRecentCargos) {
                    history.append("Kargo ID: ").append(cargo.getId())
                            .append(" Gönderim: ").append(cargo.getSendLocation())
                            .append(" Teslimat: ").append(cargo.getReceiveLocation())
                            .append(" Durum: ").append(cargo.getStatus())
                            .append(" Teslim Süresi: ").append(cargo.getDeliveryDate()).append(" gün\n");
                }
                resultArea.setText(history.toString());
            } catch (NumberFormatException ex) {
                resultArea.setText("Geçerli bir müşteri ID giriniz!");
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    private static void queryCargoStatus() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel promptLabel = new JLabel("Kargo ID giriniz:");
        JTextField idField = new JTextField();
        JButton queryButton = new JButton("Sorgula");
        JButton backButton = new JButton("Geri");

        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        inputPanel.add(promptLabel);
        inputPanel.add(idField);
        inputPanel.add(queryButton);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        queryButton.addActionListener(e -> {
            try {
                int cargoId = Integer.parseInt(idField.getText());
                if (!cargos.containsKey(cargoId)) {
                    resultArea.setText("Kargo bulunamadı.");
                    return;
                }

                Cargo cargo = cargos.get(cargoId);
                int deliveryDate = cargo.getDeliveryDate();
                String location = findDistrictByDate(cargo.getSendLocation(), cargo.getReceiveLocation(), deliveryDate);

                // Kargo durumunu ve bulunduğu ili yazdırıyoruz
                resultArea.setText("Kargo ID: " + cargo.getId() +
                        "\nGönderim Yeri: " + cargo.getSendLocation() +
                        "\nTeslimat Yeri: " + cargo.getReceiveLocation() +
                        "\nDurum: " + cargo.getStatus() +
                        "\nTeslim Süresi: " + deliveryDate + " gün" +
                        "\nBulunduğu Yer: " + location); // İlçe ile birlikte şehir yazdırılıyor
            } catch (NumberFormatException ex) {
                resultArea.setText("Geçerli bir kargo ID giriniz!");
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    private static void initializePredefinedData() {
        // Tanımlı müşteriler
        customers.put(1, new Customer(1, "Ali", "Yılmaz"));
        customers.put(2, new Customer(2, "Ayşe", "Kaya"));
        customers.put(3, new Customer(3, "Fatma", "Çelik"));
        customers.put(4, new Customer(4, "Mehmet", "Demir"));
        customers.put(5, new Customer(5, "Ahmet", "Kara"));
        customers.put(6, new Customer(6, "Zehra", "Gül"));
        customers.put(7, new Customer(7, "Hakan", "Öztürk"));
        customers.put(8, new Customer(8, "Cem", "Aslan"));
        customers.put(9, new Customer(9, "Elif", "Koç"));
        customers.put(10, new Customer(10, "Serkan", "Aksoy"));

        // Tanımlı kargolar
        cargos.put(101, new Cargo(101, 1, "İstanbul", "Ankara", 5));
        cargos.put(102, new Cargo(102, 2, "İzmir", "İstanbul", 3));
        cargos.put(103, new Cargo(103, 3, "Adana", "Erzurum", 1));
        cargos.put(104, new Cargo(104, 4, "Ankara", "İzmir", 7));
        cargos.put(105, new Cargo(105, 5, "İstanbul", "Mersin", 2));
        cargos.put(106, new Cargo(106, 6, "Trabzon", "Van", 4));
        cargos.put(107, new Cargo(107, 7, "Konya", "Antalya", 6));
        cargos.put(108, new Cargo(108, 8, "Samsun", "Antalya", 3));
        cargos.put(109, new Cargo(109, 9, "Van", "Adana", 8));
        cargos.put(110, new Cargo(110, 10, "Gaziantep", "Van", 5));

        // Kargoları müşterilere bağla
        for (Cargo cargo : cargos.values()) {
            Customer customer = customers.get(cargo.getCustomerId());
            if (customer != null) {
                customer.getRecentCargos().push(cargo);
            }
        }
    }

    // İlçe ve ili bulma
    private static String findDistrictByDate(String sendLocation, String receiveLocation, int deliveryDate) {
        TreeNode root = buildCityTree();
        String district = "";

        // Eğer gönderim ve teslimat şehirleri aynıysa, Ankara'ya uğramasın
        if (sendLocation.equals(receiveLocation)) {
            district = findDistrict(root, sendLocation);
        } else if (deliveryDate <= 3) {
            district = findDistrict(root, receiveLocation);
        } else if (deliveryDate >= 8) {
            district = findDistrict(root, sendLocation);
        } else {
            district = "Ankara - Merkez"; // Ara nokta, örnek
        }

        return district; // İlçe ve il bilgisiyle döner
    }

    private static String findDistrict(TreeNode root, String city) {
        for (TreeNode region : root.getChildren()) {
            for (TreeNode cityNode : region.getChildren()) {
                if (cityNode.getName().equals(city)) {
                    String district = cityNode.getChildren().isEmpty() ? city : cityNode.getChildren().get(0).getName();
                    // Şehri ve bölgeyi direkt olarak ekleyelim
                    return region.getName() + " - " + city + " - " + district + " (" + city + ")";
                }
            }
        }
        return city; // İlçe bulunamazsa sadece şehir döner
    }

    private static TreeNode findCityNode(TreeNode rootNode, String cityName) {
        for (TreeNode child : rootNode.getChildren()) {
            // Eğer şehir ismi eşleşirse, döndür
            if (child.getName().equals(cityName)) {
                return child;
            }

            // Çocuk düğümlerde arama yap
            TreeNode result = findCityNode(child, cityName);
            if (result != null) {
                return result;
            }
        }
        return null; // Şehir bulunamazsa null döndür
    }

    private static void showCargoRoute() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel promptLabel = new JLabel("Kargo ID giriniz:");
        JTextField idField = new JTextField();
        JButton queryButton = new JButton("Sorgula");
        JButton backButton = new JButton("Geri");

        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        inputPanel.add(promptLabel);
        inputPanel.add(idField);
        inputPanel.add(queryButton);

        JTextArea routeArea = new JTextArea("Kargo Rotası:\n\n");
        routeArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(routeArea);

        queryButton.addActionListener(e -> {
            try {
                int cargoId = Integer.parseInt(idField.getText());
                if (!cargos.containsKey(cargoId)) {
                    routeArea.setText("Kargo bulunamadı.");
                    return;
                }

                Cargo cargo = cargos.get(cargoId);
                StringBuilder route = new StringBuilder();

                route.append("Kargo ID: ").append(cargo.getId())
                        .append(" - Gönderim: ").append(cargo.getSendLocation());

                // cityTree'yi başlat
                TreeNode cityTree = buildCityTree();

                // Eğer gönderim ve teslimat şehirleri aynıysa, sadece o şehri ve ilçeyi yazdır
                if (cargo.getSendLocation().equals(cargo.getReceiveLocation())) {
                    // Burada, şehirdeki ilk ilçeyi ekliyoruz.
                    TreeNode cityNode = findCityNode(cityTree, cargo.getSendLocation());
                    if (cityNode != null && !cityNode.getChildren().isEmpty()) {
                        String district = cityNode.getChildren().get(0).getName(); // İlk ilçeyi al
                        route.append(" - İlçe: ").append(district).append(" (").append(cargo.getSendLocation()).append(")");
                    }
                } else {
                    // Eğer şehirler farklıysa, her iki şehirdeki ilçeleri ekle
                    TreeNode sendCityNode = findCityNode(cityTree, cargo.getSendLocation());
                    TreeNode receiveCityNode = findCityNode(cityTree, cargo.getReceiveLocation());

                    if (sendCityNode != null && !sendCityNode.getChildren().isEmpty()) {
                        route.append(" -> ").append(sendCityNode.getChildren().get(0).getName())
                                .append(" (").append(cargo.getSendLocation()).append(")"); // Gönderim ilçesi
                    }

                    // Ankara Merkez'i ekle
                    route.append(" -> Ankara - Merkez");

                    if (receiveCityNode != null && !receiveCityNode.getChildren().isEmpty()) {
                        route.append(" -> ").append(receiveCityNode.getChildren().get(0).getName())
                                .append(" (").append(cargo.getReceiveLocation()).append(")"); // Teslimat ilçesi
                    }
                }

                routeArea.setText(route.toString());
            } catch (NumberFormatException ex) {
                routeArea.setText("Geçerli bir kargo ID giriniz!");
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
    private static void showAllCargosSorted() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JTextArea sortedCargosArea = new JTextArea("Tüm Kargolar (Teslimat Tarihine Göre Sıralı):\n\n");
        JScrollPane scrollPane = new JScrollPane(sortedCargosArea);

        List<Cargo> sortedCargos = cargos.values().stream()
                .sorted(Comparator.comparingInt(Cargo::getDeliveryDate)).collect(Collectors.toList());

        StringBuilder sortedDetails = new StringBuilder();
        for (Cargo cargo : sortedCargos) {
            sortedDetails.append("Kargo ID: ").append(cargo.getId())
                    .append(" - Gönderim: ").append(cargo.getSendLocation())
                    .append(" -> Teslimat: ").append(cargo.getReceiveLocation())
                    .append(" - Teslimat Tarihi: ").append(cargo.getDeliveryDate()).append("\n");
        }
        sortedCargosArea.setText(sortedDetails.toString());

        JButton backButton = new JButton("Geri");
        backButton.addActionListener(e -> showMainMenu());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }


    // Müşteri sınıfı
    static class Customer {
        private final int id;
        private final String name;
        private final String surname;
        private final Stack<Cargo> recentCargos;

        public Customer(int id, String name, String surname) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.recentCargos = new Stack<>();
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public Stack<Cargo> getRecentCargos() {
            return recentCargos;
        }
    }


    static class Cargo {
        private final int id;
        private final int customerId;
        private final String sendLocation;
        private final String receiveLocation;
        private final int deliveryDate;

        public Cargo(int id, int customerId, String sendLocation, String receiveLocation, int deliveryDate) {
            this.id = id;
            this.customerId = customerId;
            this.sendLocation = sendLocation;
            this.receiveLocation = receiveLocation;
            this.deliveryDate = deliveryDate;
        }

        public int getId() {
            return id;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getSendLocation() {
            return sendLocation;
        }

        public String getReceiveLocation() {
            return receiveLocation;
        }

        public int getDeliveryDate() {
            return deliveryDate;
        }

        public String getStatus() {
            return "Teslimat Aşamasında";
        }
    }
}