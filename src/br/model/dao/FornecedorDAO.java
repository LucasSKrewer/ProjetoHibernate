/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.model.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import br.connection.ConnectionFactory;
import br.model.bean.Fornecedor;

/**
 *
 * @author Computador
 */
public class FornecedorDAO {

    public void save(Fornecedor fornecedor) {
        EntityManager em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (fornecedor.getId() == -1) { //create
                em.persist(fornecedor);
            } else { //update
                em.merge(fornecedor);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public Fornecedor remove(Integer id) {
        EntityManager em = new ConnectionFactory().getConnection();
        Fornecedor fornecedor = null;
        try {
            fornecedor = em.find(Fornecedor.class, id);
            em.getTransaction().begin();
            em.remove(fornecedor);
            em.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "removido com sucesso");
        } catch (Exception e) {
            System.err.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return fornecedor;
    }

    public List<Fornecedor> findAll() {
        EntityManager em = new ConnectionFactory().getConnection();
        List<Fornecedor> fornecedores = null;
        try {
            fornecedores = em.createQuery("from Fornecedor f").getResultList();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            em.close();
        }
        return fornecedores;
    }
    
    public List<Fornecedor> findAllByFilter(String filter, String filterValue) {
        EntityManager em = new ConnectionFactory().getConnection();
        List<Fornecedor> fornecedores = null;
        try {
            fornecedores = em.createQuery("SELECT f FROM Fornecedor f WHERE f." + filter + " LIKE '%" + filterValue + "%'")
                    .getResultList();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            em.close();
        }
        return fornecedores;
    }

}
