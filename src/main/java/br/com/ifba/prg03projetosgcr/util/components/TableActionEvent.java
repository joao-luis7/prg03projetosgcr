/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.util.components;

/**
 *
 * @author joaol
 */
public interface TableActionEvent {
    void onEdit(int row);
    void onDelete(int row);
    void onView(int row);
}
