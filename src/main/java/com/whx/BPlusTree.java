package com.whx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BPlusTree <K extends Comparable<K>,V>{

    class BplusNode<K extends Comparable<K>,V>{
        boolean isLeaf;
        boolean isRoot;
        BplusNode<K,V> parant;
        //叶子节点的前节点
        BplusNode<K,V> previous;
        //叶子节点的后节点
        BplusNode<K,V> next;
        List<Map.Entry<K,V>> entries;
        List<BplusNode<K,V>> children;
        BplusNode(boolean isLeaf){
            this.isLeaf=isLeaf;
            entries=new ArrayList<Map.Entry<K,V>>();
            if(!isLeaf) {
                children = new ArrayList<BplusNode<K, V>>();
            }
        }
        BplusNode (boolean isLeaf,boolean isRoot){
            this(isLeaf);
            this.isRoot=isRoot;
        }


    }
}
