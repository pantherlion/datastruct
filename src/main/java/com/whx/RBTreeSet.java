package com.whx;

public class RBTreeSet<T extends Comparable<T>> {
    private RBTREEMap<T,T> rbtreeMap;
    public RBTreeSet(){
        this.rbtreeMap=new RBTREEMap<>();
    }
    public RBTreeSet<T> insert(T t){
        this.rbtreeMap.insert(t,t);
        return this;
    }
    public RBTreeSet<T> delete(T t){
        this.rbtreeMap.delete(t);
        return this;
    }
    public boolean exits(T t){
        return rbtreeMap.exits(t);
    }
}
