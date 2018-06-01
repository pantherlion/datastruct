import com.whx.RBTREEMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class RBTreeMapTest {

    @Test
    public void testSequenceInsert(){
        RBTREEMap<Integer,Integer>  map = new RBTREEMap<>();
        for(int i=0;i<1000;i++){
            map.insert(i,i);
            Assert.assertTrue(map.exits(i));
            if(i%2==0){
                map.delete(i);
                Assert.assertFalse(map.exits(i));
            }
        }
        map.showAllMembers();
    }

    @Test
    public void testRandomInsert(){
        Random random = new Random();
        RBTREEMap<Integer,Integer>  map = new RBTREEMap<>();
        for(int i=0;i<1000000;i++){
            int temp = random.nextInt(1000);
            map.insert(temp,temp);
            Assert.assertTrue(map.exits(temp));
            if(temp%2==0){
                map.delete(temp);
                Assert.assertFalse("temp="+temp,map.exits(temp));
            }
        }
    }

    @Test
    public void testWithSpecificNum(){
        int [] datas=new int[]{223,757,448};
        RBTREEMap<Integer,Integer>  map = new RBTREEMap<>();
        for(int i=0;i<datas.length;i++){
            map.insert(datas[i],datas[i]);
            //Assert.assertTrue(map.exits(datas[i]));
            if(datas[i]%2==0){
                map.delete(datas[i]);
                System.out.println(datas[i]+":"+map.exits(datas[i]));
                //Assert.assertFalse(map.exits(datas[i]));
            }
        }
    }
}
