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
import br.model.bean.Produto;

/**
 *
 * @author Computador
 */
public class ProdutoDAO {

    public void save(Produto produto) {
        EntityManager em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (produto.getId() == -1) { //create
                em.persist(produto);
            } else { //update
                em.merge(produto);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public Produto remove(Integer id) {
        EntityManager em = new ConnectionFactory().getConnection();
        Produto produto = null;
        try {
            produto = em.find(Produto.class, id);
            em.getTransaction().begin();
            em.remove(produto);
            em.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "removido com sucesso");
        } catch (Exception e) {
            System.err.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return produto;
    }

    public List<Produto> findAll() {
        EntityManager em = new ConnectionFactory().getConnection();
        List<Produto> produtos = null;
        try {
            produtos = em.createQuery("from Produto p").getResultList();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            em.close();
        }
        return produtos;
    }
    
    public List<Produto> findAllByFilter(String filter, String filterValue) {
        EntityManager em = new ConnectionFactory().getConnection();
        List<Produto> produtos = null;
        try {
            produtos = em.createQuery("SELECT p FROM Produto p WHERE p." + filter + " LIKE '%" + filterValue + "%'")
                    .getResultList();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            em.close();
        }
        return produtos;
    }

}
