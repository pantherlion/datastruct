package com.whx;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class SkipList {
    private int level;
    private Node root;
    private static final LoggerContext logContext = (LoggerContext)LoggerFactory.getILoggerFactory();
    private static final Logger logger=logContext.getLogger(SkipList.class);

    static {
        for (ch.qos.logback.classic.Logger logger:logContext.getLoggerList()){
            logger.setLevel(Level.toLevel("DEBUG"));
        }
    }

    private class Node{
        Node next;
        Node down;
        int value;
        Node(int value){
            this.value=value;
        }
    }

    public SkipList insert(int value){
        //随机生成层数
        int level = randomLevel();
        logger.debug("随机level:"+level);

        Node p = null;
        if(level<this.level){
            p=descentTo((this.level-level+1),value);
        }
        else{
            p=this.root;
        }

        if(p!=null){
            insertNodeToEveryLevel(p,value);
        }

        if(level>this.level){
            //需要创建新的层节点
            Node[] nodes= createKLevel(level-this.level,value);
            Node newRoot=nodes[0];
            Node tail=nodes[1];
            tail.down=this.root;
            if(this.root!=null){
                tail.next.down=indexOf(this.root,value).next;
            }
            this.root=newRoot;
            this.level=level;
        }
        return this;
    }

    public SkipList delete(int value){
        Node p = this.root;
        while(p!=null){
            Node prev=indexOf(p,value);
            if(prev.next.value==value){
                deleteNode(prev);
            }
            p=prev.down;
        }
        return this;
    }

    public boolean exits(int value){
        Node p = descentTo(this.level,value);
        if(p.next.value==value){
            return true;
        }
        return false;
    }

    public int getLevels(){
        return this.level;
    }

    private  Node indexOf(Node start ,int value){
        while(start.next.value<value){
            start=start.next;
        }
        return start;
    }

    private  Node createNewList(int value){
        Node minNode=new Node(Integer.MIN_VALUE);
        Node maxNode=new Node(Integer.MAX_VALUE);
        Node dataNode = new Node(value);
        minNode.next=dataNode;
        dataNode.next=maxNode;
        return minNode;
    }

    private Node[] createKLevel(int k, int value){
        Node newRoot=null;
        Node prev=null;
        for(int i=0;i<k;i++){
            if(i==0){
                prev=newRoot=createNewList(value);
            }
            else {
                Node first = createNewList(value);
                prev.down=first;
                prev.next.down = first.next;
                prev=first;
            }
        }
        Node[] nodes= new Node[2];
        nodes[0]=newRoot;
        nodes[1]=prev;
        return nodes;
    }

    private void insertNode(Node prev,Node mid){
        mid.next=prev.next;
        prev.next=mid;
    }

    private void deleteNode(Node prev){
        prev.next=prev.next.next;
    }

    private Node descentTo(int level,int value){
        if(this.level<level){
            throw new  IllegalArgumentException("下降层数过多");
        }
        Node p = this.root;
        for(int i=1;i<level;i++){
            p=indexOf(p,value);
            p=p.down;
        }
        while(p.next.value<value){
            p=p.next;
        }
        return p;
    }

    public void showAllMember(){
        Node p = this.root;
        while(p!=null){
            Node first=p;
            first=first.next;
            while(first.next!=null){
                logger.debug("结点数据为:"+first.value);
                first=first.next;
            }
            logger.debug("---------------");
            p=p.down;
        }
    }

    private void insertNodeToEveryLevel(Node p,int value){
        Node upperNode=null;
        while(p!=null){
            Node prev=indexOf(p,value);
            Node newNode = new Node(value);
            insertNode(prev,newNode);
            if(upperNode!=null){
                upperNode.down=newNode;
            }
            upperNode=newNode;
            p=prev.down;
        }
    }

    private static int randomLevel(){
        int level=1;
        while (Math.random()<0.5){
            level++;
        }
        return level;
    }

}
