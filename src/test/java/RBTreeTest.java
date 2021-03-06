import com.whx.RBTree;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class RBTreeTest {

    @Test
    public void testSequence(){
        RBTree tree = new RBTree();
        for(int i=0;i<100000;i++){
            tree.insert(i);
            Assert.assertTrue(tree.exits(i));
            if(i%2==0){
                tree.delete(i);
                Assert.assertFalse(tree.exits(i));
            }
        }
        tree.showAllMembers();
    }

    @Test
    public void testRandom(){
        {
            RBTree tree = new RBTree();
            Random random = new Random();
            for(int i=0;i<1000000;i++){
                int temp = random.nextInt(100);
                tree.insert(temp);
                Assert.assertTrue(temp+"存在",tree.exits(temp));
              if(temp%2==0){
                    tree.delete(temp);
              }
            }
        }
    }

    @Test
    public void insert1(){
        int [] datas=new int[]{70,89,98,16,17,86,39,19,35};
        RBTree tree=new RBTree();
        for (int i :datas){
            tree.insert(i);
            tree.showAllMembers();
            System.out.println("-----------");
            Assert.assertTrue(i+"存在",tree.exits(i));
        }
    }
}
