import com.whx.RBTree;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class RBTreeTest {

    @Test
    public void testSequence(){
        RBTree tree = new RBTree();
        for(int i=0;i<100;i++){
            tree.insert(i);
            Assert.assertTrue(tree.exits(i));
            if(i%2==0){
                tree.delete(i);
            }
        }
        tree.showAllMembers();
    }

    @Test
    public void testRandom(){
        {
            RBTree tree = new RBTree();
            Random random = new Random();
            for(int i=100;i>=0;i--){
                int temp = random.nextInt(100);
                tree.insert(temp);
                Assert.assertTrue(tree.exits(temp));
                if(temp%2==0){
                    tree.delete(temp);
                }
            }
            tree.showAllMembers();
        }
    }
}
