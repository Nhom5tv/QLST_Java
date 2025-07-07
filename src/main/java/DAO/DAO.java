/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author THANH AN COMPUTER
 */
public interface DAO<T> {
    List<T> getAll();
    Optional<T> getById(int id);
    boolean insert(T t);
    boolean update(T t);
    boolean delete(int id);
}

