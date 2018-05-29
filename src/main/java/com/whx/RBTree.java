package com.whx;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.Assert;
import org.slf4j.LoggerFactory;
import java.util.Stack;
import java.util.concurrent.BlockingDeque;

public class RBTree {
    static int RED=1;
    static int BLACK=0;
    private Node root;

    private static LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    private static Logger logger = context.getLogger(RBTree.class);

    static {
        for(Logger logger:context.getLoggerList()){
            logger.setLevel(Level.toLevel("debug"));
        }
    }

    private class Node{
       int color;
       Node left;
       Node right;
       Node parent;
       int value;
       Node(int value){
           this.value=value;
       }
   }
   public boolean exits(int value){
        Node p = indexOf(value);
        if(p==null){
            return false;
        }
        return true;
   }

   public RBTree insert(int value){
        Node p = indexOfInsert(value);
        Node newNode = new Node(value);
        if(p==null){
            this.root=newNode;
            return this;
        }
        newNode.color=RED;
        newNode.parent=p;
        if(p.value>value){
            p.left=newNode;
        }
        else{
            p.right=newNode;
        }
       insertFix(newNode);
        return this;
   }

   private Node successor(Node p){
        Assert.assertNotNull(p);
        Assert.assertNotNull(p.right);
        p=p.right;
        while(p.left!=null){
            p=p.left;
        }
        return p;
   }

   public RBTree delete(int value){
        Node node=indexOf(value);
        if(node==null){
            return this;
        }
        if(node.left!=null&&node.right!=null){
            Node temp = successor(node);
            node.value=temp.value;
            node=temp;
        }
        delete(node);
        return this;
   }

    private void delete(Node p){
        Assert.assertNotNull(p);
        //只有删除黑色叶子节点才需要deleteFix
        if(p.left==null&&p.right==null&&colorOfNode(p)==BLACK){
            deleteFix(p);
        }

        Node temp = (p.left==null?p.right:p.left);
        Node father = p.parent;
        if(temp!=null){
            temp.color=BLACK;
            temp.parent=father;
        }
        if(father==null){
            this.root=temp;
        }
        else{
            if(p==father.left){
                father.left=temp;
            }
            else{
                father.right=temp;
            }
        }
    }

   private void deleteFix(Node p){
        Assert.assertNotNull(p);
        Assert.assertEquals(p.color,BLACK);
        while (p.parent!=null&&p.color==BLACK){
            Node father = p.parent;
            Node brother= (p==father.left?father.right:father.left);
            Assert.assertNotNull(brother);
            if(colorOfNode(brother)==RED){
                if(p==father.left){
                    rotateLeft(father);
                }
                else{
                    rotateRight(father);
                }
                father.color=RED;
                brother.color=BLACK;
            }
            else if(colorOfNode(brother.left)==BLACK&&colorOfNode(brother.right)==BLACK){
                if(colorOfNode(father)==RED){
                    father.color=BLACK;
                    brother.color=RED;
                    p=father;
                    break;
                }
                else{
                    brother.color=RED;
                    p=father;
                }
            }
            else if(p==father.left&&colorOfNode(brother.left)==RED&&colorOfNode(brother.right)==BLACK){
                rotateRight(brother);
                brother.color=RED;
                brother.parent.color=BLACK;
            }
            else if(p==father.right&&colorOfNode(brother.right)==RED&&colorOfNode(brother.left)==BLACK){
                rotateLeft(brother);
                brother.color=RED;
                brother.parent.color=BLACK;
            }
            else if(p==father.left&&colorOfNode(p.right)==RED){
                rotateLeft(father);
                brother.color=father.color;
                father.color=BLACK;
                brother.right.color=BLACK;
                p=brother;
                break;
            }
            else if(p==father.right&&colorOfNode(p.left)==RED){
                rotateRight(father);
                brother.color=father.color;
                father.color=BLACK;
                brother.left.color=BLACK;
                p=brother;
                break;
            }
        }
        if(p.parent==null){
            this.root=p;
        }
        p.color=BLACK;
   }

   private Node indexOfInsert(int value){
       Node p = this.root;
       Node prev=null;
       while(p!=null){
           prev=p;
           if(p.value<=value){
               p=p.right;
           }
           else{
               p=p.left;
           }
       }
       return prev;
   }

   private Node indexOf(int value){
       Node p = this.root;
       while(p!=null){
           if(p.value==value){
               break;
           }
           if(p.value<value){
               p=p.right;
           }
           else{
               p=p.left;
           }
       }
       return p;
   }

   private void rotateLeft(Node p){
        Node father = p.parent;
        Node rightChild=p.right;
        rightChild.parent=father;
        if(father!=null){
            if(p==father.left){
                father.left=rightChild;
            }
            else{
                father.right=rightChild;
            }
        }
        p.parent=rightChild;
        p.right=rightChild.left;
        if(rightChild.left!=null){
            rightChild.left.parent=p;
        }
        rightChild.left=p;
   }

   private void rotateRight(Node p){
        Node father=p.parent;
        Node leftChild=p.left;
        leftChild.parent=father;
        if(father!=null){
            if(p==father.left){
                father.left=leftChild;
            }
            else {
                father.right=leftChild;
            }
        }
        p.parent=leftChild;
        p.left=leftChild.right;
        if(leftChild.right!=null){
            leftChild.right.parent=p;
        }
        leftChild.right=p;
   }

   private void insertFix(Node p){
        Assert.assertNotNull(p);
        Assert.assertEquals(RED,p.color);
        while(colorOfNode(p.parent)==RED){
            Node father=p.parent;
            Node grandFather=father.parent;
            Assert.assertNotNull(grandFather);
            Node uncle = (father==grandFather.left?grandFather.right:grandFather.left);
            //CASE 1. uncle is red
            if(colorOfNode(uncle)==RED){
                uncle.color=BLACK;
                father.color=BLACK;
                grandFather.color=RED;
                p=grandFather;
            }
            //CASE 2. self is left right  child of father and father is left child of grandFather
            //          or self is left  child of father and father is right child of grandFather
            else if((grandFather.left==father&&father.right==p)||(grandFather.right==father&&father.left==p)){
                if(grandFather.left==father){
                    rotateLeft(father);
                }
                else{
                    rotateRight(father);
                }
                p=father;
            }
            //CASE 3. self is left child of father and father is left child of grandFather
            //        or self is right child of father and father is right child of grandFather
            else{
                if(grandFather.left==father&&father.left==p){
                    rotateRight(grandFather);
                }
                else{
                    rotateLeft(grandFather);
                }
                grandFather.color=RED;
                father.color=BLACK;
                p=father;
                break;
            }
        }
        if(p.parent==null){
            this.root=p;
            this.root.color=BLACK;
        }
   }

    private int colorOfNode(Node p){
        if(p==null){
            return BLACK;
        }
        return p.color;
    }

    public void showAllMembers(){
        Node p = this.root;
        Stack<Node> stack = new Stack<>();
        while(!stack.isEmpty()||p!=null){
            while(p!=null){
                stack.push(p);
                p=p.left;
            }
            p=stack.pop();
            logger.debug(""+p.value);
            p=p.right;
        }
        }
        }