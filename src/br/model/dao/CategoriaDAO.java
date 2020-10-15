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
import br.model.bean.Categoria;

/**
 *
 * @author Computador
 */
public class CategoriaDAO {

    public void save(Categoria categoria) {
        EntityManager em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (categoria.getId() == -1) { //create
                em.persist(categoria);
            } else { //update
                em.merge(categoria);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public Categoria remove(Integer id) {
        EntityManager em = new ConnectionFactory().getConnection();
        Categoria categoria = null;
        try {
            categoria = em.find(Categoria.class, id);
            em.getTransaction().begin();
            em.remove(categoria);
            em.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "removido com sucesso");
        } catch (Exception e) {
            System.err.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return categoria;
    }

    public List<Categoria> findAll() {
        EntityManager em = new ConnectionFactory().getConnection();
        List<Categoria> categorias = null;
        try {
            categorias = em.createQuery("from Categoria c").getResultList();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            em.close();
        }
        return categorias;
    }
    
    public List<Categoria> findAllByFilter(String filter, String filterValue) {
        EntityManager em = new ConnectionFactory().getConnection();
        List<Categoria> categorias = null;
        try {
            categorias = em.createQuery("SELECT c FROM Categoria c WHERE c." + filter + " LIKE '%" + filterValue + "%'")
                    .getResultList();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            em.close();
        }
        return categorias;
    }

}
