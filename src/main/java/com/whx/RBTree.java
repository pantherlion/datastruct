package com.whx;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.Assert;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Stack;

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

   private Node delete(Node start,Node target){
        Assert.assertTrue(target.left==null||target.right==null);
        Node prev = start.parent;
        Node p = start;
        while(p!=null){
            if(p==target){
                Node temp = target.left==null?target.right:target.left;
                if(temp!=null){
                    temp.parent=prev;
                }
                if(prev==null){
                    this.root=temp;
                }
                else{
                    if(prev.left==p){
                        prev.left=temp;
                    }
                    else{
                        prev.right=temp;
                    }
                }
                break;
            }
            else if(p.value<target.value){
                prev=p;
                p=p.right;
            }
            else{
                prev=p;
                p=p.left;
            }
        }
        return target.left==null?target.right:target.left;
   }

   public RBTree delete(int value){
        Node p = this.root;
        Node fixed=null;
        while(p!=null){
            if(p.value==value){
                if(p.left==null||p.right==null){
                    fixed=delete(p,p);
                }
                else{
                    Node successor = successor(p);
                    int temp = successor.value;
                    fixed=delete(p,successor);
                    p.value=temp;
                }
                break;
            }
            else if(p.value<value){
                p=p.right;
            }
            else{
                p=p.left;
            }
        }
        if(fixed!=null){
            deleteFix(fixed);
        }
        return this;
   }

   private void deleteFix(Node p){
        Assert.assertNotNull(p);
        while(p.parent!=null && p.color!=RED){
            Node father = p.parent;
            Node brother = father.left==p?father.right:father.left;
            Assert.assertNotNull(brother);
            //CASE1. brother is red
            if(colorOfNode(brother)==RED){
                rotateLeft(father);
                father.color=RED;
                brother.color=BLACK;
            }
            //CASE2 brother is black and brother'children is both black
            else if(colorOfNode(brother.left)==BLACK&&colorOfNode(brother.right)==BLACK){
                brother.color=RED;
                p=father;
            }
            //CASE 3 brother is black and brother's left child is red
            else if(colorOfNode(brother.left)==RED){
                rotateRight(brother);
                brother.color=RED;
                brother.parent.color=BLACK;
            }
            //CASE 4 brother is black and brother's right child is red
            else if(colorOfNode(brother.right)==RED){
                rotateLeft(father);
                brother.color=father.color;
                father.color=BLACK;
                brother.right.color=BLACK;
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
        leftChild.right=p;
   }

   private void insertFix(Node p){
        Assert.assertNotNull(p);
        Assert.assertEquals(RED,p.color);
        while(p.parent!=null&&p.parent.color==RED){
            Node father=p.parent;
            Node grandFather=father.parent;
            Node uncle=father==grandFather.left?grandFather.right:grandFather.left;
            //CASE 1. uncle is red
            if(colorOfNode(uncle)==RED){
                uncle.color=BLACK;
                father.color=BLACK;
                grandFather.color=RED;
                p=grandFather;
            }
            //CASE 2. self is left right  child of father and father is left child of grandFather
            //          or self is left  child of father and father is right child of grandFather
            else if(grandFather.left==father&&father.right==p||grandFather.right==father&&father.left==p){
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
                    grandFather.color=RED;
                    father.color=BLACK;
                }
                else{
                    rotateLeft(grandFather);
                    grandFather.color=RED;
                    father.color=BLACK;
                }
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
