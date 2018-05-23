import com.whx.SkipList;
import org.junit.Assert;
import org.junit.Test;

public class SkipListTest {

    @Test
    public void test()

    {
        SkipList list= new SkipList();
        for(int i =0;i<100;i++){
            list.insert(i);
        }
        list.showAllMember();
        for(int i=0;i<100;i++){
            Assert.assertEquals(true,list.exits(i));
        }
        System.out.println("高度为:"+list.getLevels());
        for(int i=0;i<20;i++){
            list.delete(i);
        }
        for(int i=0;i<20;i++){
            Assert.assertEquals(false,list.exits(i));
        }
        for(int i=20;i<100;i++){
            Assert.assertEquals(true,list.exits(i));
        }
    }

}
