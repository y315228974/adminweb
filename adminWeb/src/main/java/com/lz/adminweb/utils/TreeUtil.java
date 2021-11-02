package com.lz.adminweb.utils;

import com.lz.adminweb.vo.system.MenuTreeNode;
import com.lz.adminweb.vo.system.PermissionTreeNode;
import com.lz.adminweb.vo.system.RolePermissionTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pangshihe on 2019/7/29.
 */
public class TreeUtil {
    /**
     * 权限list 转 Tree
     */
    public static List<PermissionTreeNode> listToTree(List<PermissionTreeNode> list) {
        //用递归找子。
        List<PermissionTreeNode> treeList = new ArrayList<PermissionTreeNode>();
        for (PermissionTreeNode tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static PermissionTreeNode findChildren(PermissionTreeNode tree, List<PermissionTreeNode> list) {
        for (PermissionTreeNode node : list) {
            if (node.getParentId() == tree.getId()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<PermissionTreeNode>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }

    /**
     * 角色权限转树状结构
     */
    public static List<RolePermissionTreeNode> rolePermissionToTree(List<RolePermissionTreeNode> list) {
        //用递归找子。
        List<RolePermissionTreeNode> treeList = new ArrayList<RolePermissionTreeNode>();
        for (RolePermissionTreeNode tree : list) {
            if (tree.getPermissionParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static RolePermissionTreeNode findChildren(RolePermissionTreeNode tree, List<RolePermissionTreeNode> list) {
        for (RolePermissionTreeNode node : list) {
            if (node.getPermissionParentId() == tree.getPermissionId()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<RolePermissionTreeNode>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }

    public static List<MenuTreeNode> MenuListToTree(List<MenuTreeNode> list) {
        //用递归找子。
        List<MenuTreeNode> treeList = new ArrayList<>();
        for (MenuTreeNode tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static MenuTreeNode findChildren(MenuTreeNode tree, List<MenuTreeNode> list) {
        for (MenuTreeNode node : list) {
            if (node.getParentId() == tree.getId()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
