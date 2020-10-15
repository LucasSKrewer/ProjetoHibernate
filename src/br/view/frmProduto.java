/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.view;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import br.model.bean.Categoria;
import br.model.bean.Fornecedor;
import br.model.bean.Produto;
import br.model.dao.CategoriaDAO;
import br.model.dao.FornecedorDAO;
import br.model.dao.ProdutoDAO;

/**
 *
 * @author Computador
 */
public class frmProduto extends javax.swing.JFrame {

    /**
     * Creates new form frmUsuarios
     */
    enum PHASE {
        NONE,
        NEW,
        VIEW,
        EDIT
    }

    PHASE registerPhase = PHASE.NONE;

    Component categories, suppliers;

    public frmProduto() throws ParseException {
        initComponents();
        this.getContentPane().setBackground(new Color(20, 20, 20));

        categories = tabcProdutos.getComponent(2);
        suppliers = tabcProdutos.getComponent(3);
        tabcProdutos.remove(3);
        tabcProdutos.remove(2);

        DecimalFormat formatQnt = new DecimalFormat("#,##0");
        NumberFormatter formatterQnt = new NumberFormatter((NumberFormat) formatQnt);
        formatterQnt.setValueClass(Integer.class);
        formatterQnt.setMinimum(0.0);
        formatterQnt.setMaximum(Double.MAX_VALUE);
        DefaultFormatterFactory factoryQnt = new DefaultFormatterFactory(formatterQnt);
        txtQuantity.setFormatterFactory(factoryQnt);

        DecimalFormat formatPrice = new DecimalFormat("#,##0.00");
        NumberFormatter formatterPrice = new NumberFormatter((NumberFormat) formatPrice);
        formatterQnt.setValueClass(Double.class);
        formatterPrice.setMinimum(0.0);
        formatterPrice.setMaximum(Double.MAX_VALUE);
        DefaultFormatterFactory factoryPrice = new DefaultFormatterFactory(formatterPrice);
        txtPrice.setFormatterFactory(factoryPrice);

        populateProductsTable();
    }

    private boolean checkParameters() {
       
        if (txtDescription.getText().isEmpty() || txtQuantity.getText().isEmpty() || txtPrice.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Algum campo está vazio.");
            return false;
        }
        return true;
    }

    
    private void populateProductsTable() {
        DefaultTableModel model = (DefaultTableModel) tableProdutos.getModel();
        model.setNumRows(0);

        ProdutoDAO dao = new ProdutoDAO();

        for (Produto p : dao.findAll()) {
            model.addRow(new Object[]{
                p.getId(),
                p.getDescription(),
                p.getQuantity(),
                p.getPrice(),
                p.getCategory(),
                p.getSupplier()
            });
        }
    }

    private void populateProductsTableCustom() {
        DefaultTableModel model = (DefaultTableModel) tableProdutos.getModel();
        model.setNumRows(0);

        ProdutoDAO dao = new ProdutoDAO();

        for (Produto p : dao.findAllByFilter(String.valueOf(comboSearch.getSelectedItem()), txtSearch.getText())) {
            model.addRow(new Object[]{
                p.getId(),
                p.getDescription(),
                p.getQuantity(),
                p.getPrice(),
                p.getCategory(),
                p.getSupplier()
            });

        }

    }
    
   
    private void populateCategoriesTable() {
        DefaultTableModel model = (DefaultTableModel) tableCategories.getModel();
        model.setNumRows(0);

        CategoriaDAO dao = new CategoriaDAO();

        for (Categoria c : dao.findAll()) {
            model.addRow(new Object[]{
                c.getId(),
                c.getDescription()
            });

        }
    }

    private void populateCategoriesTableCustom() {
        DefaultTableModel model = (DefaultTableModel) tableCategories.getModel();
        model.setNumRows(0);

        CategoriaDAO dao = new CategoriaDAO();

        for (Categoria c : dao.findAllByFilter(String.valueOf(comboSearchCategories.getSelectedItem()), txtSearchCategories.getText())) {
            model.addRow(new Object[]{
                c.getId(),
                c.getDescription()
            });

        }

    }
    
  
    private void populateSuppliersTable() {
        DefaultTableModel model = (DefaultTableModel) tableSuppliers.getModel();
        model.setNumRows(0);

        FornecedorDAO dao = new FornecedorDAO();

        for (Fornecedor f : dao.findAll()) {
            model.addRow(new Object[]{
                f.getId(),
                f.getSocialReason(),
                f.getFantasyName(),
                f.getPhone(),
                f.getEmail(),
                f.getManager()
            });

        }
    }

    private void populateSuppliersTableCustom() {
        DefaultTableModel model = (DefaultTableModel) tableSuppliers.getModel();
        model.setNumRows(0);

        FornecedorDAO dao = new FornecedorDAO();

        for (Fornecedor f : dao.findAllByFilter(String.valueOf(comboSearchSuppliers.getSelectedItem()), txtSearchSuppliers.getText())) {
            model.addRow(new Object[]{
                f.getId(),
                f.getSocialReason(),
                f.getFantasyName(),
                f.getPhone(),
                f.getEmail(),
                f.getManager()
            });

        }

    }

    private Categoria selectedCategory;
    private Fornecedor selectedSupplier;
    private void showAdditionalTabs(boolean show) {
        if (show) {
            if (tabcProdutos.getComponentCount() == 2) {
                tabcProdutos.add("categorias", categories);
                tabcProdutos.add("fornecedores", suppliers);
                populateCategoriesTable();
                populateSuppliersTable();
            }

        } else {
            if (tabcProdutos.getComponentCount() == 4) {
                tabcProdutos.remove(3);
                tabcProdutos.remove(2);
            }
        }
    }

    private void updateControls(PHASE phase) {
        switch (phase) {
            case NONE:
                showAdditionalTabs(false);
               
                txtDescription.setEditable(false);
                txtQuantity.setEditable(false);
                txtPrice.setEditable(false);
               
                btNew.setEnabled(true);
                btSave.setEnabled(false);
                btEdit.setEnabled(false);
                btDelete.setEnabled(false);
                btCancel.setEnabled(false);

                txtId.setText("");
                txtDescription.setText("");
                txtQuantity.setText("");
                txtPrice.setText("");
                txtCategory.setText("");
                txtSupplier.setText("");
                break;

            case NEW:
                showAdditionalTabs(true);
               
                txtDescription.setEditable(true);
                txtQuantity.setEditable(true);
                txtPrice.setEditable(true);
                
                btNew.setEnabled(false);
                btSave.setEnabled(true);
                btEdit.setEnabled(false);
                btDelete.setEnabled(false);
                btCancel.setEnabled(true);
                break;

            case VIEW:
                showAdditionalTabs(false);
                txtDescription.setEditable(false);
                txtQuantity.setEditable(false);
                txtPrice.setEditable(false);
                btNew.setEnabled(false);
                btSave.setEnabled(false);
                btEdit.setEnabled(true);
                btDelete.setEnabled(true);
                btCancel.setEnabled(true);
                break;

            case EDIT:
                showAdditionalTabs(true);         
                txtDescription.setEditable(true);
                txtQuantity.setEditable(true);
                txtPrice.setEditable(true);
                btNew.setEnabled(false);
                btSave.setEnabled(true);
                btEdit.setEnabled(false);
                btDelete.setEnabled(false);
                btCancel.setEnabled(true);
                break;

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabcProdutos = new javax.swing.JTabbedPane();
        tabProdutosReg = new javax.swing.JPanel();
        txtId = new javax.swing.JTextField();
        lbId = new javax.swing.JLabel();
        lbDescription = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lbQuantity = new javax.swing.JLabel();
        lbPrice = new javax.swing.JLabel();
        btNew = new javax.swing.JButton();
        btSave = new javax.swing.JButton();
        btEdit = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        lbCategory = new javax.swing.JLabel();
        lbSupplier = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JFormattedTextField();
        txtPrice = new javax.swing.JFormattedTextField();
        txtCategory = new javax.swing.JTextField();
        txtSupplier = new javax.swing.JTextField();
        tabProdutosList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProdutos = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        comboSearch = new javax.swing.JComboBox<>();
        btSearch = new javax.swing.JButton();
        btSearchReset = new javax.swing.JButton();
        tabProdutosCategoria = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCategories = new javax.swing.JTable();
        txtSearchCategories = new javax.swing.JTextField();
        comboSearchCategories = new javax.swing.JComboBox<>();
        btSearchCategories = new javax.swing.JButton();
        btSearchResetCategories = new javax.swing.JButton();
        tabProdutosFornecedor = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableSuppliers = new javax.swing.JTable();
        txtSearchSuppliers = new javax.swing.JTextField();
        comboSearchSuppliers = new javax.swing.JComboBox<>();
        btSearchSuppliers = new javax.swing.JButton();
        btSearchResetSuppliers = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tabProdutosReg.setBackground(new java.awt.Color(0, 0, 102));

        txtId.setEditable(false);

        lbId.setForeground(new java.awt.Color(153, 153, 153));
        lbId.setText("id");

        lbDescription.setForeground(new java.awt.Color(153, 153, 153));
        lbDescription.setText("descrição");

        txtDescription.setEditable(false);

        lbQuantity.setForeground(new java.awt.Color(153, 153, 153));
        lbQuantity.setText("quantidade");

        lbPrice.setForeground(new java.awt.Color(153, 153, 153));
        lbPrice.setText("preço");

        btNew.setText("novo");
        btNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewActionPerformed(evt);
            }
        });

        btSave.setText("salvar");
        btSave.setEnabled(false);
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        btEdit.setText("editar");
        btEdit.setEnabled(false);
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });

        btDelete.setText("excluir");
        btDelete.setEnabled(false);
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btCancel.setText("cancelar");
        btCancel.setEnabled(false);
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        lbCategory.setForeground(new java.awt.Color(153, 153, 153));
        lbCategory.setText("categoria");

        lbSupplier.setForeground(new java.awt.Color(153, 153, 153));
        lbSupplier.setText("fornecedor");

        txtQuantity.setEditable(false);
        txtQuantity.setBackground(new java.awt.Color(255, 255, 255));
        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });

        txtPrice.setEditable(false);
        txtPrice.setBackground(new java.awt.Color(255, 255, 255));

        txtCategory.setEditable(false);

        txtSupplier.setEditable(false);
        txtSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabProdutosRegLayout = new javax.swing.GroupLayout(tabProdutosReg);
        tabProdutosReg.setLayout(tabProdutosRegLayout);
        tabProdutosRegLayout.setHorizontalGroup(
            tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabProdutosRegLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabProdutosRegLayout.createSequentialGroup()
                        .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDescription)
                            .addComponent(lbId))
                        .addGap(18, 18, 18)
                        .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tabProdutosRegLayout.createSequentialGroup()
                        .addComponent(lbSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tabProdutosRegLayout.createSequentialGroup()
                                .addComponent(btNew)
                                .addGap(18, 18, 18)
                                .addComponent(btSave)
                                .addGap(18, 18, 18)
                                .addComponent(btEdit)
                                .addGap(18, 18, 18)
                                .addComponent(btDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btCancel))
                            .addGroup(tabProdutosRegLayout.createSequentialGroup()
                                .addComponent(txtSupplier)
                                .addGap(141, 141, 141))))
                    .addGroup(tabProdutosRegLayout.createSequentialGroup()
                        .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbQuantity)
                            .addComponent(lbPrice)
                            .addComponent(lbCategory))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                            .addComponent(txtQuantity)
                            .addComponent(txtCategory))))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        tabProdutosRegLayout.setVerticalGroup(
            tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabProdutosRegLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbQuantity)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPrice)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCategory)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSupplier)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(tabProdutosRegLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btNew)
                    .addComponent(btSave)
                    .addComponent(btEdit)
                    .addComponent(btDelete)
                    .addComponent(btCancel))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        tabcProdutos.addTab("cadastrar", tabProdutosReg);

        tabProdutosList.setBackground(new java.awt.Color(0, 0, 102));

        tableProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "descrição", "quantidade", "preço", "categoria", "fornecedor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProdutosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableProdutos);

        comboSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "description", "quantity", "price" }));

        btSearch.setText("procurar");
        btSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchActionPerformed(evt);
            }
        });

        btSearchReset.setText("reset");
        btSearchReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabProdutosListLayout = new javax.swing.GroupLayout(tabProdutosList);
        tabProdutosList.setLayout(tabProdutosListLayout);
        tabProdutosListLayout.setHorizontalGroup(
            tabProdutosListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addGroup(tabProdutosListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSearchReset)
                .addContainerGap())
        );
        tabProdutosListLayout.setVerticalGroup(
            tabProdutosListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabProdutosListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabProdutosListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSearch)
                    .addComponent(btSearchReset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );

        tabcProdutos.addTab("listar", tabProdutosList);

        tabProdutosCategoria.setBackground(new java.awt.Color(0, 0, 102));

        tableCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "descrição"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCategories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCategoriesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableCategories);

        comboSearchCategories.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "description" }));

        btSearchCategories.setText("procurar");
        btSearchCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchCategoriesActionPerformed(evt);
            }
        });

        btSearchResetCategories.setText("reset");
        btSearchResetCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchResetCategoriesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabProdutosCategoriaLayout = new javax.swing.GroupLayout(tabProdutosCategoria);
        tabProdutosCategoria.setLayout(tabProdutosCategoriaLayout);
        tabProdutosCategoriaLayout.setHorizontalGroup(
            tabProdutosCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addGroup(tabProdutosCategoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearchCategories)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboSearchCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSearchCategories)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSearchResetCategories)
                .addContainerGap())
        );
        tabProdutosCategoriaLayout.setVerticalGroup(
            tabProdutosCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabProdutosCategoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabProdutosCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboSearchCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSearchCategories)
                    .addComponent(btSearchResetCategories))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );

        tabcProdutos.addTab("categorias", tabProdutosCategoria);

        tabProdutosFornecedor.setBackground(new java.awt.Color(0, 0, 102));

        tableSuppliers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "razao social", "nome fantasia", "telefone", "email", "gerente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSuppliersMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableSuppliers);

        comboSearchSuppliers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "socialReason", "fantasyName", "phone", "email", "manager" }));

        btSearchSuppliers.setText("procurar");
        btSearchSuppliers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchSuppliersActionPerformed(evt);
            }
        });

        btSearchResetSuppliers.setText("reset");
        btSearchResetSuppliers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchResetSuppliersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabProdutosFornecedorLayout = new javax.swing.GroupLayout(tabProdutosFornecedor);
        tabProdutosFornecedor.setLayout(tabProdutosFornecedorLayout);
        tabProdutosFornecedorLayout.setHorizontalGroup(
            tabProdutosFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addGroup(tabProdutosFornecedorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearchSuppliers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboSearchSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSearchSuppliers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSearchResetSuppliers)
                .addContainerGap())
        );
        tabProdutosFornecedorLayout.setVerticalGroup(
            tabProdutosFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabProdutosFornecedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabProdutosFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboSearchSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSearchSuppliers)
                    .addComponent(btSearchResetSuppliers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );

        tabcProdutos.addTab("fornecedores", tabProdutosFornecedor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabcProdutos)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabcProdutos)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tableProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProdutosMouseClicked

        if (tableProdutos.getSelectedRow() != -1) {

            txtId.setText(tableProdutos.getValueAt(tableProdutos.getSelectedRow(), 0).toString());
            txtDescription.setText(tableProdutos.getValueAt(tableProdutos.getSelectedRow(), 1).toString());
            txtQuantity.setText(tableProdutos.getValueAt(tableProdutos.getSelectedRow(), 2).toString());            
            txtPrice.setText(tableProdutos.getValueAt(tableProdutos.getSelectedRow(), 3).toString().replaceAll("\\.", ","));

            selectedCategory = (Categoria) tableProdutos.getValueAt(tableProdutos.getSelectedRow(), 4);
            selectedSupplier = (Fornecedor) tableProdutos.getValueAt(tableProdutos.getSelectedRow(), 5);
            
            txtCategory.setText(selectedCategory.toString()); 
            txtSupplier.setText(selectedSupplier.toString()); 
            
            registerPhase = PHASE.VIEW;
            updateControls(registerPhase);

            tabcProdutos.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tableProdutosMouseClicked

    private void tableCategoriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCategoriesMouseClicked
        if (tableCategories.getSelectedRow() != -1) {
            
            Categoria c = new Categoria();
            c.setId(Integer.parseInt(tableCategories.getValueAt(tableCategories.getSelectedRow(), 0).toString()));
            selectedCategory = c;
            
            txtCategory.setText(tableCategories.getValueAt(tableCategories.getSelectedRow(), 1).toString());
           
            tabcProdutos.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tableCategoriesMouseClicked

    private void tableSuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSuppliersMouseClicked

        if (tableSuppliers.getSelectedRow() != -1) {

            Fornecedor f = new Fornecedor();
            f.setId(Integer.parseInt(tableSuppliers.getValueAt(tableSuppliers.getSelectedRow(), 0).toString()));
            selectedSupplier = f;
            
            txtSupplier.setText(tableSuppliers.getValueAt(tableSuppliers.getSelectedRow(), 2).toString() + " (" + tableSuppliers.getValueAt(tableSuppliers.getSelectedRow(), 1).toString() + ")");
           
            tabcProdutos.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tableSuppliersMouseClicked

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        ProdutoDAO dao = new ProdutoDAO();

        dao.remove(Integer.parseInt(txtId.getText())); //deleta o produto

        registerPhase = PHASE.NONE;
        updateControls(registerPhase);

        populateProductsTable();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewActionPerformed
        registerPhase = PHASE.NEW;
        updateControls(registerPhase);
    }//GEN-LAST:event_btNewActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        if (checkParameters()) {
            Produto p = new Produto();
            ProdutoDAO dao = new ProdutoDAO();
            switch (registerPhase) {
                case EDIT:
                    p.setId(Integer.parseInt(txtId.getText()));
                case NEW:
                    p.setDescription(txtDescription.getText());
                    p.setQuantity(Integer.parseInt(txtQuantity.getText()));
                   
                    String priceFix = txtPrice.getText().replaceAll("\\.", "").replaceAll(",", ".");
                    p.setPrice(Double.parseDouble(priceFix));
                    p.setCategory(selectedCategory);
                    p.setSupplier(selectedSupplier);
                    dao.save(p); //cria/update 
                    break;
            }

            registerPhase = PHASE.NONE;
            updateControls(registerPhase);
            populateProductsTable();
        }
    }//GEN-LAST:event_btSaveActionPerformed

    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        registerPhase = PHASE.EDIT;
        updateControls(registerPhase);
    }//GEN-LAST:event_btEditActionPerformed

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
         registerPhase = PHASE.NONE;
        updateControls(registerPhase);
    }//GEN-LAST:event_btCancelActionPerformed

    private void btSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchActionPerformed
        if (txtSearch.getText() != "") {
            populateProductsTableCustom();
        }
    }//GEN-LAST:event_btSearchActionPerformed

    private void btSearchResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchResetActionPerformed
        populateProductsTable();
    }//GEN-LAST:event_btSearchResetActionPerformed

    private void btSearchCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchCategoriesActionPerformed
       if (txtSearchCategories.getText() != "") {
            populateCategoriesTableCustom();
        }
    }//GEN-LAST:event_btSearchCategoriesActionPerformed

    private void btSearchResetCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchResetCategoriesActionPerformed
        populateCategoriesTable();
    }//GEN-LAST:event_btSearchResetCategoriesActionPerformed

    private void btSearchSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchSuppliersActionPerformed
        if (txtSearchSuppliers.getText() != "") {
            populateSuppliersTableCustom();
        }
    }//GEN-LAST:event_btSearchSuppliersActionPerformed

    private void btSearchResetSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchResetSuppliersActionPerformed
        populateSuppliersTable();
    }//GEN-LAST:event_btSearchResetSuppliersActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    private void txtSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new frmProduto().setVisible(true);
                } catch (ParseException ex) {
                    Logger.getLogger(frmProduto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btNew;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSearch;
    private javax.swing.JButton btSearchCategories;
    private javax.swing.JButton btSearchReset;
    private javax.swing.JButton btSearchResetCategories;
    private javax.swing.JButton btSearchResetSuppliers;
    private javax.swing.JButton btSearchSuppliers;
    private javax.swing.JComboBox<String> comboSearch;
    private javax.swing.JComboBox<String> comboSearchCategories;
    private javax.swing.JComboBox<String> comboSearchSuppliers;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbCategory;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbId;
    private javax.swing.JLabel lbPrice;
    private javax.swing.JLabel lbQuantity;
    private javax.swing.JLabel lbSupplier;
    private javax.swing.JPanel tabProdutosCategoria;
    private javax.swing.JPanel tabProdutosFornecedor;
    private javax.swing.JPanel tabProdutosList;
    private javax.swing.JPanel tabProdutosReg;
    private javax.swing.JTabbedPane tabcProdutos;
    private javax.swing.JTable tableCategories;
    private javax.swing.JTable tableProdutos;
    private javax.swing.JTable tableSuppliers;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtId;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JFormattedTextField txtQuantity;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearchCategories;
    private javax.swing.JTextField txtSearchSuppliers;
    private javax.swing.JTextField txtSupplier;
    // End of variables declaration//GEN-END:variables
}
